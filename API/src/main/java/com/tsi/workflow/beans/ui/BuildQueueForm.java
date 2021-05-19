package com.tsi.workflow.beans.ui;

/**
 * @author vinoth.ponnurangan
 *
 */
public class BuildQueueForm {

    private String planid;

    private String targetsystem;

    private String buildtype;

    private String buildstatus;

    private String buildstartdate;

    private String servername;

    private String buildtriggerdate;

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

    public String getBuildtriggerdate() {
	return buildtriggerdate;
    }

    public void setBuildtriggerdate(String buildtriggerdate) {
	this.buildtriggerdate = buildtriggerdate;
    }

    public String getServername() {
	return servername;
    }

    public void setServername(String servername) {
	this.servername = servername;
    }

    public String getBuildstartdate() {
	return buildstartdate;
    }

    public void setBuildstartdate(String buildstartdate) {
	this.buildstartdate = buildstartdate;
    }

    @Override
    public String toString() {
	return "BuildQueueForm [planid=" + planid + ", targetsystem=" + targetsystem + ", buildtype=" + buildtype + ", buildstatus=" + buildstatus + ", buildtriggerdate=" + buildtriggerdate + ", buildstartdate=" + buildstartdate + "]";
    }

}
