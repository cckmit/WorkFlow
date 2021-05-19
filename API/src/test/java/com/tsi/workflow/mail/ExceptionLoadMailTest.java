/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author Radha.Adhimoolam
 */
public class ExceptionLoadMailTest {

    public ExceptionLoadMailTest() {
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
     * Test of setPlanId method, of class ExceptionLoadMail.
     */
    @Test
    public void testSetPlanId() throws Exception {
	String planId = "1";
	ExceptionLoadMail instance = new ExceptionLoadMail();
	instance.setPlanId(planId);
	Field field = ReflectionUtils.findField(ExceptionLoadMail.class, "planId");
	field.setAccessible(true);
	assertEquals(planId, field.get(instance));

    }

    /**
     * Test of setStatus method, of class ExceptionLoadMail.
     */
    @Test
    public void testSetStatus() throws Exception {
	String status = "1";
	ExceptionLoadMail instance = new ExceptionLoadMail();
	instance.setStatus(status);
	Field field = ReflectionUtils.findField(ExceptionLoadMail.class, "status");
	field.setAccessible(true);
	assertEquals(status, field.get(instance));

    }

    /**
     * Test of setPlanDetails method, of class ExceptionLoadMail.
     */
    @Test
    public void testSetPlanDetails() throws Exception {
	String planDetails = "1";
	ExceptionLoadMail instance = new ExceptionLoadMail();
	instance.setPlanDetails(planDetails);
	Field field = ReflectionUtils.findField(ExceptionLoadMail.class, "planDetails");
	field.setAccessible(true);
	assertEquals(planDetails, field.get(instance));

    }

    /**
     * Test of processMessage method, of class ExceptionLoadMail.
     */
    @Test
    public void testProcessMessage() {

	ExceptionLoadMail instance = new ExceptionLoadMail();
	instance.processMessage();
	assertEquals("Source Artifact : [null] belonging to implementation plan <b> null </b> " + "has been null for exception load. <br>" + "Please sync up your codes with the latest.", instance.getMessage());

    }

}
