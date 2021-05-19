package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.util.Date;
import org.junit.Test;

public class DependenciesFormTest {

    @Test
    public void test001() throws Throwable {
	DependenciesForm dependenciesForm = new DependenciesForm();
	dependenciesForm.setLoadtype("AUX");
	String loadType = dependenciesForm.getLoadtype();
	assertEquals("AUX", loadType);
    }

    @Test
    public void test002() throws Throwable {
	DependenciesForm dependenciesForm = new DependenciesForm();
	dependenciesForm.setPlanid("T1900111");
	String planId = dependenciesForm.getPlanid();
	assertEquals("T1900111", planId);
    }

    @Test
    public void test003() throws Throwable {
	DependenciesForm dependenciesForm = new DependenciesForm();
	Date date = new Date();
	dependenciesForm.setLoaddatetime(date);
	Date loaddatetime = dependenciesForm.getLoaddatetime();
	assertEquals(date, loaddatetime);
    }

    @Test
    public void test004() throws Throwable {
	DependenciesForm dependenciesForm = new DependenciesForm();
	dependenciesForm.setSegments("abcd.asm");
	String segments = dependenciesForm.getSegments();
	assertEquals("abcd.asm", segments);
    }

    @Test
    public void test005() throws Throwable {
	DependenciesForm dependenciesForm = new DependenciesForm();
	dependenciesForm.setStatus("ACTIVE");
	String status = dependenciesForm.getStatus();
	assertEquals("ACTIVE", status);
    }

    @Test
    public void test006() throws Throwable {
	DependenciesForm dependenciesForm = new DependenciesForm();
	dependenciesForm.setTargetsystem("WSP");
	String targetSystem = dependenciesForm.getTargetsystem();
	assertEquals("WSP", targetSystem);
    }

}
