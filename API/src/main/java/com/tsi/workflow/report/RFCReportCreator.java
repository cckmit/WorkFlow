package com.tsi.workflow.report;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.tsi.workflow.beans.ui.RFCReportForm;
import com.tsi.workflow.excel.utils.ExcelAttachmentCreator;
import com.tsi.workflow.helper.CommonHelper;
import com.tsi.workflow.utils.DateHelper;
import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.log4j.Logger;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class RFCReportCreator {

    private static final Logger LOG = Logger.getLogger(RFCReportCreator.class.getName());

    HSSFWorkbook lWorkBook;
    Sheet lActiveSheet;
    String lName;
    int lCurrentRow = 0;
    int lCurrentCell = 0;
    ExcelAttachmentCreator excelAttachmentCreator;

    public RFCReportCreator() {
	lWorkBook = new HSSFWorkbook();
	excelAttachmentCreator = new ExcelAttachmentCreator(lWorkBook);
    }

    public int getNewRow() {
	lCurrentCell = 0;
	return lCurrentRow++;
    }

    public int getNextCell() {
	return lCurrentCell++;
    }

    public String getName() {
	return lName;
    }

    public Workbook getWorkBook() {
	return lWorkBook;
    }

    public ExcelAttachmentCreator getExcelAttachmentCreator() {
	return excelAttachmentCreator;
    }

    public void addRFCDetails(CommonHelper commonHelper, List<RFCReportForm> pResults, HashMap<String, String> approvalFileMap, HashMap<String, Set<String>> planBasedSegments, HashMap<String, String> testFileNameMap, HashMap<String, Set<String>> dbcrByPlans, Boolean formHyperLink) {
	lActiveSheet = lWorkBook.createSheet("RFC Report");
	Row row = lActiveSheet.createRow(getNewRow());
	Cell lCell = row.createCell(getNextCell());
	addHeaderDetails(row, lCell);
	addRFCReports(commonHelper, pResults, approvalFileMap, planBasedSegments, testFileNameMap, dbcrByPlans, formHyperLink);
    }

    private void addRFCReports(CommonHelper commonHelper, List<RFCReportForm> pResults, HashMap<String, String> approvalFileMap, HashMap<String, Set<String>> planBasedSegments, HashMap<String, String> testFileNameMap, HashMap<String, Set<String>> dbcrByPlans, Boolean formHyperLink) {

	CellStyle hlink_style = lWorkBook.createCellStyle();
	Font hlink_font = lWorkBook.createFont();
	hlink_font.setUnderline(Font.U_SINGLE);
	hlink_font.setColor(IndexedColors.BLUE.getIndex());
	hlink_style.setFont(hlink_font);

	pResults.forEach(rfc -> {
	    Row valueRow = lActiveSheet.createRow(getNewRow());
	    if (!formHyperLink) {
		valueRow.setHeight((short) 700);
	    }

	    Cell rowCell;
	    rowCell = valueRow.createCell(getNextCell());
	    if (approvalFileMap != null && approvalFileMap.containsKey(rfc.getPlanid()) && !approvalFileMap.get(rfc.getPlanid()).isEmpty()) {
		if (!formHyperLink) {
		    try {
			String zipFileUrl = approvalFileMap.get(rfc.getPlanid());
			List<NameValuePair> params = URLEncodedUtils.parse(new URI(zipFileUrl), "UTF-8");
			ByteArrayOutputStream zipFileStream = commonHelper.getInMemoryZipFile(params.get(0).getValue());
			if (zipFileStream != null) {
			    String fileName = rfc.getPlanid() + "_" + rfc.getTargetsystem() + "_app_doc.zip";
			    excelAttachmentCreator.embedAttachments(zipFileStream.toByteArray(), fileName);
			    excelAttachmentCreator.attach(lActiveSheet.getSheetName(), valueRow.getRowNum(), rowCell.getColumnIndex(), fileName);
			} else {
			    LOG.info("Zip file byte stream is empty");
			}
		    } catch (Exception e) {
			e.printStackTrace();
		    }

		} else {
		    rowCell.setCellValue("Click to download Business Approval");

		    Hyperlink link = lWorkBook.getCreationHelper().createHyperlink(HyperlinkType.URL);
		    link.setAddress(approvalFileMap.get(rfc.getPlanid()));
		    rowCell.setHyperlink(link);
		    rowCell.setCellStyle(hlink_style);
		}

	    } else {
		rowCell.setCellValue("");
	    }

	    rowCell = valueRow.createCell(getNextCell());
	    if (testFileNameMap != null && testFileNameMap.containsKey(rfc.getPlanid()) && !testFileNameMap.get(rfc.getPlanid()).isEmpty()) {
		if (!formHyperLink) {
		    try {
			String zipFileUrl = testFileNameMap.get(rfc.getPlanid());
			List<NameValuePair> params = URLEncodedUtils.parse(new URI(zipFileUrl), "UTF-8");
			ByteArrayOutputStream zipFileStream = commonHelper.getInMemoryZipFile(params.get(0).getValue());
			if (zipFileStream != null) {
			    String fileName = rfc.getPlanid() + "_" + rfc.getTargetsystem() + "_test_doc.zip";
			    excelAttachmentCreator.embedAttachments(zipFileStream.toByteArray(), fileName);
			    excelAttachmentCreator.attach(lActiveSheet.getSheetName(), valueRow.getRowNum(), rowCell.getColumnIndex(), fileName);
			} else {
			    LOG.info("Test Zip file byte stream is empty");
			}
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		} else {
		    rowCell.setCellValue("Click to download Test Scripts");

		    Hyperlink link = lWorkBook.getCreationHelper().createHyperlink(HyperlinkType.URL);
		    link.setAddress(testFileNameMap.get(rfc.getPlanid()));
		    rowCell.setHyperlink(link);
		    rowCell.setCellStyle(hlink_style);
		}

	    } else {
		rowCell.setCellValue("");
	    }

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(rfc.getTargetsystem());
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue((rfc.getLoaddatetime() != null) ? DateHelper.convertGMTtoEST(rfc.getLoaddatetime()) : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(rfc.getPlanid());
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue((rfc.getRfcnumber() != null) ? rfc.getRfcnumber() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue((rfc.getLoadcategory() != null) ? rfc.getLoadcategory() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue((rfc.getIncidentnumber() != null) ? rfc.getIncidentnumber() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    if (dbcrByPlans != null && dbcrByPlans.containsKey(rfc.getPlanid() + "_" + rfc.getTargetsystem()) && !dbcrByPlans.get(rfc.getPlanid() + "_" + rfc.getTargetsystem()).isEmpty()) {
		rowCell.setCellValue(dbcrByPlans.get(rfc.getPlanid() + "_" + rfc.getTargetsystem()).stream().collect(Collectors.joining(",")));
	    } else {
		rowCell.setCellValue("");
	    }
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue((rfc.getImpdesc() != null) ? rfc.getImpdesc() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue((rfc.getBreakfix() != null) ? rfc.getBreakfix() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue((rfc.getImpactlevel() != null) ? rfc.getImpactlevel() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue((rfc.getConfigvalue() != null) ? rfc.getConfigvalue() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue((rfc.getLeadname() != null) ? rfc.getLeadname() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue((rfc.getManagername() != null) ? rfc.getManagername() : "");
	    rowCell = valueRow.createCell(getNextCell());

	    String loadAttPhone = "";
	    if (rfc.getLoadattendeecontact() != null && !rfc.getLoadattendeecontact().isEmpty()) {
		try {
		    PhoneNumber number = PhoneNumberUtil.getInstance().parseAndKeepRawInput(("+" + rfc.getLoadattendeecontact()), "");
		    loadAttPhone = " - " + PhoneNumberUtil.getInstance().formatInOriginalFormat(number, "");
		} catch (NumberParseException e) {
		    e.printStackTrace();
		    LOG.error("Error in phone number format: " + e);
		}
	    }
	    rowCell.setCellValue(rfc.getLoadattendee() + " / " + loadAttPhone);

	    rowCell = valueRow.createCell(getNextCell());
	    if (rfc.getVsflag() != null && rfc.getVsflag()) {
		rowCell.setCellValue("YES");
	    } else {
		rowCell.setCellValue("N/A");
	    }

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue((rfc.getVsarea() != null) ? rfc.getVsarea() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    if (rfc.getVstestflag() != null && rfc.getVstestflag()) {
		rowCell.setCellValue("YES");
	    } else {
		rowCell.setCellValue("N/A");
	    }
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue((rfc.getVsdesc() != null) ? rfc.getVsdesc() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    if (planBasedSegments != null && planBasedSegments.containsKey(rfc.getPlanid()) && !planBasedSegments.get(rfc.getPlanid()).isEmpty()) {
		rowCell.setCellValue(planBasedSegments.get(rfc.getPlanid()).stream().collect(Collectors.joining(",")));
	    } else {
		rowCell.setCellValue("");
	    }

	});

	if (lActiveSheet.getPhysicalNumberOfRows() > 0) {
	    Row row = lActiveSheet.getRow(lActiveSheet.getFirstRowNum());
	    Iterator<Cell> cellIterator = row.cellIterator();
	    while (cellIterator.hasNext()) {
		Cell cell = cellIterator.next();
		int columnIndex = cell.getColumnIndex();
		lActiveSheet.autoSizeColumn(columnIndex);
	    }
	}
    }

    private void addHeaderDetails(Row row, Cell lCell) {

	lCell.setCellValue("Business Approval");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Test Results");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("System");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Load Date/Time");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Implementation Number");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("RFC Number");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Implementation Specification");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("QC/CR Incident Number");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("DBCR");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Description");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Break-Fix/Expedited");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("TPF Resources Impact");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Configuration Item");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Requestor");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Responsible manager");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Coverage");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Does VS use this process?");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Area of VS does this affect?");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Can VS test this?");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("VS expect to see with this change?");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Software to be installed");
    }
}
