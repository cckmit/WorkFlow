/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.gitblit.model;

import static org.junit.Assert.assertEquals;

import java.util.SortedSet;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class RepositoryTest {

    public RepositoryTest() {
    }

    @Test
    public void testGetName() {
	Repository instance = new Repository();
	String expResult = "";
	String name = "";
	instance.setName(name);
	String result = instance.getName();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetDescription() {
	Repository instance = new Repository();
	String expResult = "";
	String description = "";
	instance.setDescription(description);
	String result = instance.getDescription();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetOwners() {
	Repository instance = new Repository();
	SortedSet<String> expResult = null;
	SortedSet<String> owners = null;
	instance.setOwners(owners);
	SortedSet<String> result = instance.getOwners();
	assertEquals(expResult, result);
    }

    @Test
    public void testIsAllowAuthenticated() {
	Repository instance = new Repository();
	boolean expResult = false;
	boolean allowAuthenticated = false;
	instance.setAllowAuthenticated(allowAuthenticated);
	boolean result = instance.isAllowAuthenticated();
	assertEquals(expResult, result);
    }

    @Test
    public void testIsUseIncrementalPushTags() {
	Repository instance = new Repository();
	boolean expResult = false;
	boolean useIncrementalPushTags = false;
	instance.setUseIncrementalPushTags(useIncrementalPushTags);
	boolean result = instance.isUseIncrementalPushTags();
	assertEquals(expResult, result);
    }

    @Test
    public void testIsIsFrozen() {
	Repository instance = new Repository();
	boolean expResult = false;
	boolean isFrozen = false;
	instance.setIsFrozen(isFrozen);
	boolean result = instance.isIsFrozen();
	assertEquals(expResult, result);
    }

}
