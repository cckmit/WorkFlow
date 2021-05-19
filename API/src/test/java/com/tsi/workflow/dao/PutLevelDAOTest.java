/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.dao;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;

import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.PutLevel;
import java.util.ArrayList;
import java.util.Date;
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
public class PutLevelDAOTest {

    PutLevelDAO instance;

    public PutLevelDAOTest() {
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
	    PutLevelDAO realInstance = new PutLevelDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(PutLevelDAO.class, PutLevel.class, instance);
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
	TestCaseExecutor.doTestDAO(instance, PutLevelDAO.class);
    }

    @Test
    public void testIsExists() {

	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));

	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(PutLevelDAO.class);
	Mockito.when(session.createCriteria(PutLevelDAO.class)).thenReturn(mockedCriteria);
	Mockito.when(mockedCriteria.uniqueResult()).thenReturn(new Long(1));
	PutLevel put = new PutLevel();
	put.setPutLevel("PUT13A");
	put.setPutDateTime(new Date());
	put.setSystemId(new com.tsi.workflow.beans.dao.System(1));
	put.setId(1);
	instance.isExists(new com.tsi.workflow.beans.dao.System(), "");
    }

    @Test
    public void testFindBySystemAndPutName() {

	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));

	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(PutLevelDAO.class);
	Mockito.when(session.createCriteria(PutLevelDAO.class)).thenReturn(mockedCriteria);
	instance.findBySystemAndPutName("123", 123);
    }

    @Test
    public void testFindByPutLevelandSystem() {

	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));

	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(PutLevelDAO.class);
	Mockito.when(session.createCriteria(PutLevelDAO.class)).thenReturn(mockedCriteria);
	instance.findByPutLevelandSystem("putLevel", new Integer(1), "putLevelStatus");
    }

    @Test
    public void testFindByStatus() {
	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));

	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(PutLevelDAO.class);
	Mockito.when(session.createCriteria(PutLevelDAO.class)).thenReturn(mockedCriteria);
	instance.findByStatus(new ArrayList<String>());
    }

    @Test
    public void testFindPutLevelBySys() {

	String lQuery = "SELECT put from PutLevel put" + " WHERE put.active = 'Y'" + " AND put.systemId.name = :system" + " AND put.status IN (:status)  " + " AND put.systemId.platformId.nickName = :Company";
	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));
	Session session = Mockito.mock(Session.class);
	Query mckedQuery = Mockito.mock(Query.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(session.createQuery(lQuery)).thenReturn(mckedQuery);
	String pSystem = "pSystem";
	Mockito.when(mckedQuery.setParameter("system", pSystem.toUpperCase().trim())).thenReturn(mckedQuery);
	String pCompany = "pCompany";
	Mockito.when(mckedQuery.setParameter("Company", pCompany.toLowerCase().trim())).thenReturn(mckedQuery);
	List<String> status = new ArrayList<String>();
	Mockito.when(mckedQuery.setParameterList("status", status)).thenReturn(mckedQuery);
	instance.findPutLevelBySys(pCompany, pSystem, status);
    }

    @Test
    public void testgetPutLevelList() {

	String lQueryString = "SELECT put FROM PutLevel put" + " WHERE put.active = 'Y'" + " AND put.status in (:status)" + " AND put.deploymentDateMailFlag = :mailFlag " + " AND put.putDateTime < :currentDate";
	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));
	Session session = Mockito.mock(Session.class);
	Query mckedQuery = Mockito.mock(Query.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(session.createQuery(lQueryString)).thenReturn(mckedQuery);
	Mockito.when(mckedQuery.setParameter("mailFlag", Boolean.FALSE)).thenReturn(mckedQuery);
	Mockito.when(mckedQuery.setParameter("currentDate", new Date())).thenReturn(mckedQuery);
	List<String> status = new ArrayList<String>();
	Mockito.when(mckedQuery.setParameterList("status", status)).thenReturn(mckedQuery);
	instance.getPutLevelList(status);
    }
}
