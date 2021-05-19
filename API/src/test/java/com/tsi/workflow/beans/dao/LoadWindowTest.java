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
public class LoadWindowTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	Date mockDate0 = new Date(0, 0, 0, 174, 692, 0);
	loadWindow0.setTimeSlot("");
	String date0 = loadWindow0.getTimeSlot();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	Date mockDate0 = new Date();
	loadWindow0.setModifiedDt(mockDate0);
	Date date0 = loadWindow0.getModifiedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	loadWindow0.setModifiedBy(".kATi%+!W~sN");
	String string0 = loadWindow0.getModifiedBy();
	assertEquals(".kATi%+!W~sN", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	loadWindow0.setModifiedBy("");
	String string0 = loadWindow0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	Integer integer0 = Integer.valueOf((-1630));
	LoadWindow loadWindow0 = new LoadWindow(integer0);
	LoadCategories loadCategories0 = new LoadCategories();
	loadWindow0.setLoadCategoryId(loadCategories0);
	LoadCategories loadCategories1 = loadWindow0.getLoadCategoryId();
	assertNull(loadCategories1.getDescription());
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	Integer integer0 = new Integer(0);
	loadWindow0.setId(integer0);
	Integer integer1 = loadWindow0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	Integer integer0 = new Integer(878);
	loadWindow0.setId(integer0);
	Integer integer1 = loadWindow0.getId();
	assertEquals(878, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	Integer integer0 = new Integer((-3595));
	LoadWindow loadWindow0 = new LoadWindow(integer0);
	Integer integer1 = loadWindow0.getId();
	assertEquals((-3595), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	loadWindow0.setDaysOfWeek("");
	String string0 = loadWindow0.getDaysOfWeek();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	Date mockDate0 = new Date();
	loadWindow0.setCreatedDt(mockDate0);
	Date date0 = loadWindow0.getCreatedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	loadWindow0.setCreatedBy("com.tsi.workflow.beans.dao.LoadWindow");
	String string0 = loadWindow0.getCreatedBy();
	assertEquals("com.tsi.workflow.beans.dao.LoadWindow", string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	loadWindow0.setCreatedBy("");
	String string0 = loadWindow0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	loadWindow0.setActive("The filters must not be null");
	String string0 = loadWindow0.getActive();
	assertEquals("The filters must not be null", string0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	Integer integer0 = new Integer(0);
	LoadWindow loadWindow0 = new LoadWindow();
	loadWindow0.setId(integer0);
	LoadWindow loadWindow1 = new LoadWindow();
	boolean boolean0 = loadWindow0.equals(loadWindow1);
	assertFalse(boolean0);
	assertFalse(loadWindow1.equals((Object) loadWindow0));
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	Integer integer0 = new Integer((-3350));
	LoadWindow loadWindow1 = new LoadWindow(integer0);
	boolean boolean0 = loadWindow0.equals(loadWindow1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	boolean boolean0 = loadWindow0.equals("");
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	boolean boolean0 = loadWindow0.equals(loadWindow0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	Integer integer0 = new Integer(5560);
	loadWindow0.setId(integer0);
	loadWindow0.hashCode();
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	loadWindow0.hashCode();
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	Integer integer0 = new Integer(0);
	LoadWindow loadWindow0 = new LoadWindow();
	loadWindow0.setId(integer0);
	boolean boolean0 = loadWindow0.equals(loadWindow0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	Integer integer0 = loadWindow0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	loadWindow0.setActive("");
	String string0 = loadWindow0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	String string0 = loadWindow0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	Date date0 = loadWindow0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	loadWindow0.setDaysOfWeek("The list of names must not be null");
	String string0 = loadWindow0.getDaysOfWeek();
	assertEquals("The list of names must not be null", string0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	String string0 = loadWindow0.getDaysOfWeek();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	LoadCategories loadCategories0 = loadWindow0.getLoadCategoryId();
	assertNull(loadCategories0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	Date date0 = loadWindow0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	String string0 = loadWindow0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	String date0 = loadWindow0.getTimeSlot();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	String string0 = loadWindow0.toString();
	assertEquals("com.tsi.workflow.beans.dao.LoadWindow[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	LoadWindow loadWindow0 = new LoadWindow();
	String string0 = loadWindow0.getCreatedBy();
	assertNull(string0);
    }
}
