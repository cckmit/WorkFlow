/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.PreProductionLoads;
import java.util.List;

/**
 *
 * @author Radha.Adhimoolam
 */
public class PreProdTOSForm {
    private String action;
    private List<PreProductionLoads> preProdLoads;

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public List<PreProductionLoads> getPreProdLoads() {
	return preProdLoads;
    }

    public void setPreProdLoads(List<PreProductionLoads> preProdLoads) {
	this.preProdLoads = preProdLoads;
    }

}
