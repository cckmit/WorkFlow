package com.tsi.workflow.beans.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RFCSysDetail {
    private String targetSystem;
    private Date loadDateTime;
    private String category;
    private List<String> dbcr = new ArrayList<>();
    private String rfcNumber;

    public String getTargetSystem() {
	return targetSystem;
    }

    public void setTargetSystem(String targetSystem) {
	this.targetSystem = targetSystem;
    }

    public Date getLoadDateTime() {
	return loadDateTime;
    }

    public void setLoadDateTime(Date loadDateTime) {
	this.loadDateTime = loadDateTime;
    }

    public String getCategory() {
	return category;
    }

    public void setCategory(String category) {
	this.category = category;
    }

    public List<String> getDbcr() {
	return dbcr;
    }

    public void setDbcr(List<String> dbcr) {
	this.dbcr = dbcr;
    }

    /**
     * @return the rfcNumber
     */
    public String getRfcNumber() {
	return rfcNumber;
    }

    /**
     * @param rfcNumber
     *            the rfcNumber to set
     */
    public void setRfcNumber(String rfcNumber) {
	this.rfcNumber = rfcNumber;
    }
}