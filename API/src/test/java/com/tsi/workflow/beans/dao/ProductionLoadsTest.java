
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
public class ProductionLoadsTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	Integer integer0 = new Integer((-601));
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	productionLoads0.setSystemLoadId(systemLoad0);
	SystemLoad systemLoad1 = productionLoads0.getSystemLoadId();
	assertNull(systemLoad1.getActive());
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	productionLoads0.setStatus("rNI5Dj vo]e45[");
	String string0 = productionLoads0.getStatus();
	assertEquals("rNI5Dj vo]e45[", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	productionLoads0.setStatus("");
	String string0 = productionLoads0.getStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	ImpPlan impPlan0 = new ImpPlan("");
	productionLoads0.setPlanId(impPlan0);
	ImpPlan impPlan1 = productionLoads0.getPlanId();
	assertNull(impPlan1.getDevMgrComment());
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	Date mockDate0 = new Date(1277, 1277, (-2107), (-2107), 1277);
	productionLoads0.setModifiedDt(mockDate0);
	Date date0 = productionLoads0.getModifiedDt();
	assertNotEquals("Fri May 28 02:17:00 GMT 3277", date0.toString());
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	Integer integer0 = new Integer(22);
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	productionLoads0.setModifiedBy("4");
	String string0 = productionLoads0.getModifiedBy();
	assertEquals("4", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	Integer integer0 = new Integer(22);
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	productionLoads0.setModifiedBy("");
	String string0 = productionLoads0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	productionLoads0.setLastActionStatus("^+>LFpps'q=g*ew/");
	String string0 = productionLoads0.getLastActionStatus();
	assertEquals("^+>LFpps'q=g*ew/", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	productionLoads0.setLastActionStatus("");
	String string0 = productionLoads0.getLastActionStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	Integer integer0 = productionLoads0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	Integer integer0 = new Integer((-4444));
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	Integer integer1 = productionLoads0.getId();
	assertEquals((-4444), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	Integer integer0 = new Integer(0);
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	Integer integer1 = productionLoads0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	Integer integer0 = new Integer(0);
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	Integer integer1 = new Integer(5);
	productionLoads0.setId(integer1);
	Integer integer2 = productionLoads0.getId();
	assertEquals(5, (int) integer2);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	Integer integer0 = new Integer(0);
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	Date Date0 = new Date(0, 0, 5, 0, 0);
	productionLoads0.setFallbackDeActivatedDateTime(Date0);
	Date date0 = productionLoads0.getFallbackDeActivatedDateTime();
	assertNotEquals("Fri Jan 05 00:00:00 GMT 1900", date0.toString());
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	Date Date0 = new Date((-4444), (-4444), (-4444), (-4444), (-4444));
	productionLoads0.setFallbackActivatedDateTime(Date0);
	Date date0 = productionLoads0.getFallbackActivatedDateTime();
	assertSame(date0, Date0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	Date Date0 = new Date();
	productionLoads0.setDeActivatedDateTime(Date0);
	Date date0 = productionLoads0.getDeActivatedDateTime();
	assertSame(date0, Date0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	Date Date0 = new Date((-2302), 1, (-82), 1, (-2302), 1);
	productionLoads0.setCreatedDt(Date0);
	Date date0 = productionLoads0.getCreatedDt();
	assertSame(date0, Date0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	productionLoads0.setCreatedBy("z(NR39N.BHc|K,tfADs");
	String string0 = productionLoads0.getCreatedBy();
	assertEquals("z(NR39N.BHc|K,tfADs", string0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	productionLoads0.setCreatedBy("");
	String string0 = productionLoads0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	SystemCpu systemCpu0 = new SystemCpu();
	productionLoads0.setCpuId(systemCpu0);
	SystemCpu systemCpu1 = productionLoads0.getCpuId();
	assertNull(systemCpu1.getCpuCode());
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	Integer integer0 = new Integer(0);
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	productionLoads0.setActive("(");
	String string0 = productionLoads0.getActive();
	assertEquals("(", string0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	Integer integer0 = new Integer(22);
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	productionLoads0.setActive("");
	String string0 = productionLoads0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	Integer integer0 = new Integer((-1));
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	Date Date0 = new Date(1, 0, (-323), (-1768), (-1));
	productionLoads0.setActivatedDateTime(Date0);
	Date date0 = productionLoads0.getActivatedDateTime();
	assertSame(date0, Date0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	Integer integer0 = new Integer((-3324));
	Integer integer1 = Integer.getInteger((String) null, (-922));
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	ProductionLoads productionLoads1 = new ProductionLoads(integer1);
	boolean boolean0 = productionLoads0.equals(productionLoads1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	Integer integer0 = new Integer((-4444));
	ProductionLoads productionLoads1 = new ProductionLoads(integer0);
	boolean boolean0 = productionLoads0.equals(productionLoads1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	Integer integer0 = new Integer((-3324));
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	boolean boolean0 = productionLoads0.equals(productionLoads0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	boolean boolean0 = productionLoads0.equals((Object) null);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	boolean boolean0 = productionLoads0.equals(productionLoads0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	Integer integer0 = new Integer(671);
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	productionLoads0.hashCode();
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	productionLoads0.hashCode();
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	Date date0 = productionLoads0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	Integer integer0 = new Integer(671);
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	SystemCpu systemCpu0 = productionLoads0.getCpuId();
	assertNull(systemCpu0);
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	SystemLoad systemLoad0 = productionLoads0.getSystemLoadId();
	assertNull(systemLoad0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	String string0 = productionLoads0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	String string0 = productionLoads0.getLastActionStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	Date date0 = productionLoads0.getFallbackActivatedDateTime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test36() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	String string0 = productionLoads0.getStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test37() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	Date date0 = productionLoads0.getDeActivatedDateTime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test38() throws Throwable {
	Integer integer0 = new Integer(5);
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	System system0 = productionLoads0.getSystemId();
	assertNull(system0);
    }

    @Test(timeout = 4000)
    public void test39() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	Date date0 = productionLoads0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test40() throws Throwable {
	Integer integer0 = new Integer(5);
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	System system0 = new System(integer0);
	productionLoads0.setSystemId(system0);
	System system1 = productionLoads0.getSystemId();
	assertNull(system1.getDefaultProdCpu());
    }

    @Test(timeout = 4000)
    public void test41() throws Throwable {
	Integer integer0 = new Integer((-3324));
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	String string0 = productionLoads0.toString();
	assertEquals("com.tsi.workflow.beans.dao.ProductionLoads[ id=-3324 ]", string0);
    }

    @Test(timeout = 4000)
    public void test42() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	String string0 = productionLoads0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test43() throws Throwable {
	Integer integer0 = new Integer((-3324));
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	Date date0 = productionLoads0.getActivatedDateTime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test44() throws Throwable {
	Integer integer0 = new Integer((-30));
	ProductionLoads productionLoads0 = new ProductionLoads(integer0);
	ImpPlan impPlan0 = productionLoads0.getPlanId();
	assertNull(impPlan0);
    }

    @Test(timeout = 4000)
    public void test45() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	String string0 = productionLoads0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test46() throws Throwable {
	ProductionLoads productionLoads0 = new ProductionLoads();
	Date date0 = productionLoads0.getFallbackDeActivatedDateTime();
	assertNull(date0);
    }
}
