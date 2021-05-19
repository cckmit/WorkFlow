
package com.tsi.workflow.beans.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class PdddsLibraryTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	pdddsLibrary0.setType(" ]");
	String string0 = pdddsLibrary0.getType();
	assertEquals(" ]", string0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	pdddsLibrary0.setType("");
	String string0 = pdddsLibrary0.getType();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	LinkedList<SystemPdddsMapping> linkedList0 = new LinkedList<SystemPdddsMapping>();
	SystemPdddsMapping systemPdddsMapping0 = new SystemPdddsMapping();
	linkedList0.add(systemPdddsMapping0);
	pdddsLibrary0.setSystemPdddsMappingList(linkedList0);
	List<SystemPdddsMapping> list0 = pdddsLibrary0.getSystemPdddsMappingList();
	assertFalse(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	Integer integer0 = new Integer(0);
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary(integer0);
	System system0 = new System(integer0);
	pdddsLibrary0.setSystemId(system0);
	System system1 = pdddsLibrary0.getSystemId();
	assertNull(system1.getActive());
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	pdddsLibrary0.setName("com.tsi.workflow.beans.dao.PdddsLibrary[ id=null ]");
	String string0 = pdddsLibrary0.getName();
	assertEquals("com.tsi.workflow.beans.dao.PdddsLibrary[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary((Integer) null);
	pdddsLibrary0.setName("");
	String string0 = pdddsLibrary0.getName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	Date mockDate0 = new Date((-598), 0, (-3204), 0, (-3204), 4);
	pdddsLibrary0.setModifiedDt(mockDate0);
	Date date0 = pdddsLibrary0.getModifiedDt();
	assertNotEquals("Sat Mar 21 18:36:04 GMT 1293", date0.toString());
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	Integer integer0 = new Integer((-2649));
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary(integer0);
	pdddsLibrary0.setModifiedBy(" ]");
	String string0 = pdddsLibrary0.getModifiedBy();
	assertEquals(" ]", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	pdddsLibrary0.setModifiedBy("");
	String string0 = pdddsLibrary0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	Integer integer0 = new Integer(0);
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary(integer0);
	Integer integer1 = pdddsLibrary0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	Integer integer0 = new Integer(1);
	pdddsLibrary0.setId(integer0);
	Integer integer1 = pdddsLibrary0.getId();
	assertEquals(1, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	Integer integer0 = new Integer((-25));
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary(integer0);
	Integer integer1 = pdddsLibrary0.getId();
	assertEquals((-25), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	Integer integer0 = new Integer(2019);
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary(integer0);
	Date date0 = new Date(990, 990, 4);
	pdddsLibrary0.setCreatedDt(date0);
	Date date1 = pdddsLibrary0.getCreatedDt();
	assertNotEquals("Thu Jan 01 00:00:04 GMT 1970", date1.toString());
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	pdddsLibrary0.setCreatedBy("com.tsi.workflow.beans.dao.PdddsLibrary[ id=null ]");
	String string0 = pdddsLibrary0.getCreatedBy();
	assertEquals("com.tsi.workflow.beans.dao.PdddsLibrary[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	pdddsLibrary0.setCreatedBy("");
	String string0 = pdddsLibrary0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary((Integer) null);
	pdddsLibrary0.setActive("20F:,");
	String string0 = pdddsLibrary0.getActive();
	assertEquals("20F:,", string0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	pdddsLibrary0.setActive("");
	String string0 = pdddsLibrary0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	Integer integer0 = new Integer(379);
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary(integer0);
	PdddsLibrary pdddsLibrary1 = new PdddsLibrary();
	boolean boolean0 = pdddsLibrary0.equals(pdddsLibrary1);
	assertFalse(pdddsLibrary1.equals((Object) pdddsLibrary0));
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	PdddsLibrary pdddsLibrary1 = new PdddsLibrary();
	Integer integer0 = new Integer(1);
	pdddsLibrary1.setId(integer0);
	boolean boolean0 = pdddsLibrary0.equals(pdddsLibrary1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	boolean boolean0 = pdddsLibrary0.equals(pdddsLibrary0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	boolean boolean0 = pdddsLibrary0.equals((Object) null);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	Integer integer0 = new Integer(2019);
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary(integer0);
	boolean boolean0 = pdddsLibrary0.equals(pdddsLibrary0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	pdddsLibrary0.hashCode();
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	List<SystemPdddsMapping> list0 = pdddsLibrary0.getSystemPdddsMappingList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	String string0 = pdddsLibrary0.toString();
	assertEquals("com.tsi.workflow.beans.dao.PdddsLibrary[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	String string0 = pdddsLibrary0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	Integer integer0 = pdddsLibrary0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	String string0 = pdddsLibrary0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	String string0 = pdddsLibrary0.getType();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	System system0 = pdddsLibrary0.getSystemId();
	assertNull(system0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	Integer integer0 = new Integer((-3));
	pdddsLibrary0.setId(integer0);
	pdddsLibrary0.hashCode();
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	Date date0 = pdddsLibrary0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	LinkedList<SystemPdddsMapping> linkedList0 = new LinkedList<SystemPdddsMapping>();
	pdddsLibrary0.setSystemPdddsMappingList(linkedList0);
	List<SystemPdddsMapping> list0 = pdddsLibrary0.getSystemPdddsMappingList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	Date date0 = pdddsLibrary0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	String string0 = pdddsLibrary0.getName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	PdddsLibrary pdddsLibrary0 = new PdddsLibrary();
	String string0 = pdddsLibrary0.getCreatedBy();
	assertNull(string0);
    }
}
