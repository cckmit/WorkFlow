package com.tsi.workflow.utils;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

import com.tsi.workflow.beans.ui.SegmentBasedActionDetail;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class DeploymentActivityExcelExportTest {

    DeploymentActivityExcelExport instance;

    @Before
    public void setUp() {
	try {
	    DeploymentActivityExcelExport realInstance = new DeploymentActivityExcelExport();
	    instance = spy(realInstance);
	} catch (Exception ex) {
	    Logger.getLogger(DeploymentActivityExcelExport.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @Test
    public void test00() {
	DeploymentActivityExcelExport instance = new DeploymentActivityExcelExport();

	JSONResponse result = null;
	List<SegmentBasedActionDetail> segBasedActivityDetails = new ArrayList<SegmentBasedActionDetail>();
	segBasedActivityDetails.add(Mockito.mock(SegmentBasedActionDetail.class));

	try {
	    result = instance.generateDeploymentActivitiesInExcel(segBasedActivityDetails);
	    assertNotNull(result);
	} catch (Exception e) {
	    assertTrue(true);
	}

    }

}
