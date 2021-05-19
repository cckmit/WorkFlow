/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.external;

import java.io.Serializable;

public class ProblemTicket implements Serializable {

    private static final long serialVersionUID = 1L;

    private String refNum;

    private String status;

    public ProblemTicket() {
    }

    public String getRefNum() {
	return refNum;
    }

    public void setRefNum(String refNum) {
	this.refNum = refNum;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (refNum != null ? refNum.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {

	if (!(object instanceof ProblemTicket)) {
	    return false;
	}
	ProblemTicket other = (ProblemTicket) object;
	if ((this.refNum == null && other.refNum != null) || (this.refNum != null && !this.refNum.equals(other.refNum))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.ProblemTicket_1[ refNum=" + refNum + " ]";
    }

}
