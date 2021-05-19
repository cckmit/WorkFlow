/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.dao.external;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.User;
import com.tsi.workflow.beans.ui.YodaResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class TestSystemLoadDAOTest {

    public TestSystemLoadDAOTest() {
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

    TestSystemLoadDAO instance = spy(new TestSystemLoadDAO());

    @Test
    public void testTestSystemLoadDAO() {
	TestCaseExecutor.doTest(instance, TestSystemLoadDAO.class);
    }

    @Test
    public void testSetDataSource() {
	instance.setDataSource(mock(DataSource.class));
    }

    // @Test
    public void testGetVparsIP() {

	JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
	SimpleJdbcCall jdbcCall = mock(SimpleJdbcCall.class);
	MapSqlParameterSource lParams = new MapSqlParameterSource();
	lParams.addValue("lab", "test");
	Map<String, Object> lResult = new HashMap();
	lResult.put("#result-set-1", new YodaResult());
	when(jdbcTemplate.getDataSource()).thenReturn(mock(DataSource.class));
	ReflectionTestUtils.setField(instance, "jdbcTemplate", jdbcTemplate);
	instance.setDataSource(mock(DataSource.class));

	// when(jdbcCall.withProcedureName("usp_tpf_linux_test_getip")).thenReturn(jdbcCall);
	// when(jdbcCall.withProcedureName("usp_tpf_linux_test_getip").returningResultSet("#result-set-1",
	// BeanPropertyRowMapper.newInstance(YodaResult.class))).thenReturn(jdbcCall);
	// when(jdbcCall.execute(lParams)).thenReturn(lResult);
	instance.getVparsIP("test");
    }

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	PGPoolingDataSource pGPoolingDataSource0 = new PGPoolingDataSource();
	testSystemLoadDAO0.setDataSource(pGPoolingDataSource0);
	JdbcTemplate jdbcTemplate0 = testSystemLoadDAO0.getJdbcTemplate();
	assertEquals((-1), jdbcTemplate0.getMaxRows());
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	// Undeclared exception!
	try {
	    testSystemLoadDAO0.setDataSource((DataSource) null);
	    fail("Expecting exception: IllegalArgumentException");

	} catch (IllegalArgumentException e) {
	    //
	    // Property 'dataSource' is required
	    //
	}
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	PGPoolingDataSource pGPoolingDataSource0 = new PGPoolingDataSource();
	testSystemLoadDAO0.setDataSource(pGPoolingDataSource0);
	// Undeclared exception!
	try {
	    testSystemLoadDAO0.loadAndActivate((User) null, "OK", "usp_tpf_linux_test_deactivate");
	    fail("Expecting exception: RuntimeException");

	} catch (RuntimeException e) {
	    //
	    // Error retrieving database metadata; nested exception is
	    // org.evosuite.runtime.mock.java.lang.MockThrowable: Could not get Connection
	    // for extracting meta data
	    //
	}
    }

    // @Test(timeout = 4000)
    public void test03() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	PGPoolingDataSource pGPoolingDataSource0 = new PGPoolingDataSource();
	testSystemLoadDAO0.setDataSource(pGPoolingDataSource0);
	// Undeclared exception!
	try {
	    testSystemLoadDAO0.getVparsIP((String) null);
	    fail("Expecting exception: RuntimeException");

	} catch (RuntimeException e) {
	    //
	    // Error retrieving database metadata; nested exception is
	    // org.evosuite.runtime.mock.java.lang.MockThrowable: Could not get Connection
	    // for extracting meta data
	    //
	}
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	PGPoolingDataSource pGPoolingDataSource0 = new PGPoolingDataSource();
	testSystemLoadDAO0.setDataSource(pGPoolingDataSource0);
	User user0 = new User();
	// Undeclared exception!
	try {
	    testSystemLoadDAO0.deleteAndDeActivate(user0, "8f;^0.U=#8`m[T\"z", "0;vNpZKk=j#=m");
	    fail("Expecting exception: RuntimeException");

	} catch (RuntimeException e) {
	    //
	    // Error retrieving database metadata; nested exception is
	    // org.evosuite.runtime.mock.java.lang.MockThrowable: Could not get Connection
	    // for extracting meta data
	    //
	}
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	PGPoolingDataSource pGPoolingDataSource0 = new PGPoolingDataSource();
	testSystemLoadDAO0.setDataSource(pGPoolingDataSource0);
	User user0 = new User();
	pGPoolingDataSource0.close();
	// Undeclared exception!
	try {
	    testSystemLoadDAO0.deleteAndDeActivate(user0, "8f;^0.U=#8`m[T\"z", "0;vNpZKk=j#=m");
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //
	}
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	PGPoolingDataSource pGPoolingDataSource0 = new PGPoolingDataSource();
	testSystemLoadDAO0.setDataSource(pGPoolingDataSource0);
	User user0 = new User();
	// Undeclared exception!
	try {
	    testSystemLoadDAO0.deActivate(user0, "<XG2y0F{)g'td", "P-B%kz+-Pm");
	    fail("Expecting exception: RuntimeException");

	} catch (RuntimeException e) {
	    //
	    // Error retrieving database metadata; nested exception is
	    // org.evosuite.runtime.mock.java.lang.MockThrowable: Could not get Connection
	    // for extracting meta data
	    //
	}
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	JdbcTemplate jdbcTemplate0 = testSystemLoadDAO0.getJdbcTemplate();
	assertNull(jdbcTemplate0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	User user0 = new User((String) null);
	List<YodaResult> list0 = testSystemLoadDAO0.deleteAndDeActivate(user0, "Eb1k", (String) null);
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	User user0 = new User();
	// Undeclared exception!
	try {
	    testSystemLoadDAO0.deleteAndDeActivate(user0, "8f;^0.U=#8`m[T\"z", "0;vNpZKk=j#=m");
	    fail("Expecting exception: IllegalArgumentException");

	} catch (IllegalArgumentException e) {
	    //
	    // JdbcTemplate must not be null
	    //
	}
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	User user0 = new User((String) null);
	List<YodaResult> list0 = testSystemLoadDAO0.deActivate(user0, "Eb1k", (String) null);
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	User user0 = new User();
	// Undeclared exception!
	try {
	    testSystemLoadDAO0.deActivate(user0, "<XG2y0F{)g'td", "P-B%kz+-Pm");
	    fail("Expecting exception: IllegalArgumentException");

	} catch (IllegalArgumentException e) {
	    //
	    // JdbcTemplate must not be null
	    //
	}
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	User user0 = new User((String) null);
	List<YodaResult> list0 = testSystemLoadDAO0.activate(user0, (String) null, (String) null);
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	User user0 = new User("OK");
	// Undeclared exception!
	try {
	    testSystemLoadDAO0.activate(user0, "OK", "usp_tpf_linux_test_deactivate");
	    fail("Expecting exception: IllegalArgumentException");

	} catch (IllegalArgumentException e) {
	    //
	    // JdbcTemplate must not be null
	    //
	}
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	User user0 = new User();
	List<YodaResult> list0 = testSystemLoadDAO0.loadAndActivate(user0, "<XG2y0F{)g'td", (String) null);
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	// Undeclared exception!
	try {
	    testSystemLoadDAO0.loadAndActivate((User) null, "OK", "usp_tpf_linux_test_deactivate");
	    fail("Expecting exception: IllegalArgumentException");

	} catch (IllegalArgumentException e) {
	    //
	    // JdbcTemplate must not be null
	    //
	}
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	PGPoolingDataSource pGPoolingDataSource0 = new PGPoolingDataSource();
	testSystemLoadDAO0.setDataSource(pGPoolingDataSource0);
	User user0 = new User("k@UT9_qRToK;-8]L .");
	// Undeclared exception!
	try {
	    testSystemLoadDAO0.activate(user0, (String) null, " |g_kXIQk");
	    fail("Expecting exception: RuntimeException");

	} catch (RuntimeException e) {
	    //
	    // Error retrieving database metadata; nested exception is
	    // org.evosuite.runtime.mock.java.lang.MockThrowable: Could not get Connection
	    // for extracting meta data
	    //
	}
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	TestSystemLoadDAO testSystemLoadDAO0 = new TestSystemLoadDAO();
	// Undeclared exception!
	try {
	    testSystemLoadDAO0.getVparsIP((String) null);
	    fail("Expecting exception: IllegalArgumentException");

	} catch (IllegalArgumentException e) {
	    //
	    // JdbcTemplate must not be null
	    //
	}
    }
}
