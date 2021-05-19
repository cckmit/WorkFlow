/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.activity;

import static org.junit.Assert.assertNotNull;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.utils.Constants;
import org.apache.log4j.Priority;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class RejectionActivityMessageTest {

    RejectionActivityMessage instance;

    public RejectionActivityMessageTest() {
	instance = new RejectionActivityMessage(DataWareHouse.getPlan(), DataWareHouse.getPlan().getImplementationList().get(0));
	instance.setUser(DataWareHouse.getUser());
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetLogLevel() {
	Priority result = instance.getLogLevel();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage() {
	DataWareHouse.getPlan().setMgmtComment(null);
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage1() {
	DataWareHouse.getPlan().setMgmtComment("Test Comment");
	String result = instance.processMessage();
	assertNotNull(result);
    }

    @Test
    public void testProcessMessage2() {
	instance.setComments(null);
	String result = instance.processMessage();
	assertNotNull(result);
	instance.setComments("notnull");
	result = instance.processMessage();

	instance.setTsdActivity(true);
	instance.processMessage();

	instance.setTsdActivity(true);
	instance.setComments(null);
	instance.processMessage();

	instance.setTsdActivity(false);
	instance.isTsdActivity();

	User usr = DataWareHouse.getUser();
	usr.setCurrentRole("Lead");
	instance.setComments("not null");
	instance.processMessage();

	instance.setComments(Constants.AUTOREJECT_COMMENT.LOAD_DATE_CHANGE.getValue());
	instance.setUser(usr);
	instance.processMessage();

    }

    /**
     * Test of getComments method, of class RejectionActivityMessage.
     */
    @Test
    public void testGetComments() {

	String expResult = "";
	instance.setComments("comments");
	String result = instance.getComments();
	assertNotNull(result);
    }

    /**
     * Test of setComments method, of class RejectionActivityMessage.
     */
    @Test
    public void testSetComments() {
	String comments = "";
	instance.setComments(comments);
    }

    /**
     * Test of setArguments method, of class RejectionActivityMessage.
     */
    @Test
    public void testSetArguments() {

	IBeans[] beans = null;
	instance.setArguments(beans);
    }

}
