
package com.tsi.workflow.beans.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class ActivityLogTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog((Integer) null);
	ImpPlan impPlan0 = new ImpPlan("c)U)@Ydb.T&,&9A");
	activityLog0.setPlanId(impPlan0);
	ImpPlan impPlan1 = activityLog0.getPlanId();
	assertNull(impPlan1.getProcessId());
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	Date date0 = new Date();
	activityLog0.setModifiedDt(date0);
	Date date1 = activityLog0.getModifiedDt();
	assertSame(date1, date0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	activityLog0.setModifiedBy("%]A)Tlb^@uF'-sV5");
	String string0 = activityLog0.getModifiedBy();
	assertEquals("%]A)Tlb^@uF'-sV5", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	Integer integer0 = new Integer(563);
	ActivityLog activityLog0 = new ActivityLog(integer0);
	activityLog0.setModifiedBy("");
	String string0 = activityLog0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	activityLog0.setMessage("y");
	String string0 = activityLog0.getMessage();
	assertEquals("y", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	Integer integer0 = new Integer(968);
	ActivityLog activityLog0 = new ActivityLog(integer0);
	activityLog0.setLogLevel("");
	String string0 = activityLog0.getLogLevel();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	Integer integer0 = new Integer((-2));
	ActivityLog activityLog0 = new ActivityLog(integer0);
	Implementation implementation0 = new Implementation((String) null);
	activityLog0.setImpId(implementation0);
	Implementation implementation1 = activityLog0.getImpId();
	assertNull(implementation1.getPeerReviewersName());
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	Integer integer0 = new Integer((-2076));
	ActivityLog activityLog0 = new ActivityLog(integer0);
	Integer integer1 = activityLog0.getId();
	assertEquals((-2076), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	Integer integer0 = new Integer(0);
	ActivityLog activityLog0 = new ActivityLog(integer0);
	Integer integer1 = activityLog0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	Integer integer0 = new Integer(3);
	ActivityLog activityLog0 = new ActivityLog(integer0);
	Integer integer1 = activityLog0.getId();
	assertEquals(3, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	Date mockDate0 = new Date(1641L);
	activityLog0.setCreatedDt(mockDate0);
	Date date0 = activityLog0.getCreatedDt();
	assertNotEquals("Thu Jan 01 00:00:01 GMT 1970", date0.toString());
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	activityLog0.setCreatedBy("com.tsi.workflow.beans.dao.ActivityLog[ id=null ]");
	String string0 = activityLog0.getCreatedBy();
	assertEquals("com.tsi.workflow.beans.dao.ActivityLog[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	Integer integer0 = new Integer(968);
	ActivityLog activityLog0 = new ActivityLog(integer0);
	activityLog0.setCreatedBy("");
	String string0 = activityLog0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	activityLog0.setActive("Invalid IOCase name: ");
	String string0 = activityLog0.getActive();
	assertEquals("Invalid IOCase name: ", string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	activityLog0.setActive("");
	String string0 = activityLog0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	Integer integer0 = new Integer(1);
	ActivityLog activityLog0 = new ActivityLog(integer0);
	ActivityLog activityLog1 = new ActivityLog();
	boolean boolean0 = activityLog0.equals(activityLog1);
	assertFalse(boolean0);
	assertFalse(activityLog1.equals((Object) activityLog0));
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	ActivityLog activityLog1 = new ActivityLog();
	Integer integer0 = new Integer((-1049));
	activityLog1.setId(integer0);
	boolean boolean0 = activityLog0.equals(activityLog1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	Integer integer0 = new Integer(43);
	ActivityLog activityLog0 = new ActivityLog(integer0);
	boolean boolean0 = activityLog0.equals(activityLog0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	Integer integer0 = new Integer(0);
	ActivityLog activityLog0 = new ActivityLog(integer0);
	boolean boolean0 = activityLog0.equals(integer0);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	boolean boolean0 = activityLog0.equals(activityLog0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	Integer integer0 = new Integer(968);
	ActivityLog activityLog0 = new ActivityLog(integer0);
	activityLog0.hashCode();
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	activityLog0.hashCode();
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	String string0 = activityLog0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	Integer integer0 = new Integer(968);
	ActivityLog activityLog0 = new ActivityLog(integer0);
	Date date0 = activityLog0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	Integer integer0 = activityLog0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	Integer integer0 = new Integer(43);
	ActivityLog activityLog0 = new ActivityLog(integer0);
	Date date0 = activityLog0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	activityLog0.setMessage("");
	String string0 = activityLog0.getMessage();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	activityLog0.setLogLevel("com.tsi.workflow.beans.dao.ActivityLog");
	String string0 = activityLog0.getLogLevel();
	assertEquals("com.tsi.workflow.beans.dao.ActivityLog", string0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	String string0 = activityLog0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	String string0 = activityLog0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	ImpPlan impPlan0 = activityLog0.getPlanId();
	assertNull(impPlan0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	String string0 = activityLog0.toString();
	assertEquals("com.tsi.workflow.beans.dao.ActivityLog[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	String string0 = activityLog0.getMessage();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	Implementation implementation0 = activityLog0.getImpId();
	assertNull(implementation0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	ActivityLog activityLog0 = new ActivityLog();
	String string0 = activityLog0.getLogLevel();
	assertNull(string0);
    }
}
