package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.util.Date;
import org.junit.Test;

public class DSLDetailsFormTest {

    @Test
    public void test001() throws Throwable {
	DSLDetailsForm dSLDetailsForm = new DSLDetailsForm();
	dSLDetailsForm.setFallbackloadsetname("G19009T9");
	String loadsetName = dSLDetailsForm.getFallbackloadsetname();
	assertEquals("G19009T9", loadsetName);
    }

    @Test
    public void test002() throws Throwable {
	DSLDetailsForm dSLDetailsForm = new DSLDetailsForm();
	dSLDetailsForm.setLoadsetname("G19009D9");
	String loadsetName = dSLDetailsForm.getLoadsetname();
	assertEquals("G19009D9", loadsetName);
    }

    @Test
    public void test003() throws Throwable {
	DSLDetailsForm dSLDetailsForm = new DSLDetailsForm();
	dSLDetailsForm.setLoaddatetime(new Date());
	Date loadDate = dSLDetailsForm.getLoaddatetime();
	assertEquals(new Date(), loadDate);
    }

    @Test
    public void test004() throws Throwable {
	DSLDetailsForm dSLDetailsForm = new DSLDetailsForm();
	dSLDetailsForm.setPlanstatus("ACTIVE");
	String status = dSLDetailsForm.getPlanstatus();
	assertEquals("ACTIVE", status);
    }

    @Test
    public void test005() throws Throwable {
	DSLDetailsForm dSLDetailsForm = new DSLDetailsForm();
	dSLDetailsForm.setVparname("VPO12RES");
	String vpar = dSLDetailsForm.getVparname();
	assertEquals("VPO12RES", vpar);
    }

    @Test
    public void test006() throws Throwable {
	DSLDetailsForm dSLDetailsForm = new DSLDetailsForm();
	dSLDetailsForm.setSystemname("WSP");
	String sysName = dSLDetailsForm.getSystemname();
	assertEquals("WSP", sysName);
    }

}
