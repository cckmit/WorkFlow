
package com.tsi.workflow.beans.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class SystemLoadTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	Integer integer0 = new Integer((-1938));
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	LinkedList<SystemPdddsMapping> linkedList0 = new LinkedList<SystemPdddsMapping>();
	systemLoad0.setSystemPdddsMappingList(linkedList0);
	List<SystemPdddsMapping> list0 = systemLoad0.getSystemPdddsMappingList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	Integer integer0 = new Integer((-1938));
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	LinkedList<SystemPdddsMapping> linkedList0 = new LinkedList<SystemPdddsMapping>();
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	linkedList0.add(systemPdddsMapping0);
	systemLoad0.setSystemPdddsMappingList(linkedList0);
	List<SystemPdddsMapping> list0 = systemLoad0.getSystemPdddsMappingList();
	assertFalse(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	LinkedList<SystemLoadActions> linkedList0 = new LinkedList<SystemLoadActions>();
	systemLoad0.setSystemLoadActionsList(linkedList0);
	List<SystemLoadActions> list0 = systemLoad0.getSystemLoadActionsList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	System system0 = new System();
	systemLoad0.setSystemId(system0);
	System system1 = systemLoad0.getSystemId();
	assertNull(system1.getIpaddress());
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	Integer integer0 = new Integer((-1938));
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	systemLoad0.setQaRegressionBypassComment("");
	String string0 = systemLoad0.getQaRegressionBypassComment();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	systemLoad0.setQaFunctionalBypassComment("");
	String string0 = systemLoad0.getQaFunctionalBypassComment();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setQaBypassStatus("org.apache.commons.io.filefilter.FalseFileFilter");
	String string0 = systemLoad0.getQaBypassStatus();
	assertEquals("org.apache.commons.io.filefilter.FalseFileFilter", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	systemLoad0.setQaBypassStatus("");
	String string0 = systemLoad0.getQaBypassStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	Integer integer0 = new Integer(0);
	PutLevel putLevel0 = new PutLevel(integer0);
	systemLoad0.setPutLevelId(putLevel0);
	PutLevel putLevel1 = systemLoad0.getPutLevelId();
	assertNull(putLevel1.getCreatedBy());
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	LinkedList<ProductionLoads> linkedList0 = new LinkedList<ProductionLoads>();
	systemLoad0.setProductionLoadsList(linkedList0);
	List<ProductionLoads> list0 = systemLoad0.getProductionLoadsList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	LinkedList<ProductionLoads> linkedList0 = new LinkedList<ProductionLoads>();
	ProductionLoads productionLoads0 = new ProductionLoads();
	linkedList0.offerLast(productionLoads0);
	systemLoad0.setProductionLoadsList(linkedList0);
	List<ProductionLoads> list0 = systemLoad0.getProductionLoadsList();
	assertFalse(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setProdLoadStatus("a");
	String string0 = systemLoad0.getProdLoadStatus();
	assertEquals("a", string0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setPreloadJust("");
	String string0 = systemLoad0.getPreloadJust();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setPreloadDesc("P jbMOa3Wn%~_:9>u");
	String string0 = systemLoad0.getPreloadDesc();
	assertEquals("P jbMOa3Wn%~_:9>u", string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setPreloadDesc("");
	String string0 = systemLoad0.getPreloadDesc();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setPreload("org.apache.commons.io.filefilter.FileFileFilter");
	String string0 = systemLoad0.getPreload();
	assertEquals("org.apache.commons.io.filefilter.FileFileFilter", string0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setPreload("");
	String string0 = systemLoad0.getPreload();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	Date mockDate0 = new Date(338L);
	systemLoad0.setModifiedDt(mockDate0);
	Date date0 = systemLoad0.getModifiedDt();
	assertNotEquals("Thu Jan 01 00:00:00 GMT 1970", date0.toString());
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setModifiedBy("com.tsi.workflow.beans.dao.Platform[ id=");
	String string0 = systemLoad0.getModifiedBy();
	assertEquals("com.tsi.workflow.beans.dao.Platform[ id=", string0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setModifiedBy("");
	String string0 = systemLoad0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	Integer integer0 = new Integer((-1938));
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	systemLoad0.setLoadSetName("com.tsi.workflow.beans.dao.SystemLoad");
	String string0 = systemLoad0.getLoadSetName();
	assertEquals("com.tsi.workflow.beans.dao.SystemLoad", string0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	systemLoad0.setLoadSetName("");
	String string0 = systemLoad0.getLoadSetName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	Integer integer0 = new Integer((-1938));
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	systemLoad0.setLoadScrDesc("4294965358");
	String string0 = systemLoad0.getLoadScrDesc();
	assertEquals("4294965358", string0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	Integer integer0 = new Integer(524);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	systemLoad0.setLoadScrDesc("");
	String string0 = systemLoad0.getLoadScrDesc();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setLoadScr("com.tsi.workflow.beans.dao.SystemLoad[ id=");
	String string0 = systemLoad0.getLoadScr();
	assertEquals("com.tsi.workflow.beans.dao.SystemLoad[ id=", string0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setLoadScr("");
	String string0 = systemLoad0.getLoadScr();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	systemLoad0.setLoadInstruction("r");
	String string0 = systemLoad0.getLoadInstruction();
	assertEquals("r", string0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	Integer integer0 = new Integer(27);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	systemLoad0.setLoadInstruction("");
	String string0 = systemLoad0.getLoadInstruction();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	Date mockDate0 = new Date(0, 4062, (-1709), 0, 0, 1455);
	systemLoad0.setLoadDateTime(mockDate0);
	Date date0 = systemLoad0.getLoadDateTime();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	Integer integer0 = new Integer((-1019));
	LoadCategories loadCategories0 = new LoadCategories(integer0);
	systemLoad0.setLoadCategoryId(loadCategories0);
	LoadCategories loadCategories1 = systemLoad0.getLoadCategoryId();
	assertNull(loadCategories1.getName());
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setLoadAttendeeId("P jbMOa3Wn%~_:9>u");
	String string0 = systemLoad0.getLoadAttendeeId();
	assertEquals("P jbMOa3Wn%~_:9>u", string0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	systemLoad0.setLoadAttendeeId("");
	String string0 = systemLoad0.getLoadAttendeeId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setLoadAttendee("7-5%cEl");
	String string0 = systemLoad0.getLoadAttendee();
	assertEquals("7-5%cEl", string0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setLoadAttendee("");
	String string0 = systemLoad0.getLoadAttendee();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	Integer integer0 = new Integer((-224));
	systemLoad0.setId(integer0);
	Integer integer1 = systemLoad0.getId();
	assertEquals((-224), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	Integer integer1 = systemLoad0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test36() throws Throwable {
	Integer integer0 = new Integer(27);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	Integer integer1 = systemLoad0.getId();
	assertEquals(27, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test37() throws Throwable {
	Integer integer0 = new Integer(955);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	Integer integer1 = systemLoad0.getId();
	assertEquals(955, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test38() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setFlbkScrDesc("");
	String string0 = systemLoad0.getFlbkScrDesc();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test39() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setFlbkScr("");
	String string0 = systemLoad0.getFlbkScr();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test40() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setFallbackLoadSetName("XyPQ,^b(*v'^t(ZVkg");
	String string0 = systemLoad0.getFallbackLoadSetName();
	assertEquals("XyPQ,^b(*v'^t(ZVkg", string0);
    }

    @Test(timeout = 4000)
    public void test41() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setFallbackLoadSetName("");
	String string0 = systemLoad0.getFallbackLoadSetName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test44() throws Throwable {
	Integer integer0 = new Integer(3011);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	Date mockDate0 = new Date(3011, (-1533), (-905), (-905), 1);
	systemLoad0.setCreatedDt(mockDate0);
	Date date0 = systemLoad0.getCreatedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test45() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setCreatedBy("^`cAsMTJ ]6D1");
	String string0 = systemLoad0.getCreatedBy();
	assertEquals("^`cAsMTJ ]6D1", string0);
    }

    @Test(timeout = 4000)
    public void test46() throws Throwable {
	Integer integer0 = new Integer(2718);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	systemLoad0.setCreatedBy("");
	String string0 = systemLoad0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test47() throws Throwable {
	Integer integer0 = new Integer((-1938));
	SystemLoad systemLoad0 = new SystemLoad();
	LinkedList<CheckoutSegments> linkedList0 = new LinkedList<CheckoutSegments>();
	CheckoutSegments checkoutSegments0 = new CheckoutSegments(integer0);
	linkedList0.add(checkoutSegments0);
	systemLoad0.setCheckoutSegmentsList(linkedList0);
	List<CheckoutSegments> list0 = systemLoad0.getCheckoutSegmentsList();
	assertFalse(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test48() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad((Integer) null);
	systemLoad0.setActive("]y7UdSP99w')5U-");
	String string0 = systemLoad0.getActive();
	assertEquals("]y7UdSP99w')5U-", string0);
    }

    @Test(timeout = 4000)
    public void test49() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad((Integer) null);
	systemLoad0.setActive("");
	String string0 = systemLoad0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test50() throws Throwable {
	Integer integer0 = new Integer(3011);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	SystemLoad systemLoad1 = new SystemLoad();
	boolean boolean0 = systemLoad0.equals(systemLoad1);
	assertFalse(systemLoad1.equals((Object) systemLoad0));
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test51() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	Integer integer0 = new Integer((-3702));
	SystemLoad systemLoad1 = new SystemLoad(integer0);
	boolean boolean0 = systemLoad0.equals(systemLoad1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test52() throws Throwable {
	Integer integer0 = new Integer(3011);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	boolean boolean0 = systemLoad0.equals(systemLoad0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test53() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	boolean boolean0 = systemLoad0.equals("7-5%cEl");
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test54() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	boolean boolean0 = systemLoad0.equals(systemLoad0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test55() throws Throwable {
	Integer integer0 = new Integer((-526));
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	systemLoad0.hashCode();
    }

    @Test(timeout = 4000)
    public void test56() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad((Integer) null);
	systemLoad0.hashCode();
    }

    @Test(timeout = 4000)
    public void test57() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getQaRegressionBypassComment();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test58() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setPreloadJust("FOg3x1");
	String string0 = systemLoad0.getPreloadJust();
	assertEquals("FOg3x1", string0);
    }

    @Test(timeout = 4000)
    public void test59() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setQaFunctionalBypassComment("XyPQ,^b(*v'^t(ZVkg");
	String string0 = systemLoad0.getQaFunctionalBypassComment();
	assertEquals("XyPQ,^b(*v'^t(ZVkg", string0);
    }

    @Test(timeout = 4000)
    public void test60() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	System system0 = systemLoad0.getSystemId();
	assertNull(system0);
    }

    @Test(timeout = 4000)
    public void test61() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getFallbackLoadSetName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test62() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	List<SystemPdddsMapping> list0 = systemLoad0.getSystemPdddsMappingList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test63() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setProdLoadStatus("");
	String string0 = systemLoad0.getProdLoadStatus();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test64() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	List<SystemLoadActions> list0 = systemLoad0.getSystemLoadActionsList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test65() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getLoadInstruction();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test66() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getPreload();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test67() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getFlbkScrDesc();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test68() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test69() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getLoadSetName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test70() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	List<ProductionLoads> list0 = systemLoad0.getProductionLoadsList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test71() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getLoadAttendeeId();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test72() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getPreloadDesc();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test73() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	PutLevel putLevel0 = systemLoad0.getPutLevelId();
	assertNull(putLevel0);
    }

    @Test(timeout = 4000)
    public void test74() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	String string0 = systemLoad0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test75() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.toString();
	assertEquals("com.tsi.workflow.beans.dao.SystemLoad[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test76() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getLoadAttendee();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test77() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getProdLoadStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test78() throws Throwable {
	Integer integer0 = new Integer(0);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	LoadCategories loadCategories0 = systemLoad0.getLoadCategoryId();
	assertNull(loadCategories0);
    }

    @Test(timeout = 4000)
    public void test79() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getPreloadJust();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test80() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	Date date0 = systemLoad0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test81() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getQaFunctionalBypassComment();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test82() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test83() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getLoadScr();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test84() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	LinkedList<CheckoutSegments> linkedList0 = new LinkedList<CheckoutSegments>();
	systemLoad0.setCheckoutSegmentsList(linkedList0);
	List<CheckoutSegments> list0 = systemLoad0.getCheckoutSegmentsList();
	assertEquals(0, list0.size());
    }

    @Test(timeout = 4000)
    public void test85() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	List<CheckoutSegments> list0 = systemLoad0.getCheckoutSegmentsList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test86() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	Date date0 = systemLoad0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test87() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setFlbkScr("FOg3x1");
	String string0 = systemLoad0.getFlbkScr();
	assertEquals("FOg3x1", string0);
    }

    @Test(timeout = 4000)
    public void test88() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	Integer integer0 = systemLoad0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test90() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	ImpPlan impPlan0 = new ImpPlan();
	systemLoad0.setPlanId(impPlan0);
	ImpPlan impPlan1 = systemLoad0.getPlanId();
	assertNull(impPlan1.getLeadContact());
    }

    @Test(timeout = 4000)
    public void test91() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setQaRegressionBypassComment("XyPQ,^b(*v'^t(ZVkg");
	String string0 = systemLoad0.getQaRegressionBypassComment();
	assertEquals("XyPQ,^b(*v'^t(ZVkg", string0);
    }

    @Test(timeout = 4000)
    public void test92() throws Throwable {
	Integer integer0 = new Integer(3011);
	SystemLoad systemLoad0 = new SystemLoad(integer0);
	String string0 = systemLoad0.getLoadScrDesc();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test93() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	ImpPlan impPlan0 = systemLoad0.getPlanId();
	assertNull(impPlan0);
    }

    @Test(timeout = 4000)
    public void test94() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getQaBypassStatus();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test95() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	String string0 = systemLoad0.getFlbkScr();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test96() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	Date date0 = systemLoad0.getLoadDateTime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test97() throws Throwable {
	SystemLoad systemLoad0 = new SystemLoad();
	systemLoad0.setFlbkScrDesc("com.tsi.workflow.beans.dao.SystemLoad[ id=null ]");
	String string0 = systemLoad0.getFlbkScrDesc();
	assertEquals("com.tsi.workflow.beans.dao.SystemLoad[ id=null ]", string0);
    }
}
