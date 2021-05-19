/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.report;

import com.tsi.workflow.beans.dao.Project;
import com.tsi.workflow.beans.ui.AdvancedMetaSearchResult;
import com.tsi.workflow.beans.ui.FileExtnReportForm;
import com.tsi.workflow.beans.ui.ReportDetailView;
import com.tsi.workflow.beans.ui.ReportForm;
import com.tsi.workflow.beans.ui.ReportQATestingContent;
import com.tsi.workflow.beans.ui.ReportQATestingData;
import com.tsi.workflow.beans.ui.ReportQATestingSummary;
import com.tsi.workflow.beans.ui.ReportTable;
import com.tsi.workflow.beans.ui.ReportView;
import com.tsi.workflow.beans.ui.SegmentReportDetailView;
import com.tsi.workflow.beans.ui.SummaryDetailView;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.DateHelper;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author USER
 */
public class SearchExcelCreator {

    private static final Logger LOG = Logger.getLogger(SearchExcelCreator.class.getName());

    XSSFWorkbook lWorkBook;
    Sheet lActiveSheet;
    String lName;
    int lCurrentRow = 0;
    int lCurrentCell = 0;

    public SearchExcelCreator() {
	lWorkBook = new XSSFWorkbook();
    }

    public int getNewRow() {
	lCurrentCell = 0;
	return lCurrentRow++;
    }

    public int getCurrentRowValue() {
	return lCurrentRow;
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

    private void resetPage() {
	lCurrentCell = 0;
	lCurrentRow = 0;
    }

    private Cell writeCell(Row row, String cellValue, CellStyle cellStyle) throws Exception {
	Cell lCell = row.createCell(getNextCell());
	lCell.setCellStyle(cellStyle);
	lCell.setCellValue(cellValue);
	return lCell;
    }

    private Cell writeCell(Row row, String cellValue) throws Exception {
	Cell lCell = row.createCell(getNextCell());
	lCell.setCellStyle(getDefaultStyle(lWorkBook));
	lCell.setCellValue(cellValue);
	return lCell;
    }

    private Cell writeCell(Row row, Integer cellValue, CellStyle cellStyle) throws Exception {
	Cell lCell = row.createCell(getNextCell());
	lCell.setCellStyle(cellStyle);
	lCell.setCellValue(cellValue);
	return lCell;
    }

    private Cell writeCell(Row row, Integer cellValue) throws Exception {
	Cell lCell = row.createCell(getNextCell());
	lCell.setCellStyle(getDefaultStyle(lWorkBook));
	lCell.setCellValue(cellValue);
	return lCell;
    }

    private Cell writeEmptyCell(Row row, CellStyle cellStyle) throws Exception {
	Cell lCell = row.createCell(getNextCell());
	lCell.setCellValue("                    ");
	lCell.setCellStyle(cellStyle);
	return lCell;
    }

    private Cell writeEmptyCell(Row row) throws Exception {
	Cell lCell = row.createCell(getNextCell());
	lCell.setCellStyle(getDefaultStyle(lWorkBook));
	return lCell;
    }

    public void addSearchResult(Map<String, List<AdvancedMetaSearchResult>> pResults) {
	lActiveSheet = lWorkBook.createSheet("Search Result");
	Row row = lActiveSheet.createRow(getNewRow());
	Cell lCell = row.createCell(getNextCell());
	lCell.setCellValue("Plan Id");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Project Number");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Project Name");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Plan Description");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Plan Status");
	lCell = row.createCell(getNextCell());

	lCell.setCellValue("Dev Manager");
	lCell = row.createCell(getNextCell());

	lCell.setCellValue("Lead");
	lCell = row.createCell(getNextCell());

	lCell.setCellValue("Load Attendee");
	lCell = row.createCell(getNextCell());

	lCell.setCellValue("Reviewer(s)");
	lCell = row.createCell(getNextCell());

	lCell.setCellValue("Developer Name");
	lCell = row.createCell(getNextCell());

	lCell.setCellValue("Program Name");
	lCell = row.createCell(getNextCell());

	lCell.setCellValue("Functional Package(s)");
	lCell = row.createCell(getNextCell());

	lCell.setCellValue("QA Bypassed");
	lCell = row.createCell(getNextCell());

	lCell.setCellValue("Problem Ticket Number(s)");
	lCell = row.createCell(getNextCell());

	lCell.setCellValue("DBCR");
	lCell = row.createCell(getNextCell());

	lCell.setCellValue("Target System");
	lCell = row.createCell(getNextCell());

	lCell.setCellValue("Load Category");
	lCell = row.createCell(getNextCell());

	lCell.setCellValue("Special Load Instruction");
	lCell = row.createCell(getNextCell());

	lCell.setCellValue("Load Date Time");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Activated Date Time");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Fallback Activated Date Time");
	CellStyle style = lWorkBook.createCellStyle();

	pResults.forEach((key, value) -> {
	    Row valueRow = lActiveSheet.createRow(getNewRow());

	    Cell rowCell;

	    rowCell = valueRow.createCell(getNextCell());

	    rowCell.setCellValue(value.stream().findFirst().get().getPlanid());

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(value.stream().findFirst().get().getCsrnumber());

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(value.stream().findFirst().get().getProjectname());

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(value.stream().findFirst().get().getPlandescription());

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(value.stream().findFirst().get().getPlanstatus());

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(value.stream().findFirst().get().getManagername());

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(value.stream().findFirst().get().getLeadname());

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(value.stream().findFirst().get().getLoadattendee());

	    rowCell = valueRow.createCell(getNextCell());
	    String peerReviewer = null;
	    long reviewerCount = value.stream().map(AdvancedMetaSearchResult::getPeerreviewer).count();
	    peerReviewer = value.stream().map(AdvancedMetaSearchResult::getPeerreviewer).distinct().collect(Collectors.joining("\n", "", ""));
	    rowCell.setCellValue(peerReviewer);
	    style.setWrapText(true);
	    rowCell.setCellStyle(style);
	    rowCell = valueRow.createCell(getNextCell());
	    String developer = null;
	    long developerCount = value.stream().map(AdvancedMetaSearchResult::getDevelopername).count();
	    developer = value.stream().map(AdvancedMetaSearchResult::getDevelopername).distinct().collect(Collectors.joining("\n", "", ""));

	    rowCell.setCellValue(developer);
	    style.setWrapText(true);
	    rowCell.setCellStyle(style);
	    rowCell = valueRow.createCell(getNextCell());
	    String programName = null;
	    long programNameCount = value.stream().map(AdvancedMetaSearchResult::getProgramname).count();
	    programName = value.stream().map(AdvancedMetaSearchResult::getProgramname).distinct().collect(Collectors.joining("\n", "", ""));

	    rowCell.setCellValue(programName);
	    style.setWrapText(true);
	    rowCell.setCellStyle(style);
	    rowCell = valueRow.createCell(getNextCell());
	    String functionalArea = null;
	    long functionalAreaCount = value.stream().map(AdvancedMetaSearchResult::getFunctionalarea).count();
	    functionalArea = value.stream().map(AdvancedMetaSearchResult::getFunctionalarea).distinct().collect(Collectors.joining("\n", "", ""));

	    rowCell.setCellValue(functionalArea);
	    style.setWrapText(true);
	    rowCell.setCellStyle(style);
	    rowCell = valueRow.createCell(getNextCell());

	    rowCell.setCellValue(value.stream().findFirst().get().getQastatus());

	    rowCell = valueRow.createCell(getNextCell());
	    // ZTPFM-2158 Problem TicketNum added
	    String problemTickNum = null;
	    problemTickNum = value.stream().map(AdvancedMetaSearchResult::getProblemticketnum).distinct().collect(Collectors.joining("\n", "", ""));
	    rowCell.setCellValue(problemTickNum);
	    style.setWrapText(true);
	    rowCell.setCellStyle(style);

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(value.stream().findFirst().get().getDbcrname());

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(value.stream().findFirst().get().getTargetsystem());

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(value.stream().findFirst().get().getLoadcategory());

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(value.stream().findFirst().get().getLoadinstruction());

	    rowCell = valueRow.createCell(getNextCell());
	    if (value.stream().findFirst().get().getLoaddatetime() != null) {
		rowCell.setCellValue(DateHelper.convertGMTtoEST(value.stream().findFirst().get().getLoaddatetime()));
	    } else {
		rowCell.setCellValue("");
	    }

	    rowCell = valueRow.createCell(getNextCell());
	    if (value.stream().findFirst().get().getActivateddatetime() != null) {
		rowCell.setCellValue(DateHelper.convertGMTtoEST(value.stream().findFirst().get().getActivateddatetime()));
	    } else {
		rowCell.setCellValue("");
	    }

	    rowCell = valueRow.createCell(getNextCell());
	    if (value.stream().findFirst().get().getFallbackdatetime() != null) {
		rowCell.setCellValue(DateHelper.convertGMTtoEST(value.stream().findFirst().get().getFallbackdatetime()));
	    } else {
		rowCell.setCellValue("");
	    }

	});
	lActiveSheet.autoSizeColumn(8);
	lActiveSheet.autoSizeColumn(9);
	lActiveSheet.autoSizeColumn(10);
	lActiveSheet.autoSizeColumn(11);
	lActiveSheet.autoSizeColumn(13);
    }

    public boolean generateUserReport(ReportView userReportView) throws WorkflowException, Exception {
	lActiveSheet = lWorkBook.createSheet("User Report");
	for (ReportTable lReport : userReportView.getReportTable()) {
	    createHeaderForUserReport(lActiveSheet, userReportView.getReportForm(), lReport.getSystemName());
	    List<ReportDetailView> lUserDetailViews = lReport.getSystemAndDetails();
	    for (ReportDetailView lUserInfo : lUserDetailViews) {
		Row row = lActiveSheet.createRow(getNewRow());
		writeCell(row, lUserInfo.getUserName()); // Name
		writeCell(row, lUserInfo.getTotoalDeployments()); // Total Deployment
		writeCell(row, lUserInfo.getTotalSharedObjects()); // Total SO
		writeCell(row, lUserInfo.getTotalOnlineDeployments()); // Online Deployment
		writeCell(row, lUserInfo.getTotalOnlineSharedObjects()); // Online SO
		writeCell(row, lUserInfo.getTotalFallbackDeployments()); // Fallback Deployment
		writeCell(row, lUserInfo.getTotalFallbackSharedObjects()); // Fallback SO cnt
		writeCell(row, lUserInfo.getSuccessPerForDeployment()); // Success % on Deployments
		writeCell(row, lUserInfo.getSuccessPerForSourceObjects()); // Success % on SO

	    }
	    SummaryDetailView summInfo = lReport.getSystemAndSummaryDetails();
	    Row summRow = lActiveSheet.createRow(getNewRow());
	    writeCell(summRow, summInfo.getTotalUsers()); // Name
	    writeCell(summRow, summInfo.getTotalDeployments()); // Total Deployment
	    writeCell(summRow, summInfo.getTotalSourceObjects()); // Total SO
	    writeCell(summRow, summInfo.getTotalOnlineDeployments()); // Online Deployment
	    writeCell(summRow, summInfo.getTotalOnlineSharedObjects()); // Online SO
	    writeCell(summRow, summInfo.getTotalFallbackDeployments()); // Fallback Deployment
	    writeCell(summRow, summInfo.getTotalFallbackSharedObjects()); // Fallback SO cnt
	    writeCell(summRow, summInfo.getAverageSuccessPerOnOnlineDeploy()); // Success % on Deployments
	    writeCell(summRow, summInfo.getAverageSuccessPerOnSO()); // Success % on SO
	    lActiveSheet.createRow(getNewRow());
	    lActiveSheet.createRow(getNewRow());
	    lActiveSheet.createRow(getNewRow());
	    lActiveSheet.createRow(getNewRow());

	}

	for (int i = 0; i <= 15; i++) {
	    lActiveSheet.autoSizeColumn(i);
	}

	return true;
    }

    private CellStyle getDefaultStyle(Workbook workbook) {
	CellStyle style;
	style = workbook.createCellStyle();
	style.setAlignment(HorizontalAlignment.CENTER);
	style.setWrapText(true);
	style.setBorderRight(BorderStyle.THIN);
	style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	style.setBorderLeft(BorderStyle.THIN);
	style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	style.setBorderTop(BorderStyle.THIN);
	style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	style.setBorderBottom(BorderStyle.THIN);
	style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	return style;
    }

    private void createHeaderForUserReport(Sheet activeSheet, ReportForm userReportView, String systemName) throws Exception {
	Row row = lActiveSheet.createRow(getNewRow());
	row = lActiveSheet.createRow(getNewRow());
	// Start Date:<current date - 30 days> End Date:<current date> Role: <Dev
	// Manager> Target System:<RES>
	// Start Date
	SimpleDateFormat lDateFormat = new SimpleDateFormat("MM-dd-yyyy");
	CellStyle cellStyle = getDefaultStyle(lWorkBook);
	cellStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());

	writeCell(row, "Start Date: " + lDateFormat.format(userReportView.getStartDate()), cellStyle);
	writeEmptyCell(row, cellStyle);

	// End Date
	writeCell(row, "End Date: " + lDateFormat.format(userReportView.getEndDate()), cellStyle);
	writeEmptyCell(row, cellStyle);

	// Role
	writeCell(row, "Role: " + userReportView.getRole());
	writeEmptyCell(row, cellStyle);

	// Target System
	writeCell(row, "Target System: " + systemName);
	writeEmptyCell(row, cellStyle);
	writeEmptyCell(row, cellStyle);

	int topRow = lCurrentRow;
	// Name Total Deployments Total Shared Objects Online Fallback Success % by Impl
	// Plan count Success % by Shared Object
	row = lActiveSheet.createRow(getNewRow());
	writeCell(row, "Name", cellStyle);
	writeCell(row, "Total Deployments", cellStyle);
	writeCell(row, "Total Shared Objects", cellStyle);
	writeCell(row, "Online", cellStyle);
	writeEmptyCell(row, cellStyle);
	writeCell(row, "Fallback", cellStyle);
	writeEmptyCell(row, cellStyle);
	writeCell(row, "Success Percentage by Imp Plan Count", cellStyle);
	writeCell(row, "Success Percentage by Shared Objects Count", cellStyle);

	// Count of Impl Plan Count of Shared Objects Count of Impl Plan Count of Source
	// Artifacts
	row = lActiveSheet.createRow(getNewRow());
	writeEmptyCell(row, cellStyle);
	writeEmptyCell(row, cellStyle);
	writeEmptyCell(row, cellStyle);
	writeCell(row, "Count of Imp Plan", cellStyle);
	writeCell(row, "Count of Shared Objects", cellStyle);
	writeCell(row, "Count of Imp Plan", cellStyle);
	writeCell(row, "Count of Shared Objects", cellStyle);
	writeEmptyCell(row, cellStyle);
	writeEmptyCell(row, cellStyle);

	int nameCol = 0;
	int planCol = 1;
	int sOCol = 2;
	int onlineCol = 3;
	int FallbackCol = 5;
	int PerDeploymentCol = 7;
	int PerSOCol = 8;

	activeSheet.addMergedRegion(new CellRangeAddress(topRow, topRow + 1, nameCol, nameCol)); // Name
	activeSheet.addMergedRegion(new CellRangeAddress(topRow, topRow + 1, planCol, planCol)); // Deployment Count
	activeSheet.addMergedRegion(new CellRangeAddress(topRow, topRow + 1, sOCol, sOCol)); // Deployment Count
	activeSheet.addMergedRegion(new CellRangeAddress(topRow, topRow, onlineCol, onlineCol + 1)); // Online
	activeSheet.addMergedRegion(new CellRangeAddress(topRow, topRow, FallbackCol, FallbackCol + 1)); // Fallback
	activeSheet.addMergedRegion(new CellRangeAddress(topRow, topRow + 1, PerDeploymentCol, PerDeploymentCol)); // Name
	activeSheet.addMergedRegion(new CellRangeAddress(topRow, topRow + 1, PerSOCol, PerSOCol)); // Name
    }

    private void createHeaderForFuncAreaReport(Sheet activeSheet, ReportForm reportForm, String systemName) throws Exception {
	Row row = lActiveSheet.createRow(getNewRow());
	row = lActiveSheet.createRow(getNewRow());

	SimpleDateFormat lDateFormat = new SimpleDateFormat("MM-dd-yyyy");
	CellStyle cellStyle = getDefaultStyle(lWorkBook);
	cellStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());

	// Start Date:<current date - 30 days> End Date:<current date> Functional
	// Package(s): <PNR, FLS> Target System: <RES>
	writeEmptyCell(row, cellStyle);
	writeCell(row, "Start Date: " + lDateFormat.format(reportForm.getStartDate()), cellStyle); // Start Date
	writeCell(row, "End Date: " + lDateFormat.format(reportForm.getEndDate()), cellStyle); // End Date
	writeCell(row, "Target System: " + systemName); // Target System
	String lFuncDesc = "";
	if (reportForm.getFuncAreas() != null) {
	    lFuncDesc = String.join(",", reportForm.getFuncAreas());
	}
	writeEmptyCell(row, cellStyle);
	writeEmptyCell(row, cellStyle);
	writeCell(row, "Functional Packages: " + lFuncDesc);

	// Name|Description of the Functional Package|Total Shared Objects
	// Deployed|Count of Source Artifacts marked ONLINE|Count of Source Artifacts
	// marked FALLBACK|Success %
	row = lActiveSheet.createRow(getNewRow());
	writeCell(row, "Name", cellStyle);
	writeCell(row, "Description of the Functional Package", cellStyle);
	writeCell(row, "Total Shared Objects Deployed", cellStyle);
	writeCell(row, "Count of Source Artifacts marked ONLINE", cellStyle);
	writeCell(row, "Count of Source Artifacts marked FALLBACK", cellStyle);
	writeCell(row, "Success %", cellStyle);
	writeEmptyCell(row, cellStyle);

    }

    public boolean generateFuncAreaReport(ReportView funcAreaReportView) throws WorkflowException, Exception {
	lActiveSheet = lWorkBook.createSheet("FuncArea Report");
	for (ReportTable lReport : funcAreaReportView.getReportTable()) {
	    createHeaderForFuncAreaReport(lActiveSheet, funcAreaReportView.getReportForm(), lReport.getSystemName());
	    List<ReportDetailView> lFuncDetailViews = lReport.getSystemAndDetails();
	    for (ReportDetailView lFuncInfo : lFuncDetailViews) {
		Row row = lActiveSheet.createRow(getNewRow());
		writeCell(row, lFuncInfo.getFuncArea()); // Func Area Name
		writeCell(row, lFuncInfo.getFuncAreaDesc()); // Func Area Desc
		writeCell(row, lFuncInfo.getTotalSharedObjects()); // Total SO
		writeCell(row, lFuncInfo.getTotalOnlineSegmentsCount()); // Online Segments per FuncArea
		writeCell(row, lFuncInfo.getTotalFallbackSegmentsCount()); // Fallback segments per FuncArea
		writeCell(row, lFuncInfo.getSuccessPerFunc()); // Average Success per
		writeEmptyCell(row);
	    }
	    SummaryDetailView summInfo = lReport.getSystemAndSummaryDetails();
	    Row summRow = lActiveSheet.createRow(getNewRow());
	    writeCell(summRow, summInfo.getTotalFuncAreaCnt()); // Total Func Count
	    writeEmptyCell(summRow);
	    writeCell(summRow, summInfo.getTotalSourceObjects()); // Total SO
	    writeCell(summRow, summInfo.getTotalOnlineSegments()); // Total Online segments
	    writeCell(summRow, summInfo.getTotalFallbackSegments()); // Total fallback segments
	    writeCell(summRow, summInfo.getSuccessPerFunc()); // Average Success percentage
	    writeEmptyCell(summRow);

	    lActiveSheet.createRow(getNewRow());
	    lActiveSheet.createRow(getNewRow());
	    lActiveSheet.createRow(getNewRow());
	    lActiveSheet.createRow(getNewRow());

	}

	for (int i = 0; i <= 15; i++) {
	    lActiveSheet.autoSizeColumn(i);
	}

	return true;
    }

    /**
     * Created by : Ramkumar Seenivasan Date :08/12/2019 JIRA : 2037 Exporting
     * RepoReport data
     */
    public boolean generateRepoReport(FileExtnReportForm fileExtnReportForm, List<SegmentReportDetailView> segmentReportDetailView) throws Exception {
	lActiveSheet = lWorkBook.createSheet("Repository Report");
	createHeaderForRepoReport(lActiveSheet, fileExtnReportForm);
	for (SegmentReportDetailView pSegmentReportDetailView : segmentReportDetailView) {
	    Row row = lActiveSheet.createRow(getNewRow());
	    writeCell(row, pSegmentReportDetailView.getProgramName()); // Source Artifact Name
	    writeCell(row, pSegmentReportDetailView.getTotoalOnlineDeployments()); // Total Online Deployments
	    writeCell(row, pSegmentReportDetailView.getTotalSecuredDeployments()); // Total Secured Deployments
	    writeCell(row, pSegmentReportDetailView.getTotalActiveSegCount()); // Total Active Deployments
	    writeCell(row, pSegmentReportDetailView.getTotalAllCount()); // Total Deployments
	}
	for (int i = 0; i <= 4; i++) {
	    lActiveSheet.autoSizeColumn(i);
	}
	return Boolean.TRUE;
    }

    private Boolean createHeaderForQATestingReport(Sheet activeSheet, ReportForm reportForm) throws Exception {
	Row row = activeSheet.createRow(getNewRow());
	row = activeSheet.createRow(getNewRow());
	SimpleDateFormat lDateFormat = new SimpleDateFormat("MM-dd-yyyy");
	CellStyle cellStyle = getDefaultStyle(lWorkBook);
	cellStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
	activeSheet.addMergedRegion(new CellRangeAddress(getCurrentRowValue() - 1, getCurrentRowValue() - 1, 1, 4));
	activeSheet.addMergedRegion(new CellRangeAddress(getCurrentRowValue() - 1, getCurrentRowValue() - 1, 5, 8));
	// First Row
	// | |Start Date:<current date - 30 days>| | | |End Date:<current date>| | | |
	writeCell(row, "                           ", cellStyle); // For Column adjustment
	writeCell(row, "Start Date: " + lDateFormat.format(reportForm.getStartDate()), cellStyle); // Start Date
	writeEmptyCell(row, cellStyle);
	writeEmptyCell(row, cellStyle);
	writeEmptyCell(row, cellStyle);
	writeCell(row, "End Date: " + lDateFormat.format(reportForm.getEndDate()), cellStyle); // End Date
	writeEmptyCell(row, cellStyle);
	writeEmptyCell(row, cellStyle);
	writeEmptyCell(row, cellStyle);

	// Second Row
	// DevManager|QA Functional Testing| | | |QA Regression Testing| | | |
	row = activeSheet.createRow(getNewRow());
	int currentRow = getCurrentRowValue() - 1;
	activeSheet.addMergedRegion(new CellRangeAddress(getCurrentRowValue() - 1, getCurrentRowValue() - 1, 1, 4));
	activeSheet.addMergedRegion(new CellRangeAddress(getCurrentRowValue() - 1, getCurrentRowValue() - 1, 5, 8));

	writeCell(row, "Dev Manger", cellStyle); // DevManager
	writeCell(row, "QA Functional Testing ", cellStyle); // QA Functional Testing
	writeEmptyCell(row, cellStyle);
	writeEmptyCell(row, cellStyle);
	writeEmptyCell(row, cellStyle);
	writeCell(row, "QA Regression Testing ", cellStyle); // End Date
	writeEmptyCell(row, cellStyle);
	writeEmptyCell(row, cellStyle);
	writeEmptyCell(row, cellStyle);

	// Third Row Row
	// DevManager|Online| |Fallback | |Online| |Fallback| |
	row = activeSheet.createRow(getNewRow());
	activeSheet.addMergedRegion(new CellRangeAddress(getCurrentRowValue() - 1, getCurrentRowValue() - 1, 1, 2));
	activeSheet.addMergedRegion(new CellRangeAddress(getCurrentRowValue() - 1, getCurrentRowValue() - 1, 3, 4));
	activeSheet.addMergedRegion(new CellRangeAddress(getCurrentRowValue() - 1, getCurrentRowValue() - 1, 5, 6));
	activeSheet.addMergedRegion(new CellRangeAddress(getCurrentRowValue() - 1, getCurrentRowValue() - 1, 7, 8));

	writeEmptyCell(row, cellStyle);
	writeCell(row, "Online ", cellStyle); // QA Functional Testing - Online
	writeEmptyCell(row, cellStyle);
	writeCell(row, "Fallback ", cellStyle); // QA Functional Testing - Fallback
	writeEmptyCell(row, cellStyle);
	writeCell(row, "Online ", cellStyle); // QA Regression Testing - Online
	writeEmptyCell(row, cellStyle);
	writeCell(row, "Fallback ", cellStyle); // QA Regression Testing - Fallback
	writeEmptyCell(row, cellStyle);

	// Fourth Row Row
	// DevManager|Passed|Bypassed|Passed|Bypassed|Passed|Bypassed|
	row = activeSheet.createRow(getNewRow());
	activeSheet.addMergedRegion(new CellRangeAddress(currentRow, currentRow + 2, 0, 0));
	writeEmptyCell(row, cellStyle);
	writeCell(row, "Passed ", cellStyle); // QA Functional Testing - Online - Passed
	writeCell(row, "Bypassed ", cellStyle); // QA Functional Testing - Online - Bypassed
	writeCell(row, "Passed ", cellStyle); // QA Functional Testing - Online - Passed
	writeCell(row, "Bypassed ", cellStyle); // QA Functional Testing - Online - Bypassed

	writeCell(row, "Passed ", cellStyle); // QA Regression Testing - Online - Passed
	writeCell(row, "Bypassed ", cellStyle); // QA Regression Testing - Online - Bypassed
	writeCell(row, "Passed ", cellStyle); // QA Regression Testing - Online - Passed
	writeCell(row, "Bypassed ", cellStyle); // QA Regression Testing - Online - Bypassed

	for (int i = 0; i <= 15; i++) {
	    lActiveSheet.autoSizeColumn(i);
	}

	return Boolean.TRUE;
    }

    /*
     * JIRA Ticket : 2038 Created By: Radhakrishnan Created Dt: 06-Aug-2019
     * Description: Export the report of online and fallback plan with details on QA
     * Testing
     */
    public boolean generateQATestingReport(ReportQATestingData reportData) throws WorkflowException, Exception {
	lActiveSheet = lWorkBook.createSheet("QA Testing Report");

	createHeaderForQATestingReport(lActiveSheet, reportData.getReportForm());
	for (ReportQATestingContent lData : reportData.getDetailData()) {
	    Row row = lActiveSheet.createRow(getNewRow());
	    writeCell(row, lData.getDevManager()); // DevManager Name
	    writeCell(row, lData.getQaFuncOnlinePassedCnt()); // QA Functional Online Passed Count
	    writeCell(row, lData.getQaFuncOnlineBypassedCnt()); // QA Functional Online Bypassed Count
	    writeCell(row, lData.getQaFuncFallbackPassedCnt()); // QA Functional Fallback Passed Count
	    writeCell(row, lData.getQaFuncFallbackBypassedCnt()); // QA Functional Fallback Bypassed Count
	    writeCell(row, lData.getQaRegOnlinePassedCnt()); // QA regression Online Passed Count
	    writeCell(row, lData.getQaRegOnlineBypassedCnt()); // QA regression Online Bypassed Count
	    writeCell(row, lData.getQaRegFallbackPassedCnt()); // QA Regression Fallback Passed Count
	    writeCell(row, lData.getQaRegFallbackBypassedCnt()); // QA Regression Fallback Bypassed Count

	}
	Row row = lActiveSheet.createRow(getNewRow());
	ReportQATestingSummary lSummData = reportData.getSummaryData();
	writeCell(row, "Summary");
	writeCell(row, lSummData.getTotalQaFuncOnlinePassedCnt()); // Total QA Functional Online Passed Count
	writeCell(row, lSummData.getTotalQaFuncOnlineBypassedCnt()); // Total QA Functional Online Bypassed Count
	writeCell(row, lSummData.getTotalQaFuncFallbackPassedCnt()); // Toal QA Functional Fallback Passed Count
	writeCell(row, lSummData.getTotalQaFuncFallbackBypassedCnt()); // Total QA Functional Fallback Bypassed Count
	writeCell(row, lSummData.getTotalQaRegOnlinePassedCnt()); // Total QA regression Online Passed Count
	writeCell(row, lSummData.getTotalQaRegOnlineBypassedCnt()); // Total QA regression Online Bypassed Count
	writeCell(row, lSummData.getTotalQaRegFallbackPassedCnt()); // Total QA Regression Fallback Passed Count
	writeCell(row, lSummData.getTotalQaRegFallbackBypassedCnt()); // Total QA Regression Fallback Bypassed Count

	lActiveSheet.createRow(getNewRow());
	lActiveSheet.createRow(getNewRow());
	lActiveSheet.createRow(getNewRow());
	lActiveSheet.createRow(getNewRow());

	return Boolean.TRUE;
    }

    /**
     * Created By :Ramkumar Seenivasan Date:08/09/2019 JIRA :2224 Exporting Data For
     * Project NBR Display Screen
     */
    public boolean generateDTNReport(List<Project> dTNReport) throws Exception {
	lActiveSheet = lWorkBook.createSheet("DTN Report");
	createHeaderForDTNReport();
	for (Project dtnProject : dTNReport) {
	    Row row = lActiveSheet.createRow(getNewRow());
	    writeCell(row, dtnProject.getProjectNumber());// Project Number
	    writeCell(row, dtnProject.getManagerName()); // Project Manager
	    writeCell(row, dtnProject.getProjectName()); // Project Title
	    writeCell(row, dtnProject.getLineOfBusiness()); // Line of Business
	    writeCell(row, dtnProject.getSponsorId()); // Project Sponsor
	}
	for (int i = 0; i <= 4; i++) {
	    lActiveSheet.autoSizeColumn(i);
	}
	return Boolean.TRUE;
    }

    private void createHeaderForRepoReport(Sheet lActiveSheet, FileExtnReportForm fileExtnReportForm) throws Exception {
	Row row = lActiveSheet.createRow(getNewRow());

	SimpleDateFormat lDateFormat = new SimpleDateFormat("MM-dd-yyyy");
	writeEmptyCell(row);
	writeCell(row, "Start Date: " + lDateFormat.format(fileExtnReportForm.getStartDate())); // Start Date
	writeCell(row, "End Date: " + lDateFormat.format(fileExtnReportForm.getEndDate())); // End Date
	String lTargetSystem = "";
	if (fileExtnReportForm.getSystems() == null || fileExtnReportForm.getSystems().isEmpty()) {
	    lTargetSystem = "ALL";
	} else if (fileExtnReportForm.getSystems() != null) {
	    lTargetSystem = String.join(",", fileExtnReportForm.getSystems());
	}
	writeCell(row, "Target System: " + lTargetSystem); // Target System

	String lFileExten = "";
	if (fileExtnReportForm.getFileExten() == null || fileExtnReportForm.getFileExten().isEmpty()) {
	    lFileExten = "ALL";
	} else if (fileExtnReportForm.getFileExten() != null) {
	    lFileExten = String.join(",", fileExtnReportForm.getFileExten());
	}
	writeCell(row, "File types to be included: " + lFileExten);

	row = lActiveSheet.createRow(getNewRow());
	writeCell(row, "Source Artifact");
	writeCell(row, "# of times deployed in Production during the reporting period (ONLINE/FALLBACK/Deployed in Production)");
	writeCell(row, "# of times checked out in Secured Implementation plans during the reporting period (Submitted, ..... Ready for Production Deployment)");
	writeCell(row, "# of times checked out in unsecured implementation plans during the reporting period (Active)");
	writeCell(row, "Total (Production, Secured, Active)");

    }

    private void createHeaderForDTNReport() throws Exception {
	Row row = lActiveSheet.createRow(getNewRow());
	row = lActiveSheet.createRow(getNewRow());
	CellStyle cellStyle = getDefaultStyle(lWorkBook);
	cellStyle.setFillBackgroundColor(IndexedColors.YELLOW.getIndex());
	writeCell(row, "Project Number", cellStyle);
	writeCell(row, "Project Manager", cellStyle);
	writeCell(row, "Project Title", cellStyle);
	writeCell(row, "Line of Business", cellStyle);
	writeCell(row, "Project Sponsor", cellStyle);
    }
}
