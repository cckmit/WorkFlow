/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.git;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class GitBaseMetaResultTest {

    public GitBaseMetaResultTest() {
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
     * Test of getFileHashCode method, of class GitBaseMetaResult.
     */
    @Test
    public void testGetFileHashCode() {

	GitBaseMetaResult instance = new GitBaseMetaResult("", "");
	String expResult = "";
	String result = instance.getFileHashCode();

    }

    /**
     * Test of getFileName method, of class GitBaseMetaResult.
     */
    @Test
    public void testGetFileName() {

	GitBaseMetaResult instance = new GitBaseMetaResult("", "");
	String expResult = "";
	String result = instance.getFileName();
    }

    /**
     * Test of getProgramName method, of class GitBaseMetaResult.
     */
    @Test
    public void testGetProgramName() {

	GitBaseMetaResult instance = new GitBaseMetaResult("", "");
	String expResult = "";
	String result = instance.getProgramName();

    }

    /**
     * Test of getFileNameWithHash method, of class GitBaseMetaResult.
     */
    @Test
    public void testGetFileNameWithHash() {

	GitBaseMetaResult instance = new GitBaseMetaResult("", "");
	String expResult = "";
	String result = instance.getFileNameWithHash();

    }

    @Test
    public void testSetFuncArea() {

	GitBaseMetaResult instance = new GitBaseMetaResult("", "");
	instance.setFuncArea("sysPackage");
    }

    @Test
    public void testGetFuncArea() {

	GitBaseMetaResult instance = new GitBaseMetaResult("", "");
	String expResult = "sysPackage";
	instance.setFuncArea(expResult);
	assertEquals(expResult, instance.getFuncArea());

    }

    @Test
    public void testGetFileNametoGroupSort() {

	GitBaseMetaResult instance = new GitBaseMetaResult("", "");
	instance.getFileNametoGroupSort();

    }
}
