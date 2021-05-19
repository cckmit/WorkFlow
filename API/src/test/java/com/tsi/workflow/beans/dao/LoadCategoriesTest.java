
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
public class LoadCategoriesTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	Integer integer0 = new Integer((-1253));
	LoadCategories loadCategories0 = new LoadCategories(integer0);
	LoadCategories loadCategories1 = new LoadCategories((Integer) null);
	// Undeclared exception!
	try {
	    loadCategories0.compareTo(loadCategories1);
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	}
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	Integer integer0 = new Integer((-2904));
	LoadCategories loadCategories0 = new LoadCategories(integer0);
	loadCategories0.hashCode();
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	LinkedList<SystemLoad> linkedList0 = new LinkedList<SystemLoad>();
	loadCategories0.setSystemLoadList(linkedList0);
	List<SystemLoad> list0 = loadCategories0.getSystemLoadList();
	assertEquals(0, list0.size());
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	System system0 = new System();
	loadCategories0.setSystemId(system0);
	System system1 = loadCategories0.getSystemId();
	assertNull(system1.getIpaddress());
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	loadCategories0.setName("com.tsi.workflow.beans.dao.LoadCategories[ id=");
	String string0 = loadCategories0.getName();
	assertEquals("com.tsi.workflow.beans.dao.LoadCategories[ id=", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	loadCategories0.setName("");
	String string0 = loadCategories0.getName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	Integer integer0 = new Integer((-2904));
	LoadCategories loadCategories0 = new LoadCategories(integer0);
	loadCategories0.setModifiedBy("com.tsi.workflow.beans.dao.LoadCategories[ id=-2904 ]");
	String string0 = loadCategories0.getModifiedBy();
	assertEquals("com.tsi.workflow.beans.dao.LoadCategories[ id=-2904 ]", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	LinkedList<LoadWindow> linkedList0 = new LinkedList<LoadWindow>();
	loadCategories0.setLoadWindowList(linkedList0);
	List<LoadWindow> list0 = loadCategories0.getLoadWindowList();
	assertEquals(0, list0.size());
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	LinkedList<LoadWindow> linkedList0 = new LinkedList<LoadWindow>();
	LoadWindow loadWindow0 = new LoadWindow((Integer) null);
	linkedList0.add(loadWindow0);
	loadCategories0.setLoadWindowList(linkedList0);
	List<LoadWindow> list0 = loadCategories0.getLoadWindowList();
	assertEquals(1, list0.size());
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	Integer integer0 = new Integer((-3817));
	LoadCategories loadCategories0 = new LoadCategories(integer0);
	LinkedList<LoadFreeze> linkedList0 = new LinkedList<LoadFreeze>();
	loadCategories0.setLoadFreezeList(linkedList0);
	List<LoadFreeze> list0 = loadCategories0.getLoadFreezeList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	Integer integer0 = Integer.getInteger("", (-1606));
	LoadCategories loadCategories0 = new LoadCategories(integer0);
	LinkedList<LoadFreeze> linkedList0 = new LinkedList<LoadFreeze>();
	linkedList0.add((LoadFreeze) null);
	loadCategories0.setLoadFreezeList(linkedList0);
	List<LoadFreeze> list0 = loadCategories0.getLoadFreezeList();
	assertEquals(1, list0.size());
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	Integer integer0 = new Integer(1);
	LoadCategories loadCategories0 = new LoadCategories(integer0);
	Integer integer1 = loadCategories0.getId();
	assertEquals(1, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	Integer integer0 = new Integer(0);
	loadCategories0.setId(integer0);
	Integer integer1 = loadCategories0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	Integer integer0 = new Integer((-1253));
	LoadCategories loadCategories0 = new LoadCategories(integer0);
	Integer integer1 = loadCategories0.getId();
	assertEquals((-1253), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	Integer integer0 = new Integer((-1041));
	loadCategories0.setId(integer0);
	Integer integer1 = loadCategories0.getId();
	assertEquals((-1041), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	Integer integer0 = new Integer((-2904));
	LoadCategories loadCategories0 = new LoadCategories(integer0);
	loadCategories0.setDescription("com.tsi.workflow.beans.dao.LoadCategories[ id=-2904 ]");
	String string0 = loadCategories0.getDescription();
	assertEquals("com.tsi.workflow.beans.dao.LoadCategories[ id=-2904 ]", string0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	Integer integer0 = new Integer(0);
	LoadCategories loadCategories0 = new LoadCategories(integer0);
	loadCategories0.setDescription("");
	String string0 = loadCategories0.getDescription();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	Date date0 = new Date();
	loadCategories0.setCreatedDt(date0);
	Date date1 = loadCategories0.getCreatedDt();
	assertNotEquals("Thu Jan 01 00:00:00 GMT 1970", date1.toString());
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	loadCategories0.setCreatedBy("");
	String string0 = loadCategories0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	loadCategories0.setActive("");
	String string0 = loadCategories0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	loadCategories0.setName("=2x~F");
	int int0 = loadCategories0.compareTo(loadCategories0);
	assertEquals(0, int0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	String string0 = loadCategories0.getName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	Integer integer0 = new Integer(0);
	loadCategories0.setId(integer0);
	LoadCategories loadCategories1 = new LoadCategories();
	boolean boolean0 = loadCategories0.equals(loadCategories1);
	assertFalse(loadCategories1.equals((Object) loadCategories0));
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	Integer integer0 = new Integer((-1511));
	LoadCategories loadCategories1 = new LoadCategories(integer0);
	boolean boolean0 = loadCategories0.equals(loadCategories1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	Integer integer0 = new Integer(0);
	loadCategories0.setId(integer0);
	boolean boolean0 = loadCategories0.equals(loadCategories0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	Integer integer0 = new Integer((-1511));
	boolean boolean0 = loadCategories0.equals(integer0);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	boolean boolean0 = loadCategories0.equals(loadCategories0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	loadCategories0.hashCode();
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	String string0 = loadCategories0.getDescription();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	String string0 = loadCategories0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	loadCategories0.setCreatedBy("com.tsi.workflow.beans.dao.LoadCategories[ id=null ]");
	String string0 = loadCategories0.getCreatedBy();
	assertEquals("com.tsi.workflow.beans.dao.LoadCategories[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	Date mockDate0 = new Date(0, (-25), 0);
	loadCategories0.setModifiedDt(mockDate0);
	Date date0 = loadCategories0.getModifiedDt();
	assertNotEquals("Tue Nov 30 00:00:00 GMT 1897", date0.toString());
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	List<SystemLoad> list0 = loadCategories0.getSystemLoadList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	Date date0 = loadCategories0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	String string0 = loadCategories0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	Date date0 = loadCategories0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test36() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	Integer integer0 = loadCategories0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test37() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	List<LoadFreeze> list0 = loadCategories0.getLoadFreezeList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test38() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	List<LoadWindow> list0 = loadCategories0.getLoadWindowList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test39() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	String string0 = loadCategories0.toString();
	assertEquals("com.tsi.workflow.beans.dao.LoadCategories[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test40() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories((Integer) null);
	loadCategories0.setActive("com.tsi.workflow.beans.dao.ImpPlanApprovals");
	String string0 = loadCategories0.getActive();
	assertEquals("com.tsi.workflow.beans.dao.ImpPlanApprovals", string0);
    }

    @Test(timeout = 4000)
    public void test41() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	System system0 = loadCategories0.getSystemId();
	assertNull(system0);
    }

    @Test(timeout = 4000)
    public void test42() throws Throwable {
	Integer integer0 = new Integer(0);
	LoadCategories loadCategories0 = new LoadCategories(integer0);
	String string0 = loadCategories0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test43() throws Throwable {
	LoadCategories loadCategories0 = new LoadCategories();
	loadCategories0.setModifiedBy("");
	String string0 = loadCategories0.getModifiedBy();
	assertEquals("", string0);
    }
}
