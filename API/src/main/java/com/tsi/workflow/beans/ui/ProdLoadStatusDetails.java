/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

/**
 *
 * @author vamsi.krishnarao
 */
public class ProdLoadStatusDetails {

    private String planid;
    private Integer systemid;
    private String loadsetstatus;

    public String getPlanid() {
	return planid;
    }

    public void setPlanid(String planid) {
	this.planid = planid;
    }

    public Integer getSystemid() {
	return systemid;
    }

    public void setSystemid(Integer systemid) {
	this.systemid = systemid;
    }

    public String getLoadsetstatus() {
	return loadsetstatus;
    }

    public void setLoadsetstatus(String loadsetstatus) {
	this.loadsetstatus = loadsetstatus;
    }
}
