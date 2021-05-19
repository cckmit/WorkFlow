/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
 * @author deepa.jayakumar
 */
public class SequenceGeneratorTest {

    public SequenceGeneratorTest() {
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
     * Test of getNewImplementationId method, of class SequenceGenerator.
     */
    @Test
    public void testGetNewImplementationId() {
	String planId = "1";
	SequenceGenerator instance = new SequenceGenerator();
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);

	String lQuery = "CREATE SEQUENCE seq_1";
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);

	lQuery = "select nextval('seq_1') AS RESULT";
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	when(mockedQry.uniqueResult()).thenReturn(new Integer(1234));
	lQuery = "SELECT to_regclass('seq_1') AS result";
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	String result = instance.getNewImplementationId(planId);
	assertNotNull(result);

    }

    /**
     * Test of getNewImplementationPlanId method, of class SequenceGenerator.
     */
    @Test
    public void testGetNewImplementationPlanId() {

	String pPlatform = "TP";
	SequenceGenerator instance = new SequenceGenerator();
	SessionFactory sessionFactory = mock(SessionFactory.class);
	ReflectionTestUtils.setField(instance, "sessionFactory", sessionFactory);
	Session session = mock(Session.class);
	SQLQuery mockedQry = mock(SQLQuery.class);

	String lQuery = "CREATE SEQUENCE seq_TP_18";
	when(sessionFactory.getCurrentSession()).thenReturn(session);
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	lQuery = "select nextval('seq_TP_18') AS result";
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	lQuery = "SELECT to_regclass('seq_TP_18') AS result";
	when(session.createSQLQuery(lQuery)).thenReturn(mockedQry);
	String result = instance.getNewImplementationPlanId(pPlatform);
	assertNotNull(result);

    }

}
