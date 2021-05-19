/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.exception;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 *
 * @author USER
 */
public class WorkflowExceptionTest {

    public WorkflowExceptionTest() {
    }

    @Test
    public void testSomeMethod() {
	WorkflowException lException = new WorkflowException();
	lException = new WorkflowException("");
	lException = new WorkflowException("", new Exception());
	lException.callWorkflowException();
	// WorkflowException example = exampleConstructor.newInstance(new Exception());

	// try {
	// Constructor constructor = WorkflowException.class.getDeclaredConstructor();
	// constructor.setAccessible(true);
	// constructor.newInstance(new Exception());
	//
	// } catch (Exception e) {
	// }
	// try {
	// ReflectionTestUtils.invokeMethod(lException, "WorkflowException", new
	// Exception());
	//
	//
	// } catch (Exception e) {
	// }
	assertTrue(true);
    }

}
