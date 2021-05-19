package com.tsi.workflow.gi.response;

import static org.junit.Assert.*;

import org.junit.Test;

public class DirRuleTest {

    @Test
    public void test() {
	DirRule dirRule = new DirRule();
	dirRule.setDirectory(new Directory());
	assertNotNull(dirRule.getDirectory());
	dirRule.setMask("mask");
	assertEquals("mask", dirRule.getMask());
	dirRule.setRecurse("recurse");
	assertEquals("recurse", dirRule.getRecurse());
    }

}
