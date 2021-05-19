/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.SystemCpu;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class ProductionLoadsForm {

    private List<ProductionLoads> productionLoadsList;
    private ImpPlan plan;
    private List<SystemCpu> activationSystemCpusList = new ArrayList<>();
    private List<SystemCpu> deActivationSystemCpusList = new ArrayList<>();
    private Boolean isAnyLoadsInProgress;
    private Boolean isAnyLoadsDeleted;
    private Boolean showAddActivateButton = Boolean.FALSE;
    private Boolean showAddDeActivateButton = Boolean.FALSE;
    private Boolean isMultipleCPUAllowed = Boolean.FALSE;
    private Boolean selectDeActivateAll = Boolean.TRUE;
    private Boolean selectActivateAll = Boolean.TRUE;
    private String loadsetAddtInfo;
    private String loadsetDeavctivatedInfo;
    private Boolean showDeleteButton = Boolean.FALSE;
    private Boolean showFallbackPendingButton = Boolean.FALSE;
    private Boolean showLoadAndActivateButtonEnable = Boolean.FALSE;

    public String getLoadsetDeavctivatedInfo() {
	return loadsetDeavctivatedInfo;
    }

    public void setLoadsetDeavctivatedInfo(String loadsetDeavctivatedInfo) {
	this.loadsetDeavctivatedInfo = loadsetDeavctivatedInfo;
    }

    public List<ProductionLoads> getProductionLoadsList() {
	return productionLoadsList;
    }

    public Boolean getIsAnyLoadsDeleted() {
	return isAnyLoadsDeleted;
    }

    public void setIsAnyLoadsDeleted(Boolean isAnyLoadsDeleted) {
	this.isAnyLoadsDeleted = isAnyLoadsDeleted;
    }

    public void setProductionLoadsList(List<ProductionLoads> productionLoadsList) {
	this.productionLoadsList = productionLoadsList;
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

    public Boolean getSelectDeActivateAll() {
	return selectDeActivateAll;
    }

    public void setSelectDeActivateAll(Boolean selectDeActivateAll) {
	this.selectDeActivateAll = selectDeActivateAll;
    }

    public Boolean getSelectActivateAll() {
	return selectActivateAll;
    }

    public void setSelectActivateAll(Boolean selectActivateAll) {
	this.selectActivateAll = selectActivateAll;
    }

    public String getLoadsetAddtInfo() {
	return loadsetAddtInfo;
    }

    public void setLoadsetAddtInfo(String loadsetAddtInfo) {
	this.loadsetAddtInfo = loadsetAddtInfo;
    }

    public Boolean getShowDeleteButton() {
	return showDeleteButton;
    }

    public void setShowDeleteButton(Boolean showDeleteButton) {
	this.showDeleteButton = showDeleteButton;
    }

    public Boolean getShowFallbackPendingButton() {
	return showFallbackPendingButton;
    }

    public void setShowFallbackPendingButton(Boolean showFallbackPendingButton) {
	this.showFallbackPendingButton = showFallbackPendingButton;
    }

    public Boolean getShowLoadAndActivateButtonEnable() {
	return showLoadAndActivateButtonEnable;
    }

    public void setShowLoadAndActivateButtonEnable(Boolean showLoadAndActivateButtonEnable) {
	this.showLoadAndActivateButtonEnable = showLoadAndActivateButtonEnable;
    }

}
