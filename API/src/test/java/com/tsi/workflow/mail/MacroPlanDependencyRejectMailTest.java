package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class MacroPlanDependencyRejectMailTest {

    @Test
    public void test00() throws Throwable {
	MacroPlanDependencyRejectMail macroPlanDependencyRejectMail = new MacroPlanDependencyRejectMail();
	macroPlanDependencyRejectMail.setActionType("actionType");
	assertEquals("actionType", macroPlanDependencyRejectMail.getActionType());
	List<String> list = new ArrayList<String>();
	list.add("A");
	list.add("B");
	macroPlanDependencyRejectMail.setDependencyPlanMsg(list);
	assertEquals(list, macroPlanDependencyRejectMail.getDependencyPlanMsg());
	macroPlanDependencyRejectMail.setMailIds(list);
	assertEquals(list, macroPlanDependencyRejectMail.getMailIds());
	macroPlanDependencyRejectMail.setMessage("message");
	assertEquals("message", macroPlanDependencyRejectMail.getMessage());
	macroPlanDependencyRejectMail.setPlanId("T1900111");
	assertEquals("T1900111", macroPlanDependencyRejectMail.getPlanId());
	macroPlanDependencyRejectMail.processMessage();

    }

}
