/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.SystemCpu;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class PreProductionLoadsForm {

    private List<PreProductionLoads> preProductionLoadsList;
    private ImpPlan plan;
    private List<SystemCpu> activationSystemCpusList = new ArrayList<>();
    private List<SystemCpu> deActivationSystemCpusList = new ArrayList<>();
    private Boolean isAnyLoadsInProgress;
    private Boolean showAddActivateButton = Boolean.FALSE;
    private Boolean showAddDeActivateButton = Boolean.FALSE;
    private Boolean isMultipleCPUAllowed = Boolean.FALSE;

    public List<PreProductionLoads> getPreProductionLoadsList() {
	return preProductionLoadsList;
    }

    public void setPreProductionLoadsList(List<PreProductionLoads> preProductionLoadsList) {
	this.preProductionLoadsList = preProductionLoadsList;
    }

    public Boolean getShowAddActivateButton() {
	return showAddActivateButton;
    }

    public void setShowAddActivateButton(Boolean showAddActivateButton) {
	this.showAddActivateButton = showAddActivateButton;
    }

    public Boolean getShowAddDeActivateButton() {
	return showAddDeActivateButton;
    }

    public void setShowAddDeActivateButton(Boolean showAddDeActivateButton) {
	this.showAddDeActivateButton = showAddDeActivateButton;
    }

    public Boolean getIsMultipleCPUAllowed() {
	return isMultipleCPUAllowed;
    }

    public void setIsMultipleCPUAllowed(Boolean isMultipleCPUAllowed) {
	this.isMultipleCPUAllowed = isMultipleCPUAllowed;
    }

    public ImpPlan getPlan() {
	return plan;
    }

    public void setPlan(ImpPlan plan) {
	this.plan = plan;
    }

    public List<SystemCpu> getActivationSystemCpusList() {
	return activationSystemCpusList;
    }

    public void setActivationSystemCpusList(List<SystemCpu> activationSystemCpusList) {
	this.activationSystemCpusList = activationSystemCpusList;
    }

    public List<SystemCpu> getDeActivationSystemCpusList() {
	return deActivationSystemCpusList;
    }

    public void setDeActivationSystemCpusList(List<SystemCpu> deActivationSystemCpusList) {
	this.deActivationSystemCpusList = deActivationSystemCpusList;
    }

    public Boolean getIsAnyLoadsInProgress() {
	return isAnyLoadsInProgress;
    }

    public void setIsAnyLoadsInProgress(Boolean isAnyLoadsInProgress) {
	this.isAnyLoadsInProgress = isAnyLoadsInProgress;
    }
}
