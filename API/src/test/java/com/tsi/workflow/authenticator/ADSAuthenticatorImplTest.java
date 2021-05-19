/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.authenticator;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.LdapConfig;
import com.tsi.workflow.LdapGroupConfig;
import com.tsi.workflow.TestCaseExecutor;
import com.tsi.workflow.User;
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
public class ADSAuthenticatorImplTest {

    public ADSAuthenticatorImplTest() {
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
     * Test of validate method, of class ADSAuthenticatorImpl.
     */
    @Test
    public void testValidate() {

	ADSAuthenticatorImpl instance = new ADSAuthenticatorImpl();
	ReflectionTestUtils.setField(instance, "ldapConfig", mock(LdapConfig.class));
	ReflectionTestUtils.setField(instance, "ldapGroupConfig", mock(LdapGroupConfig.class));
	LDAPConnection lAuthConnection = new LDAPConnection();
	ReflectionTestUtils.setField(instance, "lAuthConnection", lAuthConnection);
	ReflectionTestUtils.setField(instance, "lAnonymousConnection", lAuthConnection);
	when(instance.ldapConfig.getUrl()).thenReturn("https://vhldvztdt001.tvlport.net:8443/WorkFlow/");
	try {

	    // ReflectionTestUtils.setField(instance, "lUrl", new
	    // URL("https://vhldvztdt001.tvlport.net:8443/WorkFlow/"));
	    doReturn(true).when(lAuthConnection).connect("vhldvztdt001.tvlport.net", 8443);
	} catch (Exception ex) {
	    Logger.getLogger(ADSAuthenticatorTest.class.getName()).log(Level.SEVERE, null, ex);
	}
	TestCaseExecutor.doTest(instance, ADSAuthenticatorImpl.class);
	try {
	    instance.getUserDetails(DataWareHouse.getUser());
	} catch (Exception e) {

	}

	try {
	    instance.getGroupDetails(DataWareHouse.getUser());
	} catch (InvalidNameException ex) {
	    Logger.getLogger(ADSAuthenticatorImplTest.class.getName()).log(Level.SEVERE, null, ex);
	} catch (Exception e) {

	}

    }

    @Test
    public void testValidateADSLogin() {
	ADSAuthenticatorImpl instance = new ADSAuthenticatorImpl();
	ADSAuthenticator ADSAuthenticator = new ADSAuthenticatorImpl();

	User pUser = new User();
	pUser.setId("12");
	String pPassword = "ab";
	boolean lReturn = false;

	try {

	    ReflectionTestUtils.invokeMethod(instance, "validateADSLogin", pUser, pPassword);
	} catch (Exception e) {
	}

    }

    public void testGetUserDetails() {
	ADSAuthenticatorImpl adsAuthenticatorImpl = mock(ADSAuthenticatorImpl.class);
	User user = mock(User.class);
	adsAuthenticatorImpl.getUserDetails(user);
    }

}
