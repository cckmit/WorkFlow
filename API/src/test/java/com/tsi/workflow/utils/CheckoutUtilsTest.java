/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;

import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class CheckoutUtilsTest {

    public CheckoutUtilsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getIdString method, of class CheckoutUtils.
     */
    @Test
    public void testGetIdString() {

	CheckoutSegments lSegment = new CheckoutSegments();

	String result = CheckoutUtils.getIdString(lSegment);
	assertNotNull(result);

    }

    /**
     * Test of getIBMIdString method, of class CheckoutUtils.
     */
    @Test
    public void testGetIBMIdString() {

	CheckoutSegments lSegment = new CheckoutSegments();
	lSegment.setFileHashCode("");
	lSegment.setProgramName("");
	lSegment.setFuncArea("");
	String result = CheckoutUtils.getIBMIdString(lSegment);
	assertNotNull(result);

    }

    /**
     * Test of getIdStringWithOutSystem method, of class CheckoutUtils.
     */
    @Test
    public void testGetIdStringWithOutSystem() {

	CheckoutSegments lSegment = new CheckoutSegments();
	CheckoutUtils util = new CheckoutUtils();
	String result = CheckoutUtils.getIdStringWithOutSystem(lSegment);
	lSegment.setCommonFile(Boolean.TRUE);
	CheckoutUtils.getIdStringWithOutSystem(lSegment);
	assertNotNull(result);

    }

    /**
     * Test of getIdStringWithPlan method, of class CheckoutUtils.
     */
    @Test
    public void testGetIdStringWithPlan() {

	CheckoutSegments lSegment = new CheckoutSegments();
	ImpPlan planobj = new ImpPlan();
	lSegment.setFileHashCode("");
	lSegment.setProgramName("");
	lSegment.setFuncArea("");
	lSegment.setPlanId(planobj);
	String result = CheckoutUtils.getIdStringWithPlan(lSegment);
	assertNotNull(result);

    }

    @Test
    public void testgetIdStringWithPlanSysCheck() {
	CheckoutSegments lSegment = new CheckoutSegments();
	ImpPlan planobj = new ImpPlan();
	lSegment.setFileHashCode("");
	lSegment.setProgramName("");
	lSegment.setFuncArea("");
	lSegment.setPlanId(planobj);
	CheckoutUtils.getIdStringWithPlanSysCheck(lSegment);

    }

    @Test
    public void testgetIdStringWithPlan() {
	CheckoutSegments lSegment = new CheckoutSegments();
	ImpPlan planobj = new ImpPlan();
	lSegment.setFileHashCode("");
	lSegment.setProgramName("");
	lSegment.setFuncArea("");
	lSegment.setCommonFile(Boolean.TRUE);
	lSegment.setPlanId(planobj);
	CheckoutUtils.getIdStringWithPlan(lSegment);
    }

    @Test
    public void tesgetHexStringWithPlanSegment() {
	CheckoutSegments lSegment = new CheckoutSegments();
	ImpPlan planobj = new ImpPlan();
	lSegment.setFileHashCode("");
	lSegment.setProgramName("bdba.asm");
	lSegment.setFuncArea("FLS");
	lSegment.setId(1);
	lSegment.setId(1);
	lSegment.setPlanId(new ImpPlan("T1800194"));
	lSegment.setImpId(new Implementation("T1800194_001"));
	lSegment.setTargetSystem("APO");
	lSegment.setPlanId(planobj);
	DigestUtils.md5Hex(anyString());
	CheckoutUtils.getHexStringWithPlanSegment(lSegment);

    }

}
