
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
public class UserSettingsTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	UserSettings userSettings0 = new UserSettings((Integer) null);
	userSettings0.setValue("S+KsPn5;:MOT;Yvd");
	String string0 = userSettings0.getValue();
	assertEquals("S+KsPn5;:MOT;Yvd", string0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	UserSettings userSettings0 = new UserSettings((Integer) null);
	userSettings0.setValue("");
	String string0 = userSettings0.getValue();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	userSettings0.setUserId("~&7\u0007");
	String string0 = userSettings0.getUserId();
	assertEquals("~&7\u0007", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	userSettings0.setUserId("");
	String string0 = userSettings0.getUserId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	userSettings0.setName(" ]");
	String string0 = userSettings0.getName();
	assertEquals(" ]", string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	userSettings0.setName("");
	String string0 = userSettings0.getName();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	Date mockDate0 = new Date();
	userSettings0.setModifiedDt(mockDate0);
	Date date0 = userSettings0.getModifiedDt();
	assertNotEquals("Fri Feb 14 20:21:21 GMT 2014", date0.toString());
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	UserSettings userSettings0 = new UserSettings((Integer) null);
	userSettings0.setModifiedBy("S+KsPn5;:MOT;Yvd");
	String string0 = userSettings0.getModifiedBy();
	assertEquals("S+KsPn5;:MOT;Yvd", string0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	userSettings0.setModifiedBy("");
	String string0 = userSettings0.getModifiedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	Integer integer0 = new Integer(0);
	UserSettings userSettings0 = new UserSettings(integer0);
	Integer integer1 = userSettings0.getId();
	assertEquals(0, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	Integer integer0 = new Integer((-2033));
	UserSettings userSettings0 = new UserSettings(integer0);
	Integer integer1 = userSettings0.getId();
	assertEquals((-2033), (int) integer1);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	Integer integer0 = new Integer(2994);
	userSettings0.setId(integer0);
	Integer integer1 = userSettings0.getId();
	assertEquals(2994, (int) integer1);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	Integer integer0 = new Integer((-19));
	UserSettings userSettings0 = new UserSettings(integer0);
	Date mockDate0 = new Date(1, 1, 1, (-19), 527);
	userSettings0.setCreatedDt(mockDate0);
	Date date0 = userSettings0.getCreatedDt();
	assertSame(date0, mockDate0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	userSettings0.setCreatedBy("B");
	String string0 = userSettings0.getCreatedBy();
	assertEquals("B", string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	UserSettings userSettings0 = new UserSettings((Integer) null);
	userSettings0.setCreatedBy("");
	String string0 = userSettings0.getCreatedBy();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	userSettings0.setActive("t*vG");
	String string0 = userSettings0.getActive();
	assertEquals("t*vG", string0);
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	UserSettings userSettings0 = new UserSettings((Integer) null);
	userSettings0.setActive("");
	String string0 = userSettings0.getActive();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	Integer integer0 = new Integer((-2));
	UserSettings userSettings0 = new UserSettings(integer0);
	UserSettings userSettings1 = new UserSettings();
	boolean boolean0 = userSettings0.equals(userSettings1);
	assertFalse(boolean0);
	assertFalse(userSettings1.equals((Object) userSettings0));
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	UserSettings userSettings1 = new UserSettings((Integer) null);
	Integer integer0 = new Integer((-1));
	userSettings1.setId(integer0);
	boolean boolean0 = userSettings0.equals(userSettings1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	Integer integer0 = new Integer((-2));
	UserSettings userSettings0 = new UserSettings(integer0);
	boolean boolean0 = userSettings0.equals(userSettings0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	Integer integer0 = new Integer((-2));
	UserSettings userSettings0 = new UserSettings(integer0);
	boolean boolean0 = userSettings0.equals(integer0);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	boolean boolean0 = userSettings0.equals(userSettings0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	Integer integer0 = new Integer(328);
	UserSettings userSettings0 = new UserSettings(integer0);
	userSettings0.hashCode();
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	userSettings0.hashCode();
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	Date date0 = userSettings0.getModifiedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	Integer integer0 = new Integer((-1));
	UserSettings userSettings0 = new UserSettings(integer0);
	Date date0 = userSettings0.getCreatedDt();
	assertNull(date0);
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	String string0 = userSettings0.getModifiedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	Integer integer0 = new Integer((-1));
	UserSettings userSettings0 = new UserSettings(integer0);
	String string0 = userSettings0.getValue();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	String string0 = userSettings0.getName();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	String string0 = userSettings0.getActive();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	Integer integer0 = userSettings0.getId();
	userSettings0.setId(integer0);
	assertNull(userSettings0.getModifiedBy());
    }

    @Test(timeout = 4000)
    public void test31() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	String string0 = userSettings0.getCreatedBy();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test32() throws Throwable {
	Integer integer0 = new Integer((-2));
	UserSettings userSettings0 = new UserSettings(integer0);
	String string0 = userSettings0.toString();
	assertEquals("com.tsi.workflow.beans.dao.UserSettings[ id=-2 ]", string0);
    }

    @Test(timeout = 4000)
    public void test33() throws Throwable {
	UserSettings userSettings0 = new UserSettings();
	String string0 = userSettings0.getUserId();
	assertNull(string0);
    }
}
