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
import com.tsi.workflow.beans.dao.Dbcr;
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

/**
 *
 * @author USER
 */
public class DbcrDAOTest {

    DbcrDAO instance;

    public DbcrDAOTest() {
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
	    DbcrDAO realInstance = new DbcrDAO();
	    instance = spy(realInstance);
	    TestCaseMockService.doMockBaseDAO(DbcrDAO.class, Dbcr.class, instance);
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
	TestCaseExecutor.doTestDAO(instance, DbcrDAO.class);
    }

    @Test
    public void testfindByDBCRId() {
	ReflectionTestUtils.setField(instance, "sessionFactory", Mockito.mock(SessionFactory.class));
	Session session = Mockito.mock(Session.class);
	Criteria mockedCriteria = Mockito.mock(Criteria.class);
	Mockito.when(instance.getSessionFactory().getCurrentSession()).thenReturn(session);
	Mockito.when(instance.getDAOClass()).thenReturn(DbcrDAO.class);
	Mockito.when(session.createCriteria(DbcrDAO.class)).thenReturn(mockedCriteria);
	instance.findByDBCRId(1);
    }
}
