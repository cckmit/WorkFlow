package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RepoReportViewTest {

    private RepoReportView repoReportView;

    @Before
    public void setUp() throws Exception {
	repoReportView = new RepoReportView();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRepoReportView() {
	repoReportView.setFileExtnReportForm(new FileExtnReportForm());
	assertNotNull(repoReportView.getFileExtnReportForm());
	repoReportView.setsystemAndUserDetails(new ArrayList<SegmentReportDetailView>());
	assertNotNull(repoReportView.getsystemAndUserDetails());
    }

}
