/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tsi.workflow.beans.ui;

import java.util.List;

public class RepoReportView {

    private FileExtnReportForm fileExtnReportForm;
    private List<SegmentReportDetailView> systemAndUserDetails;

    public RepoReportView() {
    }

    public FileExtnReportForm getFileExtnReportForm() {
	return fileExtnReportForm;
    }

    public void setFileExtnReportForm(FileExtnReportForm fileExtnReportForm) {
	this.fileExtnReportForm = fileExtnReportForm;
    }

    public List<SegmentReportDetailView> getsystemAndUserDetails() {
	return systemAndUserDetails;
    }

    public void setsystemAndUserDetails(List<SegmentReportDetailView> systemAndUserDetails) {
	this.systemAndUserDetails = systemAndUserDetails;
    }
}
