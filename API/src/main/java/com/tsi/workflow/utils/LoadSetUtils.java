/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.SystemLoad;

/**
 *
 * @author USER
 */
public class LoadSetUtils {

    public static String getLoadSetName(ImpPlan pPlan, SystemLoad pSystemLoad) {
	return pSystemLoad.getSystemId().getLoadsetNamePrefix() + pSystemLoad.getLoadCategoryId().getName() + pPlan.getId().substring(2);
    }

    public static String getFallbackLoadSetName(ImpPlan pPlan, SystemLoad pSystemLoad) {
	return pSystemLoad.getSystemId().getLoadsetNamePrefix() + "B" + pPlan.getId().substring(2);
    }
}
