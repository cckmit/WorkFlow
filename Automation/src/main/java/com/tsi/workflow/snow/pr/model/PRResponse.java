/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.snow.pr.model;

import java.util.List;

/**
 *
 * @author Radha.Adhimoolam
 */
public class PRResponse {

    private String status;
    private ErrorResponse error;
    private List<PRDetails> result;

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    public ErrorResponse getError() {
	return error;
    }

    public void setError(ErrorResponse error) {
	this.error = error;
    }

    public List<PRDetails> getResult() {
	return result;
    }

    public void setResult(List<PRDetails> result) {
	this.result = result;
    }

}
