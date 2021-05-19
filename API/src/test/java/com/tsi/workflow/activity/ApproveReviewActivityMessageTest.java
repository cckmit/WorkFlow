/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.base.IBeans;
import java.util.regex.Pattern;
import org.apache.log4j.Priority;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class ApproveReviewActivityMessageTest {

    public ApproveReviewActivityMessageTest() {
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
     * Test of setReviewRequest method, of class ApproveReviewActivityMessage.
     */
    @Test
    public void testSetReviewRequest() {

	boolean reviewRequest = false;
	ApproveReviewActivityMessage instance = new ApproveReviewActivityMessage(null, null);
	instance.setReviewRequest(reviewRequest);
	assertFalse(reviewRequest);

    }

    /**
     * Test of processMessage method, of class ApproveReviewActivityMessage.
     */
    @Test
    public void testProcessMessage() {

	ApproveReviewActivityMessage instance = new ApproveReviewActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
	instance.setUser(DataWareHouse.getUser());
	String result = instance.processMessage();
	assertTrue(Pattern.matches(".*has completed peer review process for implementation.*", result));
    }

    @Test
    public void testProcessMessage1() {

	ApproveReviewActivityMessage instance = new ApproveReviewActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
	instance.setUser(DataWareHouse.getUser());
	instance.setReviewRequest(true);
	String result = instance.processMessage();
	assertTrue(Pattern.matches(".*has requested peer review for implementation.*", result));

    }

    @Test
    public void testProcessMessage2() {

	ApproveReviewActivityMessage instance = new ApproveReviewActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
	instance.setUser(DataWareHouse.getUser());
	User user = DataWareHouse.getUser();
	user.setCurrentDelegatedUser(user);
	String result = instance.processMessage();
	assertTrue(Pattern.matches(".* has marked the Implementation .* as Peer Review Completed on behalf of .*", result));
    }

    /**
     * Test of getLogLevel method, of class ApproveReviewActivityMessage.
     */
    @Test
    public void testGetLogLevel() {

	ApproveReviewActivityMessage instance = new ApproveReviewActivityMessage(null, null);
	Priority expResult = Priority.INFO;
	Priority result = instance.getLogLevel();
	assertEquals(expResult, result);
    }

    /**
     * Test of setArguments method, of class ApproveReviewActivityMessage.
     */
    @Test
    public void testSetArguments() {

	IBeans[] beans = null;
	ApproveReviewActivityMessage instance = new ApproveReviewActivityMessage(null, null);
	instance.setArguments(beans);
	assertNull(beans);

    }

}
