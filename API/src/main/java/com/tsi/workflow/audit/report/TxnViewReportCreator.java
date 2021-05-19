/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.report;

import com.tsi.workflow.audit.uiform.TransactionViewResponseForm;
import com.tsi.workflow.excel.utils.ExcelAttachmentCreator;
import com.tsi.workflow.report.RFCReportCreator;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author ramkumar.seenivasan
 */
public class TxnViewReportCreator {

    private static final Logger LOG = Logger.getLogger(RFCReportCreator.class.getName());

    HSSFWorkbook lWorkBook;
    Sheet lActiveSheet;
    String lName;
    int lCurrentRow = 0;
    int lCurrentCell = 0;
    ExcelAttachmentCreator excelAttachmentCreator;
    List<String> Systems;
    String[] fileTypes;
    String[] gitDetails;
    int totalCount;
    int totalSOCount;
    int[] mergePoints;

    public TxnViewReportCreator() {
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

    public void addSystemViewDetails(List<TransactionViewResponseForm> systemViewList, String hostProfile, List<String> userId, String planId, Date startDate, Date endDate, List<String> userAction) {
	String sheetName = getSheetName(userAction.stream().collect(Collectors.joining(",")));

	lActiveSheet = lWorkBook.createSheet(sheetName);
	Row row = lActiveSheet.createRow(getNewRow());
	Cell lCell = row.createCell(getNextCell());
	addHeaderDetails(row, lCell, hostProfile, userId, planId, startDate, endDate, userAction);
	addData(systemViewList, hostProfile, userAction.stream().collect(Collectors.joining(",")));

    }

    private void addHeaderDetails(Row row, Cell lCell, String hostProfile, List<String> userId, String planId, Date startDate, Date endDate, List<String> userAction) {
	int targetSystemCount = hostProfile != "Delta" ? 3 : 2;
	String[] deltaTargetSystems = new String[] { "AIR", "RES", "OSS", "Total" };
	String[] travelportTargetSystems = new String[] { "APO", "PRE", "PGR", "WSP", "Total" };
	String[] resTymAndServerInfo = new String[] { "ms", "sec", "min", "x86 Server", "zLinux Server", "zOS" };
	fileTypes = new String[] { ".asm", ".sbt", ".c/.cpp", ".mak", ".h/.hpp/.mac/.inc", "Others", "Total  " };
	gitDetails = new String[] { "Count of Prod Repositories in Submodule", "Prod Repositories List in Submodule" };

	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/DD/YYYY");
	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	String luserAction = userAction.stream().collect(Collectors.joining(","));
	lCell.setCellValue("WorkFlow Application: " + hostProfile);
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Start Date: " + dateFormat.format(startDate));
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Time Initiated " + timeFormat.format(startDate));
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("User Initiated Action: " + luserAction);
	row = lActiveSheet.createRow(getNewRow());
	row.setHeightInPoints((float) 75);

	Map<String, Integer> mapOfHeaders = new LinkedHashMap<>();
	mapOfHeaders.put("Start/End Date", 1);
	mapOfHeaders.put("Implementation Plan", 1);
	mapOfHeaders.put("User Initiated Action", 0);
	mapOfHeaders.put("User Role", 0);
	mapOfHeaders.put("Initiated by", 0);

	Map<String, Integer> mapOfSubHeaders = new LinkedHashMap<>();
	mapOfSubHeaders.put("Time - initiated", 0);
	mapOfSubHeaders.put("Time - completed", 0);
	mapOfSubHeaders.put("Impl Plan #", 0);
	mapOfSubHeaders.put("Impl #", 3);

	Systems = Arrays.asList(travelportTargetSystems);
	if (hostProfile.equals("Delta")) {
	    Systems = Arrays.asList(deltaTargetSystems);
	}
	if (luserAction.equals("buildPlan")) {
	    mapOfHeaders.put("# of artifacts in Plan", targetSystemCount);
	    mapOfHeaders.put("# of artifacts that were part of the Devl Build", targetSystemCount);
	    mapOfHeaders.put("# File Types in Plan", 6);
	    mapOfHeaders.put("Git Repositories", 1);
	    mapOfHeaders.put("Response Time", 2);
	    mapOfHeaders.put("Servers Involved", 2);

	    for (Map.Entry<String, Integer> entry : mapOfHeaders.entrySet()) {
		creatingCells(row, lCell, entry.getKey(), entry.getValue());
	    }

	    Systems.stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Systems.stream().forEachOrdered(t -> mapOfSubHeaders.put(t + " ", 0));
	    Arrays.asList(fileTypes).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Arrays.asList(gitDetails).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Arrays.asList(resTymAndServerInfo).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    row = lActiveSheet.createRow(getNewRow());
	    for (Map.Entry<String, Integer> entry : mapOfSubHeaders.entrySet()) {
		creatingCells(row, lCell, entry.getKey(), entry.getValue());
	    }
	    addCommonMergeRegions();
	    mergePoints = new int[] { Systems.size(), Systems.size(), Arrays.asList(fileTypes).size(), Arrays.asList(gitDetails).size(), 3, 3 };
	    addMergeRegion(mergePoints);
	}
	if (luserAction.equals("PlanSubmit")) {

	    mapOfHeaders.put("# of artifacts in Plan", targetSystemCount);
	    mapOfHeaders.put("# File Types in Plan", 6);
	    mapOfHeaders.put("# of Shared Objects in Staging Loadset", targetSystemCount);
	    mapOfHeaders.put("Git Repositories", 1);
	    mapOfHeaders.put("Response Time", 2);
	    mapOfHeaders.put("Servers Involved", 2);
	    for (Map.Entry<String, Integer> entry : mapOfHeaders.entrySet()) {
		creatingCells(row, lCell, entry.getKey(), entry.getValue());
	    }

	    Systems.stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Arrays.asList(fileTypes).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Systems.stream().forEachOrdered(t -> mapOfSubHeaders.put(t + " ", 0));
	    Arrays.asList(gitDetails).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Arrays.asList(resTymAndServerInfo).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    row = lActiveSheet.createRow(getNewRow());
	    for (Map.Entry<String, Integer> entry : mapOfSubHeaders.entrySet()) {
		creatingCells(row, lCell, entry.getKey(), entry.getValue());
	    }
	    addCommonMergeRegions();
	    mergePoints = new int[] { Systems.size(), Arrays.asList(fileTypes).size(), Systems.size(), Arrays.asList(gitDetails).size(), 3, 3 };
	    addMergeRegion(mergePoints);
	}
	if (luserAction.equals("CreateDVLLoadset")) {
	    mapOfHeaders.put("# of artifacts in Plan", targetSystemCount);
	    mapOfHeaders.put("# File Types in Plan", 6);
	    mapOfHeaders.put("# of Shared Objects in Loadset", targetSystemCount);
	    mapOfHeaders.put("Git Repositories", 1);
	    mapOfHeaders.put("Response Time", 2);
	    mapOfHeaders.put("Servers Involved", 2);
	    for (Map.Entry<String, Integer> entry : mapOfHeaders.entrySet()) {
		creatingCells(row, lCell, entry.getKey(), entry.getValue());
	    }
	    Systems.stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Arrays.asList(fileTypes).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Systems.stream().forEachOrdered(t -> mapOfSubHeaders.put(t + " ", 0));
	    Arrays.asList(gitDetails).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Arrays.asList(resTymAndServerInfo).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    row = lActiveSheet.createRow(getNewRow());
	    for (Map.Entry<String, Integer> entry : mapOfSubHeaders.entrySet()) {
		creatingCells(row, lCell, entry.getKey(), entry.getValue());
	    }
	    addCommonMergeRegions();
	    mergePoints = new int[] { Systems.size(), Arrays.asList(fileTypes).size(), Systems.size(), Arrays.asList(gitDetails).size(), 3, 3 };
	    addMergeRegion(mergePoints);
	}

	if (luserAction.equals("SegmentCheckout")) {
	    mapOfHeaders.put("# of artifacts Checked out", targetSystemCount);
	    mapOfHeaders.put("Git Repositories", 1);
	    mapOfHeaders.put("File TypesChecked Out", 6);
	    mapOfHeaders.put("Response Time", 2);
	    mapOfHeaders.put("Servers Involved", 2);
	    for (Map.Entry<String, Integer> entry : mapOfHeaders.entrySet()) {
		creatingCells(row, lCell, entry.getKey(), entry.getValue());
	    }
	    Systems.stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Arrays.asList(gitDetails).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Arrays.asList(fileTypes).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Arrays.asList(resTymAndServerInfo).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    row = lActiveSheet.createRow(getNewRow());
	    for (Map.Entry<String, Integer> entry : mapOfSubHeaders.entrySet()) {
		creatingCells(row, lCell, entry.getKey(), entry.getValue());
	    }
	    addCommonMergeRegions();
	    mergePoints = new int[] { Systems.size(), Arrays.asList(gitDetails).size(), Arrays.asList(fileTypes).size(), 3, 3 };
	    addMergeRegion(mergePoints);
	}
	if (luserAction.equals("SegmentCommit")) {
	    mapOfHeaders.put("# of artifacts selected for Local  Commit", targetSystemCount);
	    mapOfHeaders.put("Git Repositories", 1);
	    mapOfHeaders.put("File Types involved in the commit", 6);
	    mapOfHeaders.put("Response Time", 2);
	    mapOfHeaders.put("Servers Involved", 2);
	    for (Map.Entry<String, Integer> entry : mapOfHeaders.entrySet()) {
		creatingCells(row, lCell, entry.getKey(), entry.getValue());
	    }
	    Systems.stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Arrays.asList(gitDetails).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Arrays.asList(fileTypes).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Arrays.asList(resTymAndServerInfo).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    row = lActiveSheet.createRow(getNewRow());
	    for (Map.Entry<String, Integer> entry : mapOfSubHeaders.entrySet()) {
		creatingCells(row, lCell, entry.getKey(), entry.getValue());
	    }
	    addCommonMergeRegions();
	    mergePoints = new int[] { Systems.size(), Arrays.asList(gitDetails).size(), Arrays.asList(fileTypes).size(), 3, 3 };
	    addMergeRegion(mergePoints);
	}
	if (luserAction.equals("SegmentCheckin")) {
	    mapOfHeaders.put("# of artifacts Checked In", targetSystemCount);
	    mapOfHeaders.put("Git Repositories", 1);
	    mapOfHeaders.put("File Types involved in the checkin", 6);
	    mapOfHeaders.put("Response Time", 2);
	    mapOfHeaders.put("Servers Involved", 2);
	    for (Map.Entry<String, Integer> entry : mapOfHeaders.entrySet()) {
		creatingCells(row, lCell, entry.getKey(), entry.getValue());
	    }
	    Systems.stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Arrays.asList(gitDetails).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Arrays.asList(fileTypes).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    Arrays.asList(resTymAndServerInfo).stream().forEachOrdered(t -> mapOfSubHeaders.put(t, 0));
	    row = lActiveSheet.createRow(getNewRow());
	    for (Map.Entry<String, Integer> entry : mapOfSubHeaders.entrySet()) {
		creatingCells(row, lCell, entry.getKey(), entry.getValue());
	    }
	    addCommonMergeRegions();
	    mergePoints = new int[] { Systems.size(), Arrays.asList(gitDetails).size(), Arrays.asList(fileTypes).size(), 3, 3 };
	    addMergeRegion(mergePoints);
	}
    }

    private void addData(List<TransactionViewResponseForm> txnViewList, String hostProfile, String luserAction) {

	txnViewList.stream().forEachOrdered((txnViewData) -> {
	    totalCount = 0;
	    totalSOCount = 0;
	    Row valueRow = lActiveSheet.createRow(getNewRow());
	    Cell rowCell = null;
	    if (luserAction.equals("buildPlan")) {
		addingTxnData(valueRow, rowCell, txnViewData);
		addingSysBasedData(valueRow, rowCell, txnViewData, Systems);
		addingSoObjData(valueRow, rowCell, txnViewData, Systems);
		addingFileTypeData(valueRow, rowCell, txnViewData, fileTypes);
		addingGitData(valueRow, rowCell, txnViewData);
		addingServerAndResposneData(valueRow, rowCell, txnViewData);

	    }
	    if (luserAction.equals("PlanSubmit")) {
		addingTxnData(valueRow, rowCell, txnViewData);
		addingSysBasedData(valueRow, rowCell, txnViewData, Systems);
		addingFileTypeData(valueRow, rowCell, txnViewData, fileTypes);
		addingSoObjData(valueRow, rowCell, txnViewData, Systems);
		addingGitData(valueRow, rowCell, txnViewData);
		addingServerAndResposneData(valueRow, rowCell, txnViewData);

	    }
	    if (luserAction.equals("CreateDVLLoadset")) {
		addingTxnData(valueRow, rowCell, txnViewData);
		addingSysBasedData(valueRow, rowCell, txnViewData, Systems);
		addingFileTypeData(valueRow, rowCell, txnViewData, fileTypes);
		addingSoObjData(valueRow, rowCell, txnViewData, Systems);
		addingGitData(valueRow, rowCell, txnViewData);
		addingServerAndResposneData(valueRow, rowCell, txnViewData);

	    }
	    if (luserAction.equals("SegmentCheckout")) {
		addingTxnData(valueRow, rowCell, txnViewData);
		addingSysBasedData(valueRow, rowCell, txnViewData, Systems);
		addingGitData(valueRow, rowCell, txnViewData);
		addingFileTypeData(valueRow, rowCell, txnViewData, fileTypes);
		addingServerAndResposneData(valueRow, rowCell, txnViewData);

	    }
	    if (luserAction.equals("SegmentCommit")) {
		addingTxnData(valueRow, rowCell, txnViewData);
		addingSysBasedData(valueRow, rowCell, txnViewData, Systems);
		addingGitData(valueRow, rowCell, txnViewData);
		addingFileTypeData(valueRow, rowCell, txnViewData, fileTypes);
		addingServerAndResposneData(valueRow, rowCell, txnViewData);

	    }
	    if (luserAction.equals("SegmentCheckin")) {
		addingTxnData(valueRow, rowCell, txnViewData);
		addingSysBasedData(valueRow, rowCell, txnViewData, Systems);
		addingGitData(valueRow, rowCell, txnViewData);
		addingFileTypeData(valueRow, rowCell, txnViewData, fileTypes);
		addingServerAndResposneData(valueRow, rowCell, txnViewData);
	    }
	});

    }

    private <T> boolean isObjectNullAndEmpty(T object) {
	if (object == null) {
	    return true;
	}
	if (object instanceof String && ((String) object).isEmpty()) {
	    return true;
	}
	if (object instanceof Date && ((Date) object).toString().isEmpty()) {
	    return true;
	}
	if (object instanceof Float && ((Float) object).toString().isEmpty()) {
	    return true;
	}
	if (object instanceof List && ((List) object).isEmpty()) {
	    return true;
	}
	return false;
    }

    private String getSheetName(String userAction) {
	String sheetName = " Transaction View ";
	if (userAction.equals("planSubmit")) {
	    sheetName = "Submit";
	} else if (userAction.equals("buildPlan")) {
	    sheetName = "DevlBuild";
	} else if (userAction.equals("CreateDVLLoadset")) {
	    sheetName = "Devl LoadSet";
	} else if (userAction.equals("SegmentCheckin")) {
	    sheetName = "Checkin";
	} else if (userAction.equals("SegmentCheckout")) {
	    sheetName = "Checkout";
	} else if (userAction.equals("SegmentCommit")) {
	    sheetName = "Commit";
	}

	return sheetName;
    }

    private void getCellBlock(Row row, Cell lCell, int numberOfCell) {
	for (int i = 0; i < numberOfCell; i++) {
	    lCell = row.createCell(getNextCell());
	}

    }

    private void creatingCells(Row row, Cell lCell, String headerValue, int cellCount) {
	lCell = row.createCell(getNextCell());
	lCell.setCellValue(headerValue);
	getCellBlock(row, lCell, cellCount);
    }

    private void addCommonMergeRegions() {
	lActiveSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
	lActiveSheet.addMergedRegion(new CellRangeAddress(1, 1, 2, 3));
	lActiveSheet.addMergedRegion(new CellRangeAddress(1, 2, 4, 4));
	lActiveSheet.addMergedRegion(new CellRangeAddress(1, 2, 5, 5));
	lActiveSheet.addMergedRegion(new CellRangeAddress(1, 2, 6, 6));
    }

    private void addMergeRegion(int[] mergePoints) {
	int rowFrom = 1;
	int rowTo = 1;
	int colFrom = 7;
	int colTo = 0;
	for (int t : mergePoints) {
	    colTo = colFrom + (t - 1);
	    lActiveSheet.addMergedRegion(new CellRangeAddress(rowFrom, rowTo, colFrom, colTo));
	    colFrom = colTo + 1;
	}

    }

    // adding transaction details
    private void addingTxnData(Row valueRow, Cell rowCell, TransactionViewResponseForm txnViewData) {

	HSSFFont headerFont = lWorkBook.createFont();
	headerFont.setBoldweight(headerFont.BOLDWEIGHT_BOLD);
	headerFont.setFontHeightInPoints((short) 10);
	CellStyle cellStyle = lWorkBook.createCellStyle();
	cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);
	cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	// Date Converter
	String excelDateFormatPattern = "MM-dd-yyyy HH:mm:ss";
	CellStyle cellStyleForDate = lWorkBook.createCellStyle();
	DataFormat poiFormat = lWorkBook.createDataFormat();
	cellStyleForDate.setDataFormat(poiFormat.getFormat(excelDateFormatPattern));
	rowCell = valueRow.createCell(getNextCell());
	rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getStartDateTime()) ? txnViewData.getStartDateTime() : new Date());
	rowCell.setCellStyle(cellStyleForDate);
	rowCell = valueRow.createCell(getNextCell());
	rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getEndDateTime()) ? txnViewData.getEndDateTime() : new Date());
	rowCell.setCellStyle(cellStyleForDate);
	rowCell = valueRow.createCell(getNextCell());
	rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getPlanId()) ? txnViewData.getPlanId() : "");
	rowCell = valueRow.createCell(getNextCell());
	rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getImpId()) ? txnViewData.getImpId() : "");
	rowCell = valueRow.createCell(getNextCell());
	rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getUserAction()) ? txnViewData.getUserAction() : "");
	rowCell = valueRow.createCell(getNextCell());
	rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getUserRole()) ? txnViewData.getUserRole() : "");
	rowCell = valueRow.createCell(getNextCell());
	rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getUserName()) ? txnViewData.getUserName() : "");
    }

    // adding system based details
    private void addingSysBasedData(Row valueRow, Cell rowCell, TransactionViewResponseForm txnViewData, List<String> Systems) {
	for (String system : Systems) {
	    LOG.info(system);
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(0);
	    if (txnViewData.getTotalCountBySystem().containsKey(system)) {
		rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getTotalCountBySystem().get(system)) ? txnViewData.getTotalCountBySystem().get(system) : (long) 0);
		totalCount = totalCount + txnViewData.getTotalCountBySystem().get(system);
	    }
	    if (system.equals("Total")) {
		rowCell.setCellValue(totalCount);
	    }
	}
    }

    // adding SO objects details
    private void addingSoObjData(Row valueRow, Cell rowCell, TransactionViewResponseForm txnViewData, List<String> Systems) {
	for (String system : Systems) {
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(0);
	    if (txnViewData.getSocount().containsKey(system + " ")) {
		rowCell = valueRow.createCell(getNextCell());
		rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getSocount().get(system)) ? txnViewData.getSocount().get(system) : (long) 0);
		totalSOCount = totalSOCount + txnViewData.getSocount().get(system);
	    }
	    if (system.equals("Total ")) {
		rowCell.setCellValue(totalSOCount);
	    }
	}
    }

    // adding File Type details
    private void addingFileTypeData(Row valueRow, Cell rowCell, TransactionViewResponseForm txnViewData, String[] fileTypes) {
	for (String fileType : Arrays.asList(fileTypes)) {
	    if (fileType.equals(".asm")) {
		rowCell = valueRow.createCell(getNextCell());
		rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getAsmcount()) ? txnViewData.getAsmcount() : (long) 0);
	    } else if (fileType.equals(".sbt")) {
		rowCell = valueRow.createCell(getNextCell());
		rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getSbtcount()) ? txnViewData.getSbtcount() : (long) 0);
	    } else if (fileType.equals(".c/.cpp")) {
		rowCell = valueRow.createCell(getNextCell());
		rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getCcppcount()) ? txnViewData.getCcppcount() : (long) 0);
	    } else if (fileType.equals(".mak")) {
		rowCell = valueRow.createCell(getNextCell());
		rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getMakcount()) ? txnViewData.getMakcount() : (long) 0);
	    } else if (fileType.equals(".h/.hpp/.mac/.inc")) {
		rowCell = valueRow.createCell(getNextCell());
		rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getHeadercount()) ? txnViewData.getHeadercount() : (long) 0);
	    } else if (fileType.equals("Others")) {
		rowCell = valueRow.createCell(getNextCell());
		rowCell.setCellValue(totalCount != 0 ? totalCount - (txnViewData.getAsmcount() + txnViewData.getSbtcount() + txnViewData.getCcppcount() + txnViewData.getMakcount() + txnViewData.getHeadercount()) : 0);
	    } else if (fileType.equals("Total  ")) {
		rowCell = valueRow.createCell(getNextCell());
		rowCell.setCellValue(totalCount);
	    }
	}
    }

    // adding Git details
    private void addingGitData(Row valueRow, Cell rowCell, TransactionViewResponseForm txnViewData) {
	rowCell = valueRow.createCell(getNextCell());
	rowCell.setCellValue(txnViewData.getRepocount());
	rowCell = valueRow.createCell(getNextCell());
	rowCell.setCellValue(txnViewData.getReponamelist());
    }

    // adding Respose time and Server details
    private void addingServerAndResposneData(Row valueRow, Cell rowCell, TransactionViewResponseForm txnViewData) {
	rowCell = valueRow.createCell(getNextCell());
	rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getResponseTimeMs()) ? txnViewData.getResponseTimeMs() : (long) 0);
	rowCell = valueRow.createCell(getNextCell());
	rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getResponseTimeSec()) ? txnViewData.getResponseTimeSec() : (long) 0);
	rowCell = valueRow.createCell(getNextCell());
	rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getResponseTimeMin()) ? txnViewData.getResponseTimeMin() : "");
	rowCell = valueRow.createCell(getNextCell());
	rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getHostName()) ? txnViewData.getHostName() : "");
	rowCell = valueRow.createCell(getNextCell());
	rowCell.setCellValue(!isObjectNullAndEmpty(txnViewData.getTdx()) ? txnViewData.getTdx() : "");
	rowCell = valueRow.createCell(getNextCell());
	rowCell.setCellValue(txnViewData.getzOs());
    }

}
