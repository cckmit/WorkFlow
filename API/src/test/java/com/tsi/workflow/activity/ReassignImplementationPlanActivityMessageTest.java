package com.tsi.workflow.activity;

import static org.junit.Assert.*;

import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.apache.log4j.Priority;
import org.junit.Test;

public class ReassignImplementationPlanActivityMessageTest {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
	Implementation implementation0 = new Implementation((String) null);
	ReassignImplementationPlanActivityMessage reassignImplementationPlanActivityMessage0 = new ReassignImplementationPlanActivityMessage((ImpPlan) null, implementation0);
	reassignImplementationPlanActivityMessage0.setRole("t}`G");
	String string0 = reassignImplementationPlanActivityMessage0.getRole();
	assertEquals("t}`G", string0);
    }

    @Test(timeout = 4000)
    public void test1() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan("W0[PF@K2");
	Implementation implementation0 = new Implementation("org.apache.commons.io.filefilter.CanWriteFileFilter");
	ReassignImplementationPlanActivityMessage reassignImplementationPlanActivityMessage0 = new ReassignImplementationPlanActivityMessage(impPlan0, implementation0);
	reassignImplementationPlanActivityMessage0.setRole("");
	String string0 = reassignImplementationPlanActivityMessage0.getRole();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test2() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	ReassignImplementationPlanActivityMessage reassignImplementationPlanActivityMessage0 = new ReassignImplementationPlanActivityMessage(impPlan0, (Implementation) null);
	reassignImplementationPlanActivityMessage0.setOldLeadName("com.tsi.workflow.beans.dao.PdddsLibrary[ id=");
	String string0 = reassignImplementationPlanActivityMessage0.getOldLeadName();
	assertEquals("com.tsi.workflow.beans.dao.PdddsLibrary[ id=", string0);
    }

    @Test(timeout = 4000)
    public void test3() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	ReassignImplementationPlanActivityMessage reassignImplementationPlanActivityMessage0 = new ReassignImplementationPlanActivityMessage(impPlan0, (Implementation) null);
	reassignImplementationPlanActivityMessage0.setOldLeadName("");
	String string0 = reassignImplementationPlanActivityMessage0.getOldLeadName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test5() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	ReassignImplementationPlanActivityMessage reassignImplementationPlanActivityMessage0 = new ReassignImplementationPlanActivityMessage(impPlan0, (Implementation) null);
	String string0 = reassignImplementationPlanActivityMessage0.processMessage();
	assertEquals("null Application Developer Lead has been reassigned from null to null by null  null", string0);
    }

    @Test(timeout = 4000)
    public void test6() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	ReassignImplementationPlanActivityMessage reassignImplementationPlanActivityMessage0 = new ReassignImplementationPlanActivityMessage(impPlan0, (Implementation) null);
	Priority priority0 = reassignImplementationPlanActivityMessage0.getLogLevel();
	assertEquals(20000, priority0.toInt());
    }

    @Test(timeout = 4000)
    public void test7() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	ReassignImplementationPlanActivityMessage reassignImplementationPlanActivityMessage0 = new ReassignImplementationPlanActivityMessage(impPlan0, (Implementation) null);
	String string0 = reassignImplementationPlanActivityMessage0.getOldLeadName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test8() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	ReassignImplementationPlanActivityMessage reassignImplementationPlanActivityMessage0 = new ReassignImplementationPlanActivityMessage(impPlan0, implementation0);
	IBeans[] iBeansArray0 = new IBeans[1];
	reassignImplementationPlanActivityMessage0.setArguments(iBeansArray0);
	assertEquals(1, iBeansArray0.length);
    }

    @Test(timeout = 4000)
    public void test9() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	ReassignImplementationPlanActivityMessage reassignImplementationPlanActivityMessage0 = new ReassignImplementationPlanActivityMessage(impPlan0, implementation0);
	String string0 = reassignImplementationPlanActivityMessage0.getRole();
	assertNull(string0);
    }
}
