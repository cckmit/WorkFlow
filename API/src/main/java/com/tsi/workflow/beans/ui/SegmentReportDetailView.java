/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.tsi.workflow.beans.ui;

public class SegmentReportDetailView {
    private String programName;
    private Integer totoalOnlineDeployments = 0; // Plan in Online and Fallback Status
    private Integer totalSecuredDeployments = 0;
    private Integer totalActiveSegCount = 0;
    private Integer totalAllCount = 0;

    /**
     * @return the totoalOnlineDeployments
     */
    public Integer getTotoalOnlineDeployments() {
	return totoalOnlineDeployments;
    }

    /**
     * @param totoalOnlineDeployments
     *            the totoalOnlineDeployments to set
     */
    public void setTotoalOnlineDeployments(Integer totoalOnlineDeployments) {
	this.totoalOnlineDeployments = totoalOnlineDeployments;
    }

    /**
     * @return the totalSecuredDeployments
     */
    public Integer getTotalSecuredDeployments() {
	return totalSecuredDeployments;
    }

    /**
     * @param totalSecuredDeployments
     *            the totalSecuredDeployments to set
     */
    public void setTotalSecuredDeployments(Integer totalSecuredDeployments) {
	this.totalSecuredDeployments = totalSecuredDeployments;
    }

    /**
     * @return the totalActiveSegCount
     */
    public Integer getTotalActiveSegCount() {
	return totalActiveSegCount;
    }

    /**
     * @param totalActiveSegCount
     *            the totalActiveSegCount to set
     */
    public void setTotalActiveSegCount(Integer totalActiveSegCount) {
	this.totalActiveSegCount = totalActiveSegCount;
    }

    /**
     * @return the totalAllCount
     */
    public Integer getTotalAllCount() {
	return totalAllCount;
    }

    /**
     * @param totalAllCount
     *            the totalAllCount to set
     */
    public void setTotalAllCount(Integer totalAllCount) {
	this.totalAllCount = totalAllCount;
    }

    /**
     * @return the programName
     */
    public String getProgramName() {
	return programName;
    }

    /**
     * @param programName
     *            the programName to set
     */
    public void setProgramName(String programName) {
	this.programName = programName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "SegmentReportDetailView [totoalOnlineDeployments=" + totoalOnlineDeployments + ", totalSecuredDeployments=" + totalSecuredDeployments + ", totalActiveSegCount=" + totalActiveSegCount + ", totalAllCount=" + totalAllCount + "]";
    }

}
