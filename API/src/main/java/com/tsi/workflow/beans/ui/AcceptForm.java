/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.ProductionLoads;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Radha.Adhimoolam
 */
public class AcceptForm {

    private Boolean isAcceptInProgress;
    private Boolean showAcceptAllButton = Boolean.FALSE;
    private List<String> allowedSystems = new ArrayList();
    private List<ProductionLoads> productionLoadsList;

    public AcceptForm() {
    }

    public AcceptForm(Boolean isAcceptInProgress, List<ProductionLoads> productionLoadsList) {
	this.isAcceptInProgress = isAcceptInProgress;
	this.productionLoadsList = productionLoadsList;
    }

    public AcceptForm(Boolean isAcceptInProgress, List<ProductionLoads> productionLoadsList, Boolean showAcceptAllButton, List<String> allowedSystems) {
	this.isAcceptInProgress = isAcceptInProgress;
	this.productionLoadsList = productionLoadsList;
	this.showAcceptAllButton = showAcceptAllButton;
	if (allowedSystems != null) {
	    this.allowedSystems.addAll(allowedSystems);
	}
    }

    public Boolean getIsAcceptInProgress() {
	return isAcceptInProgress;
    }

    public void setIsAcceptInProgress(Boolean isAcceptInProgress) {
	this.isAcceptInProgress = isAcceptInProgress;
    }

    public List<ProductionLoads> getProductionLoadsList() {
	return productionLoadsList;
    }

    public void setProductionLoadsList(List<ProductionLoads> productionLoadsList) {
	this.productionLoadsList = productionLoadsList;
    }

    public Boolean getShowAcceptAllButton() {
	return showAcceptAllButton;
    }

    public void setShowAcceptAllButton(Boolean showAcceptAllButton) {
	this.showAcceptAllButton = showAcceptAllButton;
    }

    public List<String> getAllowedSystems() {
	return allowedSystems;
    }

    public void setAllowedSystems(List<String> allowedSystems) {
	this.allowedSystems = allowedSystems;
    }

}
