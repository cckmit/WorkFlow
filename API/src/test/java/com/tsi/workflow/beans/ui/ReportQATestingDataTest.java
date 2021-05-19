package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReportQATestingDataTest {

    private ReportQATestingData reportQATestingData;

    @Before
    public void setUp() throws Exception {
	reportQATestingData = new ReportQATestingData();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testReportQATestingData() {

	reportQATestingData.setDetailData(new ArrayList<ReportQATestingContent>());
	assertNotNull(reportQATestingData.getDetailData());
	reportQATestingData.setReportForm(new ReportForm());
	assertNotNull(reportQATestingData.getReportForm());
	reportQATestingData.setSummaryData(new ReportQATestingSummary());
	assertNotNull(reportQATestingData.getSummaryData());

    }

}
