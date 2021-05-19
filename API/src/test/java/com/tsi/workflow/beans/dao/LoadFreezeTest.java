
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
public class LoadFreezeTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	Date mockDate0 = new Date();
	loadFreeze0.setToDate(mockDate0);
	Date date0 = loadFreeze0.getToDate();
	assertNotEquals("Fri Feb 14 20:21:21 GMT 2014", date0.toString());
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	loadFreeze0.setReason("");
	String string0 = loadFreeze0.getReason();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	Integer integer0 = new Integer(2);
	LoadFreeze loadFreeze0 = new LoadFreeze(integer0);
	Date mockDate0 = new Date(0, 2, 0, 0, 2, (-140));
	loadFreeze0.setModifiedDt(mockDate0);
	Date date0 = loadFreeze0.getModifiedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	loadFreeze0.setModifiedBy(">");
	String string0 = loadFreeze0.getModifiedBy();
	assertEquals(">", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	Integer integer0 = new Integer(0);
	LoadFreeze loadFreeze0 = new LoadFreeze(integer0);
	loadFreeze0.setModifiedBy("");
	String string0 = loadFreeze0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze((Integer) null);
	LoadCategories loadCategories0 = new LoadCategories();
	loadFreeze0.setLoadCategoryId(loadCategories0);
	LoadCategories loadCategories1 = loadFreeze0.getLoadCategoryId();
	assertNull(loadCategories1.getDescription());
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	Integer integer0 = new Integer(0);
	LoadFreeze loadFreeze0 = new LoadFreeze(integer0);
	Integer integer1 = loadFreeze0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	Integer integer0 = new Integer(1);
	LoadFreeze loadFreeze0 = new LoadFreeze(integer0);
	Integer integer1 = loadFreeze0.getId();
	assertEquals(1, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	Integer integer0 = new Integer((-2070));
	loadFreeze0.setId(integer0);
	Integer integer1 = loadFreeze0.getId();
	assertEquals((-2070), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	Integer integer0 = new Integer(2);
	LoadFreeze loadFreeze0 = new LoadFreeze(integer0);
	Date mockDate0 = new Date(0, 2, 0, 1, 2, (-140));
	loadFreeze0.setFromDate(mockDate0);
	Date date0 = loadFreeze0.getFromDate();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	Date mockDate0 = new Date(5944, 5944, 5944, 5944, 5);
	loadFreeze0.setCreatedDt(mockDate0);
	Date date0 = loadFreeze0.getCreatedDt();
	assertNotEquals("Wed Apr 11 16:05:00 GMT 8356", date0.toString());
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	loadFreeze0.setCreatedBy("bJf?{GWPgi_Es1_~");
	String string0 = loadFreeze0.getCreatedBy();
	assertEquals("bJf?{GWPgi_Es1_~", string0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	Integer integer0 = new Integer(0);
	LoadFreeze loadFreeze0 = new LoadFreeze(integer0);
	loadFreeze0.setCreatedBy("");
	String string0 = loadFreeze0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	Integer integer0 = new Integer(2);
	LoadFreeze loadFreeze0 = new LoadFreeze(integer0);
	loadFreeze0.setActive("6)V8~:EP");
	String string0 = loadFreeze0.getActive();
	assertEquals("6)V8~:EP", string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	loadFreeze0.setActive("");
	String string0 = loadFreeze0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	Integer integer0 = new Integer(2);
	LoadFreeze loadFreeze0 = new LoadFreeze(integer0);
	LoadFreeze loadFreeze1 = new LoadFreeze();
	boolean boolean0 = loadFreeze0.equals(loadFreeze1);
	assertFalse(boolean0);
	assertFalse(loadFreeze1.equals((Object) loadFreeze0));
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	Integer integer0 = Integer.valueOf((-1173));
	LoadFreeze loadFreeze1 = new LoadFreeze(integer0);
	boolean boolean0 = loadFreeze0.equals(loadFreeze1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	Integer integer0 = new Integer(1);
	loadFreeze0.setId(integer0);
	boolean boolean0 = loadFreeze0.equals(loadFreeze0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	Date date0 = new Date();
	boolean boolean0 = loadFreeze0.equals(date0);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	boolean boolean0 = loadFreeze0.equals(loadFreeze0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	Integer integer0 = new Integer(1336);
	LoadFreeze loadFreeze0 = new LoadFreeze(integer0);
	loadFreeze0.hashCode();
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	loadFreeze0.hashCode();
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	String string0 = loadFreeze0.toString();
	assertEquals("com.tsi.workflow.beans.dao.LoadFreeze[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	LoadCategories loadCategories0 = loadFreeze0.getLoadCategoryId();
	assertNull(loadCategories0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	Date date0 = loadFreeze0.getFromDate();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	String string0 = loadFreeze0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	String string0 = loadFreeze0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	Integer integer0 = loadFreeze0.getId();
	loadFreeze0.setId(integer0);
	assertNull(loadFreeze0.getReason());
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	loadFreeze0.setReason("J=EBos0w2AFp>l9111");
	String string0 = loadFreeze0.getReason();
	assertEquals("J=EBos0w2AFp>l9111", string0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	Date date0 = loadFreeze0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	Date date0 = loadFreeze0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	String string0 = loadFreeze0.getReason();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	Integer integer0 = new Integer(2);
	LoadFreeze loadFreeze0 = new LoadFreeze(integer0);
	String string0 = loadFreeze0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	LoadFreeze loadFreeze0 = new LoadFreeze();
	Date date0 = loadFreeze0.getToDate();
	assertNull(date0);
    }
}
