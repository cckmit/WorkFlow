/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import static org.junit.Assert.assertEquals;

import com.tsi.workflow.beans.dao.LoadCategories;
import com.tsi.workflow.beans.dao.LoadWindow;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Radha.Adhimoolam
 */
public class LoadCategoriesFormTest {

    public LoadCategoriesFormTest() {
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
     * Test of getLoadCategory method, of class LoadCategoriesForm.
     */
    @Test
    public void testGetLoadCategory() {
	LoadCategoriesForm instance = new LoadCategoriesForm();
	LoadCategories expResult = new LoadCategories(1);
	instance.setLoadCategory(expResult);
	LoadCategories result = instance.getLoadCategory();
	assertEquals(expResult, result);
    }

    /**
     * Test of getLoadWindows method, of class LoadCategoriesForm.
     */
    @Test
    public void testGetLoadWindows() {
	LoadCategoriesForm instance = new LoadCategoriesForm();
	List<LoadWindow> expResult = new ArrayList();
	instance.setLoadWindows(expResult);
	List<LoadWindow> result = instance.getLoadWindows();
	assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class LoadCategoriesForm.
     */
    @Test
    public void testEquals() {
	LoadCategoriesForm obj = new LoadCategoriesForm();
	obj.setLoadCategory(new LoadCategories());
	LoadCategoriesForm instance = new LoadCategoriesForm();
	instance.setLoadCategory(new LoadCategories());
	boolean expResult = true;
	boolean result = instance.equals(obj);
	assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class LoadCategoriesForm.
     */
    @Test
    public void testHashCode() {

	LoadCategories lobj = new LoadCategories();
	LoadCategoriesForm instance = new LoadCategoriesForm();
	instance.setLoadCategory(new LoadCategories());
	int expResult = 0;
	int result = instance.hashCode();
	assertEquals(expResult, result);
	instance.equals(lobj);
    }

}
