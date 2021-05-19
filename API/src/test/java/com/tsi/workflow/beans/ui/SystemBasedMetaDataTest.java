package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.sql.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SystemBasedMetaDataTest {
    private SystemBasedMetaData systemBasedMetaData;

    @Before
    public void setUp() throws Exception {
	systemBasedMetaData = new SystemBasedMetaData();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSystemBasedMetaData() {
	systemBasedMetaData.setAdlMail("ADLMailID");
	assertEquals("ADLMailID", systemBasedMetaData.getAdlMail());
	systemBasedMetaData.setAdlName("ADLName");
	assertEquals("ADLName", systemBasedMetaData.getAdlName());
	systemBasedMetaData.setApprover("Approver");
	assertEquals("Approver", systemBasedMetaData.getApprover());
	systemBasedMetaData.setApprovingManMail("ApprovingManMail");
	assertEquals("ApprovingManMail", systemBasedMetaData.getApprovingManMail());
	systemBasedMetaData.setApprovingManName("ApprovingManName");
	assertEquals("ApprovingManName", systemBasedMetaData.getApprovingManName());
	systemBasedMetaData.setCommitid("C8943");
	assertEquals("C8943", systemBasedMetaData.getCommitid());
	systemBasedMetaData.setDbcrname("DCBRName");
	assertEquals("DCBRName", systemBasedMetaData.getDbcrname());
	systemBasedMetaData.setDerivedObj("DerivedObj");
	assertEquals("DerivedObj", systemBasedMetaData.getDerivedObj());
	systemBasedMetaData.setDeveloperEmail("DeveloperEmail");
	assertEquals("DeveloperEmail", systemBasedMetaData.getDeveloperEmail());
	systemBasedMetaData.setDevelopername("DeveloperName");
	assertEquals("DeveloperName", systemBasedMetaData.getDevelopername());
	systemBasedMetaData.setDevid("DevID");
	assertEquals("DevID", systemBasedMetaData.getDevid());
	systemBasedMetaData.setDevmanager("DevManager");
	assertEquals("DevManager", systemBasedMetaData.getDevmanager());
	systemBasedMetaData.setDevmanagername("DevManagerName");
	assertEquals("DevManagerName", systemBasedMetaData.getDevmanagername());
	systemBasedMetaData.setFilename("FileName");
	assertEquals("FileName", systemBasedMetaData.getFilename());
	systemBasedMetaData.setFiletype("FileType");
	assertEquals("FileType", systemBasedMetaData.getFiletype());
	systemBasedMetaData.setFuncarea("FunArea");
	assertEquals("FunArea", systemBasedMetaData.getFuncarea());
	systemBasedMetaData.setImpid("ImplID");
	assertEquals("ImplID", systemBasedMetaData.getImpid());
	systemBasedMetaData.setLeadid("L78435");
	assertEquals("L78435", systemBasedMetaData.getLeadid());
	systemBasedMetaData.setLoadattendeeid("JHGD876");
	assertEquals("JHGD876", systemBasedMetaData.getLoadattendeeid());
	systemBasedMetaData.setLaodAttnName("LoadAttendeeName");
	assertEquals("LoadAttendeeName", systemBasedMetaData.getLaodAttnName());
	systemBasedMetaData.setLoadAttnMail("LoadattnMail");
	assertEquals("LoadattnMail", systemBasedMetaData.getLoadAttnMail());
	systemBasedMetaData.setLoadcategory("LoadCategory");
	assertEquals("LoadCategory", systemBasedMetaData.getLoadcategory());
	systemBasedMetaData.setLoaddatetime(new Date(2019, 6, 7));
	assertEquals(new Date(2019, 6, 7), systemBasedMetaData.getLoaddatetime());
	systemBasedMetaData.setLoadsettype("LoadSetType");
	assertEquals("LoadSetType", systemBasedMetaData.getLoadsettype());
	systemBasedMetaData.setLoadtype("LoadType");
	assertEquals("LoadType", systemBasedMetaData.getLoadtype());
	systemBasedMetaData.setManagerMail("ManagerMail");
	assertEquals("ManagerMail", systemBasedMetaData.getManagerMail());
	systemBasedMetaData.setManagerName("ManagerName");
	assertEquals("ManagerName", systemBasedMetaData.getManagerName());
	systemBasedMetaData.setPeerreviewerids("PeerReviewers ID");
	assertEquals("PeerReviewers ID", systemBasedMetaData.getPeerreviewerids());
	systemBasedMetaData.setPlandesc("Plan Desc");
	assertEquals("Plan Desc", systemBasedMetaData.getPlandesc());
	systemBasedMetaData.setPlanid("P76345");
	assertEquals("P76345", systemBasedMetaData.getPlanid());
	systemBasedMetaData.setPlanstatus("Plan Status");
	assertEquals("PLAN STATUS", systemBasedMetaData.getPlanstatus());
	systemBasedMetaData.setProgname("Program name");
	assertEquals("Program name", systemBasedMetaData.getProgname());
	systemBasedMetaData.setProjnumber("Project Number");
	assertEquals("Project Number", systemBasedMetaData.getProjnumber());
	systemBasedMetaData.setPrtktnum("PKRT Num");
	assertEquals("PKRT Num", systemBasedMetaData.getPrtktnum());
	systemBasedMetaData.setQabypassstatus("QAbypassStatus");
	assertEquals("QAbypassStatus", systemBasedMetaData.getQabypassstatus());
	systemBasedMetaData.setRepodetail("Repo Detail");
	assertEquals("Repo Detail", systemBasedMetaData.getRepodetail());
	systemBasedMetaData.setReviewerMail("Reviewer mail");
	assertEquals("Reviewer mail", systemBasedMetaData.getReviewerMail());
	systemBasedMetaData.setReviewerName("Reviewer Name");
	assertEquals("Reviewer Name", systemBasedMetaData.getReviewerName());
	systemBasedMetaData.setSdmtktnum("SDMTKNum");
	assertEquals("SDMTKNum", systemBasedMetaData.getSdmtktnum());
	systemBasedMetaData.setSrcObj("SrcObj");
	assertEquals("SrcObj", systemBasedMetaData.getSrcObj());
	systemBasedMetaData.setStatusrank(new BigInteger("8765"));
	assertEquals(new Integer("8765"), systemBasedMetaData.getStatusrank());
	systemBasedMetaData.setTargetsys("XYZ");
	assertEquals("XYZ", systemBasedMetaData.getTargetsys());
	assertNotNull(systemBasedMetaData.isPlanBypassedRegression());

    }

}
