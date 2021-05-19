package com.tsi.workflow.dao;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.OnlineBuild;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class OnlineBuildDAOTest {

    OnlineBuildDAO instance;

    @Before
    public void setUp() throws Exception {
	try {
	    OnlineBuildDAO buildDAO = new OnlineBuildDAO();
	    instance = Mockito.spy(buildDAO);
	    TestCaseMockService.doMockBaseDAO(OnlineBuildDAO.class, OnlineBuild.class, instance);
	} catch (Exception ex) {
	    Logger.getLogger(OnlineBuildDAOTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testDeveloperManagerService() throws Exception {
	TestCaseExecutor.doTestDAO(instance, OnlineBuildDAO.class);
    }

    @Test
    public void test() {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(OnlineBuildDAO.class);
	when(session.createCriteria(OnlineBuildDAO.class)).thenReturn(mockedCriteria);
	instance.findByImpPlan(DataWareHouse.getPlan());
	instance.findByImpPlan(new ArrayList<ImpPlan>());
	instance.findByImpPlan(DataWareHouse.getPlan().getId());
	instance.findByImpPlan(new String[20]);
	instance.findAll(DataWareHouse.getPlan(), com.tsi.workflow.utils.Constants.BUILD_TYPE.DVL_BUILD);
	instance.findBySystem(DataWareHouse.getSystemList().get(0).getId());
	instance.findBySystem(new Integer[20]);
	instance.findBySystem(DataWareHouse.getSystemList().get(0));
	instance.findBySystem(new ArrayList<System>());
	instance.findByBuild(DataWareHouse.getPlan().getId(), DataWareHouse.getSystemList().get(0), new Integer(324), com.tsi.workflow.utils.Constants.BUILD_TYPE.DVL_BUILD);
	instance.findLastBuild(DataWareHouse.getPlan().getId(), new ArrayList<System>(), com.tsi.workflow.utils.Constants.BUILD_TYPE.DVL_BUILD);
	instance.findOnlineBuildInProgress(DataWareHouse.getPlan(), com.tsi.workflow.utils.Constants.BUILD_TYPE.DVL_BUILD);
	instance.findAllSuccessBuildForPlan(DataWareHouse.getPlan(), com.tsi.workflow.utils.Constants.BUILD_TYPE.DVL_BUILD);
	instance.findLastBuildInProgress(DataWareHouse.getPlan().getId(), new ArrayList<System>(), com.tsi.workflow.utils.Constants.BUILD_TYPE.DVL_BUILD);
	instance.findBySystemAndType(DataWareHouse.getPlan(), DataWareHouse.getSystemList().get(0), com.tsi.workflow.utils.Constants.BUILD_TYPE.DVL_BUILD.name());
    }

}
