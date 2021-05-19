package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.Date;
import java.util.List;

public class RFCReportSearchForm {

    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date startDate;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date endDate;
    private List<String> systems;
    private String rfcNumber;

    public Date getStartDate() {
	return startDate;
    }

    public void setStartDate(Date startDate) {
	this.startDate = startDate;
    }

    public Date getEndDate() {
	return endDate;
    }

    public void setEndDate(Date endDate) {
	this.endDate = endDate;
    }

    public List<String> getSystems() {
	return systems;
    }

    public void setSystems(List<String> systems) {
	this.systems = systems;
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
