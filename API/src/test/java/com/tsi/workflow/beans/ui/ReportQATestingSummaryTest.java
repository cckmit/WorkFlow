package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReportQATestingSummaryTest {

    private ReportQATestingSummary reportQATestingSummary;

    @Before
    public void setUp() throws Exception {
	reportQATestingSummary = new ReportQATestingSummary();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
	reportQATestingSummary.setTotalQaFuncFallbackBypassedCnt(843);
	assertEquals(new Integer(843), reportQATestingSummary.getTotalQaFuncFallbackBypassedCnt());
	reportQATestingSummary.setTotalQaFuncFallbackPassedCnt(78);
	assertEquals(new Integer(78), reportQATestingSummary.getTotalQaFuncFallbackPassedCnt());
	reportQATestingSummary.setTotalQaFuncOnlineBypassedCnt(76);
	assertEquals(new Integer(76), reportQATestingSummary.getTotalQaFuncOnlineBypassedCnt());
	reportQATestingSummary.setTotalQaFuncOnlinePassedCnt(845);
	assertEquals(new Integer(845), reportQATestingSummary.getTotalQaFuncOnlinePassedCnt());
	reportQATestingSummary.setTotalQaRegFallbackBypassedCnt(7643);
	assertEquals(new Integer(7643), reportQATestingSummary.getTotalQaRegFallbackBypassedCnt());
	reportQATestingSummary.setTotalQaRegFallbackPassedCnt(64);
	assertEquals(new Integer(64), reportQATestingSummary.getTotalQaRegFallbackPassedCnt());
	reportQATestingSummary.setTotalQaRegOnlineBypassedCnt(643);
	assertEquals(new Integer(643), reportQATestingSummary.getTotalQaRegOnlineBypassedCnt());
	reportQATestingSummary.setTotalQaRegOnlinePassedCnt(762);
	assertEquals(new Integer(762), reportQATestingSummary.getTotalQaRegOnlinePassedCnt());
    }

}
