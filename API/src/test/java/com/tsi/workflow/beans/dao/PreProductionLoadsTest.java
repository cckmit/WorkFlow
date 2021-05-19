
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
public class PreProductionLoadsTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	SystemLoad systemLoad0 = new SystemLoad();
	preProductionLoads0.setSystemLoadId(systemLoad0);
	SystemLoad systemLoad1 = preProductionLoads0.getSystemLoadId();
	assertNull(systemLoad1.getLoadAttendee());
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	preProductionLoads0.setStatus(" ]");
	String string0 = preProductionLoads0.getStatus();
	assertEquals(" ]", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	Integer integer0 = new Integer((-1073));
	PreProductionLoads preProductionLoads0 = new PreProductionLoads(integer0);
	preProductionLoads0.setStatus("");
	String string0 = preProductionLoads0.getStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	Integer integer0 = new Integer((-1073));
	PreProductionLoads preProductionLoads0 = new PreProductionLoads(integer0);
	ImpPlan impPlan0 = new ImpPlan();
	preProductionLoads0.setPlanId(impPlan0);
	ImpPlan impPlan1 = preProductionLoads0.getPlanId();
	assertNull(impPlan1.getProdVer());
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	Integer integer0 = new Integer(0);
	PreProductionLoads preProductionLoads0 = new PreProductionLoads(integer0);
	Date mockDate0 = new Date(0, 0, 0, 0, 0);
	preProductionLoads0.setModifiedDt(mockDate0);
	Date date0 = preProductionLoads0.getModifiedDt();
	assertNotEquals("Sun Dec 31 00:00:00 GMT 1899", date0.toString());
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	Integer integer0 = new Integer(0);
	PreProductionLoads preProductionLoads0 = new PreProductionLoads(integer0);
	preProductionLoads0.setModifiedBy("Y:JQ)");
	String string0 = preProductionLoads0.getModifiedBy();
	assertEquals("Y:JQ)", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	preProductionLoads0.setModifiedBy("");
	String string0 = preProductionLoads0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	preProductionLoads0.setLastActionStatus("com.tsi.workflow.beans.dao.Platform[ id=0 ]");
	String string0 = preProductionLoads0.getLastActionStatus();
	assertEquals("com.tsi.workflow.beans.dao.Platform[ id=0 ]", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	Integer integer0 = new Integer((-1073));
	PreProductionLoads preProductionLoads0 = new PreProductionLoads(integer0);
	preProductionLoads0.setLastActionStatus("");
	String string0 = preProductionLoads0.getLastActionStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	Integer integer0 = new Integer(0);
	preProductionLoads0.setId(integer0);
	Integer integer1 = preProductionLoads0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	Integer integer0 = new Integer(5);
	PreProductionLoads preProductionLoads0 = new PreProductionLoads(integer0);
	Integer integer1 = preProductionLoads0.getId();
	assertEquals(5, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	Integer integer0 = new Integer((-1073));
	PreProductionLoads preProductionLoads0 = new PreProductionLoads(integer0);
	Integer integer1 = preProductionLoads0.getId();
	assertEquals((-1073), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	Date mockDate0 = new Date(0L);
	preProductionLoads0.setDeActivatedDateTime(mockDate0);
	Date date0 = preProductionLoads0.getDeActivatedDateTime();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	Date mockDate0 = new Date(1111L);
	preProductionLoads0.setCreatedDt(mockDate0);
	Date date0 = preProductionLoads0.getCreatedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	preProductionLoads0.setCreatedBy(".`6Ty2VW*DW;,Ne7");
	String string0 = preProductionLoads0.getCreatedBy();
	assertEquals(".`6Ty2VW*DW;,Ne7", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	preProductionLoads0.setCreatedBy("");
	String string0 = preProductionLoads0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	SystemCpu systemCpu0 = new SystemCpu();
	preProductionLoads0.setCpuId(systemCpu0);
	SystemCpu systemCpu1 = preProductionLoads0.getCpuId();
	assertSame(systemCpu1, systemCpu0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	Integer integer0 = new Integer(1986);
	PreProductionLoads preProductionLoads0 = new PreProductionLoads(integer0);
	preProductionLoads0.setActive("Invalid IOCase name: ");
	String string0 = preProductionLoads0.getActive();
	assertEquals("Invalid IOCase name: ", string0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	preProductionLoads0.setActive("");
	String string0 = preProductionLoads0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	Integer integer0 = new Integer((-1073));
	PreProductionLoads preProductionLoads0 = new PreProductionLoads(integer0);
	Date mockDate0 = new Date();
	preProductionLoads0.setActivatedDateTime(mockDate0);
	Date date0 = preProductionLoads0.getActivatedDateTime();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	PreProductionLoads preProductionLoads1 = new PreProductionLoads();
	Integer integer0 = Integer.valueOf(0);
	preProductionLoads0.setId(integer0);
	boolean boolean0 = preProductionLoads0.equals(preProductionLoads1);
	assertFalse(preProductionLoads1.equals((Object) preProductionLoads0));
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	Integer integer0 = new Integer(0);
	PreProductionLoads preProductionLoads1 = new PreProductionLoads(integer0);
	boolean boolean0 = preProductionLoads0.equals(preProductionLoads1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	Integer integer0 = Integer.valueOf(0);
	preProductionLoads0.setId(integer0);
	boolean boolean0 = preProductionLoads0.equals(preProductionLoads0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	Object object0 = new Object();
	boolean boolean0 = preProductionLoads0.equals(object0);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	boolean boolean0 = preProductionLoads0.equals(preProductionLoads0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	Integer integer0 = new Integer((-434));
	PreProductionLoads preProductionLoads0 = new PreProductionLoads(integer0);
	preProductionLoads0.hashCode();
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	preProductionLoads0.hashCode();
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	Integer integer0 = new Integer(0);
	PreProductionLoads preProductionLoads0 = new PreProductionLoads(integer0);
	Date date0 = preProductionLoads0.getDeActivatedDateTime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	Date date0 = preProductionLoads0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	System system0 = preProductionLoads0.getSystemId();
	assertNull(system0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	String string0 = preProductionLoads0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	Integer integer0 = preProductionLoads0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	Date date0 = preProductionLoads0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	Integer integer0 = new Integer(0);
	PreProductionLoads preProductionLoads0 = new PreProductionLoads(integer0);
	String string0 = preProductionLoads0.getLastActionStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	String string0 = preProductionLoads0.toString();
	assertEquals("com.tsi.workflow.beans.dao.PreProductionLoads[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	Integer integer0 = new Integer(0);
	PreProductionLoads preProductionLoads0 = new PreProductionLoads(integer0);
	ImpPlan impPlan0 = preProductionLoads0.getPlanId();
	assertNull(impPlan0);
    }

    @Test(timeout = 4000)
    public void test36() throws Throwable {
	Integer integer0 = new Integer(0);
	PreProductionLoads preProductionLoads0 = new PreProductionLoads(integer0);
	Date date0 = preProductionLoads0.getActivatedDateTime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test37() throws Throwable {
	Integer integer0 = new Integer(0);
	PreProductionLoads preProductionLoads0 = new PreProductionLoads(integer0);
	String string0 = preProductionLoads0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test38() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	SystemLoad systemLoad0 = preProductionLoads0.getSystemLoadId();
	assertNull(systemLoad0);
    }

    @Test(timeout = 4000)
    public void test39() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	System system0 = new System();
	preProductionLoads0.setSystemId(system0);
	System system1 = preProductionLoads0.getSystemId();
	assertNull(system1.getLoadsetNamePrefix());
    }

    @Test(timeout = 4000)
    public void test40() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	String string0 = preProductionLoads0.getStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test41() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	SystemCpu systemCpu0 = preProductionLoads0.getCpuId();
	assertNull(systemCpu0);
    }

    @Test(timeout = 4000)
    public void test42() throws Throwable {
	PreProductionLoads preProductionLoads0 = new PreProductionLoads();
	String string0 = preProductionLoads0.getCreatedBy();
	assertNull(string0);
    }
}
