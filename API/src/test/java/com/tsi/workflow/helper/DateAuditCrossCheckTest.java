/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.GITConfig;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.SystemLoadDAO;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class DateAuditCrossCheckTest {

    public DateAuditCrossCheckTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	try {
	    DateAuditCrossCheck realInstance = new DateAuditCrossCheck();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockDAO(instance, CheckoutSegmentsDAO.class);
	    TestCaseMockService.doMockDAO(instance, SystemLoadDAO.class);
	    TestCaseMockService.doMockDAO(instance, GITConfig.class);

	} catch (Exception ex) {
	    Logger.getLogger(DateAuditCrossCheckTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    DateAuditCrossCheck instance;

    @Test
    public void testDateAuditCrossCheck() {
	TestCaseExecutor.doTest(instance, DateAuditCrossCheck.class);
    }

    @Test
    public void testDateAutditForMigration() {
	try {
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	    ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	    // SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
	    SystemLoad lSystemLoad = DataWareHouse.getPlan().getSystemLoadList().get(0);
	    List<CheckoutSegments> lSegments = DataWareHouse.getPlan().getImplementationList().get(0).getCheckoutSegmentsList();
	    when(instance.checkoutSegmentsDAO.findBySystemLoad(lSystemLoad.getId())).thenReturn(lSegments);
	    when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan())).thenReturn(Arrays.asList(lSystemLoad));
	    String lCommand = Constants.SystemScripts.DATE_AUDIT_CROSS_CHECK.getScript() + " " + lSystemLoad.getSystemId().getName().toLowerCase() + " " + Constants.REX_DATEFORMAT.get().format(lSystemLoad.getLoadDateTime()) + " " + StringUtils.join(lSegments, ",");
	    lCommand = "${MTP_ENV}/mtptpfaudit apo 20171030 0700 lnklogc.h,lnklogc.h,ab.8,ab.8,ab.8,regex.h,resize_no_store_hash_fn_imps.hpp,qw30gi.mac,qw30gi.mac,qw30gi.mac,lnai.asm,lnai.asm,lnco.asm,lnco.asm,abm2.asm";

	    Session lSession = mock(Session.class);
	    ChannelExec lChannel = mock(ChannelExec.class);
	    when(lSession.openChannel("exec")).thenReturn(lChannel);
	    when(lChannel.getInputStream()).thenReturn(mock(InputStream.class));
	    // when(sshUtil.executeCommand(lCommand)).thenReturn(DataWareHouse.getPositiveResponse());
	    // doReturn(true).when(sshUtil).connectSSH(lSystemLoad.getSystemId());
	    // when(instance.getSSHUtil()).thenReturn(sshUtil);
	    instance.dateAutditForMigration(DataWareHouse.getUser(), DataWareHouse.getPlan());
	    JSONResponse response = DataWareHouse.getNegativeResponse();
	    response.setErrorMessage("XXX\nXXX\nXXX\nXXX");
	    // when(sshUtil.executeCommand(lCommand)).thenReturn(response);
	    instance.dateAutditForMigration(DataWareHouse.getUser(), DataWareHouse.getPlan());
	} catch (Exception e) {

	}
    }

    @Test
    public void testDateAutditForMigration1() {
	try {
	    ReflectionTestUtils.setField(instance, "systemLoadDAO", mock(SystemLoadDAO.class));
	    ReflectionTestUtils.setField(instance, "gITConfig", mock(GITConfig.class));
	    ReflectionTestUtils.setField(instance, "checkoutSegmentsDAO", mock(CheckoutSegmentsDAO.class));
	    // SSHUtil sshUtil = spy(new SSHUtil(mock(IGitConfig.class)));
	    SystemLoad lSystemLoad = DataWareHouse.getPlan().getSystemLoadList().get(0);
	    List<CheckoutSegments> lSegments = new ArrayList<CheckoutSegments>();
	    when(instance.getCheckoutSegmentsDAO().findBySystemLoad(lSystemLoad.getId())).thenReturn(lSegments);
	    when(instance.getSystemLoadDAO().findByImpPlan(DataWareHouse.getPlan())).thenReturn(Arrays.asList(lSystemLoad));
	    String lCommand = Constants.SystemScripts.DATE_AUDIT_CROSS_CHECK.getScript() + " " + lSystemLoad.getSystemId().getName().toLowerCase() + " " + Constants.REX_DATEFORMAT.get().format(lSystemLoad.getLoadDateTime()) + " " + StringUtils.join(lSegments, ",");
	    lCommand = "${MTP_ENV}/mtptpfaudit apo 20171030 0700 lnklogc.h,lnklogc.h,ab.8,ab.8,ab.8,regex.h,resize_no_store_hash_fn_imps.hpp,qw30gi.mac,qw30gi.mac,qw30gi.mac,lnai.asm,lnai.asm,lnco.asm,lnco.asm,abm2.asm";

	    Session lSession = mock(Session.class);
	    ChannelExec lChannel = mock(ChannelExec.class);
	    when(lSession.openChannel("exec")).thenReturn(lChannel);
	    when(lChannel.getInputStream()).thenReturn(mock(InputStream.class));
	    // when(sshUtil.executeCommand(lCommand)).thenReturn(DataWareHouse.getPositiveResponse());
	    // doReturn(true).when(sshUtil).connectSSH(lSystemLoad.getSystemId());
	    // when(instance.getSSHUtil()).thenReturn(sshUtil);
	    instance.dateAutditForMigration(DataWareHouse.getUser(), DataWareHouse.getPlan());
	    JSONResponse response = DataWareHouse.getNegativeResponse();
	    response.setErrorMessage("XXX\nXXX\nXXX\nXXX");
	    // when(sshUtil.executeCommand(lCommand)).thenReturn(response);
	    instance.dateAutditForMigration(DataWareHouse.getUser(), DataWareHouse.getPlan());
	} catch (Exception e) {

	}
    }
}
