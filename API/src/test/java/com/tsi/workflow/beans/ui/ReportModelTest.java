// package com.tsi.workflow.beans.ui;
//
// import static org.junit.Assert.*;
//
// import org.junit.After;
// import org.junit.Before;
// import org.junit.Test;
//
// public class ReportModelTest {
//
// private ReportModel reportModel;
//
// @Before
// public void setUp() throws Exception {
// reportModel = new ReportModel();
// }
//
// @After
// public void tearDown() throws Exception {
// }
//
// @Test
// public void testReportModel() {
//
// reportModel.setFuncArea("FunctionalArea");
// assertEquals("FunctionalArea", reportModel.getFuncArea());
// reportModel.setPlanId("P1323");
// assertEquals("P1323", reportModel.getPlanId());
// reportModel.setPlanStatus("ONLINE");
// assertEquals("ONLINE", reportModel.getPlanStatus());
// reportModel.setSegmentsCount(43);
// assertEquals(new Integer(43), reportModel.getSegmentsCount());
// reportModel.setSoCount(543);
// assertEquals(new Integer(543), reportModel.getSoCount());
// reportModel.setSystemName("XYZ");
// assertEquals("XYZ", reportModel.getSystemName());
// reportModel.setUserName("User Name");
// assertEquals("User Name", reportModel.getUserName());
//
// ReportModel reportModel1 = new ReportModel(reportModel.getUserName(),
// reportModel.getPlanId(), reportModel.getPlanStatus(),
// reportModel.getSoCount());
//
// }
//
// }
