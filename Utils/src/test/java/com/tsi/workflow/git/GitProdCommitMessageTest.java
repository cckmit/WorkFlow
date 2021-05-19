/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.git;

import static org.junit.Assert.assertEquals;

import java.util.Date;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class GitProdCommitMessageTest {

    public GitProdCommitMessageTest() {
    }

    @Test
    public void testGetDate() {
	GitProdCommitMessage instance = new GitProdCommitMessage();
	Date expResult = null;
	Date Date = null;
	instance.setDate(Date);
	Date result = instance.getDate();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetType() {
	GitProdCommitMessage instance = new GitProdCommitMessage();
	String expResult = "";
	String Type = "";
	instance.setType(Type);
	String result = instance.getType();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetPlanID() {
	GitProdCommitMessage instance = new GitProdCommitMessage();
	String expResult = "";
	String PlanID = "";
	instance.setPlanID(PlanID);
	String result = instance.getPlanID();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetPlanOwner() {
	GitProdCommitMessage instance = new GitProdCommitMessage("", new Date());
	String expResult = "";
	String PlanOwner = "";
	instance.setPlanOwner(PlanOwner);
	String result = instance.getPlanOwner();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetSourceRef() {
	GitProdCommitMessage instance = new GitProdCommitMessage();
	String expResult = "";
	String SourceRef = "";
	instance.setSourceRef(SourceRef);
	String result = instance.getSourceRef();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetStatus() {
	GitProdCommitMessage instance = new GitProdCommitMessage();
	String expResult = "";
	String Status = "";
	instance.setStatus(Status);
	String result = instance.getStatus();
	assertEquals(expResult, result);
    }

}
