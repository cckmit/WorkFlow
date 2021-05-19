/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.dao;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.Build;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.utils.Constants;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author USER
 */
public class BuildDAOTest {

    BuildDAO instance;

    public BuildDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    private SessionFactory sessionFactory;
    private Session session;

    @Before
    public void setUp() {
	try {
	    BuildDAO realInstance = new BuildDAO();
	    instance = spy(realInstance);
	    sessionFactory = mock(SessionFactory.class);
	    session = mock(Session.class);
	    ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	    when(sessionFactory.getCurrentSession()).thenReturn(session);
	    TestCaseMockService.doMockBaseDAO(BuildDAO.class, Build.class, instance);
	} catch (Exception ex) {
	    Logger.getLogger(ImplementationDAOTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDeveloperManagerService() throws Exception {
	TestCaseExecutor.doTestDAO(instance, BuildDAO.class);
    }

    @Test
    public void testFindByBuild1() {
	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));
	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(BuildDAO.class);
	Mockito.when(session.createCriteria(BuildDAO.class)).thenReturn(mockedCriteria);
	instance.findByBuild(DataWareHouse.getPlan().getId(), new System(), Integer.MIN_VALUE, Constants.BUILD_TYPE.STG_CWS);
    }

    @Test
    public void findBuildWithPlanAndSystem() {
	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));
	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(BuildDAO.class);
	Mockito.when(session.createCriteria(BuildDAO.class)).thenReturn(mockedCriteria);
	List<System> lSystem = new ArrayList<>();
	instance.findBuildWithPlanAndSystem(DataWareHouse.getPlan().getId(), lSystem, Constants.BUILD_TYPE.STG_CWS);
    }

    @Test
    public void testFindLastSuccessfulBuild1() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT build FROM Build build, ImpPlan plan, System sys " + " WHERE build.planId.id = :PlanId" + " AND build.systemId.id = :SystemId" + " AND build.active LIKE 'Y'" + " AND build.jobStatus LIKE 'S'" + " AND build.buildType = :BuildType" + " AND plan.id = :PlanId" + " AND sys.id = :SystemId ORDER BY build.id DESC";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("PlanId", DataWareHouse.getPlan().getId())).thenReturn(mockedQry);
	when(mockedQry.setParameter("SystemId", 1)).thenReturn(mockedQry);
	when(mockedQry.setParameter("BuildType", Constants.BUILD_TYPE.STG_CWS.name())).thenReturn(mockedQry);
	when(mockedQry.setMaxResults(1)).thenReturn(mockedQry);
	instance.findLastSuccessfulBuild(DataWareHouse.getPlan().getId(), 1, Constants.BUILD_TYPE.STG_CWS);
    }

    @Test
    public void testgetBuildInProgressPlan() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT b.planId.id FROM Build b" + " WHERE b.planId.id IN (:PlanIdList)" + " AND b.jobStatus = 'P'" + " AND b.buildType IN (:BuildType)" + " AND active = 'Y'";
	List<String> pPlanId = new ArrayList<>();
	pPlanId.add("T1700484");
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("PlanIdList", pPlanId)).thenReturn(mockedQry);
	when(mockedQry.setParameterList("BuildType", new ArrayList(Constants.BUILD_TYPE.getStagingBuildType().keySet()))).thenReturn(mockedQry);
	instance.getBuildInProgressPlan(pPlanId, new ArrayList(Constants.BUILD_TYPE.getStagingBuildType().keySet()));
    }
}
