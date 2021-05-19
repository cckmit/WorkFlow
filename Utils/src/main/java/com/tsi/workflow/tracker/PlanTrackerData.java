/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tracker;

import com.tsi.workflow.utils.Constants.TrackStatusForm;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author prabhu.prabhakaran
 */
public class PlanTrackerData {

    private String planId;
    private List<TrackStatusForm> stages;
    // private TrackStatusForm currentStage;
    // private List<String> messages;
    private List<ImplementationTrackerData> implementations;
    private boolean isQaFuncBypassed = false;
    private boolean isQaRegBypassed = false;

    public PlanTrackerData() {
	setStages(new ArrayList<>());
	implementations = new ArrayList<>();
	// messages = new ArrayList<>();
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    // public TrackStatusForm getCurrentStage() {
    // return currentStage;
    // }
    //
    // public void setCurrentStage(TrackStatusForm currentStage) {
    // this.currentStage = currentStage;
    // }

    // public List<String> getMessages() {
    // return messages;
    // }
    //
    // public void setMessages(List<String> messages) {
    // this.messages = messages;
    // }

    public List<ImplementationTrackerData> getImplementations() {
	return implementations;
    }

    public void setImplementations(List<ImplementationTrackerData> implementations) {
	this.implementations = implementations;
    }

    public List<TrackStatusForm> getStages() {
	return stages;
    }

    public void setStages(List<TrackStatusForm> stages) {
	this.stages = stages;
    }

    public boolean isQaFuncBypassed() {
	return isQaFuncBypassed;
    }

    public void setQaFuncBypassed(boolean isQaFuncBypassed) {
	this.isQaFuncBypassed = isQaFuncBypassed;
    }

    public boolean isQaRegBypassed() {
	return isQaRegBypassed;
    }

    public void setQaRegBypassed(boolean isQaRegBypassed) {
	this.isQaRegBypassed = isQaRegBypassed;
    }

}
