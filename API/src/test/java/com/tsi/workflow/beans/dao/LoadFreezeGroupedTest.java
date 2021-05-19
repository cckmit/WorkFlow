
package com.tsi.workflow.beans.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class LoadFreezeGroupedTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	LinkedList<String> linkedList0 = new LinkedList<String>();
	linkedList0.add("^-cy&^0Me#wOT%e0b");
	loadFreezeGrouped0.setListIds(linkedList0);
	loadFreezeGrouped0.hashCode();
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	loadFreezeGrouped0.setIds("INSENSITIV");
	List<String> list0 = loadFreezeGrouped0.getListIds();
	assertTrue(list0.contains("INSENSITIV"));
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	Date mockDate0 = new Date(1, 1, 1);
	loadFreezeGrouped0.setTo_date(mockDate0);
	Date date0 = loadFreezeGrouped0.getTo_date();
	assertNotEquals("Fri Feb 01 00:00:00 GMT 1901", date0.toString());
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	loadFreezeGrouped0.setReason("zJ ]D9?z,");
	String string0 = loadFreezeGrouped0.getReason();
	assertEquals("zJ ]D9?z,", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	loadFreezeGrouped0.setReason("");
	String string0 = loadFreezeGrouped0.getReason();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	loadFreezeGrouped0.setName("R/9cz");
	String string0 = loadFreezeGrouped0.getName();
	assertEquals("R/9cz", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	loadFreezeGrouped0.setName("");
	String string0 = loadFreezeGrouped0.getName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	loadFreezeGrouped0.setLoad_categories("8sVZ)]r9Fl;X8");
	String string0 = loadFreezeGrouped0.getLoad_categories();
	assertEquals("8sVZ)]r9Fl;X8", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	loadFreezeGrouped0.setLoad_categories("");
	String string0 = loadFreezeGrouped0.getLoad_categories();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	LinkedList<String> linkedList0 = new LinkedList<String>();
	linkedList0.add("^-cy&^0Me#wOT%e0b");
	loadFreezeGrouped0.setListIds(linkedList0);
	String string0 = loadFreezeGrouped0.getIds();
	assertEquals("^-cy&^0Me#wOT%e0b", string0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	loadFreezeGrouped0.setIds("");
	String string0 = loadFreezeGrouped0.getIds();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	Date mockDate0 = new Date((-35), (-35), 3247, 1, 1651, 3247);
	loadFreezeGrouped0.setFrom_date(mockDate0);
	Date date0 = loadFreezeGrouped0.getFrom_date();
	assertNotEquals("Fri Dec 23 05:25:07 GMT 1870", date0.toString());
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	// Undeclared exception!
	try {
	    loadFreezeGrouped0.setListIds((List<String>) null);
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //
	}
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	loadFreezeGrouped0.hashCode();
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	LoadFreezeGrouped loadFreezeGrouped1 = new LoadFreezeGrouped();
	loadFreezeGrouped0.setIds("PF/`lr[rdR");
	boolean boolean0 = loadFreezeGrouped0.equals(loadFreezeGrouped1);
	assertFalse(loadFreezeGrouped1.equals((Object) loadFreezeGrouped0));
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	LoadFreezeGrouped loadFreezeGrouped1 = new LoadFreezeGrouped();
	loadFreezeGrouped1.setIds("uT.6A0$f-LWPS[pvZ00");
	boolean boolean0 = loadFreezeGrouped0.equals(loadFreezeGrouped1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	loadFreezeGrouped0.setIds("INSENSITIV");
	boolean boolean0 = loadFreezeGrouped0.equals(loadFreezeGrouped0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	LinkedList<String> linkedList0 = new LinkedList<String>();
	boolean boolean0 = loadFreezeGrouped0.equals(linkedList0);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	boolean boolean0 = loadFreezeGrouped0.equals(loadFreezeGrouped0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	Date date0 = loadFreezeGrouped0.getTo_date();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	String string0 = loadFreezeGrouped0.getLoad_categories();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	String string0 = loadFreezeGrouped0.getIds();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	// Undeclared exception!
	try {
	    loadFreezeGrouped0.getListIds();
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //
	}
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	String string0 = loadFreezeGrouped0.getReason();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	Date date0 = loadFreezeGrouped0.getFrom_date();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	LoadFreezeGrouped loadFreezeGrouped0 = new LoadFreezeGrouped();
	String string0 = loadFreezeGrouped0.getName();
	assertNull(string0);
    }
}
