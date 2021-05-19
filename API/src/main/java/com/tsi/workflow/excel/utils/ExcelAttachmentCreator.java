/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.excel.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ddf.EscherContainerRecord;
import org.apache.poi.ddf.EscherOptRecord;
import org.apache.poi.ddf.EscherProperties;
import org.apache.poi.ddf.EscherProperty;
import org.apache.poi.ddf.EscherSimpleProperty;
import org.apache.poi.ddf.EscherSpRecord;
import org.apache.poi.hpsf.ClassID;
import org.apache.poi.hssf.record.CommonObjectDataSubRecord;
import org.apache.poi.hssf.record.EmbeddedObjectRefSubRecord;
import org.apache.poi.hssf.record.EndSubRecord;
import org.apache.poi.hssf.record.ObjRecord;
import org.apache.poi.hssf.record.SubRecord;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFObjectData;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.DirectoryNode;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.LittleEndianByteArrayInputStream;

/**
 *
 * @author prabhu.prabhakaran
 */
public class ExcelAttachmentCreator {

    HashMap<String, Integer> icons;
    HashMap<String, Integer> attachments;
    HSSFWorkbook wb;
    POIFSFileSystem poifs;

    public ExcelAttachmentCreator(HSSFWorkbook wb) {
	this.wb = wb;
	this.poifs = new POIFSFileSystem();
	this.icons = new HashMap<>();
	this.attachments = new HashMap<>();
    }

    public void embedAttachments(String pFileName) throws IOException {
	embedAttachments(new File("C:\\Users\\prabhu.prabhakaran\\Desktop\\zTPFM.zip"), FilenameUtils.getName(pFileName));
    }

    public void embedAttachments(File pFile, String pFileName) throws IOException {
	embedAttachments(FileUtils.readFileToByteArray(pFile), pFileName);
    }

    public void embedAttachments(byte[] pFileByteArray, String pFileName) throws IOException {
	if (!attachments.containsKey(pFileName)) {
	    int storageIdPpt = packageOleData(poifs, pFileByteArray, pFileName);
	    attachments.put(pFileName, storageIdPpt);
	    embedIcon(FilenameUtils.getExtension(pFileName));
	}
    }

    public void attach(String pSheetName, int rowoffset, int coloffset, String pFileName) throws Exception {
	attach(pSheetName, rowoffset, coloffset, 1, pFileName);
    }

    public void attach(String pSheetName, int rowoffset, int coloffset, int rowoffincr, String pFileName) throws Exception {
	HSSFPatriarch patriarch = wb.getSheet(pSheetName).createDrawingPatriarch();
	pFileName = FilenameUtils.getName(pFileName);
	int storageIdPpt = attachments.get(pFileName);
	int previewIdxPpt = icons.get(FilenameUtils.getExtension(pFileName));
	CreationHelper ch = wb.getCreationHelper();
	HSSFClientAnchor anchor = (HSSFClientAnchor) ch.createClientAnchor();
	anchor.setAnchor((short) (coloffset), rowoffset, 0, 0, (short) (1 + coloffset), rowoffincr + rowoffset, 0, 0);
	anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);

	HSSFObjectData oleShape = createObjectData(poifs, storageIdPpt, 1, anchor, previewIdxPpt);
	addShape(patriarch, oleShape);
    }

    public void write(String pFileNameWithPath) throws IOException {
	poifs.getRoot().createDocument("Workbook", new ByteArrayInputStream(wb.getBytes()));
	FileOutputStream fos = new FileOutputStream(pFileNameWithPath);
	poifs.writeFilesystem(fos);
	fos.close();
    }

    public void write(ByteArrayOutputStream lExcelStream) throws IOException {
	poifs.getRoot().createDocument("Workbook", new ByteArrayInputStream(wb.getBytes()));
	poifs.writeFilesystem(lExcelStream);
    }

    private HSSFObjectData createObjectData(POIFSFileSystem poifs, int storageId, int objectIdx, HSSFClientAnchor anchor, int previewIdx) {
	ObjRecord obj = new ObjRecord();
	CommonObjectDataSubRecord ftCmo = new CommonObjectDataSubRecord();
	ftCmo.setObjectType(CommonObjectDataSubRecord.OBJECT_TYPE_PICTURE);
	ftCmo.setObjectId(objectIdx);
	ftCmo.setLocked(true);
	ftCmo.setPrintable(true);
	ftCmo.setAutofill(true);
	ftCmo.setAutoline(true);
	ftCmo.setReserved1(0);
	ftCmo.setReserved2(0);
	ftCmo.setReserved3(0);
	obj.addSubRecord(ftCmo);

	obj.addSubRecord(SubRecord.createSubRecord(new LittleEndianByteArrayInputStream(new byte[] { 7, 0, 2, 0, 2, 0 }), 0));
	obj.addSubRecord(SubRecord.createSubRecord(new LittleEndianByteArrayInputStream(new byte[] { 8, 0, 2, 0, 1, 0 }), 0));

	EmbeddedObjectRefSubRecord ftPictFmla;
	try {
	    Constructor<EmbeddedObjectRefSubRecord> con = EmbeddedObjectRefSubRecord.class.getDeclaredConstructor();
	    con.setAccessible(true);
	    ftPictFmla = con.newInstance();
	} catch (Exception e) {
	    throw new RuntimeException("oops", e);
	}

	setField(ftPictFmla, "field_2_unknownFormulaData", new byte[] { 2, 0, 0, 0, 0 });
	setField(ftPictFmla, "field_4_ole_classname", "Paket");
	setField(ftPictFmla, "field_5_stream_id", (Integer) storageId);

	obj.addSubRecord(ftPictFmla);
	obj.addSubRecord(new EndSubRecord());

	// create temporary picture, but don't attach it.
	// It's neccessary to create the sp-container, which need to be minimal modified
	// for oleshapes
	HSSFPicture shape = new HSSFPicture(null, anchor);
	EscherContainerRecord spContainer;

	try {
	    Method m = HSSFPicture.class.getDeclaredMethod("createSpContainer");
	    m.setAccessible(true);
	    spContainer = (EscherContainerRecord) m.invoke(shape);
	} catch (Exception e) {
	    throw new RuntimeException("oops", e);
	}

	EscherSpRecord spRecord = spContainer.getChildById(EscherSpRecord.RECORD_ID);
	spRecord.setFlags(spRecord.getFlags() | EscherSpRecord.FLAG_OLESHAPE);
	spRecord.setShapeType((byte) 0x4B);
	EscherOptRecord optRecord = spContainer.getChildById(EscherOptRecord.RECORD_ID);

	EscherProperty ep = new EscherSimpleProperty(EscherProperties.BLIP__PICTUREID, false, false, 1);
	optRecord.addEscherProperty(ep);

	DirectoryEntry oleRoot;
	try {
	    oleRoot = (DirectoryEntry) poifs.getRoot().getEntry(formatStorageId(storageId));
	} catch (FileNotFoundException e) {
	    throw new RuntimeException("oops", e);
	}
	HSSFObjectData oleShape = new HSSFObjectData(spContainer, obj, oleRoot);
	oleShape.setPictureIndex(previewIdx);
	return oleShape;
    }

    private void addShape(HSSFPatriarch patriarch, HSSFShape shape) throws Exception {
	patriarch.addShape(shape);
	Method m = HSSFPatriarch.class.getDeclaredMethod("onCreate", HSSFShape.class);
	m.setAccessible(true);
	m.invoke(patriarch, shape);
    }

    private void setField(Object clazz, String fieldname, Object value) {
	try {
	    Field f = clazz.getClass().getDeclaredField(fieldname);
	    f.setAccessible(true);
	    f.set(clazz, value);
	} catch (Exception e) {
	    throw new RuntimeException("oops", e);
	}
    }

    private void embedIcon(String image) {
	try {
	    // zip.png, doc.png, docx.png
	    if (!icons.containsKey(image)) {
		InputStream is = ExcelAttachmentCreator.class.getResource("/" + image + ".png").openStream();
		byte previewImg[] = IOUtils.toByteArray(is);
		is.close();
		int pictIdx = wb.addPicture(previewImg, HSSFWorkbook.PICTURE_TYPE_PNG);
		icons.put(image, pictIdx);
	    }
	} catch (IOException e) {
	    throw new RuntimeException("not really?", e);
	}
    }

    private void addOleStreamEntry(DirectoryEntry dir) throws IOException {
	final String OLESTREAM_NAME = "\u0001Ole";
	if (!dir.hasEntry(OLESTREAM_NAME)) {
	    // the following data was taken from an example libre office document
	    // beside this "\u0001Ole" record there were several other records, e.g.
	    // CompObj,
	    // OlePresXXX, but it seems, that they aren't neccessary
	    byte oleBytes[] = { 1, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	    dir.createDocument(OLESTREAM_NAME, new ByteArrayInputStream(oleBytes));
	}
    }

    private String formatStorageId(int storageId) {
	return String.format("MBD%1$08X", storageId);
    }

    private int packageOleData(POIFSFileSystem poifs, byte oleData[], String label) throws IOException {
	DirectoryNode root = poifs.getRoot();
	// get free MBD-Node
	int storageId = 0;
	DirectoryEntry oleDir = null;
	do {
	    String storageStr = formatStorageId(++storageId);
	    if (!root.hasEntry(storageStr)) {
		oleDir = root.createDirectory(storageStr);
		oleDir.setStorageClsid(ClassID.OLE10_PACKAGE);
	    }
	} while (oleDir == null);

	addOleStreamEntry(oleDir);

	Ole10Native2 oleNative = new Ole10Native2();
	oleNative.setLabel(label);
	oleNative.setFileName(label);
	oleNative.setCommand(label);
	oleNative.setDataBuffer(oleData);

	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	oleNative.writeOut(bos);
	byte buf1[] = bos.toByteArray();

	oleDir.createDocument(Ole10Native2.OLE10_NATIVE, new ByteArrayInputStream(buf1));

	return storageId;
    }

}
