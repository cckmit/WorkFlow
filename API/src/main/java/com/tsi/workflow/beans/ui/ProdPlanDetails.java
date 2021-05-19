/**
 * 
 */
package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.Date;
import java.util.List;

/**
 * @author vinoth.ponnurangan
 *
 */
public class ProdPlanDetails {

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
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date loaddatetime;
    private List<ProdSystemDetails> prodSystemDetails;

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
     * @return the prodSystemDetails
     */
    public List<ProdSystemDetails> getProdSystemDetails() {
	return prodSystemDetails;
    }

    /**
     * @param prodSystemDetails
     *            the prodSystemDetails to set
     */
    public void setProdSystemDetails(List<ProdSystemDetails> prodSystemDetails) {
	this.prodSystemDetails = prodSystemDetails;
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

    public ProdPlanDetails() {

    }

    public ProdPlanDetails(String planid, String plandescription, String developerid, String developername, String devmanagerid, String devmamangername, String loadtype, String planstatus, Date activateddatetime, Date loaddatetime, List<ProdSystemDetails> prodSystemDetails) {
	this.planid = planid;
	this.plandescription = plandescription;
	this.developerid = developerid;
	this.developername = developername;
	this.devmanagerid = devmanagerid;
	this.devmamangername = devmamangername;
	this.loadtype = loadtype;
	this.planstatus = planstatus;
	this.activateddatetime = activateddatetime;
	this.loaddatetime = loaddatetime;
	this.prodSystemDetails = prodSystemDetails;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	return "ProdPlanDetails [planid=" + planid + ", plandescription=" + plandescription + ", developerid=" + developerid + ", developername=" + developername + ", devmanagerid=" + devmanagerid + ", devmamangername=" + devmamangername + ", loadtype=" + loadtype + ", planstatus=" + planstatus + ", activateddatetime=" + activateddatetime + ", loaddatetime=" + loaddatetime + ", prodSystemDetails=" + prodSystemDetails + "]";
    }

}
