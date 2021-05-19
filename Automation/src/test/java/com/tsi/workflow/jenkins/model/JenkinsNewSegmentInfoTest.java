package com.tsi.workflow.jenkins.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class JenkinsNewSegmentInfoTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() {
	JenkinsNewSegmentInfo jenkinsNewSegmentInfo = new JenkinsNewSegmentInfo();
	jenkinsNewSegmentInfo.setCommitId("commitId");
	assertEquals("commitId", jenkinsNewSegmentInfo.getCommitId());
	jenkinsNewSegmentInfo.setFileHashCode("code");
	assertEquals("code", jenkinsNewSegmentInfo.getFileHashCode());
	jenkinsNewSegmentInfo.setFileName("abcd.asm");
	assertEquals("abcd.asm", jenkinsNewSegmentInfo.getFileName());
	jenkinsNewSegmentInfo.setSourceUrl("url");
	assertEquals("url", jenkinsNewSegmentInfo.getSourceUrl());

    }

}
