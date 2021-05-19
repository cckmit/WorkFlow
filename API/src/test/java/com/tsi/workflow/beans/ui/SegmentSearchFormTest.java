package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SegmentSearchFormTest {

    private SegmentSearchForm segmentSearchForm;

    @Before
    public void setUp() throws Exception {
	segmentSearchForm = new SegmentSearchForm();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSegmentSearchForm() {
	segmentSearchForm.setActionList(new ArrayList<String>());
	assertNotNull(segmentSearchForm.getActionList());
	segmentSearchForm.setEndDate(new Date(2019, 5, 4));
	assertEquals(new Date(2019, 5, 4), segmentSearchForm.getEndDate());
	segmentSearchForm.setEnvironment("ENV");
	assertEquals("ENV", segmentSearchForm.getEnvironment());
	segmentSearchForm.setFunctionPackage(new ArrayList<String>());
	assertNotNull(segmentSearchForm.getFunctionPackage());
	segmentSearchForm.setProgramNames(new ArrayList<String>());
	assertNotNull(segmentSearchForm.getProgramNames());
	segmentSearchForm.setStartDate(new Date(2019, 5, 14));
	assertEquals(new Date(2019, 5, 14), segmentSearchForm.getStartDate());
	segmentSearchForm.setTargetSys(new ArrayList<Integer>());
	assertNotNull(segmentSearchForm.getTargetSys());
    }

}
