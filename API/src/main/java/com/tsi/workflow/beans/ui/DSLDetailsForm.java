package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DSLDetailsForm {
    private String systemname;
    private String loadsetname;
    private String fallbackloadsetname;
    private String vparname;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date loaddatetime;
    private String planstatus;

    public String getSystemname() {
	return systemname;
    }

    public void setSystemname(String systemname) {
	this.systemname = systemname;
    }

    public String getLoadsetname() {
	return loadsetname;
    }

    public void setLoadsetname(String loadsetname) {
	this.loadsetname = loadsetname;
    }

    public String getFallbackloadsetname() {
	return fallbackloadsetname;
    }

    public void setFallbackloadsetname(String fallbackloadsetname) {
	this.fallbackloadsetname = fallbackloadsetname;
    }

    public String getVparname() {
	return vparname;
    }

    public void setVparname(String vparname) {
	this.vparname = vparname;
    }

    public Date getLoaddatetime() {
	return loaddatetime;
    }

    public void setLoaddatetime(Date loaddatetime) {
	this.loaddatetime = loaddatetime;
    }

    public String getLoadDate() {
	return new SimpleDateFormat("yyyyMMdd").format(this.loaddatetime);
    }

    public String getLoadtime() {
	return new SimpleDateFormat("HHmm").format(this.loaddatetime);
    }

    public String getPlanstatus() {
	return planstatus;
    }

    public void setPlanstatus(String planstatus) {
	this.planstatus = planstatus;
    }

}
