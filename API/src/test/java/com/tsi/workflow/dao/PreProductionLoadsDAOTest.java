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
import com.tsi.workflow.beans.dao.PreProductionLoads;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
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
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class PreProductionLoadsDAOTest {

    PreProductionLoadsDAO instance;

    public PreProductionLoadsDAOTest() {
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
	    PreProductionLoadsDAO realInstance = new PreProductionLoadsDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(PreProductionLoadsDAO.class, PreProductionLoads.class, instance);
	} catch (Exception ex) {
	    Logger.getLogger(ImplementationDAOTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    // @Test
    public void testDeveloperManagerService() throws Exception {
	TestCaseExecutor.doTestDAO(instance, PreProductionLoadsDAO.class);
    }

    @Test
    public void testfindByPlanId() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(PreProductionLoadsDAO.class);
	when(session.createCriteria(PreProductionLoadsDAO.class)).thenReturn(mockedCriteria);
	instance.findByPlanId(Arrays.asList(DataWareHouse.getPlan()));
    }

    @Test
    public void testfindBySystemId() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(PreProductionLoadsDAO.class);
	when(session.createCriteria(PreProductionLoadsDAO.class)).thenReturn(mockedCriteria);
	instance.findBySystemId(Arrays.asList(DataWareHouse.getSystemList().get(0)));
    }

    @Test
    public void testfindBySystemId1() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(PreProductionLoadsDAO.class);
	when(session.createCriteria(PreProductionLoadsDAO.class)).thenReturn(mockedCriteria);
	instance.findBySystemId((Integer[]) Arrays.asList(DataWareHouse.getSystemList().get(0).getId()).toArray());
    }

    @Test
    public void testfindBySystemLoadId() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(PreProductionLoadsDAO.class);
	when(session.createCriteria(PreProductionLoadsDAO.class)).thenReturn(mockedCriteria);
	instance.findBySystemLoadId(DataWareHouse.getPlan().getSystemLoadList());
    }

    @Test
    public void testfindByLoadset() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT prodLoad FROM PreProductionLoads prodLoad, SystemLoad load" + " WHERE prodLoad.systemLoadId = load.id" + " AND prodLoad.active LIKE 'Y'" + " AND load.active LIKE 'Y'" + " AND (load.loadSetName LIKE :loadset" + " OR load.fallbackLoadSetName LIKE :loadset)"
	// + " AND prodLoad.cpuId IS NULL"
		+ " AND prodLoad.id = :ID";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("loadset", "")).thenReturn(mockedQry);
	when(mockedQry.setParameter("ID", 0)).thenReturn(mockedQry);
	instance.findByLoadset(0, "");
    }

    @Test
    public void testFindByPlanId() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT prodLoad FROM ProductionLoads prodLoad" + " WHERE prodLoad.planId = :PlanId" + " AND prodLoad.active LIKE 'Y'" + " AND prodLoad.systemId = :systemId" + " AND prodLoad.cpuId IS NULL";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("PlanId", DataWareHouse.getPlan())).thenReturn(mockedQry);
	when(mockedQry.setParameter("systemId", DataWareHouse.getSystemList().get(0))).thenReturn(mockedQry);
	instance.findByPlanId(DataWareHouse.getPlan(), DataWareHouse.getSystemList().get(0));
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(PreProductionLoadsDAO.class);
	when(session.createCriteria(PreProductionLoadsDAO.class)).thenReturn(mockedCriteria);
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemLoadId", DataWareHouse.getPlan().getSystemLoadList().get(0));
	lFilter.put("active", "Y");
	when(instance.findAll(lFilter)).thenReturn(null);
	lFilter.clear();
	lFilter.put("systemId", DataWareHouse.getSystemList().get(0));
	lFilter.put("active", "Y");
	when(instance.findAll(lFilter)).thenReturn(null);
	lFilter.clear();
	lFilter.put("planId", DataWareHouse.getPlan());
	lFilter.put("active", "Y");
	when(instance.findAll(lFilter)).thenReturn(null);
    }

    @Test
    public void testfindBySystemLoadId1() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(PreProductionLoadsDAO.class);
	when(session.createCriteria(PreProductionLoadsDAO.class)).thenReturn(mockedCriteria);
	instance.findBySystemLoadId((Integer[]) Arrays.asList(DataWareHouse.getPlan().getSystemLoadList().get(0).getId()).toArray());
    }

    @Test
    public void testfindByPlanId1() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(PreProductionLoadsDAO.class);
	when(session.createCriteria(PreProductionLoadsDAO.class)).thenReturn(mockedCriteria);
	instance.findByPlanId((String[]) Arrays.asList(DataWareHouse.getPlan().getId()).toArray());
    }

    @Test
    public void testfindByPlanId2() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(PreProductionLoadsDAO.class);
	when(session.createCriteria(PreProductionLoadsDAO.class)).thenReturn(mockedCriteria);
	instance.findByPlanId(DataWareHouse.getPlan().getId());
    }

    @Test
    public void testfindByPlanId3() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(PreProductionLoadsDAO.class);
	when(session.createCriteria(PreProductionLoadsDAO.class)).thenReturn(mockedCriteria);
	try {
	    instance.findByPlanId(DataWareHouse.getPlan());
	} catch (Exception Ex) {
	    System.out.println(Ex);
	}

    }

    @Test
    public void testfindBySystemId2() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(PreProductionLoadsDAO.class);
	when(session.createCriteria(PreProductionLoadsDAO.class)).thenReturn(mockedCriteria);
	instance.findBySystemId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId().getId());
	instance.findBySystemId(DataWareHouse.getPlan().getSystemLoadList().get(0).getSystemId());
    }

    @Test
    public void testfindBySystemLoadId2() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(PreProductionLoadsDAO.class);
	when(session.createCriteria(PreProductionLoadsDAO.class)).thenReturn(mockedCriteria);
	instance.findBySystemLoadId(DataWareHouse.getPlan().getSystemLoadList().get(0).getId());
	instance.findBySystemLoadId(DataWareHouse.getPlan().getSystemLoadList().get(0));
    }

}
