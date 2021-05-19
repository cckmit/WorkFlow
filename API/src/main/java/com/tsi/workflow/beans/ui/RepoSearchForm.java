/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vamsi.krishnarao
 */
public class RepoSearchForm {

    private String segment;

    private List<Integer> targetSys = new ArrayList<Integer>();

    public String getSegment() {
	return segment;
    }

    public void setSegment(String segment) {
	this.segment = segment;
    }

    public List<Integer> getTargetSys() {
	return targetSys;
    }

    public void setTargetSys(List<Integer> targetSys) {
	this.targetSys = targetSys;
    }

    @Override
    public String toString() {
	return "RepoSearchForm{" + "segment=" + segment + ", systemId=" + targetSys + '}';
    }

}
