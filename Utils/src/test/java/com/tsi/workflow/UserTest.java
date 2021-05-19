/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class UserTest {

    public UserTest() {
    }

    @Test
    public void testIsAllowDelegateMenu() {
	User instance = new User("");
	boolean expResult = false;
	boolean allowDelegateMenu = false;
	instance.setAllowDelegateMenu(allowDelegateMenu);
	boolean result = instance.isAllowDelegateMenu();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetCurrentDelegatedUser() {
	User instance = new User();
	User expResult = null;
	User currentDelegatedUser = null;
	instance.setCurrentDelegatedUser(currentDelegatedUser);
	User result = instance.getCurrentDelegatedUser();
	assertEquals(expResult, result);
    }

    @Test
    public void testIsDelegated() {
	User instance = new User();
	boolean expResult = false;
	boolean delegated = false;
	instance.setDelegated(delegated);
	boolean result = instance.isDelegated();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetDelegations() {
	User instance = new User();
	Map<String, User> expResult = null;
	Map<String, User> delegations = null;
	instance.setDelegations(delegations);
	Map<String, User> result = instance.getDelegations();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetId() {
	User instance = new User();
	String expResult = "";
	String id = "";
	instance.setId(id);
	String result = instance.getId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetCurrentRole() {
	User instance = new User();
	String expResult = "";
	String currentRole = "";
	instance.setCurrentRole(currentRole);
	String result = instance.getCurrentRole();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetDisplayName() {
	User instance = new User();
	String expResult = "";
	String displayName = "";
	instance.setDisplayName(displayName);
	String result = instance.getDisplayName();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetMailId() {
	User instance = new User();
	String expResult = "";
	String mailId = "";
	instance.setMailId(mailId);
	String result = instance.getMailId();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetRole() {
	User instance = new User();
	Set<String> expResult = null;
	LinkedHashSet<String> role = null;
	instance.setRole(role);
	Set<String> result = instance.getRole();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetTimeZone() {
	User instance = new User();
	String expResult = "";
	String timeZone = "";
	instance.setTimeZone(timeZone);
	String result = instance.getTimeZone();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetPassword() {
	User instance = new User();
	String expResult = "";
	String password = "";
	instance.setPassword(password);
	String result = instance.getPassword();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetDecryptedPassword() {
	User instance = new User();
	String expResult = "";
	String pPassword = "";
	instance.setEncryptedPassword(pPassword);
	String result = instance.getDecryptedPassword();
	assertEquals(expResult, result);
    }

    @Test
    public void testAddDelegators() {
	User instance = new User();
	instance.addDelegators("", instance);

    }

    @Test
    public void testGetCurrentOrDelagateUser() {
	User instance = new User();
	instance.getCurrentOrDelagateUser();

    }

    @Test
    public void testAddRole() {
	User instance = new User();
	instance.addRole("admin");

    }

    @Test
    public void testEqualsandHashCode() {
	User user1 = new User();
	user1.setId("5001");
	User user2 = new User();
	user2.setId("5001");
	assertEquals("These should be equal", user1, user2);
	User user3 = new User();
	user3.setId("");
	assertNotEquals("These should not be equal", user1, user3);
	assertEquals("These should be equal", user1.hashCode(), user2.hashCode());
	user1.setDisplayName("admin");
	user2.setDisplayName("admin");
	assertEquals(0, user1.compareTo(user2));
	Object obj = new Object();
	assertFalse(user1.equals(obj));
	User instance = new User();
	instance.setCurrentDelegatedUser(new User());
	assertNotNull(instance.getCurrentOrDelagateUser());
    }
}
