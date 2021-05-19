
package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.tsi.workflow.User;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.apache.log4j.Priority;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class DeletePlanActivityMessageTest {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
	DeletePlanActivityMessage deletePlanActivityMessage0 = new DeletePlanActivityMessage((ImpPlan) null, (Implementation) null);
	// Undeclared exception!
	try {
	    deletePlanActivityMessage0.processMessage();
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	}
    }

    @Test(timeout = 4000)
    public void test1() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	DeletePlanActivityMessage deletePlanActivityMessage0 = new DeletePlanActivityMessage(impPlan0, implementation0);
	User user0 = new User();
	user0.setCurrentDelegatedUser(user0);
	deletePlanActivityMessage0.setUser(user0);
	String string0 = deletePlanActivityMessage0.processMessage();
	assertEquals("null null has deleted the Implementation Plan - null, on behalf of null", string0);
    }

    @Test(timeout = 4000)
    public void test2() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	DeletePlanActivityMessage deletePlanActivityMessage0 = new DeletePlanActivityMessage(impPlan0, implementation0);
	User user0 = new User();
	deletePlanActivityMessage0.setUser(user0);
	String string0 = deletePlanActivityMessage0.processMessage();
	assertEquals("null null has deleted the Implementation Plan - null", string0);
    }

    @Test(timeout = 4000)
    public void test3() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	DeletePlanActivityMessage deletePlanActivityMessage0 = new DeletePlanActivityMessage(impPlan0, implementation0);
	IBeans[] iBeansArray0 = new IBeans[12];
	deletePlanActivityMessage0.setArguments(iBeansArray0);
	assertEquals(12, iBeansArray0.length);
    }

    @Test(timeout = 4000)
    public void test4() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	DeletePlanActivityMessage deletePlanActivityMessage0 = new DeletePlanActivityMessage(impPlan0, implementation0);
	Priority priority0 = deletePlanActivityMessage0.getLogLevel();
	assertEquals(50000, Priority.FATAL_INT);
    }
}
