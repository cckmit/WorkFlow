/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.exception;

/**
 *
 * @author USER
 */
public class WorkflowException extends RuntimeException {

    public WorkflowException() {
    }

    public WorkflowException(String message) {
	super(message);
    }

    public void callWorkflowException() {
	new WorkflowException(new Exception());

    }

    private WorkflowException(Throwable cause) {
	super(cause);
    }

    public WorkflowException(String message, Throwable cause) {
	super(message, cause);
    }

}
