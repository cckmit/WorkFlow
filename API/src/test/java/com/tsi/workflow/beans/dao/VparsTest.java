
package com.tsi.workflow.beans.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
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
public class VparsTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	Vpars vpars0 = new Vpars();
	vpars0.setType("`:[hA86]g6");
	String string0 = vpars0.getType();
	assertEquals("`:[hA86]g6", string0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	Integer integer0 = new Integer(2377);
	Vpars vpars0 = new Vpars(integer0);
	vpars0.setType("");
	String string0 = vpars0.getType();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	Vpars vpars0 = new Vpars();
	LinkedList<SystemLoadActions> linkedList0 = new LinkedList<SystemLoadActions>();
	vpars0.setSystemLoadActionsList(linkedList0);
	List<SystemLoadActions> list0 = vpars0.getSystemLoadActionsList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	Vpars vpars0 = new Vpars();
	LinkedList<SystemLoadActions> linkedList0 = new LinkedList<SystemLoadActions>();
	Integer integer0 = Integer.getInteger("yBs9TM{jbX", 1);
	SystemLoadActions systemLoadActions0 = new SystemLoadActions(integer0);
	linkedList0.add(systemLoadActions0);
	vpars0.setSystemLoadActionsList(linkedList0);
	List<SystemLoadActions> list0 = vpars0.getSystemLoadActionsList();
	assertEquals(1, list0.size());
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	Integer integer0 = new Integer(2584);
	Vpars vpars0 = new Vpars(integer0);
	vpars0.setPort("f%a-L~Y");
	String string0 = vpars0.getPort();
	assertEquals("f%a-L~Y", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	Vpars vpars0 = new Vpars();
	vpars0.setPort("");
	String string0 = vpars0.getPort();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	Integer integer0 = new Integer(2584);
	Vpars vpars0 = new Vpars(integer0);
	vpars0.setOwnerId("G,=lm;qi]/r.6");
	String string0 = vpars0.getOwnerId();
	assertEquals("G,=lm;qi]/r.6", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	Vpars vpars0 = new Vpars();
	vpars0.setOwnerId("");
	String string0 = vpars0.getOwnerId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	Integer integer0 = new Integer((-1244));
	Vpars vpars0 = new Vpars(integer0);
	vpars0.setName("wa\"g8\"/itxK*DW`F1");
	String string0 = vpars0.getName();
	assertEquals("wa\"g8\"/itxK*DW`F1", string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	Integer integer0 = new Integer(0);
	Vpars vpars0 = new Vpars(integer0);
	vpars0.setName("");
	String string0 = vpars0.getName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	Vpars vpars0 = new Vpars();
	Date mockDate0 = new Date(2981, 2981, 2389);
	vpars0.setModifiedDt(mockDate0);
	Date date0 = vpars0.getModifiedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	Integer integer0 = new Integer(2584);
	Vpars vpars0 = new Vpars(integer0);
	vpars0.setModifiedBy("i'>o5JU1@SR\"WE4)`B");
	String string0 = vpars0.getModifiedBy();
	assertEquals("i'>o5JU1@SR\"WE4)`B", string0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	Vpars vpars0 = new Vpars();
	vpars0.setModifiedBy("");
	String string0 = vpars0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	Integer integer0 = new Integer((-1244));
	Vpars vpars0 = new Vpars(integer0);
	vpars0.setIpaddress("'Qvvd");
	String string0 = vpars0.getIpaddress();
	assertEquals("'Qvvd", string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	Vpars vpars0 = new Vpars();
	vpars0.setIpaddress("");
	String string0 = vpars0.getIpaddress();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	Vpars vpars0 = new Vpars();
	Integer integer0 = vpars0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	Integer integer0 = new Integer(2584);
	Vpars vpars0 = new Vpars(integer0);
	Integer integer1 = vpars0.getId();
	assertEquals(2584, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	Vpars vpars0 = new Vpars();
	Integer integer0 = new Integer(0);
	vpars0.setId(integer0);
	Integer integer1 = vpars0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	Integer integer0 = new Integer((-1));
	Vpars vpars0 = new Vpars(integer0);
	Integer integer1 = vpars0.getId();
	assertEquals((-1), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	Vpars vpars0 = new Vpars();
	Date mockDate0 = new Date();
	vpars0.setCreatedDt(mockDate0);
	Date date0 = vpars0.getCreatedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	Vpars vpars0 = new Vpars();
	vpars0.setCreatedBy("");
	String string0 = vpars0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	Vpars vpars0 = new Vpars();
	vpars0.setActive("mvf6VqD@5Ro8LTd/}|");
	String string0 = vpars0.getActive();
	assertNotNull(string0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	Vpars vpars0 = new Vpars();
	vpars0.setActive("");
	String string0 = vpars0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	Vpars vpars0 = new Vpars();
	vpars0.setName("_)S");
	Vpars vpars1 = new Vpars((Integer) null);
	vpars1.setName("");
	int int0 = vpars0.compareTo(vpars1);
	assertEquals(3, int0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	Vpars vpars0 = new Vpars();
	vpars0.setName("_)S");
	Vpars vpars1 = new Vpars((Integer) null);
	vpars1.setName("");
	int int0 = vpars1.compareTo(vpars0);
	assertEquals((-3), int0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	Integer integer0 = new Integer(2377);
	Vpars vpars0 = new Vpars(integer0);
	String string0 = vpars0.getName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	Integer integer0 = new Integer((-1244));
	Vpars vpars0 = new Vpars(integer0);
	Vpars vpars1 = new Vpars();
	boolean boolean0 = vpars0.equals(vpars1);
	assertFalse(vpars1.equals((Object) vpars0));
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	Vpars vpars0 = new Vpars();
	boolean boolean0 = vpars0.equals(vpars0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	Integer integer0 = new Integer((-1244));
	Vpars vpars0 = new Vpars(integer0);
	Vpars vpars1 = new Vpars();
	boolean boolean0 = vpars1.equals(vpars0);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	Vpars vpars0 = new Vpars();
	Object object0 = new Object();
	boolean boolean0 = vpars0.equals(object0);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	Integer integer0 = new Integer((-1244));
	Vpars vpars0 = new Vpars(integer0);
	boolean boolean0 = vpars0.equals(vpars0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	Integer integer0 = new Integer(2377);
	Vpars vpars0 = new Vpars(integer0);
	vpars0.hashCode();
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	Vpars vpars0 = new Vpars();
	vpars0.hashCode();
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	Vpars vpars0 = new Vpars();
	Date date0 = vpars0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	Vpars vpars0 = new Vpars();
	String string0 = vpars0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	Vpars vpars0 = new Vpars();
	String string0 = vpars0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test36() throws Throwable {
	Integer integer0 = new Integer((-1244));
	Vpars vpars0 = new Vpars(integer0);
	String string0 = vpars0.getIpaddress();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test37() throws Throwable {
	Vpars vpars0 = new Vpars();
	System system0 = vpars0.getSystemId();
	assertNull(system0);
    }

    @Test(timeout = 4000)
    public void test38() throws Throwable {
	Vpars vpars0 = new Vpars();
	String string0 = vpars0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test39() throws Throwable {
	Vpars vpars0 = new Vpars();
	Date date0 = vpars0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test40() throws Throwable {
	Vpars vpars0 = new Vpars();
	String string0 = vpars0.getType();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test41() throws Throwable {
	Vpars vpars0 = new Vpars((Integer) null);
	List<SystemLoadActions> list0 = vpars0.getSystemLoadActionsList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test42() throws Throwable {
	Vpars vpars0 = new Vpars();
	vpars0.setCreatedBy("*");
	String string0 = vpars0.getCreatedBy();
	assertEquals("*", string0);
    }

    @Test(timeout = 4000)
    public void test43() throws Throwable {
	Integer integer0 = new Integer((-1244));
	Vpars vpars0 = new Vpars(integer0);
	System system0 = new System();
	vpars0.setSystemId(system0);
	System system1 = vpars0.getSystemId();
	assertNull(system1.getPortno());
    }

    @Test(timeout = 4000)
    public void test44() throws Throwable {
	Integer integer0 = new Integer((-1244));
	Vpars vpars0 = new Vpars(integer0);
	vpars0.setName("wa\"g8\"/itxK*DW`F1");
	int int0 = vpars0.compareTo(vpars0);
	assertEquals(0, int0);
    }

    @Test(timeout = 4000)
    public void test45() throws Throwable {
	Vpars vpars0 = new Vpars();
	String string0 = vpars0.getOwnerId();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test46() throws Throwable {
	Vpars vpars0 = new Vpars((Integer) null);
	// Undeclared exception!
	try {
	    vpars0.compareTo(vpars0);
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	}
    }

    @Test(timeout = 4000)
    public void test47() throws Throwable {
	Vpars vpars0 = new Vpars();
	String string0 = vpars0.toString();
	assertEquals("com.tsi.workflow.beans.dao.Vpars[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test48() throws Throwable {
	Vpars vpars0 = new Vpars();
	String string0 = vpars0.getPort();
	assertNull(string0);
    }
}
