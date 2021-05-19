package com.tsi.workflow.tracker;

import static org.junit.Assert.*;

import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class ImplementationTrackerDataTest {

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void test() {
	ImplementationTrackerData implementationTrackerData = new ImplementationTrackerData();
	implementationTrackerData.setStages(Constants.ImpTrackStatus.getImpTrackStatus());
	assertNotNull(implementationTrackerData.getStages());
	implementationTrackerData.setCurrentStage(implementationTrackerData.getStages().get(0));
	assertEquals(implementationTrackerData.getStages().get(0), implementationTrackerData.getCurrentStage());
	implementationTrackerData.setDeveloperName("developerName");
	assertEquals("developerName", implementationTrackerData.getDeveloperName());
	implementationTrackerData.setImplementationId("T1900111_001");
	assertEquals("T1900111_001", implementationTrackerData.getImplementationId());
	List<String> messages = new ArrayList<String>();
	implementationTrackerData.setMessages(messages);
	assertNotNull(implementationTrackerData.getMessages());
    }

}
