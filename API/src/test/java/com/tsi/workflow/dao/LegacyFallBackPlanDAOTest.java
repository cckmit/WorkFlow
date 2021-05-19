package com.tsi.workflow.dao;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;

import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.TestCaseMockService;
import com.tsi.workflow.beans.dao.LegacyFallBackPlan;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class LegacyFallBackPlanDAOTest {

    LegacyFallBackPlanDAO instance;

    public LegacyFallBackPlanDAOTest() {
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
	    LegacyFallBackPlanDAO realInstance = new LegacyFallBackPlanDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(LegacyFallBackPlanDAO.class, LegacyFallBackPlan.class, instance);
	} catch (Exception ex) {
	    Logger.getLogger(LegacyFallBackPlanDAOTest.class.getName()).log(Level.SEVERE, null, ex);
	    fail("Fail on Exception " + ex.getMessage());
	}
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testLegacyFallBackPlanDAO() throws Exception {
	TestCaseExecutor.doTestDAO(instance, LegacyFallBackPlanDAO.class);
    }

    @Test
    public void testFindByLegacyPlan() {

	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));

	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(LegacyFallBackPlanDAO.class);
	Mockito.when(session.createCriteria(LegacyFallBackPlanDAO.class)).thenReturn(mockedCriteria);
	instance.findByLegacyPlan(new LegacyFallBackPlan());
    }

    @Test
    public void testFindByLegacyPlanById() {

	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));

	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(LegacyFallBackPlanDAO.class);
	Mockito.when(session.createCriteria(LegacyFallBackPlanDAO.class)).thenReturn(mockedCriteria);
	instance.findByLegacyFallBackPlan(1);
    }

}
