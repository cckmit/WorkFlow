package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RepoBasedSrcArtifactsTest {

    private RepoBasedSrcArtifacts repoBasedSrcArtifacts;

    @Before
    public void setUp() throws Exception {
	repoBasedSrcArtifacts = new RepoBasedSrcArtifacts();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRepoBasedSrcArtifacts() {

	repoBasedSrcArtifacts.setFileext("asm");
	assertEquals("asm", repoBasedSrcArtifacts.getFileext());
	repoBasedSrcArtifacts.setFilename("FileName");
	assertEquals("FileName", repoBasedSrcArtifacts.getFilename());
	repoBasedSrcArtifacts.setProgname("Program Name");
	assertEquals("Program Name", repoBasedSrcArtifacts.getProgname());
	repoBasedSrcArtifacts.setSorucecommitid("342423ubsfufb123");
	assertEquals("342423ubsfufb123", repoBasedSrcArtifacts.getSorucecommitid());
	repoBasedSrcArtifacts.setSourcerepo("SourceRepo");
	assertEquals("SourceRepo", repoBasedSrcArtifacts.getSourcerepo());
	repoBasedSrcArtifacts.setSrcArtifactLink("SourceAritifactLink");
	assertEquals("SourceAritifactLink", repoBasedSrcArtifacts.getSrcArtifactLink());
	repoBasedSrcArtifacts.setSubsourcerepo("Sub Source repo");
	assertEquals("Sub Source repo", repoBasedSrcArtifacts.getSubsourcerepo());
	repoBasedSrcArtifacts.setTargetsystem("XYZ");
	assertEquals("XYZ", repoBasedSrcArtifacts.getTargetsystem());

    }

}
