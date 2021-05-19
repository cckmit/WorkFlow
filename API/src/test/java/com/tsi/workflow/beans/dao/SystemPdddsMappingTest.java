
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
public class SystemPdddsMappingTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	SystemLoad systemLoad0 = new SystemLoad();
	systemPdddsMapping0.setSystemLoadId(systemLoad0);
	SystemLoad systemLoad1 = systemPdddsMapping0.getSystemLoadId();
	assertNull(systemLoad1.getPreloadJust());
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	ImpPlan impPlan0 = new ImpPlan();
	systemPdddsMapping0.setPlanId(impPlan0);
	ImpPlan impPlan1 = systemPdddsMapping0.getPlanId();
	assertNull(impPlan1.getDevManagerName());
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	systemPdddsMapping0.setPdddsLibraryId(pdddsLibrary0);
	PdddsLibrary pdddsLibrary1 = systemPdddsMapping0.getPdddsLibraryId();
	assertNull(pdddsLibrary1.getModifiedBy());
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	systemPdddsMapping0.setModifiedBy("4F");
	String string0 = systemPdddsMapping0.getModifiedBy();
	assertEquals("4F", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	systemPdddsMapping0.setModifiedBy("");
	String string0 = systemPdddsMapping0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	Integer integer0 = new Integer(0);
	systemPdddsMapping0.setId(integer0);
	Integer integer1 = systemPdddsMapping0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	Integer integer0 = new Integer(1351);
	systemPdddsMapping0.setId(integer0);
	Integer integer1 = systemPdddsMapping0.getId();
	assertEquals(1351, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	Integer integer0 = new Integer((-2679));
	systemPdddsMapping0.setId(integer0);
	Integer integer1 = systemPdddsMapping0.getId();
	assertEquals((-2679), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	Date mockDate0 = new Date(990, 990, 4);
	systemPdddsMapping0.setCreatedDt(mockDate0);
	Date date0 = systemPdddsMapping0.getCreatedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	systemPdddsMapping0.setCreatedBy("aa&RBa!5EU_");
	String string0 = systemPdddsMapping0.getCreatedBy();
	assertEquals("aa&RBa!5EU_", string0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	systemPdddsMapping0.setCreatedBy("");
	String string0 = systemPdddsMapping0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	systemPdddsMapping0.setActive("SA3P/b)<Gq8/v");
	String string0 = systemPdddsMapping0.getActive();
	assertEquals("SA3P/b)<Gq8/v", string0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	systemPdddsMapping0.setActive("");
	String string0 = systemPdddsMapping0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	Integer integer0 = new Integer((-2679));
	systemPdddsMapping0.setId(integer0);
	SystemPdddsMapping systemPdddsMapping1 = new SystemPdddsMapping();
	boolean boolean0 = systemPdddsMapping0.equals(systemPdddsMapping1);
	assertFalse(boolean0);
	assertFalse(systemPdddsMapping1.equals((Object) systemPdddsMapping0));
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	SystemPdddsMapping systemPdddsMapping1 = new SystemPdddsMapping();
	Integer integer0 = new Integer(551);
	systemPdddsMapping1.setId(integer0);
	boolean boolean0 = systemPdddsMapping0.equals(systemPdddsMapping1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	Integer integer0 = new Integer((-2679));
	systemPdddsMapping0.setId(integer0);
	boolean boolean0 = systemPdddsMapping0.equals(systemPdddsMapping0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	boolean boolean0 = systemPdddsMapping0.equals("com.tsi.workflow.beans.dao.SystemPdddsMapping[ id=null ]");
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	boolean boolean0 = systemPdddsMapping0.equals(systemPdddsMapping0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	Integer integer0 = new Integer(3243);
	systemPdddsMapping0.setId(integer0);
	systemPdddsMapping0.hashCode();
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	systemPdddsMapping0.hashCode();
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	Date mockDate0 = new Date(3243, 3243, 3243);
	systemPdddsMapping0.setModifiedDt(mockDate0);
	Date date0 = systemPdddsMapping0.getModifiedDt();
	assertNotEquals("Fri Feb 15 00:00:00 GMT 5422", date0.toString());
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	String string0 = systemPdddsMapping0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	Integer integer0 = systemPdddsMapping0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	ImpPlan impPlan0 = systemPdddsMapping0.getPlanId();
	assertNull(impPlan0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	SystemLoad systemLoad0 = systemPdddsMapping0.getSystemLoadId();
	assertNull(systemLoad0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	Date date0 = systemPdddsMapping0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	String string0 = systemPdddsMapping0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	String string0 = systemPdddsMapping0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	PdddsLibrary pdddsLibrary0 = systemPdddsMapping0.getPdddsLibraryId();
	assertNull(pdddsLibrary0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	Date date0 = systemPdddsMapping0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	String string0 = systemPdddsMapping0.toString();
	assertEquals("com.tsi.workflow.beans.dao.SystemPdddsMapping[ id=null ]", string0);
    }
}
