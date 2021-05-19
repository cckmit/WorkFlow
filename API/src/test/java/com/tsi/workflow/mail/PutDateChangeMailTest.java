/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.mail;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.beans.dao.PutLevel;
import java.util.Date;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 *
 * @author Radha.Adhimoolam
 */
public class PutDateChangeMailTest {

    public PutDateChangeMailTest() {
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
     * Test of getPutBeforeUpdate method, of class PutDateChangeMail.
     */
    @Test
    public void testGetPutBeforeUpdate() {
	PutDateChangeMail instance = new PutDateChangeMail();
	PutLevel expResult = new PutLevel(1);
	instance.setPutBeforeUpdate(expResult);
	PutLevel result = instance.getPutBeforeUpdate();
	assertEquals(expResult, result);
    }

    /**
     * Test of getPutAfterUpdate method, of class PutDateChangeMail.
     */
    @Test
    public void testGetPutAfterUpdate() {
	PutDateChangeMail instance = new PutDateChangeMail();
	PutLevel expResult = new PutLevel(1);
	instance.setPutAfterUpdate(expResult);
	PutLevel result = instance.getPutAfterUpdate();
	assertEquals(expResult, result);
    }

    /**
     * Test of processMessage method, of class PutDateChangeMail.
     */
    @Test
    public void testProcessMessage() {

	PutDateChangeMail instance = new PutDateChangeMail();
	instance.setPlan(DataWareHouse.getPlan());
	Set<PutLevel> put = DataWareHouse.getPutLevelList();
	PutLevel oldPut = put.iterator().next();
	oldPut.setPutDateTime(new Date());
	instance.setPutAfterUpdate(oldPut);
	instance.setPutBeforeUpdate(oldPut);
	instance.authenticator = mock(LDAPAuthenticatorImpl.class);
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	when(instance.authenticator.getUserDetails(DataWareHouse.getPlan().getLeadId())).thenReturn(new User());
	instance.processMessage();
    }

    @Test
    public void testMail() {

	PutDateChangeMail instance = new PutDateChangeMail();
	instance.setPlan(DataWareHouse.getPlan());
    }

}
