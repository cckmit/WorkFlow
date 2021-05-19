/// *
// * To change this license header, choose License Headers in Project
/// Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
// package com.tsi.workflow.schedular.tpftestsystemmaintenance;
//
// import com.tsi.workflow.DataWareHouse;
// import com.tsi.workflow.GITConfig;
// import com.tsi.workflow.TestCaseExecutor;
// import com.tsi.workflow.WFConfig;
// import com.tsi.workflow.dao.SystemDAO;
// import com.tsi.workflow.utils.Constants;
// import com.tsi.workflow.utils.JSONResponse;
// import java.util.Date;
// import java.util.concurrent.ConcurrentLinkedQueue;
// import org.junit.After;
// import org.junit.AfterClass;
// import static org.junit.Assert.assertTrue;
// import org.junit.Before;
// import org.junit.BeforeClass;
// import org.junit.Test;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.spy;
// import static org.mockito.Mockito.when;
//
/// **
// *
// * @author deepa.jayakumar
// */
// public class MaintenanceInitatorTest {
//
// static Boolean mtpServiceBoolean;
//
// public MaintenanceInitatorTest() {
// }
//
// @BeforeClass
// public static void setUpClass() {
// mtpServiceBoolean = Constants.MTPSERVICE_KEY_MAP;
// }
//
// @AfterClass
// public static void tearDownClass() {
// Constants.MTPSERVICE_KEY_MAP = mtpServiceBoolean;
// }
//
// @Before
// public void setUp() {
// }
//
// @After
// public void tearDown() {
// }
//
// /**
// * Test of getSystemDAO method, of class MaintenanceInitator.
// */
// @Test
// public void testGetSystemDAO() {
//
// MaintenanceInitator instance = new MaintenanceInitator();
// TestCaseExecutor.doTest(instance, MaintenanceInitator.class);
// }
//
// /**
// * Test of testSystemMaintenance method, of class MaintenanceInitator.
// */
// @Test
// public void testTestSystemMaintenance() {
// MaintenanceInitator instance = new MaintenanceInitator();
// instance.wFConfig = mock(WFConfig.class);
// instance.gITConfig = mock(GITConfig.class);
// instance.maintenanceHelper = mock(MaintenanceHelper.class);
// instance.systemDAO = mock(SystemDAO.class);
// ConcurrentLinkedQueue<TestSystemMaintenance> testSystemMaintenanceList = new
/// ConcurrentLinkedQueue<>();
// TestSystemMaintenance testSystemMaintenance = new TestSystemMaintenance();
// testSystemMaintenance.setSystem(DataWareHouse.getSystemList().get(0));
// testSystemMaintenance.setStartedDate(new Date());
// testSystemMaintenanceList.add(testSystemMaintenance);
// try {
// instance.testSystemMaintenanceList = testSystemMaintenanceList;
// when(instance.getSystemDAO().findAll()).thenReturn(DataWareHouse.getSystemList());
// instance.testSystemMaintenance();
// } catch (Exception e) {
// assertTrue(true);
// }
// }
//
// /**
// * Test of testSystemMaintenance method, of class MaintenanceInitator.
// */
// @Test
// public void testTestSystemMaintenance2() {
// try {
// MaintenanceInitator instance = spy(new MaintenanceInitator());
// instance.wFConfig = mock(WFConfig.class);
// instance.gITConfig = mock(GITConfig.class);
// instance.maintenanceHelper = mock(MaintenanceHelper.class);
// instance.systemDAO = mock(SystemDAO.class);
// ConcurrentLinkedQueue<TestSystemMaintenance> testSystemMaintenanceList = new
/// ConcurrentLinkedQueue<>();
// TestSystemMaintenance testSystemMaintenance = new TestSystemMaintenance();
// testSystemMaintenance.setSystem(DataWareHouse.getSystemList().get(0));
// testSystemMaintenance.setStartedDate(new Date());
// testSystemMaintenanceList.add(testSystemMaintenance);
// instance.testSystemMaintenanceList = testSystemMaintenanceList;
// when(instance.getSystemDAO().findAll()).thenReturn(DataWareHouse.getSystemList());
// // SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
// //
/// doReturn(true).when(sshUtil).connectSSH(DataWareHouse.getSystemList().get(0));
// // when(instance.getSSHUtil()).thenReturn(sshUtil);
// JSONResponse mockJSON = mock(JSONResponse.class);
// Constants.MTPSERVICE_KEY_MAP = Boolean.TRUE;
// instance.testSystemMaintenance();
// //
/// doReturn(true).when(sshUtil).connectSSH(DataWareHouse.getSystemList().get(0));
// JSONResponse lx = new JSONResponse();
// lx.setStatus(true);
// // doReturn(lx).when(sshUtil).executeCommand(any());
// // when(instance.getSSHUtil()).thenReturn(sshUtil);
// instance.testSystemMaintenance();
// } catch (Exception e) {
// // do nothing
// }
//
// }
// }
