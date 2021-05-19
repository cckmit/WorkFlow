/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.Date;
import java.util.List;

public class FileExtnReportForm {

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date startDate;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date endDate;
    private List<String> systems;
    private List<String> fileExten;

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public Date getEndDate() {
	return endDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    public List<String> getSystems() {
	return systems;
    }

    public void setSystems(List<String> systems) {
	this.systems = systems;
    }

    /**
     * @return the fileExten
     */
    public List<String> getFileExten() {
	return fileExten;
    }

    /**
     * @param fileExten
     *            the fileExten to set
     */
    public void setFileExten(List<String> fileExten) {
	this.fileExten = fileExten;
    }

}