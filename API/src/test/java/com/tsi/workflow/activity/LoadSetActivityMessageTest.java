/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.utils.Constants;
import java.util.Arrays;
import org.apache.log4j.Priority;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class LoadSetActivityMessageTest {

    LoadSetActivityMessage instance;

    public LoadSetActivityMessageTest() {
	ImpPlan lPlan = DataWareHouse.getPlan();
	lPlan.setPlanStatus(Constants.PlanStatus.APPROVED.name());
	instance = new LoadSetActivityMessage(lPlan, new Implementation(), DataWareHouse.getPlan().getSystemLoadActionsList().get(0));
	instance.setUser(DataWareHouse.getUser());
	instance.setIsAutoReject(true);
    }

    @Test
    public void testIsIsAutoReject() {
	boolean expResult = false;
	boolean isAutoReject = false;
	instance.setIsAutoReject(isAutoReject);
	boolean result = instance.isIsAutoReject();
	assertEquals(expResult, result);
    }

    @Test
    public void testProcessMessage() {
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage1() {
	SystemLoadActions sysLoad = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	sysLoad.setTestStatus("");
	instance.setUser(DataWareHouse.getUserWithDelagated());
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage2() {
	SystemLoadActions sysLoad = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	sysLoad.setTestStatus("");
	instance.setUser(DataWareHouse.getUserWithDelagated());
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage3() {
	SystemLoadActions sysLoad = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	sysLoad.setLastActionStatus("FAIL");
	sysLoad.setTestStatus(null);
	instance.setArguments((IBeans[]) Arrays.asList(sysLoad).toArray());
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage4() {
	SystemLoadActions sysLoad = DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
	sysLoad.setLastActionStatus("SUCCESS");
	instance.getUser().setCurrentDelegatedUser(instance.getUser());
	instance.setArguments((IBeans[]) Arrays.asList(sysLoad).toArray());
	String result = instance.processMessage();
	assertNotNull(result);

    }

    @Test
    public void testGetLogLevel() {
	Priority result = instance.getLogLevel();
	assertNull(result);
    }

    @Test
    public void testSetArguments() {
	instance.setArguments(DataWareHouse.getPlan().getSystemLoadActionsList().get(0));
	assertTrue(true);
    }

}
