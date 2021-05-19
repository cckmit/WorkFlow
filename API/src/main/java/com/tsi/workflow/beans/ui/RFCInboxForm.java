package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.ImpPlan;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class RFCInboxForm {

    private ImpPlan id;
    private String loadType;
    private String planStatus;
    private List<RFCSysDetail> rfcDetails;
    private Boolean rfcSchedularFlag;

    public ImpPlan getId() {
	return id;
    }

    public void setId(ImpPlan id) {
	this.id = id;
    }

    public String getLoadType() {
	return loadType;
    }

    public void setLoadType(String loadType) {
	this.loadType = loadType;
    }

    public List<RFCSysDetail> getRfcDetails() {
	return rfcDetails;
    }

    public void setRfcDetails(List<RFCSysDetail> rfcDetails) {
	this.rfcDetails = rfcDetails;
    }

    public String getPlanStatus() {
	return planStatus;
    }

    public void setPlanStatus(String planStatus) {
	this.planStatus = planStatus;
    }

    public Date getMinLoadDateTime() {
	return getRfcDetails().stream().filter(x -> x.getLoadDateTime() != null).map(x -> x.getLoadDateTime()).min(Date::compareTo).isPresent() ? getRfcDetails().stream().filter(x -> x.getLoadDateTime() != null).map(x -> x.getLoadDateTime()).min(Date::compareTo).get() : null;
    }

    public Date getMaxLoadDateTime() {
	return getRfcDetails().stream().filter(x -> x.getLoadDateTime() != null).map(x -> x.getLoadDateTime()).max(Date::compareTo).isPresent() ? getRfcDetails().stream().filter(x -> x.getLoadDateTime() != null).map(x -> x.getLoadDateTime()).max(Date::compareTo).get() : null;
    }

    public String getTargetSystem() {
	return getRfcDetails().stream().filter(x -> x.getTargetSystem() != null && !x.getTargetSystem().isEmpty()).sorted(Comparator.comparing(RFCSysDetail::getTargetSystem)).findFirst().isPresent() ? getRfcDetails().stream().filter(x -> x.getTargetSystem() != null && !x.getTargetSystem().isEmpty()).sorted(Comparator.comparing(RFCSysDetail::getTargetSystem)).findFirst().get().getTargetSystem() : "";
    }

    public String getRfcNumber() {
	return getRfcDetails().stream().filter(x -> x.getRfcNumber() != null && !x.getRfcNumber().isEmpty()).sorted(Comparator.comparing(RFCSysDetail::getRfcNumber)).findFirst().isPresent() ? getRfcDetails().stream().filter(x -> x.getRfcNumber() != null && !x.getRfcNumber().isEmpty()).sorted(Comparator.comparing(RFCSysDetail::getRfcNumber)).findFirst().get().getRfcNumber() : "";
    }

    public String getPlanId() {
	return getId().getId();
    }

    public Boolean getRfcSchedularFlag() {
	return rfcSchedularFlag;
    }

    public void setRfcSchedularFlag(Boolean rfcSchedularFlag) {
	this.rfcSchedularFlag = rfcSchedularFlag;
    }

}
