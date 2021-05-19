/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.System;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gn.ebinezardharmaraj
 */
public class SourceArtifactSearchForm {

    private String sourceArtifactName;
    private String fileType;
    private List<System> targetSys = new ArrayList<System>();

    public String getSourceArtifactName() {
	return sourceArtifactName;
    }

    public void setSourceArtifactName(String sourceArtifactName) {
	this.sourceArtifactName = sourceArtifactName;
    }

    public String getFileType() {
	return fileType;
    }

    public void setFileType(String fileType) {
	this.fileType = fileType;
    }

    public List<System> getTargetSys() {
	return targetSys;
    }

    public void setTargetSys(List<System> targetSys) {
	this.targetSys = targetSys;
    }

}
