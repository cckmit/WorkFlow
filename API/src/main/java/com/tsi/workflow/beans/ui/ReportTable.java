/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import java.util.List;

/**
 *
 * @author Radha.Adhimoolam
 */
public class ReportTable {

    private String systemName;
    private List<ReportDetailView> systemAndDetails;
    private SummaryDetailView SystemAndSummaryDetails;

    public ReportTable() {
    }

    public String getSystemName() {
	return systemName;
    }

    public void setSystemName(String systemName) {
	this.systemName = systemName;
    }

    public List<ReportDetailView> getSystemAndDetails() {
	return systemAndDetails;
    }

    public void setSystemAndDetails(List<ReportDetailView> systemAndDetails) {
	this.systemAndDetails = systemAndDetails;
    }

    public SummaryDetailView getSystemAndSummaryDetails() {
	return SystemAndSummaryDetails;
    }

    public void setSystemAndSummaryDetails(SummaryDetailView SystemAndSummaryDetails) {
	this.SystemAndSummaryDetails = SystemAndSummaryDetails;
    }

}
