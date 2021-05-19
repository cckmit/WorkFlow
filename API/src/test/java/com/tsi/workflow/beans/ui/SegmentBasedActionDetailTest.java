package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.sql.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SegmentBasedActionDetailTest {

    private SegmentBasedActionDetail segmentBasedActionDetail;

    @Before
    public void setUp() throws Exception {
	segmentBasedActionDetail = new SegmentBasedActionDetail();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSegmentBasedActionDetail() {
	segmentBasedActionDetail.setCatid(34);
	assertEquals(34, segmentBasedActionDetail.getCatid());
	segmentBasedActionDetail.setCatname("CatName");
	assertEquals("CatName", segmentBasedActionDetail.getCatname());
	segmentBasedActionDetail.setCsrno("CSR894");
	assertEquals("CSR894", segmentBasedActionDetail.getCsrno());
	segmentBasedActionDetail.setDevmanagername("Devmanager Name");
	assertEquals("Devmanager Name", segmentBasedActionDetail.getDevmanagername());
	segmentBasedActionDetail.setDevname("DevName");
	assertEquals("DevName", segmentBasedActionDetail.getDevname());
	segmentBasedActionDetail.setEnvironment("ENV");
	assertEquals("ENV", segmentBasedActionDetail.getEnvironment());
	segmentBasedActionDetail.setFallbackdatetime(new Date(2019, 8, 19));
	assertEquals(new Date(2019, 8, 19), segmentBasedActionDetail.getFallbackdatetime());
	segmentBasedActionDetail.setFallbackloadsetname("FallBackLoadsetName");
	assertEquals("FallBackLoadsetName", segmentBasedActionDetail.getFallbackloadsetname());
	segmentBasedActionDetail.setFuncarea("FuncArea");
	assertEquals("FuncArea", segmentBasedActionDetail.getFuncarea());
	segmentBasedActionDetail.setId("ID");
	assertEquals("ID", segmentBasedActionDetail.getId());
	segmentBasedActionDetail.setImpid("IMP_ID");
	assertEquals("IMP_ID", segmentBasedActionDetail.getImpid());
	segmentBasedActionDetail.setImpplanactive("ImplPaln Active");
	assertEquals("ImplPaln Active", segmentBasedActionDetail.getImpplanactive());
	segmentBasedActionDetail.setLastactionstatus("Last Action Status");
	assertEquals("Last Action Status", segmentBasedActionDetail.getLastactionstatus());
	segmentBasedActionDetail.setLeadid("Lead ID");
	assertEquals("Lead ID", segmentBasedActionDetail.getLeadid());
	segmentBasedActionDetail.setLeadname("Lead Name");
	assertEquals("Lead Name", segmentBasedActionDetail.getLeadname());
	segmentBasedActionDetail.setLoadactive("Load Active");
	assertEquals("Load Active", segmentBasedActionDetail.getLoadactive());
	segmentBasedActionDetail.setLoadattendee("Load Attendee");
	assertEquals("Load Attendee", segmentBasedActionDetail.getLoadattendee());
	segmentBasedActionDetail.setLoaddatetime(new Date(2019, 8, 20));
	assertEquals(new Date(2019, 8, 20), segmentBasedActionDetail.getLoaddatetime());
	segmentBasedActionDetail.setLoadid(32);
	assertEquals(32, segmentBasedActionDetail.getLoadid());
	segmentBasedActionDetail.setLoadinstruction("Load Instruction");
	assertEquals("Load Instruction", segmentBasedActionDetail.getLoadinstruction());
	segmentBasedActionDetail.setLoadsetname("LoadsetName");
	assertEquals("LoadsetName", segmentBasedActionDetail.getLoadsetname());
	segmentBasedActionDetail.setLoadtype("Load Type");
	assertEquals("Load Type", segmentBasedActionDetail.getLoadtype());
	segmentBasedActionDetail.setModifieddate(new Date(2019, 9, 20));
	assertEquals(new Date(2019, 9, 20), segmentBasedActionDetail.getModifieddate());
	segmentBasedActionDetail.setPeerreviewer("Peer Reviewer");
	assertEquals("Peer Reviewer", segmentBasedActionDetail.getPeerreviewer());
	segmentBasedActionDetail.setPlanDesc(36);
	assertEquals(36, segmentBasedActionDetail.getPlanDesc());
	segmentBasedActionDetail.setPlanstatus("Plan Status");
	assertEquals("Plan Status", segmentBasedActionDetail.getPlanstatus());
	segmentBasedActionDetail.setPlatformid(34);
	assertEquals(34, segmentBasedActionDetail.getPlatformid());
	segmentBasedActionDetail.setPreid(87);
	assertEquals(87, segmentBasedActionDetail.getPreid());
	segmentBasedActionDetail.setPrelastactionstatus("Pre Last Transaction Status");
	assertEquals("Pre Last Transaction Status", segmentBasedActionDetail.getPrelastactionstatus());
	segmentBasedActionDetail.setPreproductionstatus("Pre Production Status");
	assertEquals("Pre Production Status", segmentBasedActionDetail.getPreproductionstatus());
	segmentBasedActionDetail.setProgramname("Program Name");
	assertEquals("Program Name", segmentBasedActionDetail.getProgramname());
	segmentBasedActionDetail.setProjectname("Project Name");
	assertEquals("Project Name", segmentBasedActionDetail.getProjectname());
	segmentBasedActionDetail.setQastatus("QA Status");
	assertEquals("QA Status", segmentBasedActionDetail.getQastatus());
	segmentBasedActionDetail.setRepodesc("Repo Desc");
	assertEquals("Repo Desc", segmentBasedActionDetail.getRepodesc());
	segmentBasedActionDetail.setSegid(856);
	assertEquals(856, segmentBasedActionDetail.getSegid());
	segmentBasedActionDetail.setSysid(54);
	assertEquals(54, segmentBasedActionDetail.getSysid());
	segmentBasedActionDetail.setSysname("SysName");
	assertEquals("SysName", segmentBasedActionDetail.getSysname());
	segmentBasedActionDetail.setSystemid(2);
	assertEquals(2, segmentBasedActionDetail.getSystemid());
	segmentBasedActionDetail.setTargetsystem("XYZ");
	assertEquals("XYZ", segmentBasedActionDetail.getTargetsystem());
	segmentBasedActionDetail.setPlandesc("PlanDesc");
	assertEquals("PlanDesc", segmentBasedActionDetail.getPlandesc());
	segmentBasedActionDetail.setProductionstatus("Productionstatus");
	assertEquals("Productionstatus", segmentBasedActionDetail.getProductionstatus());

    }

}
