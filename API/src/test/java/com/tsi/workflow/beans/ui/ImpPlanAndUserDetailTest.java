package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.User;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.SystemLoad;
import org.junit.Test;

public class ImpPlanAndUserDetailTest {

    @Test
    public void test00() throws Throwable {
	ImpPlanAndUserDetail impPlanAndUserDetail = new ImpPlanAndUserDetail();
	impPlanAndUserDetail.setImpPlan(DataWareHouse.getPlan());
	ImpPlan imp = impPlanAndUserDetail.getImpPlan();
	assertNotEquals("", imp);
    }

    @Test
    public void test01() throws Throwable {
	ImpPlanAndUserDetail impPlanAndUserDetail = new ImpPlanAndUserDetail();
	impPlanAndUserDetail.setUser(DataWareHouse.getUser());
	User user = impPlanAndUserDetail.getUser();
	assertNotNull(user);
    }

    @Test
    public void test02() throws Throwable {
	ImpPlanAndUserDetail impPlanAndUserDetail = new ImpPlanAndUserDetail();
	SystemLoad sys = new SystemLoad();
	impPlanAndUserDetail.setSystemLoad(sys);
	assertNotNull(impPlanAndUserDetail.getSystemLoad());
    }

}
