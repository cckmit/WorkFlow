/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author USER
 */
public class ProdDeploy {

    private List<String> plans = new ArrayList<String>();
    private HashMap<Integer, String> ips = new HashMap<Integer, String>();

    public List<String> getPlans() {
	return plans;
    }

    public void setPlans(List<String> plans) {
	this.plans = plans;
    }

    public HashMap<Integer, String> getIps() {
	return ips;
    }

    public void setIps(HashMap<Integer, String> ips) {
	this.ips = ips;
    }

}
