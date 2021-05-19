package com.tsi.workflow.beans.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Date;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class LegacyFallBackPlanTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	legacyFallBackPlan0.setTargetSystem(",");
	String string0 = legacyFallBackPlan0.getTargetSystem();
	assertEquals(",", string0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	Integer integer0 = new Integer(837);
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan(integer0);
	legacyFallBackPlan0.setTargetSystem("");
	String string0 = legacyFallBackPlan0.getTargetSystem();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	legacyFallBackPlan0.setProgramName("9<T'}");
	String string0 = legacyFallBackPlan0.getProgramName();
	assertEquals("9<T'}", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	legacyFallBackPlan0.setProgramName("");
	String string0 = legacyFallBackPlan0.getProgramName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	Integer integer0 = new Integer(2);
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan(integer0);
	legacyFallBackPlan0.setPlanId("(H{B7i");
	String string0 = legacyFallBackPlan0.getPlanId();
	assertEquals("(H{B7i", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	legacyFallBackPlan0.setPlanId("");
	String string0 = legacyFallBackPlan0.getPlanId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	Date mockDate0 = new Date((-565), (-565), 798, 798, 798);
	legacyFallBackPlan0.setModifiedDt(mockDate0);
	Date date0 = legacyFallBackPlan0.getModifiedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	legacyFallBackPlan0.setModifiedBy(", programName=");
	String string0 = legacyFallBackPlan0.getModifiedBy();
	assertEquals(", programName=", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	legacyFallBackPlan0.setModifiedBy("");
	String string0 = legacyFallBackPlan0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	Integer integer0 = new Integer(837);
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan(integer0);
	legacyFallBackPlan0.setLoadDateTime("Y|~|v");
	String string0 = legacyFallBackPlan0.getLoadDateTime();
	assertEquals("Y|~|v", string0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	legacyFallBackPlan0.setLoadDateTime("");
	String string0 = legacyFallBackPlan0.getLoadDateTime();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	Integer integer0 = new Integer(0);
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan(integer0);
	Integer integer1 = legacyFallBackPlan0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	Integer integer0 = new Integer(2);
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan(integer0);
	Integer integer1 = legacyFallBackPlan0.getId();
	assertEquals(2, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	Integer integer0 = new Integer((-1));
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan(integer0);
	Integer integer1 = legacyFallBackPlan0.getId();
	assertEquals((-1), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	legacyFallBackPlan0.setFuncArea("The filter must not be null");
	String string0 = legacyFallBackPlan0.getFuncArea();
	assertEquals("The filter must not be null", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	legacyFallBackPlan0.setFuncArea("");
	String string0 = legacyFallBackPlan0.getFuncArea();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	Date mockDate0 = new Date((-565), (-565), 798, 798, 798);
	legacyFallBackPlan0.setCreatedDt(mockDate0);
	Date date0 = legacyFallBackPlan0.getCreatedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	legacyFallBackPlan0.setCreatedBy("");
	String string0 = legacyFallBackPlan0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	legacyFallBackPlan0.setActive("tP");
	String string0 = legacyFallBackPlan0.getActive();
	assertEquals("tP", string0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	Integer integer0 = new Integer(2);
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan(integer0);
	legacyFallBackPlan0.setActive("");
	String string0 = legacyFallBackPlan0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	String string0 = legacyFallBackPlan0.getTargetSystem();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	Integer integer0 = new Integer(506);
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan(integer0);
	Date date0 = legacyFallBackPlan0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	String string0 = legacyFallBackPlan0.getFuncArea();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	String string0 = legacyFallBackPlan0.toString();
	assertEquals("LegacyFallBackPlan [id=null, loadDateTime=null, planId=null, programName=null, targetSystem=null, createdBy=null, createdDt=null, modifiedBy=null, modifiedDt=null, active=null]", string0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	String string0 = legacyFallBackPlan0.getLoadDateTime();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	legacyFallBackPlan0.setCreatedBy("\\oa`N-(R69g?@&");
	String string0 = legacyFallBackPlan0.getCreatedBy();
	assertEquals("\\oa`N-(R69g?@&", string0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	Integer integer0 = legacyFallBackPlan0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	Date date0 = legacyFallBackPlan0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	String string0 = legacyFallBackPlan0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	legacyFallBackPlan0.setId((Integer) null);
	assertNull(legacyFallBackPlan0.getFuncArea());
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	Integer integer0 = new Integer(506);
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan(integer0);
	String string0 = legacyFallBackPlan0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	String string0 = legacyFallBackPlan0.getProgramName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	String string0 = legacyFallBackPlan0.getPlanId();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	LegacyFallBackPlan legacyFallBackPlan0 = new LegacyFallBackPlan();
	String string0 = legacyFallBackPlan0.getActive();
	assertNull(string0);
    }
}
