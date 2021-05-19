package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import java.util.Date;
import org.junit.Test;

public class LoadDatePassedMacroPlanTest {

    @Test
    public void test00() {
	LoadDatePassedMacroPlan loadDatePassedMacroPlan = new LoadDatePassedMacroPlan();
	loadDatePassedMacroPlan.setImpPlan("T1900111");
	assertEquals("T1900111", loadDatePassedMacroPlan.getImpPlan());
	loadDatePassedMacroPlan.setLoadDate(new Date());
	assertEquals(new Date(), loadDatePassedMacroPlan.getLoadDate());
	loadDatePassedMacroPlan.processMessage();
    }

}
