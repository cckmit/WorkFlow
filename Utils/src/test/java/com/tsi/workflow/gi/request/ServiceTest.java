package com.tsi.workflow.gi.request;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ServiceTest {

    private Service service;

    @Before
    public void setUp() throws Exception {
	service = new Service();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetProjectCreateOrProjectAddDirRuleOrProjectAddFileRuleOrProjectRefreshOrProjectDeleteOrProjectsListOrProjectSetTargetOrFilesOpenFile() {
	assertNotNull(service.getProjectCreateOrProjectAddDirRuleOrProjectAddFileRuleOrProjectRefreshOrProjectDeleteOrProjectsListOrProjectSetTargetOrFilesOpenFile());
    }

}
