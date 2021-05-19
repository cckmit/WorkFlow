/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.report;

import com.tsi.workflow.audit.uiform.TransactionResponseForm;
import com.tsi.workflow.excel.utils.ExcelAttachmentCreator;
import com.tsi.workflow.report.RFCReportCreator;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
public class SysViewReportCreator {

    private static final Logger LOG = Logger.getLogger(RFCReportCreator.class.getName());

    HSSFWorkbook lWorkBook;
    Sheet lActiveSheet;
    String lName;
    int lCurrentRow = 0;
    int lCurrentCell = 0;
    ExcelAttachmentCreator excelAttachmentCreator;

    public SysViewReportCreator() {
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

    public void addSystemViewDetails(List<TransactionResponseForm> systemViewList, String hostProfile, List<String> userId, String planId, Date startDate, Date endDate, List<String> userAction) {
	String sheetName = getSheetName(hostProfile, userId, planId, startDate, endDate, userAction);

	lActiveSheet = lWorkBook.createSheet(sheetName);
	Row row = lActiveSheet.createRow(getNewRow());
	Cell lCell = row.createCell(getNextCell());
	addHeaderDetails(row, lCell, hostProfile, userId, planId, startDate, endDate, userAction);
	addData(systemViewList);

    }

    private void addHeaderDetails(Row row, Cell lCell, String hostProfile, List<String> userId, String planId, Date startDate, Date endDate, List<String> userAction) {

	SimpleDateFormat dateFormat = new SimpleDateFormat("MM/DD/YYYY");
	SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	lCell.setCellValue("WorkFlow Application: " + hostProfile);
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Start Date: " + dateFormat.format(startDate));
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("End Date: " + dateFormat.format(endDate));
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Start Time(GMT): " + timeFormat.format(startDate));
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("End Time(GMT): " + timeFormat.format(endDate));
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("User Initiated Action: " + (userAction.isEmpty() ? "ALL" : userAction.stream().collect(Collectors.joining(","))));
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("User: " + (userId.isEmpty() ? "ALL" : userId.stream().collect(Collectors.joining(","))));
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Implementation Plan: " + (planId != null && !planId.isEmpty() ? planId : "ALL"));
	row = lActiveSheet.createRow(getNewRow());
	row.setHeightInPoints((float) 75);
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Start/End Date");
	lCell = row.createCell(getNextCell());
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Initiated by");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Implementation Plan");
	lCell = row.createCell(getNextCell());
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("User Initiated Action");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("User Role");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Response Time");
	lCell = row.createCell(getNextCell());
	lCell = row.createCell(getNextCell());
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Servers Involved");
	lCell = row.createCell(getNextCell());
	lCell = row.createCell(getNextCell());
	row = lActiveSheet.createRow(getNewRow());
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Time - initiated");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Time - completed");
	lCell = row.createCell(getNextCell());
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Impl Plan #");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Impl #");
	lCell = row.createCell(getNextCell());
	lCell = row.createCell(getNextCell());
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("ms");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("sec");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("min");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("x86 Server");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("zLinux Server");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("zOS");
	lActiveSheet.addMergedRegion(new CellRangeAddress(1, 1, 0, 1));
	lActiveSheet.addMergedRegion(new CellRangeAddress(1, 2, 2, 2));
	lActiveSheet.addMergedRegion(new CellRangeAddress(1, 1, 3, 4));
	lActiveSheet.addMergedRegion(new CellRangeAddress(1, 2, 5, 5));
	lActiveSheet.addMergedRegion(new CellRangeAddress(1, 2, 6, 6));
	lActiveSheet.addMergedRegion(new CellRangeAddress(1, 1, 7, 9));
	lActiveSheet.addMergedRegion(new CellRangeAddress(1, 1, 10, 12));
    }

    private void addData(List<TransactionResponseForm> systemViewList) {

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

	systemViewList.forEach((systemViewData) -> {
	    Row valueRow = lActiveSheet.createRow(getNewRow());
	    Cell rowCell;
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(!isObjectNullAndEmpty(systemViewData.getStartDateTime()) ? systemViewData.getStartDateTime() : new Date());
	    rowCell.setCellStyle(cellStyleForDate);
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(!isObjectNullAndEmpty(systemViewData.getEndDateTime()) ? systemViewData.getEndDateTime() : new Date());
	    rowCell.setCellStyle(cellStyleForDate);
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(!isObjectNullAndEmpty(systemViewData.getUserName()) ? systemViewData.getUserName() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(!isObjectNullAndEmpty(systemViewData.getPlanId()) ? systemViewData.getPlanId() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(!isObjectNullAndEmpty(systemViewData.getImpId()) ? systemViewData.getImpId() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(!isObjectNullAndEmpty(systemViewData.getUserAction()) ? systemViewData.getUserAction() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(!isObjectNullAndEmpty(systemViewData.getUserRole()) ? systemViewData.getUserRole() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(!isObjectNullAndEmpty(systemViewData.getResponseTimeMs()) ? systemViewData.getResponseTimeMs() : (long) 0);
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(!isObjectNullAndEmpty(systemViewData.getResponseTimeSec()) ? systemViewData.getResponseTimeSec() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(!isObjectNullAndEmpty(systemViewData.getResponseTimeMin()) ? systemViewData.getResponseTimeMin() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(!isObjectNullAndEmpty(systemViewData.getHostName()) ? systemViewData.getHostName() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(!isObjectNullAndEmpty(systemViewData.getTdx()) ? systemViewData.getTdx() : "");
	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(systemViewData.getzOs());

	});

	for (int i = 0; i <= 13; i++) {
	    lActiveSheet.autoSizeColumn(i);
	}
	if (lActiveSheet.getPhysicalNumberOfRows() > 0) {
	    Row row = lActiveSheet.getRow(lActiveSheet.getFirstRowNum());
	    Iterator<Cell> cellIterator = row.cellIterator();
	    while (cellIterator.hasNext()) {
		Cell cell = cellIterator.next();
		cell.setCellStyle(cellStyle);
		int columnIndex = cell.getColumnIndex();
		lActiveSheet.autoSizeColumn(columnIndex);
	    }
	}

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

    private String getSheetName(String hostProfile, List<String> userId, String planId, Date startDate, Date endDate, List<String> userAction) {
	String sheetName = " System View ";
	if (isObjectNullAndEmpty(planId) && isObjectNullAndEmpty(userAction) && isObjectNullAndEmpty(userId) && new SimpleDateFormat("HH:mm:ss").format(endDate).equals("23:55:00")) {
	    sheetName = "System View - default";
	} else if (isObjectNullAndEmpty(planId) && isObjectNullAndEmpty(userAction) && isObjectNullAndEmpty(userId)) {
	    sheetName = "System View - for a time period";
	} else if (!isObjectNullAndEmpty(userAction) && isObjectNullAndEmpty(planId) && isObjectNullAndEmpty(userId)) {
	    sheetName = "System View - by user action";
	} else if (!isObjectNullAndEmpty(planId) && isObjectNullAndEmpty(userId) && isObjectNullAndEmpty(userAction)) {
	    sheetName = "System View - by impl plan";
	} else if (!isObjectNullAndEmpty(userId) && isObjectNullAndEmpty(userAction) && isObjectNullAndEmpty(planId)) {
	    sheetName = "System View - by user";
	}

	return sheetName;
    }

}
