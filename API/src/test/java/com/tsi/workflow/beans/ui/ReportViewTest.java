package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReportViewTest {

    private ReportView reportView;

    @Before
    public void setUp() throws Exception {
	reportView = new ReportView();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testReportView() {
	reportView.setReportForm(new ReportForm());
	assertNotNull(reportView.getReportForm());
	reportView.setReportTable(new ArrayList<ReportTable>());
	assertNotNull(reportView.getReportTable());
    }

}
