/**
 * 
 */
package com.tsi.workflow.beans.ui;

import java.util.Date;

/**
 * @author vinoth.ponnurangan
 *
 */
public class DependenciesForm {

    private String planid;

    private String targetsystem;

    private String loadtype;

    private String status;

    private Date loaddatetime;

    private String segments;

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

    public String getLoadtype() {
	return loadtype;
    }

    public void setLoadtype(String loadtype) {
	this.loadtype = loadtype;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public Date getLoaddatetime() {
	return loaddatetime;
    }

    public void setLoaddatetime(Date loaddatetime) {
	this.loaddatetime = loaddatetime;
    }

    public String getSegments() {
	return segments;
    }

    public void setSegments(String segments) {
	this.segments = segments;
    }

    @Override
    public String toString() {
	return "DependenciesForm [planid=" + planid + ", targetsystem=" + targetsystem + ", loadtype=" + loadtype + ", status=" + status + ", loaddatetime=" + loaddatetime + ", segments=" + segments + "]";
    }

}
