
package com.tsi.workflow.beans.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class DbcrTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	Integer integer0 = new Integer(1750);
	Dbcr dbcr0 = new Dbcr(integer0);
	System system0 = new System();
	dbcr0.setSystemId(system0);
	System system1 = dbcr0.getSystemId();
	assertNull(system1.getId());
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	dbcr0.setStatus("~#~cCB");
	String string0 = dbcr0.getStatus();
	assertEquals("~#~cCB", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	Dbcr dbcr0 = new Dbcr((Integer) null);
	dbcr0.setStatus("");
	String string0 = dbcr0.getStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	Integer integer0 = new Integer((-1527));
	Dbcr dbcr0 = new Dbcr(integer0);
	ImpPlan impPlan0 = new ImpPlan();
	dbcr0.setPlanId(impPlan0);
	ImpPlan impPlan1 = dbcr0.getPlanId();
	assertNull(impPlan1.getModifiedBy());
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	Date mockDate0 = new Date((-1), (-1), (-337), 816, (-337));
	dbcr0.setModifiedDt(mockDate0);
	Date date0 = dbcr0.getModifiedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	dbcr0.setModifiedBy("DEFAULT_INCLUSION");
	String string0 = dbcr0.getModifiedBy();
	assertEquals("DEFAULT_INCLUSION", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	Integer integer0 = new Integer((-1527));
	Dbcr dbcr0 = new Dbcr(integer0);
	dbcr0.setModifiedBy("");
	String string0 = dbcr0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	Integer integer0 = new Integer(0);
	Dbcr dbcr0 = new Dbcr(integer0);
	dbcr0.setMandatory("+vxO&oR% ");
	String string0 = dbcr0.getMandatory();
	assertEquals("+vxO&oR% ", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	Integer integer0 = new Integer((-158));
	Dbcr dbcr0 = new Dbcr(integer0);
	dbcr0.setMandatory("");
	String string0 = dbcr0.getMandatory();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	Integer integer0 = new Integer((-1527));
	Dbcr dbcr0 = new Dbcr(integer0);
	Integer integer1 = dbcr0.getId();
	assertEquals((-1527), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	Integer integer0 = Integer.valueOf(0);
	Dbcr dbcr0 = new Dbcr(integer0);
	Integer integer1 = dbcr0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	Integer integer0 = Integer.getInteger("com.fasterxml.jackson.databind.JsonSerializer", 104);
	Dbcr dbcr0 = new Dbcr(integer0);
	Integer integer1 = dbcr0.getId();
	assertEquals(104, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	Integer integer0 = new Integer(1750);
	Dbcr dbcr0 = new Dbcr(integer0);
	Integer integer1 = dbcr0.getId();
	assertEquals(1750, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	Integer integer0 = Integer.getInteger("com.fasterxml.jackson.databind.JsonSerializer", 104);
	Dbcr dbcr0 = new Dbcr(integer0);
	dbcr0.setEnvironment("com.fasterxml.jackson.databind.JsonSerializer");
	String string0 = dbcr0.getEnvironment();
	assertEquals("com.fasterxml.jackson.databind.JsonSerializer", string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	Integer integer0 = new Integer(1750);
	Dbcr dbcr0 = new Dbcr(integer0);
	dbcr0.setEnvironment("");
	String string0 = dbcr0.getEnvironment();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	Integer integer0 = new Integer(3);
	Dbcr dbcr0 = new Dbcr(integer0);
	dbcr0.setDbcrName("Sy$[NK*=");
	String string0 = dbcr0.getDbcrName();
	assertEquals("Sy$[NK*=", string0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	Integer integer0 = new Integer(0);
	Dbcr dbcr0 = new Dbcr(integer0);
	Date mockDate0 = new Date();
	dbcr0.setCreatedDt(mockDate0);
	Date date0 = dbcr0.getCreatedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	dbcr0.setCreatedBy("h");
	String string0 = dbcr0.getCreatedBy();
	assertEquals("h", string0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	dbcr0.setCreatedBy("");
	String string0 = dbcr0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	Integer integer0 = Integer.valueOf(0);
	Dbcr dbcr0 = new Dbcr(integer0);
	dbcr0.setActive("DpZK");
	String string0 = dbcr0.getActive();
	assertEquals("DpZK", string0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	Integer integer0 = new Integer((-725));
	dbcr0.setId(integer0);
	Dbcr dbcr1 = new Dbcr();
	boolean boolean0 = dbcr0.equals(dbcr1);
	assertFalse(dbcr1.equals((Object) dbcr0));
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	Integer integer0 = new Integer(1);
	Dbcr dbcr1 = new Dbcr(integer0);
	boolean boolean0 = dbcr0.equals(dbcr1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	Integer integer0 = new Integer((-21));
	dbcr0.setId(integer0);
	boolean boolean0 = dbcr0.equals(dbcr0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	Object object0 = new Object();
	boolean boolean0 = dbcr0.equals(object0);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	boolean boolean0 = dbcr0.equals(dbcr0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	dbcr0.hashCode();
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	String string0 = dbcr0.getStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	String string0 = dbcr0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	Date date0 = dbcr0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	dbcr0.setActive("");
	String string0 = dbcr0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	String string0 = dbcr0.getMandatory();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	dbcr0.setDbcrName("");
	String string0 = dbcr0.getDbcrName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	ImpPlan impPlan0 = dbcr0.getPlanId();
	assertNull(impPlan0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	String string0 = dbcr0.getDbcrName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	Date date0 = dbcr0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	String string0 = dbcr0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test36() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	Integer integer0 = dbcr0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test37() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	String string0 = dbcr0.toString();
	assertEquals("com.tsi.workflow.beans.dao.Dbcr[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test38() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	String string0 = dbcr0.getEnvironment();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test39() throws Throwable {
	Integer integer0 = new Integer((-6183));
	Dbcr dbcr0 = new Dbcr(integer0);
	String string0 = dbcr0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test40() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	System system0 = dbcr0.getSystemId();
	assertNull(system0);
    }

    @Test(timeout = 4000)
    public void test41() throws Throwable {
	Dbcr dbcr0 = new Dbcr();
	Integer integer0 = new Integer((-21));
	dbcr0.setId(integer0);
	dbcr0.hashCode();
    }
}
