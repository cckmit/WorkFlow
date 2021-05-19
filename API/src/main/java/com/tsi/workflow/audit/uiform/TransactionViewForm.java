/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.uiform;

import java.util.List;

/**
 *
 * @author ramkumar.seenivasan
 */
public class TransactionViewForm {

    private TransactionInputParamForm inputParam;
    private List<TransactionViewResponseForm> responseParam;

    public TransactionInputParamForm getInputParam() {
	return inputParam;
    }

    public void setInputParam(TransactionInputParamForm inputParam) {
	this.inputParam = inputParam;
    }

    public List<TransactionViewResponseForm> getResponseParam() {
	return responseParam;
    }

    public void setResponseParam(List<TransactionViewResponseForm> responseParam) {
	this.responseParam = responseParam;
    }

}
