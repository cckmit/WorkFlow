
package com.tsi.workflow.beans.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
public class PlatformTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	Integer integer0 = new Integer(2);
	Platform platform0 = new Platform(integer0);
	LinkedList<System> linkedList0 = new LinkedList<System>();
	System system0 = new System();
	linkedList0.offerLast(system0);
	platform0.setSystemList(linkedList0);
	List<System> list0 = platform0.getSystemList();
	assertTrue(list0.contains(system0));
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	Platform platform0 = new Platform();
	platform0.setNickName("E@!hF[6");
	String string0 = platform0.getNickName();
	assertEquals("E@!hF[6", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	Platform platform0 = new Platform((Integer) null);
	platform0.setNickName("");
	String string0 = platform0.getNickName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	Integer integer0 = new Integer(5);
	Platform platform0 = new Platform(integer0);
	platform0.setName("}k.~@].Y&");
	String string0 = platform0.getName();
	assertEquals("}k.~@].Y&", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	Platform platform0 = new Platform();
	platform0.setName("");
	String string0 = platform0.getName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	Platform platform0 = new Platform();
	Date mockDate0 = new Date();
	platform0.setModifiedDt(mockDate0);
	Date date0 = platform0.getModifiedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	Platform platform0 = new Platform((Integer) null);
	platform0.setModifiedBy(" ]");
	String string0 = platform0.getModifiedBy();
	assertEquals(" ]", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	Integer integer0 = new Integer(893);
	Platform platform0 = new Platform(integer0);
	platform0.setModifiedBy("");
	String string0 = platform0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	Integer integer0 = new Integer(2);
	Platform platform0 = new Platform(integer0);
	Integer integer1 = platform0.getId();
	assertEquals(2, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	Integer integer0 = new Integer(893);
	Platform platform0 = new Platform(integer0);
	Integer integer1 = new Integer((-859));
	platform0.setId(integer1);
	Integer integer2 = platform0.getId();
	assertFalse(integer2.equals((Object) integer0));
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	Platform platform0 = new Platform();
	Date mockDate0 = new Date((-1043), (-1032), 4687);
	platform0.setCreatedDt(mockDate0);
	Date date0 = platform0.getCreatedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	Platform platform0 = new Platform();
	platform0.setCreatedBy("");
	String string0 = platform0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	Integer integer0 = new Integer(893);
	Platform platform0 = new Platform(integer0);
	platform0.setActive("LeJPg&gbj<s(kD");
	String string0 = platform0.getActive();
	assertEquals("LeJPg&gbj<s(kD", string0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	Integer integer0 = new Integer(0);
	Platform platform0 = new Platform(integer0);
	Platform platform1 = new Platform();
	boolean boolean0 = platform0.equals(platform1);
	assertFalse(platform1.equals((Object) platform0));
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	Platform platform0 = new Platform();
	Integer integer0 = new Integer(24);
	Platform platform1 = new Platform(integer0);
	boolean boolean0 = platform0.equals(platform1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	Platform platform0 = new Platform();
	Integer integer0 = new Integer((-1057));
	platform0.setId(integer0);
	boolean boolean0 = platform0.equals(platform0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	Platform platform0 = new Platform();
	boolean boolean0 = platform0.equals((Object) null);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	Platform platform0 = new Platform();
	boolean boolean0 = platform0.equals(platform0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	Integer integer0 = new Integer(1065);
	Platform platform0 = new Platform(integer0);
	platform0.hashCode();
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	Platform platform0 = new Platform();
	platform0.hashCode();
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	Platform platform0 = new Platform();
	String string0 = platform0.getNickName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	Platform platform0 = new Platform();
	platform0.setCreatedBy("com.tsi.workflow.beans.dao.Platform");
	String string0 = platform0.getCreatedBy();
	assertEquals("com.tsi.workflow.beans.dao.Platform", string0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	Platform platform0 = new Platform();
	String string0 = platform0.getName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	Platform platform0 = new Platform();
	Date date0 = platform0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	Platform platform0 = new Platform();
	List<System> list0 = platform0.getSystemList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	Platform platform0 = new Platform();
	String string0 = platform0.toString();
	assertEquals("com.tsi.workflow.beans.dao.Platform[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	Platform platform0 = new Platform();
	String string0 = platform0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	Platform platform0 = new Platform();
	Date date0 = platform0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	Platform platform0 = new Platform();
	Integer integer0 = platform0.getId();
	assertNull(integer0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	Platform platform0 = new Platform();
	Integer integer0 = new Integer(0);
	platform0.setId(integer0);
	Integer integer1 = platform0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	Integer integer0 = new Integer(0);
	Platform platform0 = new Platform(integer0);
	String string0 = platform0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	Platform platform0 = new Platform();
	LinkedList<System> linkedList0 = new LinkedList<System>();
	platform0.setSystemList(linkedList0);
	List<System> list0 = platform0.getSystemList();
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	Platform platform0 = new Platform();
	platform0.setActive("");
	String string0 = platform0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	Platform platform0 = new Platform();
	String string0 = platform0.getCreatedBy();
	assertNull(string0);
    }
}
