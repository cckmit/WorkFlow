/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.controller;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.service.ReviewerService;
import com.tsi.workflow.utils.JSONResponse;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReviewerControllerTest {

    ReviewerController instance;

    public ReviewerControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	try {
	    ReviewerController realInstance = new ReviewerController();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockController(instance, ReviewerService.class);
	} catch (Exception ex) {
	    Logger.getLogger(ProtectedControllerTest.class.getName()).log(Level.SEVERE, null, ex);
	}

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testReviewerController() throws Exception {
	TestCaseExecutor.doTest(instance, ReviewerController.class);
    }

    @Test
    public void testReviewerService() {
	ReviewerController obj = new ReviewerController();
	obj.setReviewerService(obj.getReviewerService());

    }

    @Test
    public void testlistImplementations() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	when(instance.listImplementations(null, null, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }

    @Test
    public void testreviewerHistory() throws Exception {
	JSONResponse lResponse = new JSONResponse();
	when(instance.reviewerHistory(null, null, 0, 10, "{\"modifiedDt\":\"2017-10-10 01:02:20 +0000\"}")).thenReturn(lResponse);
    }
}
