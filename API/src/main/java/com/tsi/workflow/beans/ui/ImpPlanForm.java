/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.ImpPlan;
import java.util.Set;

/**
 *
 * @author Radha.Adhimoolam
 */
public class ImpPlanForm {

    private ImpPlan impPlan;
    private Set<String> blockedSystems;

    public ImpPlanForm() {
    }

    public ImpPlanForm(ImpPlan plan, Set<String> blockSystems) {
	this.impPlan = plan;
	this.blockedSystems = blockSystems;
    }

    public ImpPlan getImpPlan() {
	return impPlan;
    }

    public void setImpPlan(ImpPlan impPlan) {
	this.impPlan = impPlan;
    }

    public Set<String> getBlockedSystems() {
	return blockedSystems;
    }

    public void setBlockedSystems(Set<String> blockedSystems) {
	this.blockedSystems = blockedSystems;
    }

}
