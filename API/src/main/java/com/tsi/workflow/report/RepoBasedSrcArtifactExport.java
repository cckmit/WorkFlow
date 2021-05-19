package com.tsi.workflow.report;

import com.tsi.workflow.beans.ui.RepoBasedSrcArtifacts;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class RepoBasedSrcArtifactExport {
    XSSFWorkbook lWorkBook;
    Sheet lActiveSheet;
    String lName;
    int lCurrentRow = 0;
    int lCurrentCell = 0;

    public RepoBasedSrcArtifactExport() {
	lWorkBook = new XSSFWorkbook();
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

    public void addSearchResult(List<RepoBasedSrcArtifacts> pResults) {
	lActiveSheet = lWorkBook.createSheet("Source Artifacts");
	Row row = lActiveSheet.createRow(getNewRow());
	Cell lCell = row.createCell(getNextCell());
	lCell.setCellValue("File Path");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("File Name");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("Target System");
	lCell = row.createCell(getNextCell());
	lCell.setCellValue("File Type");

	pResults.forEach(srcArtifacts -> {
	    Row valueRow = lActiveSheet.createRow(getNewRow());

	    Cell rowCell;

	    rowCell = valueRow.createCell(getNextCell());

	    rowCell.setCellValue(srcArtifacts.getFilename());

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(srcArtifacts.getProgname());

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(srcArtifacts.getTargetsystem());

	    rowCell = valueRow.createCell(getNextCell());
	    rowCell.setCellValue(srcArtifacts.getFileext());

	});
    }
}
