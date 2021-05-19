package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.SystemLoad;
import java.util.Date;
import org.apache.log4j.Priority;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class LoadDateChangesActivityMessageTest {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	Implementation implementation0 = new Implementation("");
	LoadDateChangesActivityMessage loadDateChangesActivityMessage0 = new LoadDateChangesActivityMessage(impPlan0, implementation0);
	User user0 = new User("");
	loadDateChangesActivityMessage0.setUser(user0);
	// Undeclared exception!
	try {
	    loadDateChangesActivityMessage0.processMessage();
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	}
    }

    @Test(timeout = 4000)
    public void test1() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	LoadDateChangesActivityMessage loadDateChangesActivityMessage0 = new LoadDateChangesActivityMessage(impPlan0, (Implementation) null);
	IBeans[] iBeansArray0 = new IBeans[10];
	loadDateChangesActivityMessage0.setArguments(iBeansArray0);
	assertEquals(10, iBeansArray0.length);
    }

    @Test(timeout = 4000)
    public void test2() throws Throwable {
	Implementation implementation0 = new Implementation();
	LoadDateChangesActivityMessage loadDateChangesActivityMessage0 = new LoadDateChangesActivityMessage((ImpPlan) null, implementation0);
	Date mockDate0 = new Date();
	loadDateChangesActivityMessage0.setPreviousLoadDate(mockDate0);
	// Undeclared exception!
	try {
	    loadDateChangesActivityMessage0.processMessage();
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	}
    }

    @Test(timeout = 4000)
    public void test3() throws Throwable {
	Implementation implementation0 = new Implementation();
	LoadDateChangesActivityMessage loadDateChangesActivityMessage0 = new LoadDateChangesActivityMessage((ImpPlan) null, implementation0);
	Priority priority0 = loadDateChangesActivityMessage0.getLogLevel();
	assertEquals(30000, Priority.WARN_INT);
    }

    @Test(timeout = 4000)
    public void test4() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	LoadDateChangesActivityMessage loadDateChangesActivityMessage0 = new LoadDateChangesActivityMessage(impPlan0, (Implementation) null);
	SystemLoad systemLoad0 = new SystemLoad();
	loadDateChangesActivityMessage0.setSystemLoad(systemLoad0);
	assertNull(systemLoad0.getFlbkScrDesc());
    }

    @Test
    public void test5() throws Throwable {
	ImpPlan lPlan = DataWareHouse.getPlan();
	LoadDateChangesActivityMessage instance = new LoadDateChangesActivityMessage(lPlan, lPlan.getImplementationList().get(0));
	instance.setPreviousLoadDate(new Date());
	instance.setSystemLoad(lPlan.getSystemLoadList().get(0));
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(user);
	instance.setUser(user);
	try {
	    instance.processMessage();
	} catch (NullPointerException e) {
	}

    }
}
