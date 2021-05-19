/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ProductionLoads;
import java.util.List;

/**
 *
 * @author Radha.Adhimoolam
 */
public class OnlineFeedbackQueueForm {
    private ImpPlan plan;
    private List<ProductionLoads> productionLoads;

    public OnlineFeedbackQueueForm(ImpPlan plan, List<ProductionLoads> productionLoads) {
	this.plan = plan;
	this.productionLoads = productionLoads;
    }

    public OnlineFeedbackQueueForm() {
    }

    public ImpPlan getPlan() {
	return plan;
    }

    public void setPlan(ImpPlan plan) {
	this.plan = plan;
    }

    public List<ProductionLoads> getProductionLoads() {
	return productionLoads;
    }

    public void setProductionLoads(List<ProductionLoads> productionLoads) {
	this.productionLoads = productionLoads;
    }

}
