/*
 * This file was automatically generated by EvoSuite
 * Thu Aug 09 04:55:17 GMT 2018
 */

package com.tsi.workflow;

import static org.junit.Assert.*;

import org.junit.Test;

public class CacheConfigTest {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
	CacheConfig cacheConfig0 = new CacheConfig();
	cacheConfig0.setCacheServer("");
	String string0 = cacheConfig0.getCacheServer();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test1() throws Throwable {
	CacheConfig cacheConfig0 = new CacheConfig();
	cacheConfig0.setCachePort((-3186));
	int int0 = cacheConfig0.getCachePort();
	assertEquals((-3186), int0);
    }

    @Test(timeout = 4000)
    public void test2() throws Throwable {
	CacheConfig cacheConfig0 = new CacheConfig();
	cacheConfig0.setCacheServer("com.tsi.workflow.CacheConfig");
	String string0 = cacheConfig0.getCacheServer();
	assertEquals("com.tsi.workflow.CacheConfig", string0);
    }

    @Test(timeout = 4000)
    public void test3() throws Throwable {
	CacheConfig cacheConfig0 = new CacheConfig();
	int int0 = cacheConfig0.getCachePort();
	assertEquals(0, int0);
    }

    @Test(timeout = 4000)
    public void test4() throws Throwable {
	CacheConfig cacheConfig0 = new CacheConfig();
	String string0 = cacheConfig0.getCacheServer();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test5() throws Throwable {
	CacheConfig cacheConfig0 = new CacheConfig();
	cacheConfig0.setCachePort(690);
	int int0 = cacheConfig0.getCachePort();
	assertEquals(690, int0);
    }
}