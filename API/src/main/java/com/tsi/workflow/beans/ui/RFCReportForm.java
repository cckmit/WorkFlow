package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.Date;

public class RFCReportForm {

    private String targetsystem;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date loaddatetime;
    private String planid;
    private String rfcnumber;
    private String impdesc;
    private String incidentnumber;
    private String dbcr;
    private String loadcategory;
    private String breakfix;
    private String impactlevel;
    private String configvalue;
    private String leadname;
    private String managername;
    private String loadattendee;
    private String loadattendeecontact;
    private Boolean vsflag;
    private String vsarea;
    private Boolean vstestflag;
    private String vsdesc;
    private String approvalfilename;
    private String testFileName;
    private String programname;
    private String loadtype;
    private String planstatus;
    private String dbcrname;

    public String getVsarea() {
	return vsarea;
    }

    public void setVsarea(String vsarea) {
	this.vsarea = vsarea;
    }

    public String getTargetsystem() {
	return targetsystem;
    }

    public void setTargetsystem(String targetsystem) {
	this.targetsystem = targetsystem;
    }

    public Date getLoaddatetime() {
	return loaddatetime;
    }

    public void setLoaddatetime(Date loaddatetime) {
	this.loaddatetime = loaddatetime;
    }

    public String getPlanid() {
	return planid;
    }

    public void setPlanid(String planid) {
	this.planid = planid;
    }

    public String getImpdesc() {
	return impdesc;
    }

    public void setImpdesc(String impdesc) {
	this.impdesc = impdesc;
    }

    public String getIncidentnumber() {
	return incidentnumber;
    }

    public void setIncidentnumber(String incidentnumber) {
	this.incidentnumber = incidentnumber;
    }

    public String getDbcr() {
	return dbcr;
    }

    public void setDbcr(String dbcr) {
	this.dbcr = dbcr;
    }

    public String getLoadcategory() {
	return loadcategory;
    }

    public void setLoadcategory(String loadcategory) {
	this.loadcategory = loadcategory;
    }

    public String getBreakfix() {
	return breakfix;
    }

    public void setBreakfix(String breakfix) {
	this.breakfix = breakfix;
    }

    public String getImpactlevel() {
	return impactlevel;
    }

    public void setImpactlevel(String impactlevel) {
	this.impactlevel = impactlevel;
    }

    public String getConfigvalue() {
	return configvalue;
    }

    public void setConfigvalue(String configvalue) {
	this.configvalue = configvalue;
    }

    public String getLeadname() {
	return leadname;
    }

    public void setLeadname(String leadname) {
	this.leadname = leadname;
    }

    public String getManagername() {
	return managername;
    }

    public void setManagername(String managername) {
	this.managername = managername;
    }

    public String getLoadattendee() {
	return loadattendee;
    }

    public void setLoadattendee(String loadattendee) {
	this.loadattendee = loadattendee;
    }

    public String getLoadattendeecontact() {
	return loadattendeecontact;
    }

    public void setLoadattendeecontact(String loadattendeecontact) {
	this.loadattendeecontact = loadattendeecontact;
    }

    public Boolean getVsflag() {
	return vsflag;
    }

    public void setVsflag(Boolean vsflag) {
	this.vsflag = vsflag;
    }

    public Boolean getVstestflag() {
	return vstestflag;
    }

    public void setVstestflag(Boolean vstestflag) {
	this.vstestflag = vstestflag;
    }

    public String getTestFileName() {
	return testFileName;
    }

    public void setTestFileName(String testFileName) {
	this.testFileName = testFileName;
    }

    public String getProgramname() {
	return programname;
    }

    public void setProgramname(String programname) {
	this.programname = programname;
    }

    public String getLoadtype() {
	return loadtype;
    }

    public void setLoadtype(String loadtype) {
	this.loadtype = loadtype;
    }

    public String getPlanstatus() {
	return planstatus;
    }

    public void setPlanstatus(String planstatus) {
	this.planstatus = planstatus;
    }

    public String getDbcrname() {
	return dbcrname;
    }

    public void setDbcrname(String dbcrname) {
	this.dbcrname = dbcrname;
    }

    public String getRfcnumber() {
	return rfcnumber != null ? rfcnumber : "";
    }

    public void setRfcnumber(String rfcnumber) {
	this.rfcnumber = rfcnumber;
    }

    public String getApprovalfilename() {
	return approvalfilename;
    }

    public void setApprovalfilename(String approvalfilename) {
	this.approvalfilename = approvalfilename;
    }

    public String getVsdesc() {
	return vsdesc;
    }

    public void setVsdesc(String vsdesc) {
	this.vsdesc = vsdesc;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((breakfix == null) ? 0 : breakfix.hashCode());
	result = prime * result + ((configvalue == null) ? 0 : configvalue.hashCode());
	result = prime * result + ((dbcr == null) ? 0 : dbcr.hashCode());
	result = prime * result + ((impactlevel == null) ? 0 : impactlevel.hashCode());
	result = prime * result + ((impdesc == null) ? 0 : impdesc.hashCode());
	result = prime * result + ((incidentnumber == null) ? 0 : incidentnumber.hashCode());
	result = prime * result + ((leadname == null) ? 0 : leadname.hashCode());
	result = prime * result + ((loadattendee == null) ? 0 : loadattendee.hashCode());
	result = prime * result + ((loadattendeecontact == null) ? 0 : loadattendeecontact.hashCode());
	result = prime * result + ((loadcategory == null) ? 0 : loadcategory.hashCode());
	result = prime * result + ((loaddatetime == null) ? 0 : loaddatetime.hashCode());
	result = prime * result + ((managername == null) ? 0 : managername.hashCode());
	result = prime * result + ((planid == null) ? 0 : planid.hashCode());
	result = prime * result + ((rfcnumber == null) ? 0 : rfcnumber.hashCode());
	result = prime * result + ((targetsystem == null) ? 0 : targetsystem.hashCode());
	result = prime * result + ((vsarea == null) ? 0 : vsarea.hashCode());
	result = prime * result + ((vsflag == null) ? 0 : vsflag.hashCode());
	result = prime * result + ((vsdesc == null) ? 0 : vsdesc.hashCode());
	result = prime * result + ((vstestflag == null) ? 0 : vstestflag.hashCode());
	result = prime * result + ((planstatus == null) ? 0 : planstatus.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	RFCReportForm other = (RFCReportForm) obj;
	if (breakfix == null) {
	    if (other.breakfix != null)
		return false;
	} else if (!breakfix.equals(other.breakfix))
	    return false;
	if (configvalue == null) {
	    if (other.configvalue != null)
		return false;
	} else if (!configvalue.equals(other.configvalue))
	    return false;
	if (dbcr == null) {
	    if (other.dbcr != null)
		return false;
	} else if (!dbcr.equals(other.dbcr))
	    return false;
	if (impactlevel == null) {
	    if (other.impactlevel != null)
		return false;
	} else if (!impactlevel.equals(other.impactlevel))
	    return false;
	if (impdesc == null) {
	    if (other.impdesc != null)
		return false;
	} else if (!impdesc.equals(other.impdesc))
	    return false;
	if (incidentnumber == null) {
	    if (other.incidentnumber != null)
		return false;
	} else if (!incidentnumber.equals(other.incidentnumber))
	    return false;
	if (leadname == null) {
	    if (other.leadname != null)
		return false;
	} else if (!leadname.equals(other.leadname))
	    return false;
	if (loadattendee == null) {
	    if (other.loadattendee != null)
		return false;
	} else if (!loadattendee.equals(other.loadattendee))
	    return false;
	if (loadattendeecontact == null) {
	    if (other.loadattendeecontact != null)
		return false;
	} else if (!loadattendeecontact.equals(other.loadattendeecontact))
	    return false;
	if (loadcategory == null) {
	    if (other.loadcategory != null)
		return false;
	} else if (!loadcategory.equals(other.loadcategory))
	    return false;
	if (loaddatetime == null) {
	    if (other.loaddatetime != null)
		return false;
	} else if (!loaddatetime.equals(other.loaddatetime))
	    return false;
	if (managername == null) {
	    if (other.managername != null)
		return false;
	} else if (!managername.equals(other.managername))
	    return false;
	if (planid == null) {
	    if (other.planid != null)
		return false;
	} else if (!planid.equals(other.planid))
	    return false;
	if (rfcnumber == null) {
	    if (other.rfcnumber != null)
		return false;
	} else if (!rfcnumber.equals(other.rfcnumber))
	    return false;
	if (targetsystem == null) {
	    if (other.targetsystem != null)
		return false;
	} else if (!targetsystem.equals(other.targetsystem))
	    return false;
	if (vsarea == null) {
	    if (other.vsarea != null)
		return false;
	} else if (!vsarea.equals(other.vsarea))
	    return false;
	if (vsdesc == null) {
	    if (other.vsdesc != null)
		return false;
	} else if (!vsdesc.equals(other.vsdesc))
	    return false;
	if (vsflag == null) {
	    if (other.vsflag != null)
		return false;
	} else if (!vsflag.equals(other.vsflag))
	    return false;
	if (vstestflag == null) {
	    if (other.vstestflag != null)
		return false;
	} else if (!vstestflag.equals(other.vstestflag))
	    return false;
	if (loadtype == null) {
	    if (other.loadtype != null)
		return false;
	} else if (!loadtype.equals(other.loadtype))
	    return false;
	if (planstatus == null) {
	    if (other.planstatus != null)
		return false;
	} else if (!planstatus.equals(other.planstatus))
	    return false;
	return true;
    }

}
