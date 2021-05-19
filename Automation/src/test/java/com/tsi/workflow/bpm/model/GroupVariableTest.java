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
public class GroupVariableTest {

    public GroupVariableTest() {
    }

    @Test
    public void testGetGroup() {
	GroupVariable instance = new GroupVariable();
	String expResult = "";
	String group = "";
	instance.setGroup(group);
	String result = instance.getGroup();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetType() {
	GroupVariable instance = new GroupVariable();
	String expResult = "";
	String type = "";
	instance.setType(type);
	String result = instance.getType();
	assertEquals(expResult, result);
    }

}
