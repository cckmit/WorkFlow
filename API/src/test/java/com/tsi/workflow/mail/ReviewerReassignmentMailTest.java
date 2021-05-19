package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import com.tsi.workflow.User;
import com.tsi.workflow.utils.Constants;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;

public class ReviewerReassignmentMailTest {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	User user0 = new User((String) null);
	user0.setCurrentDelegatedUser(user0);
	reviewerReassignmentMail0.setUser(user0);
	reviewerReassignmentMail0.setImplementationId("open0: ");
	reviewerReassignmentMail0.processMessage();
    }

    @Test(timeout = 4000)
    public void test001() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	reviewerReassignmentMail0.setRemoved(true);
	User user0 = new User((String) null);
	// user0.setCurrentDelegatedUser(user0);
	reviewerReassignmentMail0.setUser(user0);
	reviewerReassignmentMail0.setImplementationId("open0: ");
	reviewerReassignmentMail0.processMessage();
    }

    @Test(timeout = 4000)
    public void test002() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	reviewerReassignmentMail0.setRemoved(true);
	User user0 = new User((String) null);
	user0.setCurrentDelegatedUser(user0);
	reviewerReassignmentMail0.setUser(user0);
	reviewerReassignmentMail0.setImplementationId("open0: ");
	reviewerReassignmentMail0.processMessage();
    }

    @Test(timeout = 4000)
    public void test01() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	User user0 = new User((String) null);
	reviewerReassignmentMail0.setUser(user0);
	reviewerReassignmentMail0.setImplementationId("open0: ");
	reviewerReassignmentMail0.processMessage();
    }

    @Test(timeout = 4000)
    public void test02() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	LinkedList<String> linkedList0 = new LinkedList<String>();
	reviewerReassignmentMail0.setAddedreviewersName(linkedList0);
	assertFalse(reviewerReassignmentMail0.isRemoved());
    }

    @Test(timeout = 4000)
    public void test03() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	reviewerReassignmentMail0.setRemoved(true);
	boolean boolean0 = reviewerReassignmentMail0.isRemoved();
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test04() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	reviewerReassignmentMail0.setNewRequest(true);
	boolean boolean0 = reviewerReassignmentMail0.isNewRequest();
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test05() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	User user0 = new User("");
	user0.setDelegated(true);
	reviewerReassignmentMail0.setUser(user0);
	User user1 = reviewerReassignmentMail0.getUser();
	assertEquals("", user1.getId());
    }

    @Test(timeout = 4000)
    public void test06() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	User user0 = new User("DEBUG");
	user0.setAllowDelegateMenu(true);
	reviewerReassignmentMail0.setUser(user0);
	User user1 = reviewerReassignmentMail0.getUser();
	assertNull(user1.getMailId());
    }

    @Test(timeout = 4000)
    public void test07() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	reviewerReassignmentMail0.setReviewersId((List<String>) null);
	List<String> list0 = reviewerReassignmentMail0.getReviewersId();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test08() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	reviewerReassignmentMail0.removedreviewersName = reviewerReassignmentMail0.reviewersId;
	reviewerReassignmentMail0.removedreviewersName = null;
	List<String> list0 = reviewerReassignmentMail0.getRemovedreviewersName();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test09() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	reviewerReassignmentMail0.setNewRequest(true);
	User user0 = new User();
	reviewerReassignmentMail0.lUser = user0;
	reviewerReassignmentMail0.processMessage();
	List<String> list0 = reviewerReassignmentMail0.getReviewersId();
	reviewerReassignmentMail0.setRemovedreviewersName(list0);
	reviewerReassignmentMail0.getRemovedreviewersName();
	assertTrue(reviewerReassignmentMail0.isNewRequest());
    }

    @Test(timeout = 4000)
    public void test10() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	reviewerReassignmentMail0.setImplementationId("gi");
	String string0 = reviewerReassignmentMail0.getImplementationId();
	assertEquals("gi", string0);
    }

    @Test(timeout = 4000)
    public void test11() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	reviewerReassignmentMail0.implementationId = "";
	String string0 = reviewerReassignmentMail0.getImplementationId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test12() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	reviewerReassignmentMail0.developerId = "L3EOT)iSpoo}";
	String string0 = reviewerReassignmentMail0.getDeveloperId();
	assertEquals("L3EOT)iSpoo}", string0);
    }

    @Test(timeout = 4000)
    public void test13() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	reviewerReassignmentMail0.setDeveloperId("");
	String string0 = reviewerReassignmentMail0.getDeveloperId();
	assertEquals("", string0);
    }

    @Test(timeout = 4000)
    public void test14() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	reviewerReassignmentMail0.addedreviewersName = null;
	List<String> list0 = reviewerReassignmentMail0.getAddedreviewersName();
	assertNull(list0);
    }

    @Test(timeout = 4000)
    public void test15() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	List<String> list0 = reviewerReassignmentMail0.getRemovedreviewersName();
	assertEquals(0, list0.size());
    }

    @Test(timeout = 4000)
    public void test16() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	String string0 = reviewerReassignmentMail0.getImplementationId();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test17() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	List<String> list0 = reviewerReassignmentMail0.getAddedreviewersName();
	reviewerReassignmentMail0.addcCAddressUserId(list0.get(0), Constants.MailSenderRole.LEAD);
	reviewerReassignmentMail0.addcCAddressUserId("{0} has added {1} as Reviewer for {2} on behalf of {3}", Constants.MailSenderRole.LEAD);
	List<String> list1 = reviewerReassignmentMail0.getAddedreviewersName();
	assertFalse(list1.contains("userSettingsDAO"));
    }

    @Test(timeout = 4000)
    public void test18() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	User user0 = new User((String) null);
	user0.setCurrentDelegatedUser(user0);
	reviewerReassignmentMail0.setUser(user0);
	reviewerReassignmentMail0.processMessage();
	assertEquals("Review task Added - null", reviewerReassignmentMail0.getSubject());
    }

    @Test(timeout = 4000)
    public void test19() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	User user0 = new User((String) null);
	user0.setCurrentDelegatedUser(user0);
	reviewerReassignmentMail0.setUser(user0);
	reviewerReassignmentMail0.setReAssigned(true);
	reviewerReassignmentMail0.processMessage();
	assertTrue(reviewerReassignmentMail0.isReAssigned());
    }

    @Test(timeout = 4000)
    public void test20() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	reviewerReassignmentMail0.setRemoved(true);
	// Undeclared exception!
	try {
	    reviewerReassignmentMail0.processMessage();
	    fail("Expecting exception: NullPointerException");

	} catch (NullPointerException e) {
	    //
	    // no message in exception (getMessage() returned null)
	    //
	}
    }

    @Test(timeout = 4000)
    public void test21() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	User user0 = new User();
	reviewerReassignmentMail0.lUser = user0;
	reviewerReassignmentMail0.setReAssigned(true);
	reviewerReassignmentMail0.processMessage();
	assertTrue(reviewerReassignmentMail0.isReAssigned());
    }

    @Test(timeout = 4000)
    public void test22() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	reviewerReassignmentMail0.setReAssigned(true);
	boolean boolean0 = reviewerReassignmentMail0.isReAssigned();
	assertTrue(boolean0);
    }

    @Test(timeout = 4000)
    public void test23() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	LinkedList<String> linkedList0 = new LinkedList<String>();
	reviewerReassignmentMail0.setRemovedreviewersName(linkedList0);
	assertNull(reviewerReassignmentMail0.getSubject());
    }

    @Test(timeout = 4000)
    public void test24() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	reviewerReassignmentMail0.setNewRequest(true);
	User user0 = new User();
	reviewerReassignmentMail0.lUser = user0;
	reviewerReassignmentMail0.setReAssigned(true);
	reviewerReassignmentMail0.processMessage();
	assertTrue(reviewerReassignmentMail0.isReAssigned());
    }

    @Test(timeout = 4000)
    public void test25() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	List<String> list0 = reviewerReassignmentMail0.getReviewersId();
	reviewerReassignmentMail0.setAddedreviewersName(list0);
	assertTrue(list0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test26() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	boolean boolean0 = reviewerReassignmentMail0.isRemoved();
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test27() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	boolean boolean0 = reviewerReassignmentMail0.isNewRequest();
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test28() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	String string0 = reviewerReassignmentMail0.getDeveloperId();
	assertNull(string0);
    }

    @Test(timeout = 4000)
    public void test29() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	boolean boolean0 = reviewerReassignmentMail0.isReAssigned();
	assertFalse(boolean0);
    }

    @Test(timeout = 4000)
    public void test30() throws Throwable {
	ReviewerReassignmentMail reviewerReassignmentMail0 = new ReviewerReassignmentMail();
	User user0 = reviewerReassignmentMail0.getUser();
	assertNull(user0);
    }
}
