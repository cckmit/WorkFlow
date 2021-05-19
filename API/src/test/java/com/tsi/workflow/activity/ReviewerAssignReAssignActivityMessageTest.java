
package com.tsi.workflow.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.tsi.workflow.User;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Priority;
import org.junit.Test;

/**
 * @author vinoth.ponnurangan
 *
 */
public class ReviewerAssignReAssignActivityMessageTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	ReviewerAssignReAssignActivityMessage reviewerAssignReAssignActivityMessage0 = new ReviewerAssignReAssignActivityMessage(impPlan0, implementation0);
	reviewerAssignReAssignActivityMessage0.setReviewerList((List<String>) null);
	List<String> list0 = reviewerAssignReAssignActivityMessage0.getReviewerList();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	ReviewerAssignReAssignActivityMessage reviewerAssignReAssignActivityMessage0 = new ReviewerAssignReAssignActivityMessage((ImpPlan) null, (Implementation) null);
	LinkedList<String> linkedList0 = new LinkedList<String>();
	linkedList0.add("uvNXsP^T;%ZVTdx6");
	reviewerAssignReAssignActivityMessage0.setReviewerList(linkedList0);
	List<String> list0 = reviewerAssignReAssignActivityMessage0.getReviewerList();
	assertTrue(list0.contains("uvNXsP^T;%ZVTdx6"));
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	ReviewerAssignReAssignActivityMessage reviewerAssignReAssignActivityMessage0 = new ReviewerAssignReAssignActivityMessage(impPlan0, (Implementation) null);
	reviewerAssignReAssignActivityMessage0.setAction(", on behalf of ");
	String string0 = reviewerAssignReAssignActivityMessage0.getAction();
	assertEquals(", on behalf of ", string0);
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	Implementation implementation0 = new Implementation("DYNA-IC");
	ReviewerAssignReAssignActivityMessage reviewerAssignReAssignActivityMessage0 = new ReviewerAssignReAssignActivityMessage((ImpPlan) null, implementation0);
	reviewerAssignReAssignActivityMessage0.setAction("");
	String string0 = reviewerAssignReAssignActivityMessage0.getAction();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	ReviewerAssignReAssignActivityMessage reviewerAssignReAssignActivityMessage0 = new ReviewerAssignReAssignActivityMessage(impPlan0, implementation0);
	String string0 = reviewerAssignReAssignActivityMessage0.getAction();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	Implementation implementation0 = new Implementation();
	Boolean boolean0 = Boolean.TRUE;
	implementation0.setBypassPeerReview(boolean0);
	ImpPlan impPlan0 = new ImpPlan();
	ReviewerAssignReAssignActivityMessage reviewerAssignReAssignActivityMessage0 = new ReviewerAssignReAssignActivityMessage(impPlan0, implementation0);
	User user0 = new User();
	user0.setCurrentDelegatedUser(user0);
	reviewerAssignReAssignActivityMessage0.setUser(user0);
	String string0 = reviewerAssignReAssignActivityMessage0.processMessage();
	assertEquals("null null has Bypassed Peer Review for  the implementation - null, on behalf of null", string0);
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	ReviewerAssignReAssignActivityMessage reviewerAssignReAssignActivityMessage0 = new ReviewerAssignReAssignActivityMessage(impPlan0, implementation0);
	User user0 = new User("added");
	reviewerAssignReAssignActivityMessage0.setUser(user0);
	reviewerAssignReAssignActivityMessage0.setAction("added");
	Boolean boolean0 = Boolean.valueOf("?");
	implementation0.setBypassPeerReview(boolean0);
	String string0 = reviewerAssignReAssignActivityMessage0.processMessage();
	assertEquals("null null has added reviewer -  for the implementation - null", string0);
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	ReviewerAssignReAssignActivityMessage reviewerAssignReAssignActivityMessage0 = new ReviewerAssignReAssignActivityMessage(impPlan0, implementation0);
	reviewerAssignReAssignActivityMessage0.setAction("`deed");
	Boolean boolean0 = Boolean.valueOf("f");
	implementation0.setBypassPeerReview(boolean0);
	// Undeclared exception!
	try {
	    reviewerAssignReAssignActivityMessage0.processMessage();
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //
	}
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	Implementation implementation0 = new Implementation();
	ReviewerAssignReAssignActivityMessage reviewerAssignReAssignActivityMessage0 = new ReviewerAssignReAssignActivityMessage((ImpPlan) null, implementation0);
	Priority priority0 = reviewerAssignReAssignActivityMessage0.getLogLevel();
	assertEquals(Integer.MAX_VALUE, Priority.OFF_INT);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	ImpPlan impPlan0 = new ImpPlan();
	Implementation implementation0 = new Implementation();
	ReviewerAssignReAssignActivityMessage reviewerAssignReAssignActivityMessage0 = new ReviewerAssignReAssignActivityMessage(impPlan0, implementation0);
	List<String> list0 = reviewerAssignReAssignActivityMessage0.getReviewerList();
	reviewerAssignReAssignActivityMessage0.setReviewerList(list0);
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	Implementation implementation0 = new Implementation();
	ReviewerAssignReAssignActivityMessage reviewerAssignReAssignActivityMessage0 = new ReviewerAssignReAssignActivityMessage((ImpPlan) null, implementation0);
	IBeans[] iBeansArray0 = new IBeans[1];
	reviewerAssignReAssignActivityMessage0.setArguments(iBeansArray0);
	assertNull(reviewerAssignReAssignActivityMessage0.getAction());
    }
}
