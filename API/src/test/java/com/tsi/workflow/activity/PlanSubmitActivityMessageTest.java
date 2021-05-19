
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
public class PlanSubmitActivityMessageTest {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
	Implementation implementation0 = new Implementation((String) null);
	PlanSubmitActivityMessage planSubmitActivityMessage0 = new PlanSubmitActivityMessage((ImpPlan) null, implementation0);
	// Undeclared exception!
	try {
	    planSubmitActivityMessage0.processMessage();
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	}
    }

    @Test(timeout = 4000)
    public void test1() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan(";bt-=$j5");
	Implementation implementation0 = new Implementation();
	PlanSubmitActivityMessage planSubmitActivityMessage0 = new PlanSubmitActivityMessage(impPlan0, implementation0);
	User user0 = new User();
	planSubmitActivityMessage0.setUser(user0);
	String string0 = planSubmitActivityMessage0.processMessage();
	assertEquals("null null has submitted the implementation plan ;bt-=$j5", string0);
    }

    @Test(timeout = 4000)
    public void test2() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	PlanSubmitActivityMessage planSubmitActivityMessage0 = new PlanSubmitActivityMessage(impPlan0, (Implementation) null);
	User user0 = new User("");
	user0.setCurrentDelegatedUser(user0);
	planSubmitActivityMessage0.setUser(user0);
	String string0 = planSubmitActivityMessage0.processMessage();
	assertEquals("null null has submitted the implementation plan  on behalf of null", string0);
    }

    @Test(timeout = 4000)
    public void test3() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	PlanSubmitActivityMessage planSubmitActivityMessage0 = new PlanSubmitActivityMessage(impPlan0, implementation0);
	IBeans[] iBeansArray0 = new IBeans[6];
	planSubmitActivityMessage0.setArguments(iBeansArray0);
	assertEquals(6, iBeansArray0.length);
    }

    @Test(timeout = 4000)
    public void test4() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("");
	PlanSubmitActivityMessage planSubmitActivityMessage0 = new PlanSubmitActivityMessage(impPlan0, (Implementation) null);
	Priority priority0 = planSubmitActivityMessage0.getLogLevel();
	assertEquals(10000, Priority.DEBUG_INT);
    }
}
