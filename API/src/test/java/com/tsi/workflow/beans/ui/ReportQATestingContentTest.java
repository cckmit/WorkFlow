package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReportQATestingContentTest {

    private ReportQATestingContent reportQATestingContent;

    @Before
    public void setUp() throws Exception {
	reportQATestingContent = new ReportQATestingContent();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testReportQATestingContent() {
	reportQATestingContent.setDevManager("Dev Manager");
	assertEquals("Dev Manager", reportQATestingContent.getDevManager());
	reportQATestingContent.setQaFuncFallbackBypassedCnt(32);
	assertEquals(new Integer(32), reportQATestingContent.getQaFuncFallbackBypassedCnt());
	reportQATestingContent.setQaFuncFallbackPassedCnt(423);
	assertEquals(new Integer(423), reportQATestingContent.getQaFuncFallbackPassedCnt());
	reportQATestingContent.setQaFuncOnlineBypassedCnt(534);
	assertEquals(new Integer(534), reportQATestingContent.getQaFuncOnlineBypassedCnt());
	reportQATestingContent.setQaRegFallbackBypassedCnt(234);
	assertEquals(new Integer(234), reportQATestingContent.getQaRegFallbackBypassedCnt());
	reportQATestingContent.setQaRegFallbackPassedCnt(543);
	assertEquals(new Integer(543), reportQATestingContent.getQaRegFallbackPassedCnt());
	reportQATestingContent.setQaRegOnlineBypassedCnt(876);
	assertEquals(new Integer(876), reportQATestingContent.getQaRegOnlineBypassedCnt());
	reportQATestingContent.setQaRegOnlinePassedCnt(797);
	assertEquals(new Integer(797), reportQATestingContent.getQaRegOnlinePassedCnt());
	reportQATestingContent.setQaFuncOnlinePassedCnt(7697);
	assertEquals(new Integer(7697), reportQATestingContent.getQaFuncOnlinePassedCnt());

    }

}
