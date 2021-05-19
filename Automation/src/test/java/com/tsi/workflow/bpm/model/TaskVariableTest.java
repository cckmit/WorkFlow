/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.bpm.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 *
 * @author USER
 */
public class TaskVariableTest {

    public TaskVariableTest() {
    }

    @Test
    public void testGetName() {
	TaskVariable instance = new TaskVariable(null, null);
	String expResult = "";
	String name = "";
	instance.setName(name);
	String result = instance.getName();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetScope() {
	TaskVariable instance = new TaskVariable(null, null, null, true);
	String expResult = "";
	String scope = "";
	instance.setScope(scope);
	String result = instance.getScope();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetType() {
	TaskVariable instance = new TaskVariable(null, null, null, null);
	Object expResult = null;
	Object type = null;
	instance.setType(type);
	Object result = instance.getType();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetValue() {
	TaskVariable instance = new TaskVariable(null, null, null, null);
	Object expResult = null;
	Object value = null;
	instance.setValue(value);
	Object result = instance.getValue();
	assertEquals(expResult, result);
    }

}
