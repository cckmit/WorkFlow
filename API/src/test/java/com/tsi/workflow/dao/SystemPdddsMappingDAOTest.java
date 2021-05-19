package com.tsi.workflow.dao;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.SystemPdddsMapping;
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

public class SystemPdddsMappingDAOTest {

    SystemPdddsMappingDAO instance;

    public SystemPdddsMappingDAOTest() {
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
	    SystemPdddsMappingDAO realInstance = new SystemPdddsMappingDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(SystemPdddsMappingDAO.class, SystemPdddsMapping.class, instance);
	} catch (Exception ex) {
	    Logger.getLogger(SystemPdddsMappingDAOTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLegacyFallBackPlanDAO() throws Exception {
	TestCaseExecutor.doTestDAO(instance, SystemPdddsMappingDAO.class);
    }

    @Test
    public void testfindBySystemLoadId() {
	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));
	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(SystemPdddsMappingDAO.class);
	Mockito.when(session.createCriteria(SystemPdddsMappingDAO.class)).thenReturn(mockedCriteria);
	Integer[] ids = new Integer[] { 1, 2, 3 };
	instance.findBySystemLoadId(ids);
    }

    @Test
    public void findBySystemLoadId() throws Exception {
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	Query mockedQry = mock(Query.class);
	String lQuery = "SELECT system FROM SystemPdddsMapping system, PdddsLibrary pddds" + " WHERE system.pdddsLibraryId.id = pddds.id AND " + " system.systemLoadId.id = :systemLoadId AND " + " system.active = 'Y' ORDER BY system.id ASC";

	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.setParameter("systemLoadId", 1)).thenReturn(mockedQry);
	instance.findBySystemLoadId(1);
    }

}
