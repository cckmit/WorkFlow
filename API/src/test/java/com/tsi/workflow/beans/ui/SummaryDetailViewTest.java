package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SummaryDetailViewTest {

    private SummaryDetailView summaryDetailView;

    @Before
    public void setUp() throws Exception {
	summaryDetailView = new SummaryDetailView();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSummaryDetailView() {
	summaryDetailView.setAverageSuccessPerOnOnlineDeploy(78);
	assertEquals(new Integer(78), summaryDetailView.getAverageSuccessPerOnOnlineDeploy());
	summaryDetailView.setAverageSuccessPerOnSO(376);
	assertEquals(new Integer(376), summaryDetailView.getAverageSuccessPerOnSO());
	summaryDetailView.setSuccessPerFunc(44);
	assertEquals(new Integer(44), summaryDetailView.getSuccessPerFunc());
	summaryDetailView.setTotalDeployments(64);
	assertEquals(new Integer(64), summaryDetailView.getTotalDeployments());
	summaryDetailView.setTotalFallbackDeployments(89);
	assertEquals(new Integer(89), summaryDetailView.getTotalFallbackDeployments());
	summaryDetailView.setTotalFallbackSegments(76);
	assertEquals(new Integer(76), summaryDetailView.getTotalFallbackSegments());
	summaryDetailView.setTotalFallbackSharedObjects(45);
	assertEquals(new Integer(45), summaryDetailView.getTotalFallbackSharedObjects());
	summaryDetailView.setTotalFuncAreaCnt(75);
	assertEquals(new Integer(75), summaryDetailView.getTotalFuncAreaCnt());
	summaryDetailView.setTotalOnlineDeployments(976);
	assertEquals(new Integer(976), summaryDetailView.getTotalOnlineDeployments());
	summaryDetailView.setTotalOnlineSegments(86);
	assertEquals(new Integer(86), summaryDetailView.getTotalOnlineSegments());
	summaryDetailView.setTotalOnlineSharedObjects(98);
	assertEquals(new Integer(98), summaryDetailView.getTotalOnlineSharedObjects());
	summaryDetailView.setTotalSourceObjects(56);
	assertEquals(new Integer(56), summaryDetailView.getTotalSourceObjects());
	summaryDetailView.setTotalUsers(989);
	assertEquals(new Integer(989), summaryDetailView.getTotalUsers());
    }

}
