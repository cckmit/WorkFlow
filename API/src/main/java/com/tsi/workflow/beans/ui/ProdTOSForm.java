/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.ProductionLoads;
import java.util.List;

/**
 *
 * @author Radha.Adhimoolam
 */
public class ProdTOSForm {
    private String action;
    private List<ProductionLoads> prodLoads;

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public List<ProductionLoads> getProdLoads() {
	return prodLoads;
    }

    public void setProdLoads(List<ProductionLoads> prodLoads) {
	this.prodLoads = prodLoads;
    }

}
