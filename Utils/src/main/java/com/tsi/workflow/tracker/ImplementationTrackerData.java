/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.tracker;

import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.TrackStatusForm;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author prabhu.prabhakaran
 */
public class ImplementationTrackerData {

    private String implementationId;
    private String developerName;
    private List<TrackStatusForm> stages;
    private TrackStatusForm currentStage;
    private List<String> messages;

    public ImplementationTrackerData() {
	setStages(Constants.ImpTrackStatus.getImpTrackStatus());
	messages = new ArrayList<>();
    }

    public String getImplementationId() {
	return implementationId;
    }

    public void setImplementationId(String implementationId) {
	this.implementationId = implementationId;
    }

    public List<String> getMessages() {
	return messages;
    }

    public void setMessages(List<String> messages) {
	this.messages = messages;
    }

    public List<TrackStatusForm> getStages() {
	return stages;
    }

    public void setStages(List<TrackStatusForm> stages) {
	this.stages = stages;
    }

    public TrackStatusForm getCurrentStage() {
	return currentStage;
    }

    public void setCurrentStage(TrackStatusForm currentStage) {
	this.currentStage = currentStage;
    }

    /**
     * @return the developerName
     */
    public String getDeveloperName() {
	return developerName;
    }

    /**
     * @param developerName
     *            the developerName to set
     */
    public void setDeveloperName(String developerName) {
	this.developerName = developerName;
    }

}
