/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dto.SystemLoadDTO;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ramkumar.seenivasan
 */
public class AssigendPlanView {

    private ImpPlan plan;
    private List<SystemLoadDTO> systemLoadDetails;

    public ImpPlan getPlan() {
	return plan;
    }

    public void setPlan(ImpPlan plan) {
	this.plan = plan;
    }

    public List<SystemLoadDTO> getSystemLoadDetails() {
	return systemLoadDetails;
    }

    public void setSystemLoadDetails(List<SystemLoadDTO> systemLoadDetails) {
	this.systemLoadDetails = systemLoadDetails;
    }

    public Date getMinLoadDateTime() {
	return getSystemLoadDetails().stream().map(SystemLoadDTO::getSystemLoad).filter(x -> x.getLoadDateTime() != null).map(x -> x.getLoadDateTime()).max(Date::compareTo).isPresent() ? getSystemLoadDetails().stream().map(SystemLoadDTO::getSystemLoad).filter(x -> x.getLoadDateTime() != null).map(x -> x.getLoadDateTime()).max(Date::compareTo).get() : null;
    }

    public String getPlanID() {
	return getPlan().getId();
    }

    public String getPlanDesc() {
	return getPlan().getPlanDesc();
    }

    public String getCreatedBy() {
	return getPlan().getCreatedBy();
    }

}
