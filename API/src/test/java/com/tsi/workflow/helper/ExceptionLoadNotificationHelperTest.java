/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.mail.ExceptionLoadMail;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
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
public class ExceptionLoadNotificationHelperTest {

    public ExceptionLoadNotificationHelperTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of notifyDevelopers method, of class ExceptionLoadNotificationHelper.
     */
    @Test
    public void testNotifyDevelopers() {
	List<Object[]> temp = new ArrayList();
	Object[] tempList = new Object[5];
	tempList[0] = DataWareHouse.getPlan().getImplementationList().get(0).getId();
	tempList[1] = DataWareHouse.getPlan().getId();
	tempList[2] = DataWareHouse.getPlan().getLeadId();
	tempList[3] = DataWareHouse.getPlan().getImplementationList().get(0).getDevId();
	tempList[4] = "";
	temp.add(tempList);
	String pPlanId = "";
	Constants.PlanStatus status = Constants.PlanStatus.ACTIVE;
	ExceptionLoadNotificationHelper instance = new ExceptionLoadNotificationHelper();
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	// ReflectionTestUtils.setField(instance, "authenticator",
	// mock(LDAPAuthenticatorImpl.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	ReflectionTestUtils.setField(instance.mailMessageFactory, "mailQueue", mock(BlockingQueue.class));
	when(instance.mailMessageFactory.getTemplate(ExceptionLoadMail.class)).thenReturn(new ExceptionLoadMail());
	when(instance.impPlanDAO.getSameSegmentDevelopers(pPlanId, new ArrayList<>(Constants.PlanStatus.getNonProdStatusMap().keySet()))).thenReturn(temp);
	instance.notifyDevelopers(pPlanId, status);
    }

}
