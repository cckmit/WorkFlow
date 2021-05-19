/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.ui;

import com.tsi.workflow.beans.dao.LoadFreeze;
import com.tsi.workflow.beans.dao.System;

/**
 *
 * @author USER
 */
public class LoadFreezeForm {

    private LoadFreeze loadFreeze;
    private System system;

    public LoadFreezeForm() {
    }

    public LoadFreezeForm(LoadFreeze loadFreeze, System system) {
	this.loadFreeze = loadFreeze;
	this.system = system;
    }

    public LoadFreeze getLoadFreeze() {
	return loadFreeze;
    }

    public void setLoadFreeze(LoadFreeze loadFreeze) {
	this.loadFreeze = loadFreeze;
    }

    public System getSystem() {
	return system;
    }

    public void setSystem(System system) {
	this.system = system;
    }

    @Override
    public boolean equals(Object obj) {
	if (!(obj instanceof LoadFreezeForm)) {
	    return false;
	}
	LoadFreezeForm lloadFreezeForm = (LoadFreezeForm) obj;
	return loadFreeze.equals(lloadFreezeForm.loadFreeze);
    }

}
