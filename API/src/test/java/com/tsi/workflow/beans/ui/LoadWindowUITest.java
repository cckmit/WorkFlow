package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import com.tsi.workflow.beans.dao.LoadCategories;
import java.util.Date;
import org.junit.Test;

public class LoadWindowUITest {

    @Test
    public void test00() throws Throwable {
	LoadWindowUI loadWindowUI = new LoadWindowUI();
	loadWindowUI.setActive("Y");
	String active = loadWindowUI.getActive();
	assertEquals("Y", active);
    }

    @Test
    public void test01() throws Throwable {
	LoadWindowUI loadWindowUI = new LoadWindowUI();
	loadWindowUI.setCreatedBy("createdBy");
	String createdBy = loadWindowUI.getCreatedBy();
	assertEquals("createdBy", createdBy);
    }

    @Test
    public void test02() throws Throwable {
	LoadWindowUI loadWindowUI = new LoadWindowUI();
	loadWindowUI.setCreatedDt(new Date());
	Date date = loadWindowUI.getCreatedDt();
	assertEquals(new Date(), date);
    }

    @Test
    public void test03() throws Throwable {
	LoadWindowUI loadWindowUI = new LoadWindowUI();
	loadWindowUI.setDaysOfWeek("daysOfWeek");
	String daysOfWeek = loadWindowUI.getDaysOfWeek();
	assertEquals("daysOfWeek", daysOfWeek);
    }

    @Test
    public void test04() throws Throwable {
	LoadWindowUI loadWindowUI = new LoadWindowUI();
	loadWindowUI.setId(10);
	Integer id = loadWindowUI.getId();
	assertNotEquals("", id);
    }

    @Test
    public void test05() throws Throwable {
	LoadWindowUI loadWindowUI = new LoadWindowUI();
	loadWindowUI.setModifiedBy("modifiedBy");
	String modifiedBy = loadWindowUI.getModifiedBy();
	assertEquals("modifiedBy", modifiedBy);
    }

    @Test
    public void test06() throws Throwable {
	LoadWindowUI loadWindowUI = new LoadWindowUI();
	loadWindowUI.setModifiedDt(new Date());
	Date date = loadWindowUI.getModifiedDt();
	assertEquals(new Date(), date);
    }

    @Test
    public void test07() throws Throwable {
	LoadWindowUI loadWindowUI = new LoadWindowUI();
	LoadCategories loadCategories = new LoadCategories();
	loadWindowUI.setLoadCategoryId(loadCategories);
	LoadCategories load = loadWindowUI.getLoadCategoryId();
	assertNotNull(load);
    }

}
