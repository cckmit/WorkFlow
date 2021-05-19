/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.helper;

import com.tsi.workflow.beans.dao.SystemLoadActions;
import java.util.Comparator;

/**
 *
 * @author USER
 */
public class LoadActivationCompare implements Comparator<SystemLoadActions> {

    private boolean descending;

    public LoadActivationCompare(boolean descending) {
	this.descending = descending;
    }

    @Override
    public int compare(SystemLoadActions o1, SystemLoadActions o2) {
	return (descending ? -1 : 1) * (o1.getSystemLoadId().getLoadDateTime().compareTo(o2.getSystemLoadId().getLoadDateTime()));
    }

}
