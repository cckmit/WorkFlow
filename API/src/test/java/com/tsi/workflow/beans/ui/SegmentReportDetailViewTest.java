package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SegmentReportDetailViewTest {

    private SegmentReportDetailView segmentReportDetailView;

    @Before
    public void setUp() throws Exception {
	segmentReportDetailView = new SegmentReportDetailView();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSegmentReportDetailView() {
	segmentReportDetailView.setProgramName("Program Name");
	assertEquals("Program Name", segmentReportDetailView.getProgramName());
	segmentReportDetailView.setTotalActiveSegCount(43);
	assertEquals(new Integer(43), segmentReportDetailView.getTotalActiveSegCount());
	segmentReportDetailView.setTotalAllCount(23);
	assertEquals(new Integer(23), segmentReportDetailView.getTotalAllCount());
	segmentReportDetailView.setTotalSecuredDeployments(87);
	assertEquals(new Integer(87), segmentReportDetailView.getTotalSecuredDeployments());
	segmentReportDetailView.setTotoalOnlineDeployments(123);
	assertEquals(new Integer(123), segmentReportDetailView.getTotoalOnlineDeployments());
	assertNotNull(segmentReportDetailView.toString());
    }

}
