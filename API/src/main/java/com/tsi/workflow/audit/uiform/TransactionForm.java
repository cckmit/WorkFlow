/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.uiform;

import java.util.List;

/**
 *
 * @author Radha.Adhimoolam
 */
public class TransactionForm {
    private TransactionInputParamForm inputParam;
    private List<TransactionResponseForm> responseParam;

    public TransactionInputParamForm getInputParam() {
	return inputParam;
    }

    public void setInputParam(TransactionInputParamForm inputParam) {
	this.inputParam = inputParam;
    }

    public List<TransactionResponseForm> getResponseParam() {
	return responseParam;
    }

    public void setResponseParam(List<TransactionResponseForm> responseParam) {
	this.responseParam = responseParam;
    }

}
