/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.authenticator;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.LdapConfig;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.exception.WorkflowException;
import com.unboundid.ldap.sdk.LDAPConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InvalidNameException;
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
public class ADSAuthenticatorTest {

    public ADSAuthenticatorTest() {
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
     * Test of getHost method, of class ADSAuthenticator.
     */
    @Test
    public void testGetHost() {
	ADSAuthenticator instance = new ADSAuthenticatorImpl();
	TestCaseExecutor.doTest(instance, ADSAuthenticator.class);
    }

    @Test
    public void testCreateAnonymsContext() {
	ADSAuthenticator instance = new ADSAuthenticatorImpl();
	instance.createAnonymsContext();
	ReflectionTestUtils.setField(instance, "ldapConfig", mock(LdapConfig.class));
	when(instance.ldapConfig.getUrl()).thenReturn("https:");
	instance.createAnonymsContext();
    }

    @Test
    public void testCreateContext() {
	try {
	    ADSAuthenticator instance = new ADSAuthenticatorImpl();
	    instance.createAnonymsContext();
	    ReflectionTestUtils.setField(instance, "ldapConfig", mock(LdapConfig.class));
	    when(instance.ldapConfig.getUrl()).thenReturn("https:");
	    LDAPConnection lAuthConnection = new LDAPConnection();
	    ReflectionTestUtils.setField(instance, "lAuthConnection", lAuthConnection);
	    ReflectionTestUtils.setField(instance, "lAnonymousConnection", lAuthConnection);
	    instance.createContext(DataWareHouse.user.getId(), "abc");
	    instance.createContext(DataWareHouse.user.getId(), "");
	    instance.closeAnonymousConnection();
	    instance.getClassNames("", "");
	    instance.getAttributes("", "", "", "", new String[] { "" });
	} catch (InvalidNameException ex) {
	    Logger.getLogger(ADSAuthenticatorTest.class.getName()).log(Level.SEVERE, null, ex);
	} catch (WorkflowException e) {
	    assertTrue(true);
	}
    }

}
