package com.tsi.workflow.beans.dao;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.util.Date;
import org.junit.Test;

public class GitProdSearchDbTest {

    @Test
    public void test() {
	GitProdSearchDb gitProdSearchDb = new GitProdSearchDb();
	assertEquals("Y", gitProdSearchDb.getActive());
	assertNull(gitProdSearchDb.getCreatedBy());
	assertNull(gitProdSearchDb.getCreatedDt());
	assertNull(gitProdSearchDb.getModifiedBy());
	assertNull(gitProdSearchDb.getModifiedDt());
	gitProdSearchDb.setCommitDateTime(new Date());
	assertEquals(new Date(), gitProdSearchDb.getCommitDateTime());
	gitProdSearchDb.setCommitterMailId("committerMailId");
	assertEquals("committerMailId", gitProdSearchDb.getCommitterMailId());
	gitProdSearchDb.setCommitterName("committerName");
	assertEquals("committerName", gitProdSearchDb.getCommitterName());
	gitProdSearchDb.setCompany("DL");
	assertEquals("DL", gitProdSearchDb.getCompany());
	gitProdSearchDb.setDerivedCommitId("derivedCommitId");
	assertEquals("derivedCommitId", gitProdSearchDb.getDerivedCommitId());
	gitProdSearchDb.setDerivedRepo("derivedRepo");
	assertEquals("derivedRepo", gitProdSearchDb.getDerivedRepo());
	gitProdSearchDb.setDerivedUrl("derivedUrl");
	assertEquals("derivedUrl", gitProdSearchDb.getDerivedUrl());
	gitProdSearchDb.setFileExt("asm");
	assertEquals("asm", gitProdSearchDb.getFileExt());
	gitProdSearchDb.setFileHashcode("fileHashcode");
	assertEquals("fileHashcode", gitProdSearchDb.getFileHashcode());
	gitProdSearchDb.setFileId(10);
	assertNotNull(gitProdSearchDb.getFileId());
	gitProdSearchDb.setFileName("fileName");
	assertEquals("fileName", gitProdSearchDb.getFileName());
	gitProdSearchDb.setFileType("fileType");
	assertEquals("fileType", gitProdSearchDb.getFileType());
	gitProdSearchDb.setFuncArea("FAR");
	assertEquals("FAR", gitProdSearchDb.getFuncArea());
	gitProdSearchDb.setProgramName("programName");
	assertEquals("programName", gitProdSearchDb.getProgramName());
	gitProdSearchDb.setRefLoadDateTime(new Date());
	assertEquals(new Date(), gitProdSearchDb.getRefLoadDateTime());
	gitProdSearchDb.setRefPlan("refPlan");
	assertEquals("refPlan", gitProdSearchDb.getRefPlan());
	gitProdSearchDb.setRefStatus("refStatus");
	assertEquals("refStatus", gitProdSearchDb.getRefStatus());
	gitProdSearchDb.setRepoCommitId(10);
	assertNotNull(gitProdSearchDb.getRepoCommitId());
	gitProdSearchDb.setRepoId(10);
	assertNotNull(gitProdSearchDb.getRepoId());
	gitProdSearchDb.setSourceCommitId("sourceCommitId");
	assertEquals("sourceCommitId", gitProdSearchDb.getSourceCommitId());
	gitProdSearchDb.setSourceRepo("sourceRepo");
	assertEquals("sourceRepo", gitProdSearchDb.getSourceRepo());
	gitProdSearchDb.setSourceUrl("sourceUrl");
	assertEquals("sourceUrl", gitProdSearchDb.getSourceUrl());
	gitProdSearchDb.setSubDerivedRepo("subDerivedRepo");
	assertEquals("subDerivedRepo", gitProdSearchDb.getSubDerivedRepo());
	gitProdSearchDb.setSubRepoId(10);
	assertNotNull(gitProdSearchDb.getSubRepoId());
	gitProdSearchDb.setSubSourceRepo("subSourceRepo");
	assertEquals("subSourceRepo", gitProdSearchDb.getSubSourceRepo());
	gitProdSearchDb.setTargetSystem("targetSystem");
	assertEquals("targetSystem", gitProdSearchDb.getTargetSystem());
	gitProdSearchDb.setVersion(new BigInteger("1"));
	assertNotNull(gitProdSearchDb.getVersion());

    }

}
