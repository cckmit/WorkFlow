/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dto.SystemLoadDTO;
import java.util.List;

/**
 *
 * @author ramkumar.seenivasan
 */
public class ImpPlanInboxView {

    private ImpPlanView planView;
    private List<SystemLoadDTO> systemLoadDetails;

    public ImpPlanView getPlanView() {
	return planView;
    }

    public void setPlanView(ImpPlanView planView) {
	this.planView = planView;
    }

    public List<SystemLoadDTO> getSystemLoadDetails() {
	return systemLoadDetails;
    }

    public void setSystemLoadDetails(List<SystemLoadDTO> systemLoadDetails) {
	this.systemLoadDetails = systemLoadDetails;
    }

    public String getPlanID() {
	return getPlanView().getPlan().getId();
    }
}
