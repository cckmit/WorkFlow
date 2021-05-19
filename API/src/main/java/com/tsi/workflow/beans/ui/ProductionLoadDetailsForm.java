/**
 * 
 */
package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.Date;

/**
 * @author vinoth.ponnurangan
 *
 */
public class ProductionLoadDetailsForm {

    private String planid;
    private String plandescription;
    private String developerid;
    private String developername;
    private String devmanagerid;
    private String devmamangername;
    private String loadtype;
    private String planstatus;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date activateddatetime;
    private String targetsystem;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date loaddatetime;
    private String prodstatus;
    private String programname;

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

    /**
     * @return the plandescription
     */
    public String getPlandescription() {
	return plandescription;
    }

    /**
     * @param plandescription
     *            the plandescription to set
     */
    public void setPlandescription(String plandescription) {
	this.plandescription = plandescription;
    }

    /**
     * @return the developerid
     */
    public String getDeveloperid() {
	return developerid;
    }

    /**
     * @param developerid
     *            the developerid to set
     */
    public void setDeveloperid(String developerid) {
	this.developerid = developerid;
    }

    /**
     * @return the developername
     */
    public String getDevelopername() {
	return developername;
    }

    /**
     * @param developername
     *            the developername to set
     */
    public void setDevelopername(String developername) {
	this.developername = developername;
    }

    /**
     * @return the devmanagerid
     */
    public String getDevmanagerid() {
	return devmanagerid;
    }

    /**
     * @param devmanagerid
     *            the devmanagerid to set
     */
    public void setDevmanagerid(String devmanagerid) {
	this.devmanagerid = devmanagerid;
    }

    /**
     * @return the devmamangername
     */
    public String getDevmamangername() {
	return devmamangername;
    }

    /**
     * @param devmamangername
     *            the devmamangername to set
     */
    public void setDevmamangername(String devmamangername) {
	this.devmamangername = devmamangername;
    }

    /**
     * @return the loadtype
     */
    public String getLoadtype() {
	return loadtype;
    }

    /**
     * @param loadtype
     *            the loadtype to set
     */
    public void setLoadtype(String loadtype) {
	this.loadtype = loadtype;
    }

    /**
     * @return the planstatus
     */
    public String getPlanstatus() {
	return planstatus;
    }

    /**
     * @param planstatus
     *            the planstatus to set
     */
    public void setPlanstatus(String planstatus) {
	this.planstatus = planstatus;
    }

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
    public Date getLoaddatetime() {
	return loaddatetime;
    }

    /**
     * @param loaddatetime
     *            the loaddatetime to set
     */
    public void setLoaddatetime(Date loaddatetime) {
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
     * @return the activateddatetime
     */
    public Date getActivateddatetime() {
	return activateddatetime;
    }

    /**
     * @param activateddatetime
     *            the activateddatetime to set
     */
    public void setActivateddatetime(Date activateddatetime) {
	this.activateddatetime = activateddatetime;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ProductionLoadDetailsForm [planid=" + planid + ", plandescription=" + plandescription + ", developerid=" + developerid + ", developername=" + developername + ", devmanagerid=" + devmanagerid + ", devmamangername=" + devmamangername + ", loadtype=" + loadtype + ", planstatus=" + planstatus + ", activateddatetime=" + activateddatetime + ", targetsystem=" + targetsystem + ", loaddatetime=" + loaddatetime + ", prodstatus=" + prodstatus + ", programname=" + programname + "]";
    }

}
