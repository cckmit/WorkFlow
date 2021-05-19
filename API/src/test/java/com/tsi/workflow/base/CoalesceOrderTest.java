/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.base;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.Order;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deepa.jayakumar
 */
public class CoalesceOrderTest {

    public CoalesceOrderTest() {
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
     * Test of toSqlString method, of class CoalesceOrder.
     */
    @Test
    public void testToSqlString() {
	Criteria criteria = mock(Criteria.class);
	String[] properties = new String[] { "1", "2" };
	CriteriaQuery criteriaQuery = mock(CriteriaQuery.class);
	CoalesceOrder instance = new CoalesceOrder(true, properties);
	String expResult = "";

	when(criteriaQuery.getColumnsUsingProjection(criteria, "1")).thenReturn(new String[] { "1", "2" });
	when(criteriaQuery.getColumnsUsingProjection(criteria, "2")).thenReturn(new String[] { "1", "2" });
	SessionFactoryImplementor value = mock(SessionFactoryImplementor.class);
	when(criteriaQuery.getFactory()).thenReturn(value);
	try {
	    String result = instance.toSqlString(criteria, criteriaQuery);
	} catch (Exception e) {
	}
    }

    /**
     * Test of asc method, of class CoalesceOrder.
     */
    @Test
    public void testAsc() {
	String[] properties = null;
	Order result = CoalesceOrder.asc(properties);
	assertNotNull(result);
    }

    /**
     * Test of desc method, of class CoalesceOrder.
     */
    @Test
    public void testDesc() {
	String[] properties = null;
	Order result = CoalesceOrder.desc(properties);
	assertNotNull(result);
    }

}
