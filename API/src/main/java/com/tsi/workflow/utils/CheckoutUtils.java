/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.utils;

import com.tsi.workflow.beans.dao.CheckoutSegments;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;

/**
 *
 * @author User
 */
public class CheckoutUtils {

    public static String getIdString(CheckoutSegments lSegment) {
	return lSegment.getProgramName() + "|" + lSegment.getFileHashCode() + "|" + lSegment.getFuncArea() + "|" + lSegment.getTargetSystem();
    }

    public static String getIBMIdString(CheckoutSegments lSegment) {
	return lSegment.getProgramName() + "|" + lSegment.getTargetSystem();
    }

    public static String getIdStringWithOutSystem(CheckoutSegments lSegment) {
	if (lSegment.getCommonFile()) {
	    return lSegment.getProgramName() + "|" + lSegment.getFileHashCode() + "|" + lSegment.getFuncArea();
	} else {
	    return lSegment.getProgramName() + "|" + lSegment.getFileHashCode() + "|" + lSegment.getFuncArea() + "|" + RandomStringUtils.randomAlphanumeric(10);
	}
    }

    public static String getIdStringWithPlanSysCheck(CheckoutSegments lSegment) {
	return lSegment.getProgramName() + "|" + lSegment.getFileHashCode() + "|" + lSegment.getFuncArea() + "|" + lSegment.getPlanId().getId();
    }

    public static String getIdStringWithPlan(CheckoutSegments lSegment) {
	if (lSegment.getCommonFile()) {
	    return lSegment.getProgramName() + "|" + lSegment.getFileHashCode() + "|" + lSegment.getFuncArea() + "|" + lSegment.getPlanId().getId();
	} else {
	    return lSegment.getProgramName() + "|" + lSegment.getFileHashCode() + "|" + lSegment.getFuncArea() + "|" + lSegment.getPlanId().getId() + "|" + RandomStringUtils.randomAlphanumeric(10);
	}
    }

    public static String getHexStringWithPlanSegment(CheckoutSegments lSegment) {
	return DigestUtils.md5Hex(lSegment.getProgramName() + "|" + lSegment.getFileHashCode() + "|" + lSegment.getFuncArea() + "|" + lSegment.getPlanId().getId() + "|" + lSegment.getImpId().getId() + "|" + lSegment.getTargetSystem());
    }

    public static String getIdStringWithFileName(CheckoutSegments lSegment) {
	return lSegment.getFileName() + "|" + lSegment.getTargetSystem();
    }

}
