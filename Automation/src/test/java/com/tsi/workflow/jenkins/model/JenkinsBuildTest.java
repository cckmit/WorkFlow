/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.jenkins.model;

import com.tsi.workflow.User;
import java.util.Date;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class JenkinsBuildTest extends TestCase {

    public JenkinsBuildTest(String testName) {
	super(testName);
    }

    public void testGetBuildTime() {
	JenkinsBuild instance = new JenkinsBuild(null);
	Date expResult = null;
	Date buildTime = null;
	instance.setBuildTime(buildTime);
	Date result = instance.getBuildTime();
	assertEquals(expResult, result);
    }

    public void testGetJobName() {
	JenkinsBuild instance = new JenkinsBuild();
	String expResult = "";
	String jobName = "";
	instance.setJobName(jobName);
	String result = instance.getJobName();
	assertEquals(expResult, result);
    }

    public void testGetQueueUrl() {
	JenkinsBuild instance = new JenkinsBuild();
	String expResult = "";
	String queueUrl = "";
	instance.setQueueUrl(queueUrl);
	String result = instance.getQueueUrl();
	assertEquals(expResult, result);
    }

    public void testGetBuildNumber() {
	JenkinsBuild instance = new JenkinsBuild();
	int expResult = 0;
	int buildNumber = 0;
	instance.setBuildNumber(buildNumber);
	int result = instance.getBuildNumber();
	assertEquals(expResult, result);
    }

    public void testGetSystemId() {
	JenkinsBuild instance = new JenkinsBuild();
	String expResult = "";
	String systemId = "";
	instance.setSystemLoadId(systemId);
	String result = instance.getSystemLoadId();
	assertEquals(expResult, result);
    }

    public void testGetUser() {
	JenkinsBuild instance = new JenkinsBuild();
	User expResult = null;
	User user = null;
	instance.setUser(user);
	User result = instance.getUser();
	assertEquals(expResult, result);
    }

    /**
     * Test of getByPassRegression method, of class JenkinsBuild.
     */
    @Test
    public void testGetByPassRegression() {
	System.out.println("getByPassRegression");
	JenkinsBuild instance = new JenkinsBuild();
	Boolean expResult = null;
	Boolean result = instance.getByPassRegression();
	assertEquals(expResult, result);
    }

    /**
     * Test of setByPassRegression method, of class JenkinsBuild.
     */
    @Test
    public void testSetByPassRegression() {
	System.out.println("setByPassRegression");
	Boolean byPassRegression = null;
	JenkinsBuild instance = new JenkinsBuild();
	instance.setByPassRegression(byPassRegression);
    }

    /**
     * Test of equals method, of class JenkinsBuild.
     */
    @Test
    public void testEquals() {
	System.out.println("equals");
	Object obj = null;
	JenkinsBuild instance = new JenkinsBuild();
	boolean expResult = false;
	boolean result = instance.equals(obj);
	assertEquals(expResult, result);
	JenkinsBuild newObj = new JenkinsBuild();
	newObj.setBuildNumber(0);
	newObj.setSystemLoadId("");
	newObj.setJobName("");
	assertEquals(expResult, instance.equals(newObj));
    }

    /**
     * Test of hashCode method, of class JenkinsBuild.
     */
    @Test
    public void testHashCode() {
	System.out.println("hashCode");
	JenkinsBuild instance = new JenkinsBuild();
	instance.hashCode();
    }

}
