package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import com.tsi.workflow.utils.Constants;
import java.math.BigInteger;
import java.util.Date;

public class SystemBasedMetaData {

    private String planid;
    private String targetsys;
    private String funcarea;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date loaddatetime;
    private String loadcategory;
    private String planstatus;
    private String devid;
    private String qabypassstatus;
    private String loadtype;
    private String leadid;
    private String projnumber;
    private String sdmtktnum;
    private String prtktnum;
    private String dbcrname;
    private String plandesc;
    private String devmanager;
    private String loadattendeeid;
    private String progname;
    private Integer statusrank;
    private String developername;
    private String developerEmail;
    private String reviewerName;
    private String reviewerMail;
    private String adlName;
    private String adlMail;
    private String managerName;
    private String managerMail;
    private String approver;
    private String approvingManName;
    private String approvingManMail;
    private String laodAttnName;
    private String loadAttnMail;
    private String peerreviewerids;
    private String derivedObj;
    private String srcObj;
    private String impid;
    private String filetype;
    private String commitid;
    private String repodetail;
    private String filename;
    private String devmanagername;
    private String loadsettype;
    private String lineofbusiness;
    private String qafunctionaltester;
    private String rfcnumber;

    public String getLoadsettype() {
	return loadsettype;
    }

    public void setLoadsettype(String loadsettype) {
	this.loadsettype = loadsettype;
    }

    public String getPlanid() {
	return planid;
    }

    public void setPlanid(String planid) {
	this.planid = planid;
    }

    public String getTargetsys() {
	return targetsys;
    }

    public void setTargetsys(String targetsys) {
	this.targetsys = targetsys;
    }

    public String getFuncarea() {
	return funcarea;
    }

    public void setFuncarea(String funcarea) {
	this.funcarea = funcarea;
    }

    public Date getLoaddatetime() {
	return loaddatetime;
    }

    public void setLoaddatetime(Date loaddatetime) {
	this.loaddatetime = loaddatetime;
    }

    public String getLoadcategory() {
	return loadcategory;
    }

    public void setLoadcategory(String loadcategory) {
	this.loadcategory = loadcategory;
    }

    public String getPlanstatus() {
	return planstatus.toUpperCase();
    }

    public void setPlanstatus(String planstatus) {
	this.planstatus = planstatus;
    }

    public String getDevid() {
	return devid;
    }

    public void setDevid(String devid) {
	this.devid = devid;
    }

    public String getQabypassstatus() {
	return qabypassstatus;
    }

    public void setQabypassstatus(String qabypassstatus) {
	this.qabypassstatus = qabypassstatus;
    }

    public String getLoadtype() {
	return loadtype;
    }

    public void setLoadtype(String loadtype) {
	this.loadtype = loadtype;
    }

    public String getLeadid() {
	return leadid;
    }

    public void setLeadid(String leadid) {
	this.leadid = leadid;
    }

    public String getProjnumber() {
	return projnumber;
    }

    public void setProjnumber(String projnumber) {
	this.projnumber = projnumber;
    }

    public String getSdmtktnum() {
	return sdmtktnum;
    }

    public void setSdmtktnum(String sdmtktnum) {
	this.sdmtktnum = sdmtktnum;
    }

    public String getPrtktnum() {
	return prtktnum;
    }

    public void setPrtktnum(String prtktnum) {
	this.prtktnum = prtktnum;
    }

    public String getDbcrname() {
	return dbcrname;
    }

    public void setDbcrname(String dbcrname) {
	this.dbcrname = dbcrname;
    }

    public String getPlandesc() {
	return plandesc;
    }

    public void setPlandesc(String plandesc) {
	this.plandesc = plandesc;
    }

    public String getDevmanager() {
	return devmanager;
    }

    public void setDevmanager(String devmanager) {
	this.devmanager = devmanager;
    }

    public String getLoadattendeeid() {
	return loadattendeeid;
    }

    public void setLoadattendeeid(String loadattendeeid) {
	this.loadattendeeid = loadattendeeid;
    }

    public String getProgname() {
	return progname;
    }

    public void setProgname(String progname) {
	this.progname = progname;
    }

    public Integer getStatusrank() {
	return statusrank;
    }

    public void setStatusrank(BigInteger statusrank) {
	this.statusrank = statusrank.intValue();
    }

    public String getDevelopername() {
	return developername;
    }

    public void setDevelopername(String developername) {
	this.developername = developername;
    }

    public String getDeveloperEmail() {
	return developerEmail;
    }

    public void setDeveloperEmail(String developerEmail) {
	this.developerEmail = developerEmail;
    }

    public String getReviewerName() {
	return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
	this.reviewerName = reviewerName;
    }

    public String getReviewerMail() {
	return reviewerMail;
    }

    public void setReviewerMail(String reviewerMail) {
	this.reviewerMail = reviewerMail;
    }

    public String getAdlName() {
	return adlName;
    }

    public void setAdlName(String adlName) {
	this.adlName = adlName;
    }

    public String getAdlMail() {
	return adlMail;
    }

    public void setAdlMail(String adlMail) {
	this.adlMail = adlMail;
    }

    public String getManagerName() {
	return managerName;
    }

    public void setManagerName(String managerName) {
	this.managerName = managerName;
    }

    public String getManagerMail() {
	return managerMail;
    }

    public void setManagerMail(String managerMail) {
	this.managerMail = managerMail;
    }

    public String getApprovingManName() {
	return approvingManName;
    }

    public void setApprovingManName(String approvingManName) {
	this.approvingManName = approvingManName;
    }

    public String getApprovingManMail() {
	return approvingManMail;
    }

    public void setApprovingManMail(String approvingManMail) {
	this.approvingManMail = approvingManMail;
    }

    public String getLaodAttnName() {
	return laodAttnName;
    }

    public void setLaodAttnName(String laodAttnName) {
	this.laodAttnName = laodAttnName;
    }

    public String getLoadAttnMail() {
	return loadAttnMail;
    }

    public void setLoadAttnMail(String loadAttnMail) {
	this.loadAttnMail = loadAttnMail;
    }

    public String getApprover() {
	return approver;
    }

    public void setApprover(String approver) {
	this.approver = approver;
    }

    public String getPeerreviewerids() {
	return peerreviewerids;
    }

    public void setPeerreviewerids(String peerreviewerids) {
	this.peerreviewerids = peerreviewerids;
    }

    public String getDerivedObj() {
	return derivedObj;
    }

    public void setDerivedObj(String derivedObj) {
	this.derivedObj = derivedObj;
    }

    public String getSrcObj() {
	return srcObj;
    }

    public void setSrcObj(String srcObj) {
	this.srcObj = srcObj;
    }

    public String getImpid() {
	return impid;
    }

    public void setImpid(String impid) {
	this.impid = impid;
    }

    public String getFiletype() {
	return filetype;
    }

    public void setFiletype(String filetype) {
	this.filetype = filetype;
    }

    public String isPlanBypassedRegression() {
	String result = "No";
	if (this.qabypassstatus != null && !this.qabypassstatus.isEmpty() && (this.qabypassstatus.equals(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_BOTH.name()) || this.qabypassstatus.equals(Constants.SYSTEM_QA_TESTING_STATUS.BYPASSED_REGRESSION_TESTING.name()))) {
	    result = "Yes";
	}
	return result;
    }

    public String getCommitid() {
	return commitid;
    }

    public void setCommitid(String commitid) {
	this.commitid = commitid;
    }

    public String getRepodetail() {
	return repodetail;
    }

    public void setRepodetail(String repodetail) {
	this.repodetail = repodetail;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

    public String getDevmanagername() {
	return devmanagername;
    }

    public void setDevmanagername(String devmanagername) {
	this.devmanagername = devmanagername;
    }

    public String getLineofbusiness() {
	return lineofbusiness;
    }

    public void setLineofbusiness(String lineofbusiness) {
	this.lineofbusiness = lineofbusiness;
    }

    public String getQafunctionaltester() {
	return qafunctionaltester;
    }

    public void setQafunctionaltester(String qafunctionaltester) {
	this.qafunctionaltester = qafunctionaltester;
    }

    public String getRfcnumber() {
	return rfcnumber;
    }

    public void setRfcnumber(String rfcnumber) {
	this.rfcnumber = rfcnumber;
    }

}
