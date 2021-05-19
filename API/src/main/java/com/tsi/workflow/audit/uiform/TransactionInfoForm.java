/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.uiform;

import com.tsi.workflow.audit.beans.dao.ApiTransaction;
import com.tsi.workflow.beans.ui.PlanPerformanceView;
import java.util.List;

/**
 *
 * @author Radha.Adhimoolam
 */
public class TransactionInfoForm {

    private ApiTransaction apiTransaction;
    private List<PlanPerformanceView> planInfo;

    public TransactionInfoForm() {
    }

    public TransactionInfoForm(ApiTransaction apiTransaction) {
	this.apiTransaction = apiTransaction;
    }

    public ApiTransaction getApiTransaction() {
	return apiTransaction;
    }

    public void setApiTransaction(ApiTransaction apiTransaction) {
	this.apiTransaction = apiTransaction;
    }

    public List<PlanPerformanceView> getPlanInfo() {
	return planInfo;
    }

    public void setPlanInfo(List<PlanPerformanceView> planInfo) {
	this.planInfo = planInfo;
    }

}
