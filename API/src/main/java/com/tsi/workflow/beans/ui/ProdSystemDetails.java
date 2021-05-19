package com.tsi.workflow.beans.ui;

public class ProdSystemDetails {

    private String planid;
    private String targetsystem;
    private String prodstatus;
    private String loaddatetime;
    private String programname;

    /**
     * @return the targetsystem
     */
    public String getTargetsystem() {
	return targetsystem;
    }

    /**
     * @param targetsystem
     *            the targetsystem to set
     */
    public void setTargetsystem(String targetsystem) {
	this.targetsystem = targetsystem;
    }

    /**
     * @return the prodstatus
     */
    public String getProdstatus() {
	return prodstatus;
    }

    /**
     * @param prodstatus
     *            the prodstatus to set
     */
    public void setProdstatus(String prodstatus) {
	this.prodstatus = prodstatus;
    }

    /**
     * @return the loaddatetime
     */
    public String getLoaddatetime() {
	return loaddatetime;
    }

    /**
     * @param loaddatetime
     *            the loaddatetime to set
     */
    public void setLoaddatetime(String loaddatetime) {
	this.loaddatetime = loaddatetime;
    }

    /**
     * @return the programname
     */
    public String getProgramname() {
	return programname;
    }

    /**
     * @param programname
     *            the programname to set
     */
    public void setProgramname(String programname) {
	this.programname = programname;
    }

    /**
     * @return the planid
     */
    public String getPlanid() {
	return planid;
    }

    /**
     * @param planid
     *            the planid to set
     */
    public void setPlanid(String planid) {
	this.planid = planid;
    }

    public ProdSystemDetails() {

    }

    public ProdSystemDetails(String targetsystem, String prodstatus, String loaddatetime, String programname) {
	this.targetsystem = targetsystem;
	this.prodstatus = prodstatus;
	this.loaddatetime = loaddatetime;
	this.programname = programname;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ProdSystemDetails [targetsystem=" + targetsystem + ", prodstatus=" + prodstatus + ", loaddatetime=" + loaddatetime + ", programname=" + programname + "]";
    }

}
