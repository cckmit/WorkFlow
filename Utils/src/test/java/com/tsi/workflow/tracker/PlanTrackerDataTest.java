package com.tsi.workflow.tracker;

import static org.junit.Assert.*;

import com.tsi.workflow.utils.Constants.TrackStatusForm;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class PlanTrackerDataTest {

    @Before
    public void setUp() throws Exception {
    }

    @Mock
    ImplementationTrackerData implementationTrackerData;

    @Test
    public void test() {
	PlanTrackerData planTrackerData = new PlanTrackerData();
	List<ImplementationTrackerData> data = new ArrayList<ImplementationTrackerData>();
	data.add(implementationTrackerData);
	planTrackerData.setImplementations(data);
	assertNotNull(planTrackerData.getImplementations());
	planTrackerData.setPlanId("T1900111");
	assertEquals("T1900111", planTrackerData.getPlanId());
	planTrackerData.setQaFuncBypassed(true);
	assertTrue(planTrackerData.isQaFuncBypassed());
	planTrackerData.setQaRegBypassed(true);
	assertTrue(planTrackerData.isQaRegBypassed());
	List<TrackStatusForm> list = new ArrayList<TrackStatusForm>();
	planTrackerData.setStages(list);
	assertNotNull(planTrackerData.getStages());

    }

}
