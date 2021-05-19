/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.git;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class GitSearchResultTest {

    public GitSearchResultTest() {
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
    public void testGetAdditionalInfo() {
	GitSearchResult instance = new GitSearchResult();
	HashMap<String, String> expResult = null;
	HashMap<String, String> additionalInfo = null;
	instance.setAdditionalInfo(additionalInfo);
	HashMap<String, String> result = instance.getAdditionalInfo();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetTargetSystems() {
	GitSearchResult instance = new GitSearchResult();
	SortedSet<String> expResult = null;
	SortedSet<String> targetSystems = null;
	instance.setTargetSystems(targetSystems);
	SortedSet<String> result = instance.getTargetSystems();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetFileName() {
	GitSearchResult instance = new GitSearchResult();
	String expResult = "";
	String fileName = "";
	instance.setFileName(fileName);
	String result = instance.getFileName();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetProgramName() {
	GitSearchResult instance = new GitSearchResult();
	String expResult = "";
	String programName = "";
	instance.setProgramName(programName);
	String result = instance.getProgramName();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetFileHashCode() {
	GitSearchResult instance = new GitSearchResult();
	String expResult = "";
	String fileHashCode = "";
	instance.setFileHashCode(fileHashCode);
	String result = instance.getFileHashCode();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetBranch() {
	GitSearchResult instance = new GitSearchResult();
	List<GitBranchSearchResult> expResult = null;
	List<GitBranchSearchResult> branch = null;
	instance.setBranch(branch);
	List<GitBranchSearchResult> result = instance.getBranch();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetFileNameWithHash() {
	GitSearchResult instance = new GitSearchResult();
	String expResult = "";
	String fileNameWithHash = "";
	instance.setFileNameWithHash(fileNameWithHash);
	String result = instance.getFileNameWithHash();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetProdFlag() {
	GitSearchResult instance = new GitSearchResult();
	String expResult = "";
	String prodFlag = "";
	instance.setProdFlag(prodFlag);
	String result = instance.getProdFlag();
	assertEquals(expResult, result);
    }

    /**
     * Test of setAdditionalInfo method, of class GitSearchResult.
     */
    @Test
    public void testSetAdditionalInfo() {
	System.out.println("setAdditionalInfo");
	HashMap<String, String> additionalInfo = null;
	GitSearchResult instance = new GitSearchResult();
	instance.setAdditionalInfo(additionalInfo);

    }

    /**
     * Test of addAdditionalInfo method, of class GitSearchResult.
     */
    @Test
    public void testAddAdditionalInfo() {
	System.out.println("addAdditionalInfo");
	String pKey = "";
	String pValue = "";
	GitSearchResult instance = new GitSearchResult();
	instance.addAdditionalInfo(pKey, pValue);

    }

    /**
     * Test of setTargetSystems method, of class GitSearchResult.
     */
    @Test
    public void testSetTargetSystems() {
	System.out.println("setTargetSystems");
	SortedSet<String> targetSystems = null;
	GitSearchResult instance = new GitSearchResult();
	instance.setTargetSystems(targetSystems);

    }

    /**
     * Test of setFileName method, of class GitSearchResult.
     */
    @Test
    public void testSetFileName() {
	System.out.println("setFileName");
	String fileName = "";
	GitSearchResult instance = new GitSearchResult();
	instance.setFileName(fileName);

    }

    /**
     * Test of setProgramName method, of class GitSearchResult.
     */
    @Test
    public void testSetProgramName() {
	System.out.println("setProgramName");
	String programName = "";
	GitSearchResult instance = new GitSearchResult();
	instance.setProgramName(programName);

    }

    /**
     * Test of setFileHashCode method, of class GitSearchResult.
     */
    @Test
    public void testSetFileHashCode() {
	System.out.println("setFileHashCode");
	String fileHashCode = "";
	GitSearchResult instance = new GitSearchResult();
	instance.setFileHashCode(fileHashCode);

    }

    /**
     * Test of setBranch method, of class GitSearchResult.
     */
    @Test
    public void testSetBranch() {
	System.out.println("setBranch");
	List<GitBranchSearchResult> branch = null;
	GitSearchResult instance = new GitSearchResult();
	instance.setBranch(branch);

    }

    /**
     * Test of addBranch method, of class GitSearchResult.
     */
    @Test
    public void testAddBranch() {
	System.out.println("addBranch");
	GitBranchSearchResult branch = new GitBranchSearchResult();
	GitSearchResult instance = new GitSearchResult();
	instance.setMaxLoadDate(null);
	instance.addBranch(branch);
	instance.setMaxLoadDate(new Date());
	branch.setRefLoadDate(new Date());
	instance.addBranch(branch);

    }

    @Test
    public void testAddBranch1() throws ParseException {
	GitBranchSearchResult branch = new GitBranchSearchResult();
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	GitSearchResult instance = new GitSearchResult();
	Date mockDate = mock(Date.class);
	instance.setMaxLoadDate(sdf.parse("2010/04/01 12:00:00"));
	Date lDate = sdf.parse("2011/04/01 12:00:00");
	branch.setRefLoadDate(lDate);
	when(mockDate.before(lDate)).thenReturn(true);
	instance.addBranch(branch);

    }

    /**
     * Test of setFileNameWithHash method, of class GitSearchResult.
     */
    @Test
    public void testSetFileNameWithHash() {
	System.out.println("setFileNameWithHash");
	String fileNameWithHash = "";
	GitSearchResult instance = new GitSearchResult();
	instance.setFileNameWithHash(fileNameWithHash);

    }

    /**
     * Test of setProdFlag method, of class GitSearchResult.
     */
    @Test
    public void testSetProdFlag() {
	System.out.println("setProdFlag");
	String prodFlag = "";
	GitSearchResult instance = new GitSearchResult();
	instance.setProdFlag(prodFlag);

    }

    /**
     * Test of getMaxLoadDate method, of class GitSearchResult.
     */
    @Test
    public void testGetMaxLoadDate() {
	System.out.println("getMaxLoadDate");
	GitSearchResult instance = new GitSearchResult();
	Date expResult = null;
	Date result = instance.getMaxLoadDate();
	assertEquals(expResult, result);

    }

    /**
     * Test of setMaxLoadDate method, of class GitSearchResult.
     */
    @Test
    public void testSetMaxLoadDate() {
	System.out.println("setMaxLoadDate");
	Date minLoadDate = null;
	GitSearchResult instance = new GitSearchResult();
	instance.setMaxLoadDate(minLoadDate);

    }

    /**
     * Test of compare method, of class GitSearchResult.
     */
    @Test
    public void testCompare() {
	System.out.println("compare");
	GitSearchResult o1 = new GitSearchResult();
	o1.setMaxLoadDate(new Date());
	o1.setFileName("");
	GitSearchResult o2 = new GitSearchResult();
	o2.setMaxLoadDate(new Date());
	o2.setFileName("");
	GitSearchResult instance = new GitSearchResult();
	int expResult = 0;
	int result = instance.compare(o1, o2);
	assertEquals(expResult, result);
	// Negative Case
	o1.setFileName("Test");
	expResult = 4;
	result = instance.compare(o1, o2);
	assertEquals(expResult, result);
    }

    /**
     * Test of setMaxLoadDate method, of class GitSearchResult.
     */
    @Test
    public void testGetRepoAccess() {
	GitSearchResult instance = new GitSearchResult();
	String expResult = "";
	instance.setRepoAccess(expResult);
	String result = instance.getRepoAccess();
	assertEquals(expResult, result);
    }

}
