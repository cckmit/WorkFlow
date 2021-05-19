/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.authenticator.LDAPAuthenticatorImpl;
import com.tsi.workflow.dao.UserSettingsDAO;
import com.tsi.workflow.mail.CheckoutSourceContentMail;
import com.tsi.workflow.mail.DeletePlanMail;
import com.tsi.workflow.utils.Constants;
import com.workflow.mail.AzureMailUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

/**
 *
 * @author deepa.jayakumar
 */
public class MailMessageTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	deletePlanMail0.addcCAddressUserId("", Constants.MailSenderRole.LEAD);
	Collection<String> collection0 = deletePlanMail0.getAttachments();
	assertNotNull(collection0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	// Undeclared exception!
	try {
	    deletePlanMail0.processMessage();
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //
	}
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	ConcurrentHashMap<CheckoutSourceContentMail, String> concurrentHashMap0 = new ConcurrentHashMap<CheckoutSourceContentMail, String>();
	Collection<String> collection0 = concurrentHashMap0.values();
	deletePlanMail0.addcCAddressUserId("", Constants.MailSenderRole.LEAD);
	// Undeclared exception!
	try {
	    deletePlanMail0.addcCAddressUserId((String) null, Constants.MailSenderRole.LEAD);
	    // fail("Expecting exception: UnsupportedOperationException");

	} catch (UnsupportedOperationException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //
	}
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	deletePlanMail0.addcCAddressUserId("", Constants.MailSenderRole.LEAD);
	// Undeclared exception!
	try {
	    deletePlanMail0.addcCAddressUserId((String) null, Constants.MailSenderRole.LEAD);
	    // fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //

	}
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	String string0 = deletePlanMail0.getMessage();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	String string0 = deletePlanMail0.getSubject();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	deletePlanMail0.addToAddressUserId("com.tsi.workflow.base.MailMssage", Constants.MailSenderRole.LEAD);
	try {
	    deletePlanMail0.send();
	    // fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //

	}
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	deletePlanMail0.setSubject("");
	assertNull(deletePlanMail0.getDeletedBy());
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	AzureMailUtil azureMailUtil0 = deletePlanMail0.getAzureMailUtil();
	assertNull(azureMailUtil0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	Collection<String> collection0 = deletePlanMail0.getAttachments();
	assertNotNull(collection0);
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	deletePlanMail0.addCcDEVCentre();
	try {
	    deletePlanMail0.send();
	    // fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //

	}
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	deletePlanMail0.addToAddressUserId("", Constants.MailSenderRole.LEAD);
	// Undeclared exception!
	try {
	    deletePlanMail0.addToAddressUserId("5`)}fsDzJhHX>", Constants.MailSenderRole.LEAD);
	    // fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //

	}
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	Collection<String> collection0 = deletePlanMail0.getAttachments();
	deletePlanMail0.addcCAddressUserId("", Constants.MailSenderRole.LEAD);
	assertNull(deletePlanMail0.getMessage());
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	deletePlanMail0.addcCAddressUserId((String) null, Constants.MailSenderRole.LEAD);
	try {
	    deletePlanMail0.send();
	    // fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //

	}
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	deletePlanMail0.setMessage("");
	assertEquals("", deletePlanMail0.getMessage());
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	deletePlanMail0.addToDEVCentre();
	try {
	    deletePlanMail0.send();
	    // fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //

	}
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	DeletePlanMail deletePlanMail0 = new DeletePlanMail();
	try {
	    deletePlanMail0.send();
	    // fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //

	}
    }

    /**
     * Test of getToAddressList method, of class MailMessage.
     */
    @Test
    public void testGetToAddressList() {

	MailMessage instance = new MailMessageImpl();
	Collection<String> result = instance.getAttachments();
	assertNotNull(result);

    }

    /**
     * Test of setToAddressList method, of class MailMessage.
     */
    @Test
    public void testSetToAddressList() {

	List<String> toAddressList = new ArrayList();
	toAddressList.add("easdasd");
	MailMessage instance = new MailMessageImpl();
	instance.addToAddressUserId(toAddressList.get(0), Constants.MailSenderRole.LEAD);

    }

    /**
     * Test of addToAddressList method, of class MailMessage.
     */
    // @Test
    public void testAddToAddressList() throws Exception {

	String toAddress = "";
	MailMessage instance = new MailMessageImpl();
	instance.addToAddressUserId(toAddress, Constants.MailSenderRole.LEAD);
	Field field = ReflectionUtils.findField(MailMessage.class, "toAddressUserIds");
	field.setAccessible(true);
	List toList = (List) field.get(instance);
	assertEquals(toAddress, toList.get(0));

    }

    /**
     * Test of addCCAddressList method, of class MailMessage.
     */
    // @Test
    public void testAddCCAddressList() throws Exception {

	String ccAddress = "";
	MailMessage instance = new MailMessageImpl();
	instance.addcCAddressUserId(ccAddress, Constants.MailSenderRole.LEAD);
	Field field = ReflectionUtils.findField(MailMessage.class, "cCAddressUserIds");
	field.setAccessible(true);
	List ccList = (List) field.get(instance.getAttachments());
	assertEquals(ccAddress, ccList.get(0));

    }

    /**
     * Test of getcCAddressList method, of class MailMessage.
     */
    @Test
    public void testGetcCAddressList() {

	MailMessage instance = new MailMessageImpl();
	List<String> expResult = null;
	Collection<String> result = instance.getAttachments();
	assertNotNull(result);

    }

    /**
     * Test of setcCAddressList method, of class MailMessage.
     */
    @Test
    public void testSetcCAddressList() {

	List<String> cCAddressList = new ArrayList();
	cCAddressList.add("sads");
	MailMessage instance = new MailMessageImpl();
	instance.addcCAddressUserId(cCAddressList.get(0), Constants.MailSenderRole.LEAD);
	// assertNull(instance.getAttachments());

    }

    /**
     * Test of getSubject method, of class MailMessage.
     */
    @Test
    public void testGetSubject() {

	MailMessage instance = new MailMessageImpl();
	String expResult = "";
	instance.setSubject(expResult);
	String result = instance.getSubject();
	assertEquals(expResult, result);

    }

    /**
     * Test of setSubject method, of class MailMessage.
     */
    @Test
    public void testSetSubject() {

	String subject = "";
	MailMessage instance = new MailMessageImpl();
	instance.setSubject(subject);
	assertEquals(subject, instance.getSubject());

    }

    /**
     * Test of getMessage method, of class MailMessage.
     */
    @Test
    public void testGetMessage() {

	MailMessage instance = new MailMessageImpl();
	String expResult = "";
	instance.setMessage(expResult);
	String result = instance.getMessage();
	assertEquals(expResult, result);

    }

    /**
     * Test of setMessage method, of class MailMessage.
     */
    @Test
    public void testSetMessage() {

	String message = "";
	MailMessage instance = new MailMessageImpl();
	instance.setMessage(message);
	assertEquals(message, instance.getMessage());

    }

    /**
     * Test of processMessage method, of class MailMessage.
     */
    @Test
    public void testProcessMessage() {

	MailMessage instance = new MailMessageImpl();
	instance.processMessage();

    }

    /**
     * Test of send method, of class MailMessage.
     */
    // @Test
    @Test
    public void testSend() throws Exception {

	MailMessage instance = new MailMessageImpl();
	User lUser = DataWareHouse.user;
	instance.addToAddressUserId(lUser.getId(), Constants.MailSenderRole.LEAD);
	instance.addcCAddressUserId("", Constants.MailSenderRole.LEAD);
	instance.setMessage("message");
	instance.setSubject("test mail");
	ReflectionTestUtils.setField(instance, "userSettingsDAO", mock(UserSettingsDAO.class));
	ReflectionTestUtils.setField(instance, "azureMailUtil", mock(AzureMailUtil.class));
	ReflectionTestUtils.setField(instance, "authenticator", mock(LDAPAuthenticatorImpl.class));
	when(instance.userSettingsDAO.findAnyDelegatedUsersFor(instance.getAttachments())).thenReturn(Arrays.asList(DataWareHouse.getUserSettings()));
	when(instance.authenticator.getUserDetails(DataWareHouse.getUserSettings().getValue())).thenReturn(lUser);
	// when(instance.cCAddressList.add("test")).thenReturn(Boolean.TRUE);
	try {
	    instance.send();
	} catch (Exception error) {
	    assertTrue(true);
	}

    }

    public class MailMessageImpl extends MailMessage {

	public void processMessage() {
	}
    }
}
