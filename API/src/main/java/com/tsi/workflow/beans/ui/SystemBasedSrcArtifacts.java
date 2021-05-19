/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.Date;

/**
 *
 * @author Radha.Adhimoolam
 */
public class SystemBasedSrcArtifacts {

    private String sorucecommitid;
    private String subsourcerepo;
    private String sourcerepo;
    private String filename;
    private String progname;
    private String fileext;
    // private String srcartifactLink;
    private String targetsystem;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date loaddatetime;

    public String getSorucecommitid() {
	return sorucecommitid;
    }

    public void setSorucecommitid(String sorucecommitid) {
	this.sorucecommitid = sorucecommitid;
    }

    public String getSubsourcerepo() {
	return subsourcerepo;
    }

    public void setSubsourcerepo(String subsourcerepo) {
	this.subsourcerepo = subsourcerepo;
    }

    public String getSourcerepo() {
	return sourcerepo;
    }

    public void setSourcerepo(String sourcerepo) {
	this.sourcerepo = sourcerepo;
    }

    public String getFilename() {
	return filename;
    }

    public void setFilename(String filename) {
	this.filename = filename;
    }

    public String getProgname() {
	return progname;
    }

    public void setProgname(String progname) {
	this.progname = progname;
    }

    public String getFileext() {
	return fileext;
    }

    public void setFileext(String fileext) {
	this.fileext = fileext;
    }

    // public String getSrcArtifactLink() {
    // return srcartifactLink;
    // }
    //
    // public void setSrcArtifactLink(String srcartifactLink) {
    // this.srcartifactLink = srcArtifactLink;
    // }

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

}
