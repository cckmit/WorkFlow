/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.schedular;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tsi.workflow.base.MailMessageFactory;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.dao.ImpPlanDAO;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.helper.RejectHelper;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author yeshwanth.shenoy
 */
public class LoadWindowMonitorTest {

    public LoadWindowMonitorTest() {
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
     * Test of LoadWindowCheckProcess method, of class LoadWindowMonitor.
     */
    @Test
    public void testLoadWindowCheckProcess() {

	LoadWindowMonitor instance = new LoadWindowMonitor();
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "rejectHelper", mock(RejectHelper.class));
	when(instance.impPlanDAO.getSecuredPassedLoadDate(new ArrayList(Constants.PlanStatus.getAfterSubmitStatus().keySet()))).thenReturn(new HashMap<String, Integer>());
	instance.LoadWindowCheckProcess();
    }

    @Test
    public void testLoadWindowCheckProcess1() {

	LoadWindowMonitor instance = new LoadWindowMonitor();
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "rejectHelper", mock(RejectHelper.class));
	when(instance.impPlanDAO.getSecuredPassedLoadDate(new ArrayList(Constants.PlanStatus.getAfterSubmitStatus().keySet()))).thenReturn(new HashMap<String, Integer>());
	instance.LoadWindowCheckProcess();
    }

    @Test
    public void testLoadWindowCheckProcess2() {

	List<ImpPlan> mockList = new ArrayList<>();
	ImpPlan mockUser1 = Mockito.mock(ImpPlan.class);
	mockList.add(mockUser1);
	LoadWindowMonitor instance = new LoadWindowMonitor();
	ReflectionTestUtils.setField(instance, "impPlanDAO", mock(ImpPlanDAO.class));
	ReflectionTestUtils.setField(instance, "rejectHelper", mock(RejectHelper.class));
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	ReflectionTestUtils.setField(instance, "mailMessageFactory", mock(MailMessageFactory.class));
	when(instance.impPlanDAO.getImpPlanFromLoadDateTime(new ArrayList(Constants.PlanStatus.getNonProdStatusMap().keySet()))).thenReturn(mockList);
	try {
	    instance.loadWindowPassedMailProccess();
	} catch (Exception e) {

	}
    }
}
