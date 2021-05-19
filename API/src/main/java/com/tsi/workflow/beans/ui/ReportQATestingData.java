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
public class ReportQATestingData {
    private ReportForm reportForm;
    private List<ReportQATestingContent> detailData = new ArrayList();
    private ReportQATestingSummary summaryData;

    public ReportQATestingData() {
    }

    public ReportForm getReportForm() {
	return reportForm;
    }

    public void setReportForm(ReportForm reportForm) {
	this.reportForm = reportForm;
    }

    public List<ReportQATestingContent> getDetailData() {
	return detailData;
    }

    public void setDetailData(List<ReportQATestingContent> detailData) {
	this.detailData = detailData;
    }

    public ReportQATestingSummary getSummaryData() {
	return summaryData;
    }

    public void setSummaryData(ReportQATestingSummary summaryData) {
	this.summaryData = summaryData;
    }

}
