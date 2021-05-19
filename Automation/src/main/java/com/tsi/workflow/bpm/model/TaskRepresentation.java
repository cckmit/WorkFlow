/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.bpm.model;

import java.util.List;

/**
 *
 * @author User
 */
public class TaskRepresentation {

    public String id;
    public String url;
    public Object owner;
    public Object assignee;
    public Object delegationState;
    public String name;
    public Object description;
    public String createTime;
    public Object dueDate;
    public Integer priority;
    public Boolean suspended;
    public String taskDefinitionKey;
    public String tenantId;
    public Object category;
    public Object formKey;
    public Object parentTaskId;
    public Object parentTaskUrl;
    public String executionId;
    public String executionUrl;
    public String processInstanceId;
    public String processInstanceUrl;
    public String processDefinitionId;
    public String processDefinitionUrl;
    public List<TaskVariable> variables;

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

    public Object getOwner() {
	return owner;
    }

    public void setOwner(Object owner) {
	this.owner = owner;
    }

    public Object getAssignee() {
	return assignee;
    }

    public void setAssignee(Object assignee) {
	this.assignee = assignee;
    }

    public Object getDelegationState() {
	return delegationState;
    }

    public void setDelegationState(Object delegationState) {
	this.delegationState = delegationState;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public Object getDescription() {
	return description;
    }

    public void setDescription(Object description) {
	this.description = description;
    }

    public String getCreateTime() {
	return createTime;
    }

    public void setCreateTime(String createTime) {
	this.createTime = createTime;
    }

    public Object getDueDate() {
	return dueDate;
    }

    public void setDueDate(Object dueDate) {
	this.dueDate = dueDate;
    }

    public Integer getPriority() {
	return priority;
    }

    public void setPriority(Integer priority) {
	this.priority = priority;
    }

    public Boolean getSuspended() {
	return suspended;
    }

    public void setSuspended(Boolean suspended) {
	this.suspended = suspended;
    }

    public String getTaskDefinitionKey() {
	return taskDefinitionKey;
    }

    public void setTaskDefinitionKey(String taskDefinitionKey) {
	this.taskDefinitionKey = taskDefinitionKey;
    }

    public String getTenantId() {
	return tenantId;
    }

    public void setTenantId(String tenantId) {
	this.tenantId = tenantId;
    }

    public Object getCategory() {
	return category;
    }

    public void setCategory(Object category) {
	this.category = category;
    }

    public Object getFormKey() {
	return formKey;
    }

    public void setFormKey(Object formKey) {
	this.formKey = formKey;
    }

    public Object getParentTaskId() {
	return parentTaskId;
    }

    public void setParentTaskId(Object parentTaskId) {
	this.parentTaskId = parentTaskId;
    }

    public Object getParentTaskUrl() {
	return parentTaskUrl;
    }

    public void setParentTaskUrl(Object parentTaskUrl) {
	this.parentTaskUrl = parentTaskUrl;
    }

    public String getExecutionId() {
	return executionId;
    }

    public void setExecutionId(String executionId) {
	this.executionId = executionId;
    }

    public String getExecutionUrl() {
	return executionUrl;
    }

    public void setExecutionUrl(String executionUrl) {
	this.executionUrl = executionUrl;
    }

    public String getProcessInstanceId() {
	return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
	this.processInstanceId = processInstanceId;
    }

    public String getProcessInstanceUrl() {
	return processInstanceUrl;
    }

    public void setProcessInstanceUrl(String processInstanceUrl) {
	this.processInstanceUrl = processInstanceUrl;
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

    public List<TaskVariable> getVariables() {
	return variables;
    }

    public void setVariables(List<TaskVariable> variables) {
	this.variables = variables;
    }

}
