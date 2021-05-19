package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReportFormTest {

    private ReportForm reportForm;

    @Before
    public void setUp() throws Exception {
	reportForm = new ReportForm();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {

	reportForm.setEndDate(new Date(2019, 8, 19));
	assertEquals((new Date(2019, 8, 19)), reportForm.getEndDate());
	reportForm.setFuncAreas(new ArrayList<String>());
	assertNotNull(reportForm.getFuncAreas());
	reportForm.setReportType("Repo Type");
	assertEquals("Repo Type", reportForm.getReportType());
	reportForm.setRole("Role");
	assertEquals("Role", reportForm.getRole());
	reportForm.setStartDate(new Date(2019, 8, 19));
	assertEquals((new Date(2019, 8, 19)), reportForm.getStartDate());
	reportForm.setSystems(new ArrayList<String>());
	assertNotNull(reportForm.getSystems());
	reportForm.setUserIds(new ArrayList<String>());
	assertNotNull(reportForm.getUserIds());

    }

}
