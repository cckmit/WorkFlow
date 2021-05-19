/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.jenkins.model;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class JenkinsLogTest {

    public JenkinsLogTest() {
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
    public void testGetFailedSegments() {
	JenkinsLog instance = new JenkinsLog();
	List<String> expResult = null;
	List<String> failedSegments = null;
	instance.setFailedFiles(failedSegments);
	List<String> result = instance.getFailedFiles();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetTotalFiles() {
	JenkinsLog instance = new JenkinsLog();
	Integer expResult = null;
	Integer totalFiles = null;
	instance.setTotalCount(totalFiles);
	Integer result = instance.getTotalCount();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetFailedFiles() {
	JenkinsLog instance = new JenkinsLog();
	Integer expResult = null;
	Integer failedFiles = null;
	instance.setFailedCount(failedFiles);
	Integer result = instance.getFailedCount();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetStartTime() {
	JenkinsLog instance = new JenkinsLog();
	String expResult = "";
	String startTime = "";
	instance.setStartTime(startTime);
	String result = instance.getStartTime();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetEndTime() {
	JenkinsLog instance = new JenkinsLog();
	String expResult = "";
	String endTime = "";
	instance.setEndTime(endTime);
	String result = instance.getEndTime();
	assertEquals(expResult, result);
    }

    /**
     * Test of getErrorMessage method, of class JenkinsLog.
     */
    @Test
    public void testGetErrorMessage() {
	System.out.println("getErrorMessage");
	JenkinsLog instance = new JenkinsLog();
	String expResult = "";
	instance.setErrorMessage(expResult);
	String result = instance.getErrorMessage();
	assertEquals(expResult, result);
    }

    /**
     * Test of setErrorMessage method, of class JenkinsLog.
     */
    @Test
    public void testSetErrorMessage() {
	System.out.println("setErrorMessage");
	String errorMessage = "";
	JenkinsLog instance = new JenkinsLog();
	instance.setErrorMessage(errorMessage);
    }

    /**
     * Test of getJobStatus method, of class JenkinsLog.
     */
    @Test
    public void testGetJobStatus() {
	System.out.println("getJobStatus");
	JenkinsLog instance = new JenkinsLog();
	Boolean expResult = false;
	Boolean result = instance.getJobStatus();
	assertEquals(expResult, result);
    }

    /**
     * Test of setJobStatus method, of class JenkinsLog.
     */
    @Test
    public void testSetJobStatus() {
	System.out.println("setJobStatus");
	Boolean jobStatus = null;
	JenkinsLog instance = new JenkinsLog();
	instance.setJobStatus(jobStatus);
    }

}
