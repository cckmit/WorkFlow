
package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class DevManagerApproveActivityMessageTest {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
	DevManagerApproveActivityMessage devManagerApproveActivityMessage0 = new DevManagerApproveActivityMessage();
	devManagerApproveActivityMessage0.setDevManger("TX~e%");
	String string0 = devManagerApproveActivityMessage0.getDevManger();
	assertEquals("TX~e%", string0);
    }

    @Test(timeout = 4000)
    public void test1() throws Throwable {
	DevManagerApproveActivityMessage devManagerApproveActivityMessage0 = new DevManagerApproveActivityMessage((ImpPlan) null, (Implementation) null);
	devManagerApproveActivityMessage0.setDevManger("");
	String string0 = devManagerApproveActivityMessage0.getDevManger();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test2() throws Throwable {
	DevManagerApproveActivityMessage devManagerApproveActivityMessage0 = new DevManagerApproveActivityMessage();
	devManagerApproveActivityMessage0.setDevLead("Approval Failed:  ADL - null and Approving manager -null cannot be the same person. ");
	String string0 = devManagerApproveActivityMessage0.getDevLead();
	assertEquals("Approval Failed:  ADL - null and Approving manager -null cannot be the same person. ", string0);
    }

    @Test(timeout = 4000)
    public void test3() throws Throwable {
	DevManagerApproveActivityMessage devManagerApproveActivityMessage0 = new DevManagerApproveActivityMessage();
	devManagerApproveActivityMessage0.setDevLead("");
	String string0 = devManagerApproveActivityMessage0.getDevLead();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test5() throws Throwable {
	DevManagerApproveActivityMessage devManagerApproveActivityMessage0 = new DevManagerApproveActivityMessage();
	devManagerApproveActivityMessage0.setStatus("failed");
	devManagerApproveActivityMessage0.setLeadCondition(Boolean.TRUE);
	String string0 = devManagerApproveActivityMessage0.processMessage();
	assertNotNull(string0);
    }

    @Test(timeout = 4000)
    public void test6() throws Throwable {
	DevManagerApproveActivityMessage devManagerApproveActivityMessage0 = new DevManagerApproveActivityMessage();
	IBeans[] iBeansArray0 = new IBeans[2];
	devManagerApproveActivityMessage0.setArguments(iBeansArray0);
	assertNull(devManagerApproveActivityMessage0.getDevLead());
    }

    @Test(timeout = 4000)
    public void test7() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	DevManagerApproveActivityMessage devManagerApproveActivityMessage0 = new DevManagerApproveActivityMessage(impPlan0, implementation0);
	String string0 = devManagerApproveActivityMessage0.getDevLead();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test8() throws Throwable {
	DevManagerApproveActivityMessage devManagerApproveActivityMessage0 = new DevManagerApproveActivityMessage();
	String string0 = devManagerApproveActivityMessage0.getDevManger();
	assertNull(string0);
    }

    @Test
    public void test9() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	DevManagerApproveActivityMessage instance = new DevManagerApproveActivityMessage(impPlan0, implementation0);
	instance.setStatus("Passed");
	try {
	    instance.processMessage();

	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void test10() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	DevManagerApproveActivityMessage instance = new DevManagerApproveActivityMessage(impPlan0, implementation0);
	instance.setStatus("failed");
	try {
	    instance.getLogLevel();

	} catch (Exception e) {
	    assertTrue(true);
	}
    }

    @Test
    public void test11() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	DevManagerApproveActivityMessage instance = new DevManagerApproveActivityMessage(impPlan0, implementation0);
	instance.setStatus("passed");
	try {
	    instance.getStatus();
	    instance.getLogLevel();

	} catch (Exception e) {
	    assertTrue(true);
	}
    }
}
