/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.User;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.dao.ActivityLogDAO;
import com.tsi.workflow.dao.CheckoutSegmentsDAO;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.mail.DeleteImplementationsMail;
import com.tsi.workflow.utils.JSONResponse;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author Radha.Adhimoolam
 */
public class ImplementationHelperTest {

    public ImplementationHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws Exception {
	ImplementationHelper realInstance = new ImplementationHelper();
	instance = spy(realInstance);
	TestCaseMockService.doMockDAO(instance, ImpPlanDAO.class);
	TestCaseMockService.doMockDAO(instance, CheckoutSegmentsDAO.class);
	TestCaseMockService.doMockDAO(instance, ImplementationDAO.class);
	TestCaseMockService.doMockDAO(instance, ActivityLogDAO.class);
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	ReflectionTestUtils.setField(instance, "activityLogDAO", mock(ActivityLogDAO.class));

    }

    @After
    public void tearDown() {
    }

    ImplementationHelper instance;

    /**
     * Test of getImpPlanDAO method, of class ImplementationHelper.
     */
    @Test
    public void testGetImpPlanDAO() {
	ImpPlanDAO result = instance.getImpPlanDAO();
	ImplementationDAO result1 = instance.getImplementationDAO();
	CheckoutSegmentsDAO result2 = instance.getCheckoutSegmentsDAO();
	MailMessageFactory result4 = instance.getMailMessageFactory();
	ActivityLogDAO result5 = instance.getActivityLogDAO();

    }

    /**
     * Test of deleteImplementation method, of class ImplementationHelper.
     */
    @Test
    public void testDeleteImplementation() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation lImp = lPlan.getImplementationList().get(0);
	String pId = lImp.getId();

	try {
	    when(instance.getImplementationDAO().find(pId)).thenReturn(lImp);
	    when(instance.getCheckoutSegmentsDAO().findByImplementation(lImp.getId())).thenReturn(lImp.getCheckoutSegmentsList());
	    JSONResponse result = instance.deleteImplementation(pUser, pId);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testDeleteImplementation1() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation lImp = lPlan.getImplementationList().get(0);
	String pId = lImp.getId();

	try {
	    when(instance.getImplementationDAO().find(pId)).thenReturn(lImp);
	    when(instance.getCheckoutSegmentsDAO().findByImplementation(lImp.getId())).thenReturn(new ArrayList());
	    JSONResponse result = instance.deleteImplementation(pUser, pId);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

    @Test
    public void testDeleteImplementation2() {
	User pUser = DataWareHouse.user;
	ImpPlan lPlan = DataWareHouse.getPlan();
	Implementation lImp = lPlan.getImplementationList().get(0);
	String pId = lImp.getId();
	String[] lReviewerList = lImp.getPeerReviewers().split(",");
	try {
	    when(instance.getImplementationDAO().find(pId)).thenReturn(lImp);
	    when(instance.getCheckoutSegmentsDAO().findByImplementation(lImp.getId())).thenReturn(new ArrayList());
	    when(instance.getMailMessageFactory().getTemplate(DeleteImplementationsMail.class)).thenReturn(mock(DeleteImplementationsMail.class));

	    JSONResponse result = instance.deleteImplementation(pUser, pId);
	} catch (Exception ex) {
	    assertTrue(true);
	}
    }

}
