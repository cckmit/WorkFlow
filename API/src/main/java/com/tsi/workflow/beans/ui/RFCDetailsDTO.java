package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.RFCDetails;

public class RFCDetailsDTO {

    private RFCDetails rfcDetails;
    private boolean isupdateallowed;

    public RFCDetailsDTO(RFCDetails rfcDetails) {
	this.rfcDetails = rfcDetails;
    }

    public RFCDetails getRfcDetails() {
	return rfcDetails;
    }

    public void setRfcDetails(RFCDetails rfcDetails) {
	this.rfcDetails = rfcDetails;
    }

    public boolean isIsupdateallowed() {
	return isupdateallowed;
    }

    public void setIsupdateallowed(boolean isupdateallowed) {
	this.isupdateallowed = isupdateallowed;
    }

}
