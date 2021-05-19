/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.bpm.model;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class TaskRepresentationTest {

    public TaskRepresentationTest() {
    }

    @Test
    public void testGetId() {
	TaskRepresentation instance = new TaskRepresentation();
	String expResult = "";
	String id = "";
	instance.setId(id);
	String result = instance.getId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetUrl() {
	TaskRepresentation instance = new TaskRepresentation();
	String expResult = "";
	String url = "";
	instance.setUrl(url);
	String result = instance.getUrl();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetOwner() {
	TaskRepresentation instance = new TaskRepresentation();
	Object expResult = null;
	Object owner = null;
	instance.setOwner(owner);
	Object result = instance.getOwner();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetAssignee() {
	TaskRepresentation instance = new TaskRepresentation();
	Object expResult = null;
	Object assignee = null;
	instance.setAssignee(assignee);
	Object result = instance.getAssignee();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetDelegationState() {
	TaskRepresentation instance = new TaskRepresentation();
	Object expResult = null;
	Object delegationState = null;
	instance.setDelegationState(delegationState);
	Object result = instance.getDelegationState();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetName() {
	TaskRepresentation instance = new TaskRepresentation();
	String expResult = "";
	String name = "";
	instance.setName(name);
	String result = instance.getName();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetDescription() {
	TaskRepresentation instance = new TaskRepresentation();
	Object expResult = null;
	Object description = null;
	instance.setDescription(description);
	Object result = instance.getDescription();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetCreateTime() {
	TaskRepresentation instance = new TaskRepresentation();
	String expResult = "";
	String createTime = "";
	instance.setCreateTime(createTime);
	String result = instance.getCreateTime();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetDueDate() {
	TaskRepresentation instance = new TaskRepresentation();
	Object expResult = null;
	Object dueDate = null;
	instance.setDueDate(dueDate);
	Object result = instance.getDueDate();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetPriority() {
	TaskRepresentation instance = new TaskRepresentation();
	Integer expResult = null;
	Integer priority = null;
	instance.setPriority(priority);
	Integer result = instance.getPriority();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetSuspended() {
	TaskRepresentation instance = new TaskRepresentation();
	Boolean expResult = null;
	Boolean suspended = null;
	instance.setSuspended(suspended);
	Boolean result = instance.getSuspended();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetTaskDefinitionKey() {
	TaskRepresentation instance = new TaskRepresentation();
	String expResult = "";
	String taskDefinitionKey = "";
	instance.setTaskDefinitionKey(taskDefinitionKey);
	String result = instance.getTaskDefinitionKey();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetTenantId() {
	TaskRepresentation instance = new TaskRepresentation();
	String expResult = "";
	String tenantId = "";
	instance.setTenantId(tenantId);
	String result = instance.getTenantId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetCategory() {
	TaskRepresentation instance = new TaskRepresentation();
	Object expResult = null;
	Object category = null;
	instance.setCategory(category);
	Object result = instance.getCategory();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetFormKey() {
	TaskRepresentation instance = new TaskRepresentation();
	Object expResult = null;
	Object formKey = null;
	instance.setFormKey(formKey);
	Object result = instance.getFormKey();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetParentTaskId() {
	TaskRepresentation instance = new TaskRepresentation();
	Object expResult = null;
	Object parentTaskId = null;
	instance.setParentTaskId(parentTaskId);
	Object result = instance.getParentTaskId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetParentTaskUrl() {
	TaskRepresentation instance = new TaskRepresentation();
	Object expResult = null;
	Object parentTaskUrl = null;
	instance.setParentTaskUrl(parentTaskUrl);
	Object result = instance.getParentTaskUrl();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetExecutionId() {
	TaskRepresentation instance = new TaskRepresentation();
	String expResult = "";
	String executionId = "";
	instance.setExecutionId(executionId);
	String result = instance.getExecutionId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetExecutionUrl() {
	TaskRepresentation instance = new TaskRepresentation();
	String expResult = "";
	String executionUrl = "";
	instance.setExecutionUrl(executionUrl);
	String result = instance.getExecutionUrl();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetProcessInstanceId() {
	TaskRepresentation instance = new TaskRepresentation();
	String expResult = "";
	String processInstanceId = "";
	instance.setProcessInstanceId(processInstanceId);
	String result = instance.getProcessInstanceId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetProcessInstanceUrl() {
	TaskRepresentation instance = new TaskRepresentation();
	String expResult = "";
	String processInstanceUrl = "";
	instance.setProcessInstanceUrl(processInstanceUrl);
	String result = instance.getProcessInstanceUrl();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetProcessDefinitionId() {
	TaskRepresentation instance = new TaskRepresentation();
	String expResult = "";
	String processDefinitionId = "";
	instance.setProcessDefinitionId(processDefinitionId);
	String result = instance.getProcessDefinitionId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetProcessDefinitionUrl() {
	TaskRepresentation instance = new TaskRepresentation();
	String expResult = "";
	String processDefinitionUrl = "";
	instance.setProcessDefinitionUrl(processDefinitionUrl);
	String result = instance.getProcessDefinitionUrl();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetVariables() {
	TaskRepresentation instance = new TaskRepresentation();
	List<TaskVariable> expResult = null;
	List<TaskVariable> variables = null;
	instance.setVariables(variables);
	List<TaskVariable> result = instance.getVariables();
	assertEquals(expResult, result);
    }

}
