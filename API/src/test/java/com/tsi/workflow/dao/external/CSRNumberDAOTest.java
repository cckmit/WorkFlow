/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.dao.external;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.beans.dao.Project;
import java.util.ArrayList;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class CSRNumberDAOTest {

    public CSRNumberDAOTest() {
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

    CSRNumberDAO instance = spy(new CSRNumberDAO());

    @Test
    public void testCSRNumber() {
	TestCaseExecutor.doTest(instance, CSRNumberDAO.class);
    }

    @Test
    public void testFindFiltered() {
	JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
	ReflectionTestUtils.setField(instance, "jdbcTemplate", jdbcTemplate);
	String lQuery = "SELECT TOP 50" + " CONCAT(\"CSR Number\",' ',\"Product Number\") AS projectNumber," + " ProjectName AS projectName,"
	// + " \"Project/Investment Unique ID\" AS sponsorId,"
		+ " ProjectOwnerName AS managerId," + " Portfolio_OBS AS lineOfBusiness" + " FROM dbo.MSP_EpmProject_UserView" + " WHERE" + " \"CSR Number\" is not NULL" + " AND \"Project Status\" LIKE 'Active'" + " ORDER BY \"CSR Number\"";
	when(jdbcTemplate.query(lQuery, new BeanPropertyRowMapper(Project.class))).thenReturn(new ArrayList());
	assertNotNull(instance.findFiltered("1"));
	lQuery = "SELECT TOP 50" + " CONCAT(\"CSR Number\",' ',\"Product Number\") AS projectNumber," + " ProjectName AS projectName,"
	// + " \"Project/Investment Unique ID\" AS sponsorId,"
		+ " ProjectOwnerName AS managerId," + " Portfolio_OBS AS lineOfBusiness" + " FROM dbo.MSP_EpmProject_UserView" + " WHERE" + " \"CSR Number\" is not NULL" + " AND \"Project Status\" LIKE 'Active'" + " AND \"CSR Number\" LIKE '%'" + " ORDER BY \"CSR Number\"";
	when(jdbcTemplate.query(lQuery, new BeanPropertyRowMapper(Project.class))).thenReturn(new ArrayList());
	assertNotNull(instance.findFiltered(""));
    }

    @Test
    public void testFindAll() {
	JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
	ReflectionTestUtils.setField(instance, "jdbcTemplate", jdbcTemplate);
	String lQuery = "SELECT" + " CONCAT(\"CSR Number\",' ',\"Product Number\") AS projectNumber," + " ProjectName AS projectName,"
	// + " \"Project/Investment Unique ID\" AS sponsorId,"
		+ " ProjectManagerName AS managerId," + " Portfolio_OBS AS lineOfBusiness" + " FROM dbo.MSP_EpmProject_UserView" + " WHERE" + " \"CSR Number\" is not NULL" + " AND \"Project Status\" LIKE 'Active'" + " ORDER BY \"CSR Number\"";
	when(jdbcTemplate.query(lQuery, new BeanPropertyRowMapper(Project.class))).thenReturn(new ArrayList());
	assertNotNull(instance.findAll());
	instance.getJdbcTemplate();
	instance.setDataSource(mock(DataSource.class));

    }
}
