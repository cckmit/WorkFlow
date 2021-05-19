/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.bpm.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Deepa
 */
public class BPMProcess {

    private String action;
    private String assignee;
    private String group;
    private String type;
    private String id;
    private String url;
    private String businessKey;
    private Boolean suspended;
    private Boolean ended;
    private String processDefinitionId;
    private String processDefinitionUrl;
    private String processDefinitionKey;
    private String activityId;
    private String tenantId;
    private String name;
    private Boolean completed;
    private List<TaskVariable> variables = new ArrayList();

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public String getAssignee() {
	return assignee;
    }

    public void setAssignee(String assignee) {
	this.assignee = assignee;
    }

    public String getGroup() {
	return group;
    }

    public void setGroup(String group) {
	this.group = group;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public String getBusinessKey() {
	return businessKey;
    }

    public void setBusinessKey(String businessKey) {
	this.businessKey = businessKey;
    }

    public Boolean getSuspended() {
	return suspended;
    }

    public void setSuspended(Boolean suspended) {
	this.suspended = suspended;
    }

    public Boolean getEnded() {
	return ended;
    }

    public void setEnded(Boolean ended) {
	this.ended = ended;
    }

    public String getProcessDefinitionId() {
	return processDefinitionId;
    }

    public void setProcessDefinitionId(String processDefinitionId) {
	this.processDefinitionId = processDefinitionId;
    }

    public String getProcessDefinitionUrl() {
	return processDefinitionUrl;
    }

    public void setProcessDefinitionUrl(String processDefinitionUrl) {
	this.processDefinitionUrl = processDefinitionUrl;
    }

    public String getProcessDefinitionKey() {
	return processDefinitionKey;
    }

    public void setProcessDefinitionKey(String processDefinitionKey) {
	this.processDefinitionKey = processDefinitionKey;
    }

    public String getActivityId() {
	return activityId;
    }

    public void setActivityId(String activityId) {
	this.activityId = activityId;
    }

    public String getTenantId() {
	return tenantId;
    }

    public void setTenantId(String tenantId) {
	this.tenantId = tenantId;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Boolean getCompleted() {
	return completed;
    }

    public void setCompleted(Boolean completed) {
	this.completed = completed;
    }

    public List<TaskVariable> getVariables() {
	return variables;
    }

    public void setVariables(List<TaskVariable> variables) {
	this.variables = variables;
    }

    public void addTaskVariable(String name, String value) {
	variables.add(new TaskVariable(name, value));
    }

}
