/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertNotNull;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.SystemLoad;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Priority;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class PlanCreationActivityMessageTest {

    PlanCreationActivityMessage instance;

    public PlanCreationActivityMessageTest() {
	instance = new PlanCreationActivityMessage(new ImpPlan(), new Implementation());
	instance.setUser(DataWareHouse.getUser());
    }

    @Test
    public void testGetLogLevel() {
	Priority result = instance.getLogLevel();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage() {
	instance.setSystemLoads(new ArrayList<>());
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage1() {
	instance.setUser(DataWareHouse.getUserWithDelagated());
	instance.setSystemLoads(DataWareHouse.getPlan().getSystemLoadList());
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage2() {
	instance.setUser(DataWareHouse.getUser());
	instance.setSystemLoads(DataWareHouse.getPlan().getSystemLoadList());
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage21() {
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(null);
	instance.setUser(user);
	instance.setSystemLoads(DataWareHouse.getPlan().getSystemLoadList());
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage3() {
	instance.setUser(DataWareHouse.getUser());
	instance.setSystemLoads(DataWareHouse.getPlan().getSystemLoadList());
	instance.setCreate(true);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage4() {
	instance.setUser(DataWareHouse.getUser());
	instance.setSystemLoads(DataWareHouse.getPlan().getSystemLoadList());
	instance.setUpdate(true);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage5() {
	instance.setUser(DataWareHouse.getUserWithDelagated());
	instance.setSystemLoads(DataWareHouse.getPlan().getSystemLoadList());
	instance.setCreate(true);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage6() {
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(null);
	instance.setUser(user);
	instance.setSystemLoads(DataWareHouse.getPlan().getSystemLoadList());
	instance.setUpdate(true);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage7() {
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(null);
	instance.setUser(user);
	instance.setCreate(true);
	instance.setSystemLoads(DataWareHouse.getPlan().getSystemLoadList());
	String result = instance.processMessage();
	assertNotNull(result);
    }

    /**
     * Test of setSystemLoads method, of class PlanCreationActivityMessage.
     */
    @Test
    public void testSetSystemLoads() {

	List<SystemLoad> systemLoads = null;
	PlanCreationActivityMessage instance = new PlanCreationActivityMessage(null, null);
	instance.setSystemLoads(systemLoads);
    }

    /**
     * Test of setCreate method, of class PlanCreationActivityMessage.
     */
    @Test
    public void testSetCreate() {

	boolean create = false;
	PlanCreationActivityMessage instance = new PlanCreationActivityMessage(null, null);
	instance.setCreate(create);
    }

    /**
     * Test of setUpdate method, of class PlanCreationActivityMessage.
     */
    @Test
    public void testSetUpdate() {

	boolean update = false;
	PlanCreationActivityMessage instance = new PlanCreationActivityMessage(null, null);
	instance.setUpdate(update);
    }

    /**
     * Test of setArguments method, of class PlanCreationActivityMessage.
     */
    @Test
    public void testSetArguments() {

	IBeans[] beans = null;
	PlanCreationActivityMessage instance = new PlanCreationActivityMessage(null, null);
	instance.setArguments(beans);
    }

}
