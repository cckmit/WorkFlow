/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.dao.external;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.beans.external.ProblemTicket;
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
public class ProblemTicketDAOTest {

    public ProblemTicketDAOTest() {
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

    ProblemTicketDAO instance = spy(new ProblemTicketDAO());

    @Test
    public void testProblemTicketDAO() {
	instance.setDataSource(mock(DataSource.class));
	TestCaseExecutor.doTest(instance, ProblemTicketDAO.class);
    }

    @Test
    public void testGetProblemTicket() {
	JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
	ReflectionTestUtils.setField(instance, "jdbcTemplate", jdbcTemplate);
	String lQuery = "SELECT * FROM dbo.z_pr_validation WHERE ref_num = ?";
	when(jdbcTemplate.query(lQuery, new Object[] { "1" }, new BeanPropertyRowMapper(ProblemTicket.class))).thenReturn(new ArrayList());
	instance.getProblemTicket("1");
    }
}
