package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReportDetailViewTest {

    private ReportDetailView reportDetailView;

    @Before
    public void setUp() throws Exception {
	reportDetailView = new ReportDetailView();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testReportDetailView() {

	reportDetailView.setFuncArea("ASD");
	assertEquals("ASD", reportDetailView.getFuncArea());
	reportDetailView.setFuncAreaDesc("ADASD");
	assertEquals("ADASD", reportDetailView.getFuncAreaDesc());
	reportDetailView.setSuccessPerForDeployment(5);
	assertEquals(new Integer(5), reportDetailView.getSuccessPerForDeployment());
	reportDetailView.setSuccessPerForSourceObjects(9);
	assertEquals(new Integer(9), reportDetailView.getSuccessPerForSourceObjects());
	reportDetailView.setSuccessPerFunc(7);
	assertEquals(new Integer(7), reportDetailView.getSuccessPerFunc());
	reportDetailView.setTotalFallbackDeployments(23);
	assertEquals(new Integer(23), reportDetailView.getTotalFallbackDeployments());
	reportDetailView.setTotalFallbackSegmentsCount(54);
	assertEquals(new Integer(54), reportDetailView.getTotalFallbackSegmentsCount());
	reportDetailView.setTotalFallbackSharedObjects(45);
	assertEquals(new Integer(45), reportDetailView.getTotalFallbackSharedObjects());
	reportDetailView.setTotalOnlineDeployments(52);
	assertEquals(new Integer(52), reportDetailView.getTotalOnlineDeployments());
	reportDetailView.setTotalOnlineSegmentsCount(734);
	assertEquals(new Integer(734), reportDetailView.getTotalOnlineSegmentsCount());
	reportDetailView.setTotalOnlineSharedObjects(346);
	assertEquals(new Integer(346), reportDetailView.getTotalOnlineSharedObjects());
	reportDetailView.setTotalSharedObjects(783);
	assertEquals(new Integer(783), reportDetailView.getTotalSharedObjects());
	reportDetailView.setTotoalDeployments(432);
	assertEquals(new Integer(432), reportDetailView.getTotoalDeployments());
	reportDetailView.setUserName("User Name");
	assertEquals("User Name", reportDetailView.getUserName());
	reportDetailView.setSuccessPercentage();
	assertNotNull(reportDetailView.toString());

    }

}
