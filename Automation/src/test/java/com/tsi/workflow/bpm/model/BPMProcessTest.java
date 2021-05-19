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
public class BPMProcessTest {

    public BPMProcessTest() {
    }

    @Test
    public void testGetAction() {
	BPMProcess instance = new BPMProcess();
	String expResult = "";
	String action = "";
	instance.setAction(action);
	String result = instance.getAction();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetAssignee() {
	BPMProcess instance = new BPMProcess();
	String expResult = "";
	String assignee = "";
	instance.setAssignee(assignee);
	String result = instance.getAssignee();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetGroup() {
	BPMProcess instance = new BPMProcess();
	String expResult = "";
	String group = "";
	instance.setGroup(group);
	String result = instance.getGroup();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetType() {
	BPMProcess instance = new BPMProcess();
	String expResult = "";
	String type = "";
	instance.setType(type);
	String result = instance.getType();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetId() {
	BPMProcess instance = new BPMProcess();
	String expResult = "";
	String id = "";
	instance.setId(id);
	String result = instance.getId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetUrl() {
	BPMProcess instance = new BPMProcess();
	String expResult = "";
	String url = "";
	instance.setUrl(url);
	String result = instance.getUrl();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetBusinessKey() {
	BPMProcess instance = new BPMProcess();
	String expResult = "";
	String businessKey = "";
	instance.setBusinessKey(businessKey);
	String result = instance.getBusinessKey();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetSuspended() {
	BPMProcess instance = new BPMProcess();
	Boolean expResult = null;
	Boolean suspended = null;
	instance.setSuspended(suspended);
	Boolean result = instance.getSuspended();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetEnded() {
	BPMProcess instance = new BPMProcess();
	Boolean expResult = null;
	Boolean ended = null;
	instance.setEnded(ended);
	Boolean result = instance.getEnded();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetProcessDefinitionId() {
	BPMProcess instance = new BPMProcess();
	String expResult = "";
	String processDefinitionId = "";
	instance.setProcessDefinitionId(processDefinitionId);
	String result = instance.getProcessDefinitionId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetProcessDefinitionUrl() {
	BPMProcess instance = new BPMProcess();
	String expResult = "";
	String processDefinitionUrl = "";
	instance.setProcessDefinitionUrl(processDefinitionUrl);
	String result = instance.getProcessDefinitionUrl();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetProcessDefinitionKey() {
	BPMProcess instance = new BPMProcess();
	String expResult = "";
	String processDefinitionKey = "";
	instance.setProcessDefinitionKey(processDefinitionKey);
	String result = instance.getProcessDefinitionKey();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetActivityId() {
	BPMProcess instance = new BPMProcess();
	String expResult = "";
	String activityId = "";
	instance.setActivityId(activityId);
	String result = instance.getActivityId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetTenantId() {
	BPMProcess instance = new BPMProcess();
	String expResult = "";
	String tenantId = "";
	instance.setTenantId(tenantId);
	String result = instance.getTenantId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetName() {
	BPMProcess instance = new BPMProcess();
	String expResult = "";
	String name = "";
	instance.setName(name);
	String result = instance.getName();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetCompleted() {
	BPMProcess instance = new BPMProcess();
	Boolean expResult = null;
	Boolean completed = null;
	instance.setCompleted(completed);
	Boolean result = instance.getCompleted();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetVariables() {
	BPMProcess instance = new BPMProcess();
	List<TaskVariable> expResult = null;
	List<TaskVariable> variables = null;
	instance.setVariables(variables);
	List<TaskVariable> result = instance.getVariables();
	assertEquals(expResult, result);
    }

}
