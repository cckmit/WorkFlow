package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import org.junit.Test;

public class DependentPlanRejectDetailTest {

    @Test
    public void test001() throws Throwable {
	DependentPlanRejectDetail dependentPlanRejectDetail = new DependentPlanRejectDetail(null, null, null, null);
	dependentPlanRejectDetail.setPlanId("T1900111");
	String planId = dependentPlanRejectDetail.getPlanId();
	assertEquals("T1900111", planId);
    }

    @Test
    public void test002() throws Throwable {
	DependentPlanRejectDetail dependentPlanRejectDetail = new DependentPlanRejectDetail(null, null, null, null);
	dependentPlanRejectDetail.setAutoRejectReason("autoRejectReason");
	String autoRejectReason = dependentPlanRejectDetail.getAutoRejectReason();
	assertNotEquals("RejectReason", autoRejectReason);
    }

    @Test
    public void test003() throws Throwable {
	DependentPlanRejectDetail dependentPlanRejectDetail = new DependentPlanRejectDetail(null, null, null, null);
	dependentPlanRejectDetail.setRejectReason("Reject");
	String rejectReason = dependentPlanRejectDetail.getRejectReason();
	assertEquals("Reject", rejectReason);
    }

    @Test
    public void test004() throws Throwable {
	DependentPlanRejectDetail dependentPlanRejectDetail = new DependentPlanRejectDetail(null, null, null, null);
	dependentPlanRejectDetail.setUser(DataWareHouse.getUser());
	User user = dependentPlanRejectDetail.getUser();
	assertEquals(DataWareHouse.getUser(), user);
    }
}
