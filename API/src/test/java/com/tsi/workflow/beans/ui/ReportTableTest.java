package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReportTableTest {

    private ReportTable reportTable;

    @Before
    public void setUp() throws Exception {
	reportTable = new ReportTable();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testReportTable() {
	reportTable.setSystemAndDetails(new ArrayList<ReportDetailView>());
	assertNotNull(reportTable.getSystemAndDetails());
	reportTable.setSystemAndSummaryDetails(new SummaryDetailView());
	assertNotNull(reportTable.getSystemAndSummaryDetails());
	reportTable.setSystemName("XYZ");
	assertEquals("XYZ", reportTable.getSystemName());
    }

}
