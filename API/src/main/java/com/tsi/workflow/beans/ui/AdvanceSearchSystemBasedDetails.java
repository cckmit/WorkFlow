package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.Date;

public class AdvanceSearchSystemBasedDetails {

    private String targetsystem;
    private String functionalarea;
    private String qastatus;
    private String loadcategory;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date fallbackdatetime;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date activateddatetime;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date loaddatetime;
    private String progNames;

    public String getTargetsystem() {
	return targetsystem;
    }

    public void setTargetsystem(String targetsystem) {
	this.targetsystem = targetsystem;
    }

    public String getFunctionalarea() {
	return functionalarea;
    }

    public void setFunctionalarea(String functionalarea) {
	this.functionalarea = functionalarea;
    }

    public String getQastatus() {
	return qastatus;
    }

    public void setQastatus(String qastatus) {
	this.qastatus = qastatus;
    }

    public String getLoadcategory() {
	return loadcategory;
    }

    public void setLoadcategory(String loadcategory) {
	this.loadcategory = loadcategory;
    }

    public Date getFallbackdatetime() {
	return fallbackdatetime;
    }

    public void setFallbackdatetime(Date fallbackdatetime) {
	this.fallbackdatetime = fallbackdatetime;
    }

    public Date getActivateddatetime() {
	return activateddatetime;
    }

    public void setActivateddatetime(Date activateddatetime) {
	this.activateddatetime = activateddatetime;
    }

    public Date getLoaddatetime() {
	return loaddatetime;
    }

    public void setLoaddatetime(Date loaddatetime) {
	this.loaddatetime = loaddatetime;
    }

    public String getProgNames() {
	return progNames;
    }

    public void setProgNames(String progNames) {
	this.progNames = progNames;
    }

}
