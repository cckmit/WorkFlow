
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
public class SystemLoadActionsTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	Integer integer0 = new Integer(3);
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	systemLoadActions0.hashCode();
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	Integer integer0 = new Integer((-18));
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	Vpars vpars0 = new Vpars();
	systemLoadActions0.setVparId(vpars0);
	Vpars vpars1 = systemLoadActions0.getVparId();
	assertNull(vpars1.getName());
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	systemLoadActions0.setTestStatus("eMP]qT|4;?Q9c");
	String string0 = systemLoadActions0.getTestStatus();
	assertEquals("eMP]qT|4;?Q9c", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	systemLoadActions0.setTestStatus("");
	String string0 = systemLoadActions0.getTestStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	Integer integer0 = new Integer(3);
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoadActions0.setSystemLoadId(systemLoad0);
	SystemLoad systemLoad1 = systemLoadActions0.getSystemLoadId();
	assertNull(systemLoad1.getActive());
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	systemLoadActions0.setStatus("eMP]qT|4;?Q9c");
	String string0 = systemLoadActions0.getStatus();
	assertEquals("eMP]qT|4;?Q9c", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	systemLoadActions0.setStatus("");
	String string0 = systemLoadActions0.getStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	ImpPlan impPlan0 = new ImpPlan("");
	systemLoadActions0.setPlanId(impPlan0);
	ImpPlan impPlan1 = systemLoadActions0.getPlanId();
	assertNull(impPlan1.getOthContact());
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	Date mockDate0 = new Date((-738), 2158, 1);
	systemLoadActions0.setModifiedDt(mockDate0);
	Date date0 = systemLoadActions0.getModifiedDt();
	assertNotEquals("Thu Nov 01 00:00:00 GMT 1341", date0.toString());
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	systemLoadActions0.setModifiedBy("com.tsi.workflow.beans.dao.SystemLoadActions");
	String string0 = systemLoadActions0.getModifiedBy();
	assertEquals("com.tsi.workflow.beans.dao.SystemLoadActions", string0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	Integer integer0 = new Integer(3);
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	systemLoadActions0.setLastActionStatus("Ay}W&&wPf9@.~4j_");
	String string0 = systemLoadActions0.getLastActionStatus();
	assertEquals("Ay}W&&wPf9@.~4j_", string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	Integer integer1 = systemLoadActions0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	Integer integer0 = new Integer(3);
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	Integer integer1 = systemLoadActions0.getId();
	assertEquals(3, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	Integer integer0 = new Integer((-1));
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	Integer integer1 = systemLoadActions0.getId();
	assertEquals((-1), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	Integer integer0 = new Integer(1);
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	Date mockDate0 = new Date((-3028), 0, 1, 1, 1, (-472));
	systemLoadActions0.setDeActivatedDateTime(mockDate0);
	Date date0 = systemLoadActions0.getDeActivatedDateTime();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	Date date0 = new Date();
	systemLoadActions0.setCreatedDt(date0);
	Date date1 = systemLoadActions0.getCreatedDt();
	assertSame(date1, date0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	systemLoadActions0.setCreatedBy(" |<F8s=?Y");
	String string0 = systemLoadActions0.getCreatedBy();
	assertEquals(" |<F8s=?Y", string0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	systemLoadActions0.setCreatedBy("");
	String string0 = systemLoadActions0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	systemLoadActions0.setActive("q&/");
	String string0 = systemLoadActions0.getActive();
	assertEquals("q&/", string0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	Date mockDate0 = new Date(0L);
	systemLoadActions0.setActivatedDateTime(mockDate0);
	Date date0 = systemLoadActions0.getActivatedDateTime();
	assertNotEquals("Thu Jan 01 00:00:00 GMT 1970", date0.toString());
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	Integer integer1 = new Integer((-839));
	SystemLoadActions systemLoadActions1 = new SystemLoadActions(integer1);
	boolean boolean0 = systemLoadActions0.equals(systemLoadActions1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	SystemLoadActions systemLoadActions1 = new SystemLoadActions();
	Integer integer0 = new Integer((-51));
	systemLoadActions1.setId(integer0);
	boolean boolean0 = systemLoadActions0.equals(systemLoadActions1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	boolean boolean0 = systemLoadActions0.equals(systemLoadActions0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	boolean boolean0 = systemLoadActions0.equals((Object) null);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	boolean boolean0 = systemLoadActions0.equals(systemLoadActions0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	systemLoadActions0.hashCode();
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	systemLoadActions0.setActive("");
	String string0 = systemLoadActions0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	Date date0 = systemLoadActions0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	System system0 = systemLoadActions0.getSystemId();
	assertNull(system0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	Date date0 = systemLoadActions0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	String string0 = systemLoadActions0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	Integer integer0 = systemLoadActions0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	System system0 = new System();
	systemLoadActions0.setSystemId(system0);
	System system1 = systemLoadActions0.getSystemId();
	assertNull(system1.getIpaddress());
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	Vpars vpars0 = systemLoadActions0.getVparId();
	assertNull(vpars0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	String string0 = systemLoadActions0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	String string0 = systemLoadActions0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test36() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	String string0 = systemLoadActions0.getStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test37() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	systemLoadActions0.setModifiedBy("");
	String string0 = systemLoadActions0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test38() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	Date date0 = systemLoadActions0.getDeActivatedDateTime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test39() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	Date date0 = systemLoadActions0.getActivatedDateTime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test40() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	SystemLoad systemLoad0 = systemLoadActions0.getSystemLoadId();
	assertNull(systemLoad0);
    }

    @Test(timeout = 4000)
    public void test41() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	String string0 = systemLoadActions0.getTestStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test42() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	systemLoadActions0.setLastActionStatus("");
	String string0 = systemLoadActions0.getLastActionStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test43() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	String string0 = systemLoadActions0.toString();
	assertEquals("com.tsi.workflow.beans.dao.SystemLoadActions[ id=0 ]", string0);
    }

    @Test(timeout = 4000)
    public void test44() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	String string0 = systemLoadActions0.getLastActionStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test45() throws Throwable {
	SystemLoadActions systemLoadActions0 = new SystemLoadActions();
	ImpPlan impPlan0 = systemLoadActions0.getPlanId();
	assertNull(impPlan0);
    }
}
