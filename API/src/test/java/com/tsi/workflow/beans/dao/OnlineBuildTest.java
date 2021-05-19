package com.tsi.workflow.beans.dao;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import java.util.Date;
import org.junit.Test;
import org.mockito.Mock;

public class OnlineBuildTest {

    @Mock
    System system;

    @Test
    public void test00() throws Throwable {
	OnlineBuild onlineBuild0 = new OnlineBuild();
	onlineBuild0.setActive("Y");
	assertEquals("Y", onlineBuild0.getActive());
	onlineBuild0.setBuildDateTime(new Date());
	assertEquals(new Date(), onlineBuild0.getBuildDateTime());
	onlineBuild0.setBuildNumber(10);
	assertNotNull(onlineBuild0.getBuildNumber());
	onlineBuild0.setBuildStatus("buildStatus");
	assertEquals("buildStatus", onlineBuild0.getBuildStatus());
	onlineBuild0.setBuildType("AUX");
	assertEquals("AUX", onlineBuild0.getBuildType());
	onlineBuild0.setCreatedDt(new Date());
	assertEquals(new Date(), onlineBuild0.getCreatedDt());
	onlineBuild0.setCreatedBy("createdBy");
	assertEquals("createdBy", onlineBuild0.getCreatedBy());
	onlineBuild0.setId(10);
	assertEquals("createdBy", onlineBuild0.getCreatedBy());
	onlineBuild0.setJenkinsUrl("jenkinsUrl");
	assertEquals("jenkinsUrl", onlineBuild0.getJenkinsUrl());
	onlineBuild0.setJobStatus("S");
	assertEquals("S", onlineBuild0.getJobStatus());
	onlineBuild0.setLoadSetType("loadSetType");
	assertEquals("loadSetType", onlineBuild0.getLoadSetType());
	onlineBuild0.setModifiedBy("modifiedBy");
	assertEquals("modifiedBy", onlineBuild0.getModifiedBy());
	onlineBuild0.setModifiedDt(new Date());
	assertEquals(new Date(), onlineBuild0.getModifiedDt());
	onlineBuild0.setPlanId(DataWareHouse.getPlan());
	assertEquals(DataWareHouse.getPlan(), onlineBuild0.getPlanId());
	onlineBuild0.setSystemId(system);
	assertEquals(system, onlineBuild0.getSystemId());
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	OnlineBuild OnlineBuild0 = new OnlineBuild();
	Integer integer0 = new Integer((-4341));
	OnlineBuild0.setId(integer0);
	OnlineBuild OnlineBuild1 = new OnlineBuild();
	boolean boolean0 = OnlineBuild0.equals(OnlineBuild1);
	assertFalse(boolean0);
	assertFalse(OnlineBuild1.equals((Object) OnlineBuild0));
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	OnlineBuild OnlineBuild0 = new OnlineBuild();
	Integer integer0 = new Integer((-330));
	OnlineBuild OnlineBuild1 = new OnlineBuild(integer0);
	boolean boolean0 = OnlineBuild0.equals(OnlineBuild1);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	Integer integer0 = new Integer(2396);
	OnlineBuild OnlineBuild0 = new OnlineBuild(integer0);
	boolean boolean0 = OnlineBuild0.equals(OnlineBuild0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	OnlineBuild OnlineBuild0 = new OnlineBuild();
	ImpPlan impPlan0 = new ImpPlan();
	boolean boolean0 = OnlineBuild0.equals(impPlan0);
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	OnlineBuild OnlineBuild0 = new OnlineBuild();
	boolean boolean0 = OnlineBuild0.equals(OnlineBuild0);
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	OnlineBuild OnlineBuild = new OnlineBuild();
	String string0 = OnlineBuild.toString();
	assertEquals("com.tsi.workflow.beans.dao.Build[ id=null ]", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	OnlineBuild OnlineBuild = new OnlineBuild();
	OnlineBuild.hashCode();
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	OnlineBuild OnlineBuild = new OnlineBuild();
	Integer integer0 = new Integer((-78));
	OnlineBuild.setId(integer0);
	OnlineBuild.hashCode();
    }

}
