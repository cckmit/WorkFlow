/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.bpm.model;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class TaskReponseTest {

    public TaskReponseTest() {
    }

    @Test
    public void testGetData() {
	TaskReponse instance = new TaskReponse();
	List<TaskRepresentation> expResult = null;
	List<TaskRepresentation> data = null;
	instance.setData(data);
	List<TaskRepresentation> result = instance.getData();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetTotal() {
	TaskReponse instance = new TaskReponse();
	Integer expResult = null;
	Integer total = null;
	instance.setTotal(total);
	Integer result = instance.getTotal();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetStart() {
	TaskReponse instance = new TaskReponse();
	Integer expResult = null;
	Integer start = null;
	instance.setStart(start);
	Integer result = instance.getStart();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetSort() {
	TaskReponse instance = new TaskReponse();
	String expResult = "";
	String sort = "";
	instance.setSort(sort);
	String result = instance.getSort();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetOrder() {
	TaskReponse instance = new TaskReponse();
	String expResult = "";
	String order = "";
	instance.setOrder(order);
	String result = instance.getOrder();
	assertEquals(expResult, result);
    }

    @Test
    public void testGetSize() {
	TaskReponse instance = new TaskReponse();
	Integer expResult = null;
	Integer size = null;
	instance.setSize(size);
	Integer result = instance.getSize();
	assertEquals(expResult, result);
    }

}
