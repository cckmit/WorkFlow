/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dto.SystemLoadDTO;
import java.util.List;

/**
 *
 * @author ramkumar.seenivasan
 */
public class ImplementationInboxView {

    private Implementation impl;
    private List<SystemLoadDTO> systemLoadDetails;

    public Implementation getImpl() {
	return impl;
    }

    public void setImpl(Implementation impl) {
	this.impl = impl;
    }

    public List<SystemLoadDTO> getSystemLoadDetails() {
	return systemLoadDetails;
    }

    public void setSystemLoadDetails(List<SystemLoadDTO> systemLoadDetails) {
	this.systemLoadDetails = systemLoadDetails;
    }
}
