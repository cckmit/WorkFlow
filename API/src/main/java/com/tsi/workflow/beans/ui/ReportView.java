/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Radha.Adhimoolam
 */
public class ReportView {

    private ReportForm reportForm;
    private List<ReportTable> reportTable = new ArrayList();

    public ReportView() {
    }

    public ReportForm getReportForm() {
	return reportForm;
    }

    public void setReportForm(ReportForm reportForm) {
	this.reportForm = reportForm;
    }

    public List<ReportTable> getReportTable() {
	return reportTable;
    }

    public void setReportTable(List<ReportTable> reportTable) {
	this.reportTable = reportTable;
    }

}
