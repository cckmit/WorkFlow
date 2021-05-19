/// *
// * To change this license header, choose License Headers in Project
/// Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
// package com.tsi.workflow.helper;
//
// import com.jcraft.jsch.ChannelExec;
// import com.jcraft.jsch.JSchException;
// import com.jcraft.jsch.Session;
// import com.tsi.workflow.DataWareHouse;
// import com.tsi.workflow.GITConfig;
// import com.tsi.workflow.TestCaseExecutor;
// import com.tsi.workflow.TestCaseMockService;
// import com.tsi.workflow.User;
// import com.tsi.workflow.WFConfig;
// import com.tsi.workflow.base.MailMessageFactory;
// import com.tsi.workflow.beans.dao.SystemLoadActions;
// import com.tsi.workflow.dao.BuildDAO;
// import com.tsi.workflow.dao.ImpPlanDAO;
// import com.tsi.workflow.mail.DSLFileErrorMail;
// import com.tsi.workflow.utils.JSONResponse;
// import java.io.IOException;
// import java.io.InputStream;
// import java.util.concurrent.BlockingQueue;
// import java.util.logging.Level;
// import java.util.logging.Logger;
// import org.junit.After;
// import org.junit.AfterClass;
// import static org.junit.Assert.assertFalse;
// import static org.junit.Assert.assertTrue;
// import static org.junit.Assert.fail;
// import org.junit.Before;
// import org.junit.BeforeClass;
// import org.junit.Test;
// import static org.mockito.Matchers.any;
// import static org.mockito.Mockito.mock;
// import static org.mockito.Mockito.spy;
// import static org.mockito.Mockito.when;
// import org.springframework.test.util.ReflectionTestUtils;
//
/// **
// *
// * @author Radha.Adhimoolam
// */
// public class DSLFileHelperTest {
//
// DSLFileHelper instance;
//
// public DSLFileHelperTest() {
// }
//
// @BeforeClass
// public static void setUpClass() {
// }
//
// @AfterClass
// public static void tearDownClass() {
// }
//
// @Before
// public void setUp() {
// try {
// DSLFileHelper realInstance = new DSLFileHelper();
// instance = spy(realInstance);
// TestCaseMockService.doMockDAO(instance, BuildDAO.class);
// TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
// // TestCaseMockService.doMockDAO(instance, SSHUtil.class);
// TestCaseMockService.doMockDAO(instance, GITConfig.class);
// TestCaseMockService.doMockDAO(instance, WFConfig.class);
// // TestCaseMockService.doMockDAO(instance, DSLFileErrorMail.class);
//
// } catch (Exception ex) {
// Logger.getLogger(DSLFileHelperTest.class.getName()).log(Level.SEVERE, null,
/// ex);
// fail("Fail on Exception " + ex.getMessage());
// }
// }
//
// @After
// public void tearDown() {
// }
//
// @Test
// public void testDSLFileHelper() throws Exception {
// TestCaseExecutor.doTest(instance, DSLFileHelper.class);
// }
//
// /**
// * Test of updatePlanInfoInDSLFile method, of class DSLFileHelper.
// */
// @Test
// public void testUpdatePlanInfoInDSLFile() {
// try {
// User pUser = null;
// SystemLoadActions pLoadActions =
/// DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
// ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
// ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
// when(instance.getImpPlanDAO().find(pLoadActions.getPlanId().getId())).thenReturn(DataWareHouse.getPlan());
// ReflectionTestUtils.setField(instance, "mailMessageFactory",
/// mock(MailMessageFactory.class));
// ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue",
/// mock(BlockingQueue.class));
// when(instance.mailMessageFactory.getTemplate(DSLFileErrorMail.class)).thenReturn(mock(DSLFileErrorMail.class));
// String command = "${MTP_ENV}/mtptpfdslupdate t1700484_001 wsp vpwsp59
/// GO700484 20171030 0700";
// Session lSession = mock(Session.class);
// ChannelExec lChannel = mock(ChannelExec.class);
// // SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
// when(lSession.openChannel("exec")).thenReturn(lChannel);
// when(lChannel.getInputStream()).thenReturn(mock(InputStream.class));
// //
/// when(sshUtil.executeCommand(command)).thenReturn(DataWareHouse.getPositiveResponse());
// // doReturn(false).when(sshUtil).connectSSH(pLoadActions.getSystemId());
// // when(instance.getSSHUtil()).thenReturn(sshUtil);
// Boolean result = instance.updatePlanInfoInDSLFile(pUser, pLoadActions);
// // doReturn(true).when(sshUtil).connectSSH(pLoadActions.getSystemId());
// result = instance.updatePlanInfoInDSLFile(pUser, pLoadActions);
// assertFalse(result);
// try {
// JSONResponse lx = new JSONResponse();
// lx.setStatus(false);
// // doReturn(lx).when(sshUtil).executeCommand(any());
// MailMessageFactory mockMailMessageFactory = mock(MailMessageFactory.class);
// ReflectionTestUtils.setField(instance, "wFConfig", mock(WFConfig.class));
// DSLFileErrorMail mockDSLFileErrorMail = mock(DSLFileErrorMail.class);
// when(mockMailMessageFactory.getTemplate(any())).thenReturn(mockDSLFileErrorMail);
// when(instance.wFConfig.getDevCentreMailID()).thenReturn("abc@abc.com");
// result = instance.updatePlanInfoInDSLFile(pUser, pLoadActions);
// assertFalse(result);
// } catch (Exception e) {
// assertTrue(true);
// }
// } catch (Exception e) {
// // do nothing
// }
// }
//
// /**
// * Test of deletePlanInfoInDSLFile method, of class DSLFileHelper.
// */
// @Test
// public void testDeletePlanInfoInDSLFile() {
// try {
// User pUser = null;
// SystemLoadActions pLoadActions =
/// DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
// // SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
// // when(instance.getSSHUtil()).thenReturn(sshUtil);
// // doReturn(true).when(sshUtil).connectSSH(pLoadActions.getSystemId());
// Boolean result = instance.deletePlanInfoInDSLFile(pUser, pLoadActions);
// assertFalse(result);
// // case 2
// // doReturn(false).when(sshUtil).connectSSH(pLoadActions.getSystemId());
// result = instance.deletePlanInfoInDSLFile(pUser, pLoadActions);
// assertFalse(result);
// } catch (Exception e) {
// // do nothing
// }
// }
//
// @Test
// public void testDeletePlanInfoInDSLFile_Condition() {
// try {
// User pUser = null;
// SystemLoadActions pLoadActions =
/// DataWareHouse.getPlan().getSystemLoadActionsList().get(0);
// // SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
// // when(instance.getSSHUtil()).thenReturn(sshUtil);
// // doReturn(true).when(sshUtil).connectSSH(pLoadActions.getSystemId());
//
// JSONResponse lx = new JSONResponse();
// lx.setStatus(true);
// // doReturn(lx).when(sshUtil).executeCommand(any());
// Boolean result = instance.deletePlanInfoInDSLFile(pUser, pLoadActions);
// assertFalse(result);
// } catch (Exception e) {
// // do nothing
// }
// }
//
// }
