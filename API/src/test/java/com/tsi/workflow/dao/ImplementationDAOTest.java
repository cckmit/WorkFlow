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
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.utils.Constants;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
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
 * @author USER
 */
public class ImplementationDAOTest {

    ImplementationDAO instance;

    public ImplementationDAOTest() {
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
	    ImplementationDAO realInstance = new ImplementationDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(ImplementationDAO.class, Implementation.class, instance);
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
	TestCaseExecutor.doTestDAO(instance, ImplementationDAO.class);
    }

    @Test
    public void testFindImpByDeveloper() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	LinkedHashMap<String, String> pOrderBy = null;
	String filter = "";
	String lQuery = "SELECT imp FROM Implementation imp, ImpPlan ip" + " WHERE imp.devId = :DevId" + " AND imp.active = 'Y'" + " AND imp.planId.id = ip.id" + " AND ip.active = 'Y'" + " AND ip.planStatus IN (:PlanStatus)";
	if (pOrderBy == null) {
	    pOrderBy = new LinkedHashMap();
	}
	if (pOrderBy.isEmpty()) {
	    lQuery = lQuery + " ORDER BY imp.modifiedDt DESC, imp.createdDt DESC";
	} else {
	    lQuery = lQuery + " ORDER BY";
	    for (Map.Entry<String, String> lOrderBy : pOrderBy.entrySet()) {
		lQuery = lQuery + " " + "imp." + lOrderBy.getKey() + " " + lOrderBy.getValue() + ",";
	    }
	    lQuery = lQuery.substring(0, lQuery.length() - 1);
	}
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("DevId", DataWareHouse.getPlan().getImplementationList().get(0).getDevId())).thenReturn(mockedQry);
	when(mockedQry.setParameterList("PlanStatus", Arrays.asList(DataWareHouse.getPlan().getPlanStatus()))).thenReturn(mockedQry);
	when(mockedQry.setFirstResult(0 * 0)).thenReturn(mockedQry);
	when(mockedQry.setMaxResults(0)).thenReturn(mockedQry);
	ImplementationDAO mockImplementationDAO = mock(ImplementationDAO.class);
	when(mockImplementationDAO.findImpByDeveloper(DataWareHouse.getPlan().getImplementationList().get(0).getDevId(), Arrays.asList(DataWareHouse.getPlan().getPlanStatus()), 0, 0, pOrderBy, filter)).thenReturn(DataWareHouse.getPlan().getImplementationList());
	pOrderBy.put("active", "Y");
	lQuery = "SELECT imp FROM Implementation imp, ImpPlan ip" + " WHERE imp.devId = :DevId" + " AND imp.active = 'Y'" + " AND imp.planId.id = ip.id" + " AND ip.active = 'Y'" + " AND ip.planStatus IN (:PlanStatus)";
	if (pOrderBy.isEmpty()) {
	    lQuery = lQuery + " ORDER BY imp.modifiedDt DESC, imp.createdDt DESC";
	} else {
	    lQuery = lQuery + " ORDER BY";
	    for (Map.Entry<String, String> lOrderBy : pOrderBy.entrySet()) {
		lQuery = lQuery + " " + "imp." + lOrderBy.getKey() + " " + lOrderBy.getValue() + ",";
	    }
	    lQuery = lQuery.substring(0, lQuery.length() - 1);
	}

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("DevId", DataWareHouse.getPlan().getImplementationList().get(0).getDevId())).thenReturn(mockedQry);
	when(mockedQry.setParameterList("PlanStatus", Arrays.asList(DataWareHouse.getPlan().getPlanStatus()))).thenReturn(mockedQry);
	when(mockedQry.setFirstResult(0 * 0)).thenReturn(mockedQry);
	when(mockedQry.setMaxResults(0)).thenReturn(mockedQry);
	instance.findImpByDeveloper(DataWareHouse.getPlan().getImplementationList().get(0).getDevId(), Arrays.asList(DataWareHouse.getPlan().getPlanStatus()), 0, 0, pOrderBy, filter);
    }

    @Test
    public void testGetDevelopersBySegmentForDelete() {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	String filter = "";
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);

	String lQuery = "SELECT COUNT(imp) FROM Implementation imp, ImpPlan ip" + " WHERE imp.devId = :DevId" + " AND imp.active = 'Y'" + " AND imp.planId.id = ip.id" + " AND ip.active = 'Y'" + " AND ip.planStatus IN (:PlanStatus)";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("DevId", DataWareHouse.getPlan().getImplementationList().get(0).getDevId())).thenReturn(mockedQry);
	when(mockedQry.setParameterList("PlanStatus", Arrays.asList(DataWareHouse.getPlan().getPlanStatus()))).thenReturn(mockedQry);

	instance.countImpByDeveloper(DataWareHouse.getPlan().getImplementationList().get(0).getDevId(), Arrays.asList(DataWareHouse.getPlan().getPlanStatus()), filter);
    }

    @Test
    public void testFind() throws Exception {
	ReflectionTestUtils.setField(instance, "sessionFactory", mock(SessionFactory.class));
	Session session = mock(Session.class);
	Criteria mockedCriteria = mock(Criteria.class);
	when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	when(instance.getDAOClass()).thenReturn(ImplementationDAO.class);
	when(session.createCriteria(ImplementationDAO.class)).thenReturn(mockedCriteria);
	instance.findById("", 0, 0, null);
	instance.find(Arrays.asList(""));
	instance.findByImpPlan(DataWareHouse.getPlan());
	instance.findByReviewer("", 0, 0, null);
    }

    @Test
    public void testfindBySystemLoadId() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);
	String lQuery = "SELECT impl.plan_id from implementation impl, imp_plan plan" + " WHERE impl.substatus = '" + Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name() + "'" + " AND impl.active = 'Y'" + " AND plan.active = 'Y'" + " AND plan.id = impl.plan_id" + " AND plan.plan_status = '" + Constants.PlanStatus.ACTIVE.name() + "'" + " AND impl.tkt_num is not null";
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	instance.findAllReviewPlans();
    }
}
