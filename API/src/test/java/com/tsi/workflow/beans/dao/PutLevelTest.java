
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
public class PutLevelTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	Integer integer0 = new Integer(5);
	PutLevel putLevel0 = new PutLevel(integer0);
	putLevel0.hashCode();
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	LinkedList<SystemLoad> linkedList0 = new LinkedList<SystemLoad>();
	putLevel0.setSystemLoadList(linkedList0);
	List<SystemLoad> list0 = putLevel0.getSystemLoadList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	LinkedList<SystemLoad> linkedList0 = new LinkedList<SystemLoad>();
	SystemLoad systemLoad0 = new SystemLoad();
	linkedList0.add(systemLoad0);
	putLevel0.setSystemLoadList(linkedList0);
	List<SystemLoad> list0 = putLevel0.getSystemLoadList();
	assertEquals(1, list0.size());
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	Integer integer0 = new Integer(1872);
	PutLevel putLevel0 = new PutLevel(integer0);
	System system0 = new System();
	putLevel0.setSystemId(system0);
	System system1 = putLevel0.getSystemId();
	assertNull(system1.getIpaddress());
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	putLevel0.setScmUrl("qH7+Zd(:");
	String string0 = putLevel0.getScmUrl();
	assertEquals("qH7+Zd(:", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	putLevel0.setScmUrl("");
	String string0 = putLevel0.getScmUrl();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	putLevel0.setPutLevel("I z 0UYO&IW|");
	String string0 = putLevel0.getPutLevel();
	assertEquals("I z 0UYO&IW|", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	putLevel0.setPutLevel("");
	String string0 = putLevel0.getPutLevel();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	Date mockDate0 = new Date(0, 0, 0);
	putLevel0.setPutDateTime(mockDate0);
	Date date0 = putLevel0.getPutDateTime();
	assertNotEquals("Sun Dec 31 00:00:00 GMT 1899", date0.toString());
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	Date mockDate0 = new Date(0, 0, 0);
	putLevel0.setModifiedDt(mockDate0);
	Date date0 = putLevel0.getModifiedDt();
	assertNotEquals("Sun Dec 31 00:00:00 GMT 1899", date0.toString());
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	putLevel0.setModifiedBy(" ]");
	String string0 = putLevel0.getModifiedBy();
	assertEquals(" ]", string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	putLevel0.setModifiedBy("");
	String string0 = putLevel0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	Integer integer0 = new Integer(0);
	PutLevel putLevel0 = new PutLevel(integer0);
	Integer integer1 = putLevel0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	Integer integer0 = new Integer(1872);
	PutLevel putLevel0 = new PutLevel(integer0);
	Integer integer1 = putLevel0.getId();
	assertEquals(1872, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	Integer integer0 = Integer.getInteger("", (-640));
	PutLevel putLevel0 = new PutLevel(integer0);
	Integer integer1 = putLevel0.getId();
	assertEquals((-640), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	Date mockDate0 = new Date(0, 0, 0);
	putLevel0.setCreatedDt(mockDate0);
	Date date0 = putLevel0.getCreatedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	putLevel0.setCreatedBy("&KL-eqMKO-$8A2'jS3");
	String string0 = putLevel0.getCreatedBy();
	assertEquals("&KL-eqMKO-$8A2'jS3", string0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	putLevel0.setActive("sjJxHeWvUh7vMw");
	String string0 = putLevel0.getActive();
	assertEquals("sjJxHeWvUh7vMw", string0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	putLevel0.setActive("");
	String string0 = putLevel0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	Integer integer0 = new Integer((-2846));
	putLevel0.setId(integer0);
	PutLevel putLevel1 = new PutLevel();
	boolean boolean0 = putLevel0.equals(putLevel1);
	assertFalse(putLevel1.equals((Object) putLevel0));
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	Integer integer0 = new Integer((-2419));
	PutLevel putLevel1 = new PutLevel(integer0);
	boolean boolean0 = putLevel0.equals(putLevel1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	PutLevel putLevel0 = new PutLevel((Integer) null);
	boolean boolean0 = putLevel0.equals("dd~l3XJ4rM}Od5I[r");
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	boolean boolean0 = putLevel0.equals(putLevel0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	putLevel0.hashCode();
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	PutLevel putLevel0 = new PutLevel((Integer) null);
	String string0 = putLevel0.toString();
	assertEquals("com.tsi.workflow.beans.dao.PutLevel[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	String string0 = putLevel0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	String string0 = putLevel0.getScmUrl();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	putLevel0.setCreatedBy("");
	String string0 = putLevel0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	PutLevel putLevel0 = new PutLevel((Integer) null);
	String string0 = putLevel0.getPutLevel();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	String string0 = putLevel0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	Integer integer0 = new Integer((-2811));
	putLevel0.setId(integer0);
	boolean boolean0 = putLevel0.equals(putLevel0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	PutLevel putLevel0 = new PutLevel((Integer) null);
	List<SystemLoad> list0 = putLevel0.getSystemLoadList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	System system0 = putLevel0.getSystemId();
	assertNull(system0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	Date date0 = putLevel0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test34() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	Integer integer0 = putLevel0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test35() throws Throwable {
	PutLevel putLevel0 = new PutLevel((Integer) null);
	Date date0 = putLevel0.getPutDateTime();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test36() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	Date date0 = putLevel0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test37() throws Throwable {
	PutLevel putLevel0 = new PutLevel();
	String string0 = putLevel0.getModifiedBy();
	assertNull(string0);
    }
}
