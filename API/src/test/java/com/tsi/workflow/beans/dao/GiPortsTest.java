package com.tsi.workflow.beans.dao;

import static org.junit.Assert.*;

import java.util.Date;
import org.junit.Test;

public class GiPortsTest {

    @Test
    public void test00() throws Throwable {
	GiPorts giPorts = new GiPorts();
	giPorts.setActive("Y");
	assertEquals("Y", giPorts.getActive());
	giPorts.setCreatedBy("createdBy");
	assertEquals("createdBy", giPorts.getCreatedBy());
	giPorts.setCreatedDt(new Date());
	assertEquals(new Date(), giPorts.getCreatedDt());
	giPorts.setId(10);
	assertNotNull(giPorts.getId());
	giPorts.setIpAddr("ipAddr");
	assertEquals("ipAddr", giPorts.getIpAddr());
	giPorts.setModifiedBy("modifiedBy");
	assertEquals("modifiedBy", giPorts.getModifiedBy());
	giPorts.setModifiedDt(new Date());
	assertEquals(new Date(), giPorts.getModifiedDt());
	giPorts.setPortNo(10);
	assertNotNull(giPorts.getPortNo());
	giPorts.setUserId("e738670");
	assertEquals("e738670", giPorts.getUserId());

    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	GiPorts GiPorts0 = new GiPorts();
	Integer integer0 = new Integer((-4341));
	GiPorts0.setId(integer0);
	GiPorts GiPorts1 = new GiPorts();
	boolean boolean0 = GiPorts0.equals(GiPorts1);
	assertFalse(boolean0);
	assertFalse(GiPorts1.equals((Object) GiPorts0));
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	GiPorts GiPorts0 = new GiPorts();
	Integer integer0 = new Integer((-330));
	GiPorts GiPorts1 = new GiPorts(integer0);
	boolean boolean0 = GiPorts0.equals(GiPorts1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	Integer integer0 = new Integer(2396);
	GiPorts GiPorts0 = new GiPorts(integer0);
	boolean boolean0 = GiPorts0.equals(GiPorts0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	GiPorts GiPorts0 = new GiPorts();
	ImpPlan impPlan0 = new ImpPlan();
	boolean boolean0 = GiPorts0.equals(impPlan0);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	GiPorts GiPorts0 = new GiPorts();
	boolean boolean0 = GiPorts0.equals(GiPorts0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	GiPorts GiPorts = new GiPorts();
	String string0 = GiPorts.toString();
	assertEquals("com.tsi.workflow.beans.dao.GiPorts[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	GiPorts GiPorts = new GiPorts();
	GiPorts.hashCode();
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	GiPorts GiPorts = new GiPorts();
	Integer integer0 = new Integer((-78));
	GiPorts.setId(integer0);
	GiPorts.hashCode();
    }

}
