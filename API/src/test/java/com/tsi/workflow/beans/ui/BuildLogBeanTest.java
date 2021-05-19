package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.util.SortedSet;
import java.util.TreeSet;
import org.junit.Test;

public class BuildLogBeanTest {

    @Test
    public void test001() throws Throwable {
	BuildLogBean buildLogBean = new BuildLogBean();
	buildLogBean.setPlanId("T1900111");
	String planId = buildLogBean.getPlanId();
	assertEquals("T1900111", planId);
    }

    @Test
    public void test002() throws Throwable {
	BuildLogBean buildLogBean = new BuildLogBean();
	buildLogBean.setSystemName("APO");
	String sysName = buildLogBean.getSystemName();
	assertEquals("APO", sysName);
    }

    @Test
    public void test003() throws Throwable {
	BuildLogBean buildLogBean = new BuildLogBean();
	SortedSet<String> buildFileNameList = new TreeSet<String>();
	buildFileNameList.add("wspBuildLog");
	buildFileNameList.add("apoBuildLog");
	buildLogBean.setBuildFileNameList(buildFileNameList);
	SortedSet<String> resultSet = buildLogBean.getBuildFileNameList();
	assertEquals(buildFileNameList, resultSet);
    }

    @Test
    public void test004() throws Throwable {
	BuildLogBean buildLogBean = new BuildLogBean();
	SortedSet<String> resultSet = buildLogBean.getBuildFileNameList();
	assertNull(resultSet);
    }

    @Test
    public void test005() throws Throwable {
	BuildLogBean buildLogBean = new BuildLogBean();
	SortedSet<String> buildFileNameList = new TreeSet<String>();
	buildFileNameList.add("wspBuildLog");
	buildFileNameList.add("apoBuildLog");
	buildLogBean.setBuildFileNameList(buildFileNameList);
	SortedSet<String> resultSet = buildLogBean.getBuildFileNameList();
	assertNotNull(resultSet);
    }

    @Test
    public void test006() throws Throwable {
	BuildLogBean buildLogBean = new BuildLogBean();
	SortedSet<String> buildFileNameList = new TreeSet<String>();
	buildFileNameList.add("wspBuildLog");
	buildFileNameList.add("apoBuildLog");
	buildLogBean.setLoaderFileNameList(buildFileNameList);
	SortedSet<String> resultSet = buildLogBean.getLoaderFileNameList();
	assertEquals(buildFileNameList, resultSet);
    }

    @Test
    public void test007() throws Throwable {
	BuildLogBean buildLogBean = new BuildLogBean();
	SortedSet<String> resultSet = buildLogBean.getLoaderFileNameList();
	assertNull(resultSet);
    }

    @Test
    public void test008() throws Throwable {
	BuildLogBean buildLogBean = new BuildLogBean();
	SortedSet<String> buildFileNameList = new TreeSet<String>();
	buildFileNameList.add("wspBuildLog");
	buildFileNameList.add("apoBuildLog");
	buildLogBean.setLoaderFileNameList(buildFileNameList);
	SortedSet<String> resultSet = buildLogBean.getLoaderFileNameList();
	assertNotNull(resultSet);
    }

}
