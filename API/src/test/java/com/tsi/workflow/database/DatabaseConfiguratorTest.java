/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.database;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author deepa.jayakumar
 */
public class DatabaseConfiguratorTest {

    public DatabaseConfiguratorTest() {
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
     * Test of setDataSource method, of class DatabaseConfigurator.
     */
    // @Test
    // public void testSetDataSource() {
    // DatabaseConfigurator instance = new DatabaseConfigurator();
    // TestCaseExecutor.doTest(instance, DatabaseConfigurator.class);
    // }
    //
    // @Test
    // public void testDoesTableExist() {
    // DatabaseConfigurator instance = new DatabaseConfigurator();
    // ReflectionTestUtils.setField(instance, "jdbcTemplate",
    // mock(JdbcTemplate.class));
    // instance.doesTableExist("");
    // when(instance.jdbcTemplate.queryForObject("SELECT to_regclass('')",
    // String.class)).thenReturn("avs");
    // instance.doesTableExist("");
    // }
    //
    // @Test
    // public void testClearDatabase() throws SQLException {
    // DatabaseConfigurator realinstance = new DatabaseConfigurator();
    // DatabaseConfigurator instance = spy(realinstance);
    // ReflectionTestUtils.setField(instance, "jdbcTemplate",
    // mock(JdbcTemplate.class));
    // instance.doesTableExist("");
    // when(instance.jdbcTemplate.queryForObject("SELECT TABLE_NAME FROM
    // INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA
    // LIKE 'public' ORDER BY TABLE_NAME", String.class)).thenReturn("avs");
    // instance.clearDatabase();
    // }
}
