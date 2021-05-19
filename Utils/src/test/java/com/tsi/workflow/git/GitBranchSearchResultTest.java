/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.git;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class GitBranchSearchResultTest {

    public GitBranchSearchResultTest() {
    }

    @Test
    public void testGetLoadDate() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	Date expResult = null;
	Date loadDate = null;
	instance.setRefLoadDate(loadDate);
	Date result = instance.getRefLoadDate();
	assertEquals(expResult, result);
	instance.setLoadDate(loadDate);
	instance.getLoadDate();
	instance.setRefPlan("");
	instance.getRefPlan();
	instance.addAdditionalInfo("system", "wsp");
	instance.setIsCheckoutAllowed(true);
	instance.isIsCheckoutAllowed();
	GitBranchSearchResult obj = new GitBranchSearchResult();
	obj.setRefLoadDate(new Date());
	GitBranchSearchResult obj1 = new GitBranchSearchResult();
	obj1.setRefLoadDate(new Date());
	assertEquals(0, instance.compare(obj, obj1));
    }

    @Test
    public void testGetId() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	Integer expResult = null;
	Integer id = null;
	instance.setId(id);
	Integer result = instance.getId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetAdditionalInfo() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	HashMap<String, String> expResult = null;
	HashMap<String, String> additionalInfo = null;
	instance.setAdditionalInfo(additionalInfo);
	HashMap<String, String> result = instance.getAdditionalInfo();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetFileType() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	String expResult = "";
	String fileType = "";
	instance.setFileType(fileType);
	String result = instance.getFileType();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetCommitId() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	String expResult = "";
	String commitId = "";
	instance.setCommitId(commitId);
	String result = instance.getCommitId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetAuthorName() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	String expResult = "";
	String authorName = "";
	instance.setAuthorName(authorName);
	String result = instance.getAuthorName();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetAuthorMailId() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	String expResult = "";
	String authorMailId = "";
	instance.setAuthorMailId(authorMailId);
	String result = instance.getAuthorMailId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetCommitterName() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	String expResult = "";
	String committerName = "";
	instance.setCommitterName(committerName);
	String result = instance.getCommitterName();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetCommitterMailId() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	String expResult = "";
	String committerMailId = "";
	instance.setCommitterMailId(committerMailId);
	String result = instance.getCommitterMailId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetCommitDateTime() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	Date expResult = null;
	Date commitDateTime = null;
	instance.setCommitDateTime(commitDateTime);
	Date result = instance.getCommitDateTime();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetSubject() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	String expResult = "";
	String subject = "";
	instance.setSubject(subject);
	String result = instance.getSubject();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetTags() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	List<String> expResult = null;
	List<String> tags = null;
	instance.setTags(tags);
	List<String> result = instance.getTags();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetTargetSystem() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	String expResult = "";
	String targetSystem = "";
	instance.setTargetSystem(targetSystem);
	String result = instance.getTargetSystem();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetFuncArea() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	String expResult = "";
	String funcArea = "";
	instance.setFuncArea(funcArea);
	String result = instance.getFuncArea();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetSourceUrl() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	String expResult = "";
	String sourceUrl = "";
	instance.setSourceUrl(sourceUrl);
	String result = instance.getSourceUrl();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetRepoDesc() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	String expResult = "";
	String repoDesc = "";
	instance.setRepoDesc(repoDesc);
	String result = instance.getRepoDesc();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetStatus() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	String expResult = "";
	String status = "";
	instance.setStatus(status);
	String result = instance.getStatus();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetIsCheckedout() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	boolean expResult = false;
	boolean isCheckedout = false;
	instance.setIsCheckedout(isCheckedout);
	boolean result = instance.getIsCheckedout();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetIsBranchSelected() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	boolean expResult = false;
	boolean isBranchSelected = false;
	instance.setIsBranchSelected(isBranchSelected);
	boolean result = instance.getIsBranchSelected();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetOnlineStatus() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	String expResult = "";
	String onlineStatus = "";
	instance.setRefStatus(onlineStatus);
	String result = instance.getRefStatus();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetGitSourceURL() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	String expResult = "";
	String onlineStatus = "";
	instance.setGitSourceURL(onlineStatus);
	String result = instance.getGitSourceURL();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetRemarks() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	List<String> expResult = new ArrayList();
	instance.setRemarks(expResult);
	List<String> result = instance.getRemarks();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetTargetFuncArea() {
	GitBranchSearchResult instance = new GitBranchSearchResult();
	String expResult = "";
	String onlineStatus = "";
	instance.setTargetFuncArea(onlineStatus);
	String result = instance.getTargetFuncArea();
	assertEquals(expResult, result);
    }

}
