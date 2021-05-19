package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.Date;

/**
 * @author vinoth.ponnurangan
 *
 */
public class BuildQueuePlanForm {

    private String planid;

    private String targetsystem;

    private String buildtype;

    private String buildstatus;

    private String servername;

    private ImpPlan plan;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date buildtriggerdate;

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date buildstartdate;

    public String getPlanid() {
	return planid;
    }

    public void setPlanid(String planid) {
	this.planid = planid;
    }

    public String getTargetsystem() {
	return targetsystem;
    }

    public void setTargetsystem(String targetsystem) {
	this.targetsystem = targetsystem;
    }

    public String getBuildtype() {
	return buildtype;
    }

    public void setBuildtype(String buildtype) {
	this.buildtype = buildtype;
    }

    public String getBuildstatus() {
	return buildstatus;
    }

    public void setBuildstatus(String buildstatus) {
	this.buildstatus = buildstatus;
    }

    public Date getBuildtriggerdate() {
	return buildtriggerdate;
    }

    public void setBuildtriggerdate(Date buildtriggerdate) {
	this.buildtriggerdate = buildtriggerdate;
    }

    public ImpPlan getPlan() {
	return plan;
    }

    public void setPlan(ImpPlan plan) {
	this.plan = plan;
    }

    public BuildQueuePlanForm() {

    }

    public String getServername() {
	return servername.replace("//", "");
    }

    public void setServername(String servername) {
	this.servername = servername;
    }

    public Date getBuildstartdate() {
	return buildstartdate;
    }

    public void setBuildstartdate(Date buildstartdate) {
	this.buildstartdate = buildstartdate;
    }

    public BuildQueuePlanForm(String planid, String targetsystem, String buildtype, String buildstatus, ImpPlan plan, Date buildtriggerdate, String servername, Date buildstartdate) {
	this.planid = planid;
	this.targetsystem = targetsystem;
	this.buildtype = buildtype;
	this.buildstatus = buildstatus;
	this.plan = plan;
	this.buildtriggerdate = buildtriggerdate;
	this.servername = servername;
	this.buildstartdate = buildstartdate;
    }

}
