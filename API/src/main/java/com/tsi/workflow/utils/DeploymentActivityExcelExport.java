package com.tsi.workflow.utils;

import com.tsi.workflow.beans.ui.SegmentBasedActionDetail;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

@Component
public class DeploymentActivityExcelExport {

    private static final Logger LOG = Logger.getLogger(DeploymentActivityExcelExport.class.getName());

    public JSONResponse generateDeploymentActivitiesInExcel(List<SegmentBasedActionDetail> segBasedActivityDetails) {

	JSONResponse lResponse = new JSONResponse();
	XSSFWorkbook workbook = new XSSFWorkbook();
	XSSFSheet workSheet = workbook.createSheet("Deployment Activity");
	XSSFCellStyle normalStyle = getNormalStyle(workbook);
	XSSFCellStyle headerStyle = getHeaderStyle(workbook);
	try {
	    processCachedList(workbook, workSheet, segBasedActivityDetails, normalStyle, headerStyle);
	    autoSizeColumns(workbook);
	    ByteArrayOutputStream lExcelStream = new ByteArrayOutputStream();
	    workbook.write(lExcelStream);
	    lResponse.setData(lExcelStream.toByteArray());
	    lResponse.setStatus(Boolean.TRUE);
	    lResponse.setMetaData("application/vnd.ms-excel");
	    lExcelStream.close();
	    workbook.close();
	} catch (IOException e) {
	    LOG.error("Error in Excel Creation ", e);
	    lResponse.setErrorMessage("Error in Downloading Report");
	    lResponse.setStatus(Boolean.FALSE);
	    e.printStackTrace();
	}
	return lResponse;
    }

    private void processCachedList(XSSFWorkbook workbook, XSSFSheet workSheet, List<SegmentBasedActionDetail> segBasedActivityDetails, XSSFCellStyle normalStyle, XSSFCellStyle headerStyle) {
	int currentRow = 0;
	writeHeader(workbook, workSheet, currentRow, headerStyle);
	currentRow++;
	writeActivityDetails(workbook, workSheet, segBasedActivityDetails, currentRow, normalStyle);
    }

    private int writeActivityDetails(XSSFWorkbook workbook, XSSFSheet sheet, List<SegmentBasedActionDetail> segBasedActivityDetails, int currentRow, XSSFCellStyle normalStyle) {
	int processingRow = currentRow;

	for (SegmentBasedActionDetail seg : segBasedActivityDetails) {
	    XSSFRow row = sheet.createRow(processingRow);
	    int index = 0;
	    setCellValue(row, index++, String.valueOf(seg.getEnvironment()), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getId()), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getPlanstatus()), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getTargetsystem()), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getCatname()), normalStyle);
	    setCellValue(row, index++, String.valueOf(DateHelper.convertGMTtoEST(seg.getLoaddatetime())), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getProductionstatus()), normalStyle);
	    setCellValue(row, index++, String.valueOf(DateHelper.convertGMTtoEST(seg.getModifieddate())), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getDevmanagername()), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getLeadname()), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getLoadattendee()), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getDevname()), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getProgramname()), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getPeerreviewer()), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getFuncarea()), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getCsrno()), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getProjectname()), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getPlanDesc()), normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getQastatus()), normalStyle);
	    // TODO Need to get DBCR
	    setCellValue(row, index++, "", normalStyle);
	    setCellValue(row, index++, String.valueOf(seg.getLoadinstruction()), normalStyle);
	    processingRow++;
	}
	return processingRow;
    }

    private void setCellValue(XSSFRow row, int index, String cellValue, XSSFCellStyle style) {
	XSSFCell cell = row.createCell(index);
	cell.setCellValue(cellValue);
	cell.setCellStyle(style);
    }

    private void writeHeader(XSSFWorkbook workbook, XSSFSheet sheet, int currentRow, XSSFCellStyle headerStyle) {
	int columnIndex = 0;
	XSSFRow row = sheet.createRow(currentRow);
	setCellValue(row, columnIndex++, "Environment", headerStyle);
	setCellValue(row, columnIndex++, "Plan ID", headerStyle);
	setCellValue(row, columnIndex++, "Plan Status", headerStyle);
	setCellValue(row, columnIndex++, "Target system", headerStyle);
	setCellValue(row, columnIndex++, "Load Category", headerStyle);
	setCellValue(row, columnIndex++, "Load Date Time", headerStyle);
	setCellValue(row, columnIndex++, "Deployment Action", headerStyle);
	setCellValue(row, columnIndex++, "Deployment activity date/time", headerStyle);
	setCellValue(row, columnIndex++, "Dev Manager", headerStyle);
	setCellValue(row, columnIndex++, "Lead", headerStyle);
	setCellValue(row, columnIndex++, "Load Attendee", headerStyle);
	setCellValue(row, columnIndex++, "Developer(s) ", headerStyle);
	setCellValue(row, columnIndex++, "Program names", headerStyle);
	setCellValue(row, columnIndex++, "Reviewer(s)", headerStyle);
	setCellValue(row, columnIndex++, "Package name", headerStyle);
	setCellValue(row, columnIndex++, "CSR Number", headerStyle);
	setCellValue(row, columnIndex++, "Project Name", headerStyle);
	setCellValue(row, columnIndex++, "Plan Description", headerStyle);
	setCellValue(row, columnIndex++, "QA Bypassed", headerStyle);
	setCellValue(row, columnIndex++, "DBCR", headerStyle);
	setCellValue(row, columnIndex++, "Special Load Instruction", headerStyle);
    }

    private XSSFCellStyle getHeaderStyle(XSSFWorkbook workbook) {
	XSSFCellStyle style = workbook.createCellStyle();
	XSSFFont font = workbook.createFont();
	font.setBold(true);
	style.setFont(font);
	style.setShrinkToFit(true);
	return style;
    }

    private XSSFCellStyle getNormalStyle(XSSFWorkbook workbook) {
	XSSFCellStyle style = workbook.createCellStyle();
	XSSFFont font = workbook.createFont();
	font.setBold(false);
	style.setFont(font);
	style.setShrinkToFit(true);
	return style;
    }

    private void autoSizeColumns(XSSFWorkbook workbook) {
	int sheetCount = workbook.getNumberOfSheets();
	for (int i = 0; i < sheetCount; i++) {
	    XSSFSheet sheet = workbook.getSheetAt(i);
	    for (int k = 0; k <= 22; k++) {
		sheet.autoSizeColumn(k);
	    }
	}
    }
}
