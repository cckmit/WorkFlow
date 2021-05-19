package com.tsi.workflow.mail;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StatusChangeToDependentPlanMailTest {

    public StatusChangeToDependentPlanMailTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail0 = new StatusChangeToDependentPlanMail();
	statusChangeToDependentPlanMail0.setReason("!\"T Ab=");
	String string0 = statusChangeToDependentPlanMail0.getReason();
	assertEquals("!\"T Ab=", string0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail0 = new StatusChangeToDependentPlanMail();
	statusChangeToDependentPlanMail0.setReason("");
	String string0 = statusChangeToDependentPlanMail0.getReason();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail0 = new StatusChangeToDependentPlanMail();
	statusChangeToDependentPlanMail0.setOldStatus("+2w1L=%h");
	String string0 = statusChangeToDependentPlanMail0.getOldStatus();
	assertEquals("+2w1L=%h", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail0 = new StatusChangeToDependentPlanMail();
	statusChangeToDependentPlanMail0.setNewStatus("${MTP_ENV}/mtpgitpopulate ");
	String string0 = statusChangeToDependentPlanMail0.getNewStatus();
	assertEquals("${MTP_ENV}/mtpgitpopulate ", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail0 = new StatusChangeToDependentPlanMail();
	statusChangeToDependentPlanMail0.setNewStatus("");
	String string0 = statusChangeToDependentPlanMail0.getNewStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail0 = new StatusChangeToDependentPlanMail();
	statusChangeToDependentPlanMail0.setImpPlanId(" - Status Changed");
	String string0 = statusChangeToDependentPlanMail0.getImpPlanId();
	assertEquals(" - Status Changed", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail0 = new StatusChangeToDependentPlanMail();
	statusChangeToDependentPlanMail0.setImpPlanId("");
	String string0 = statusChangeToDependentPlanMail0.getImpPlanId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail0 = new StatusChangeToDependentPlanMail();
	Date date01 = new Date();
	statusChangeToDependentPlanMail0.setActivityTime(date01);
	Date date0 = statusChangeToDependentPlanMail0.getActivityTime();
	assertSame(date0, date01);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail0 = new StatusChangeToDependentPlanMail();
	String string0 = statusChangeToDependentPlanMail0.getNewStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail0 = new StatusChangeToDependentPlanMail();
	statusChangeToDependentPlanMail0.setOldStatus("");
	String string0 = statusChangeToDependentPlanMail0.getOldStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail0 = new StatusChangeToDependentPlanMail();
	Date date0 = statusChangeToDependentPlanMail0.getActivityTime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail0 = new StatusChangeToDependentPlanMail();
	String string0 = statusChangeToDependentPlanMail0.getOldStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail0 = new StatusChangeToDependentPlanMail();
	String string0 = statusChangeToDependentPlanMail0.getReason();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	StatusChangeToDependentPlanMail statusChangeToDependentPlanMail0 = new StatusChangeToDependentPlanMail();
	String string0 = statusChangeToDependentPlanMail0.getImpPlanId();
	assertNull(string0);
    }

    /**
     * Test of getOldStatus method, of class StatusChangeToDependentPlanMail.
     */
    @Test
    public void testGetOldStatus() {
	StatusChangeToDependentPlanMail instance = new StatusChangeToDependentPlanMail();
	String expResult = "Active";
	instance.setOldStatus(expResult);
	String result = instance.getOldStatus();
	assertEquals(expResult, result);
    }

    /**
     * Test of getNewStatus method, of class StatusChangeToDependentPlanMail.
     */
    @Test
    public void testGetNewStatus() {
	StatusChangeToDependentPlanMail instance = new StatusChangeToDependentPlanMail();
	String expResult = "Active";
	instance.setNewStatus(expResult);
	String result = instance.getNewStatus();
	assertEquals(expResult, result);
    }

    /**
     * Test of getImpPlanId method, of class StatusChangeToDependentPlanMail.
     */
    @Test
    public void testGetImpPlanId() {
	StatusChangeToDependentPlanMail instance = new StatusChangeToDependentPlanMail();
	String expResult = "T1700123";
	instance.setImpPlanId(expResult);
	String result = instance.getImpPlanId();
	assertEquals(expResult, result);
    }

    /**
     * Test of processMessage method, of class StatusChangeToDependentPlanMail.
     */
    @Test
    public void testProcessMessage() {
	try {
	    StatusChangeToDependentPlanMail instance = new StatusChangeToDependentPlanMail();
	    instance.setImpPlanId("XXX");
	    instance.setOldStatus("");
	    instance.setNewStatus("");
	    instance.processMessage();
	    instance.setReason("Test");
	    instance.processMessage();
	} catch (Exception e) {

	}

    }

    @Test
    public void testProcessMessage2() {
	try {
	    StatusChangeToDependentPlanMail instance = new StatusChangeToDependentPlanMail();
	    instance.setImpPlanId("XXX");
	    instance.setOldStatus("ACTIVE");
	    instance.setNewStatus("SUBMITTED");
	    instance.setActivityTime(new Date());
	    instance.processMessage();
	    instance.setReason("Test");
	    instance.processMessage();
	} catch (Exception e) {

	}

    }

    @Test
    public void testProcessMessage002() {
	try {
	    StatusChangeToDependentPlanMail instance = new StatusChangeToDependentPlanMail();
	    instance.setImpPlanId("XXX");
	    instance.setOldStatus("ACTIVE");
	    instance.setNewStatus("SUBMITTED");
	    instance.setActivityTime(null);
	    instance.processMessage();
	    instance.setReason("Test");
	    instance.processMessage();
	} catch (Exception e) {

	}

    }

    @Test
    public void testgetActivityTime() {
	StatusChangeToDependentPlanMail instance = new StatusChangeToDependentPlanMail();
	Date lDate = new Date();
	instance.setActivityTime(lDate);
	Date result = instance.getActivityTime();
	assertEquals(lDate, result);
    }

    @Test
    public void testGetReason() {
	StatusChangeToDependentPlanMail instance = new StatusChangeToDependentPlanMail();
	instance.setReason("");
	String result = instance.getReason();
	assertEquals("", result);
    }
}
