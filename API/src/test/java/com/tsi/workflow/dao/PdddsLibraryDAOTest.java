package com.tsi.workflow.dao;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.PdddsLibrary;
import com.tsi.workflow.beans.dao.System;
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

public class PdddsLibraryDAOTest {

    PdddsLibraryDAO instance;

    public PdddsLibraryDAOTest() {
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
	    PdddsLibraryDAO realInstance = new PdddsLibraryDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(PdddsLibraryDAO.class, PdddsLibrary.class, instance);
	} catch (Exception ex) {
	    Logger.getLogger(PdddsLibraryDAOTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLegacyFallBackPlanDAO() throws Exception {
	TestCaseExecutor.doTestDAO(instance, PdddsLibraryDAO.class);
    }

    @Test
    public void testfindBySystem() {

	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));

	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(PdddsLibraryDAO.class);
	Mockito.when(session.createCriteria(PdddsLibraryDAO.class)).thenReturn(mockedCriteria);
	instance.findBySystem(new System());
    }

    @Test
    public void testFindByLegacyPlanById() {

	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));

	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(PdddsLibraryDAO.class);
	Mockito.when(session.createCriteria(PdddsLibraryDAO.class)).thenReturn(mockedCriteria);
	List<Integer> intobj = new ArrayList<Integer>();
	intobj.add(1);
	instance.findByIds(intobj);
    }

    @Test
    public void testfindBySystemLoadId() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT pddds FROM SystemPdddsMapping system, PdddsLibrary pddds" + " WHERE system.pdddsLibraryId.id = pddds.id AND " + " system.systemLoadId.id = :systemLoadId AND " + " system.active = 'Y' AND  pddds.active = 'Y' ORDER BY system.id ASC";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("systemLoadId", 1)).thenReturn(mockedQry);
	instance.findBySystemLoadId(1);
    }

}
