package com.tsi.workflow.mail;

import static org.junit.Assert.assertEquals;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.utils.Constants;
import java.util.Arrays;
import org.junit.Test;

public class NotificationMailTest {

    @Test
    public void getDeveloperNotificationMailtest() {
	assertEquals("T1800089_01", getDeveloperDetails().getImplementationId());
	assertEquals("Sample Test", getDeveloperDetails().getMessage());
	assertEquals("e738090", getDeveloperDetails().getReviewer());
	assertEquals("Peer review completed", getDeveloperDetails().getSubject());
	assertEquals(DataWareHouse.PlanSSHUrl, getDeveloperDetails().getTicketUrl());
	assertEquals(DataWareHouse.getUser(), getDeveloperDetails().getUser());
	getDeveloperDetails().processMessage();

    }

    public DeveloperNotificationMail getDeveloperDetails() {
	DeveloperNotificationMail developerNotificationMail = new DeveloperNotificationMail();
	developerNotificationMail.setImplementationId("T1800089_01");
	developerNotificationMail.setMessage("Sample Test");
	developerNotificationMail.setReviewer("e738090");
	developerNotificationMail.setSubject("Peer review completed");
	developerNotificationMail.setTicketUrl(DataWareHouse.PlanSSHUrl);
	developerNotificationMail.setUser(DataWareHouse.getUser());
	return developerNotificationMail;
    }

    @Test
    public void getReassignmentAccessMailTest() {
	assertEquals("e738090", getReassignmentAccessMail().getDevId());
	assertEquals("T1800090_01", getReassignmentAccessMail().getImpId());
	assertEquals(1, getReassignmentAccessMail().getProgramName().size());
	assertEquals(DataWareHouse.RepoName, getReassignmentAccessMail().getRepoName());
	assertEquals(2, getReassignmentAccessMail().getProgramName().size());
	assertEquals(2, getReassignmentAccessMail().getProgramName().size());
	getReassignmentAccessMail().processMessage();

    }

    public DeveloperReassignmentAccessMail getReassignmentAccessMail() {
	DeveloperReassignmentAccessMail developerReassignmentAccessMail = new DeveloperReassignmentAccessMail();
	developerReassignmentAccessMail.setDevId("e738090");
	developerReassignmentAccessMail.setImpId("T1800090_01");
	developerReassignmentAccessMail.setProgramName(Arrays.asList("WSP,OSS,RES"));
	developerReassignmentAccessMail.setRepoName(DataWareHouse.RepoName);
	String cCAddress = "e738090";
	String toAddress = "e738090";
	developerReassignmentAccessMail.addToAddressUserId(cCAddress, Constants.MailSenderRole.LEAD);
	developerReassignmentAccessMail.addToAddressUserId(toAddress, Constants.MailSenderRole.LEAD);
	return developerReassignmentAccessMail;

    }

    @Test
    public void getDevManagerAssignmentMailTest() {
	assertEquals(true, getDevManagerAssignmentMail().assignment);
	assertEquals(false, getDevManagerAssignmentMailFalse().assignment);
	getDevManagerAssignmentMail().processMessage();
	getDevManagerAssignmentMailFalse().processMessage();

    }

    public DevManagerAssignmentMail getDevManagerAssignmentMail() {

	DevManagerAssignmentMail devManagerAssignmentMail = new DevManagerAssignmentMail();
	devManagerAssignmentMail.setAssignment(true);
	devManagerAssignmentMail.setDevManagerName("e738090");
	devManagerAssignmentMail.setLeadName("e738090");
	devManagerAssignmentMail.setPlanId("T1800090");
	return devManagerAssignmentMail;

    }

    public DevManagerAssignmentMail getDevManagerAssignmentMailFalse() {

	DevManagerAssignmentMail devManagerAssignmentMail = new DevManagerAssignmentMail();
	devManagerAssignmentMail.setAssignment(false);
	devManagerAssignmentMail.setDevManagerName("e738090");
	devManagerAssignmentMail.setLeadName("e738090");
	devManagerAssignmentMail.setPlanId("T1800090");
	return devManagerAssignmentMail;

    }

    @Test
    public void getDivergedCheckoutSourceArtifactMailTest() {
	assertEquals("WSP", getDivergedCheckoutSourceArtifactMail().getCheckedOutTargetSystem());
	assertEquals("e738090", getDivergedCheckoutSourceArtifactMail().getDeveloper());
	assertEquals("aslol.asm", getDivergedCheckoutSourceArtifactMail().getSourceArtifact());
	assertEquals("OSS", getDivergedCheckoutSourceArtifactMail().getTargetSystemAfter());
	assertEquals("RES", getDivergedCheckoutSourceArtifactMail().getTargetSystemBefore());
	getDivergedCheckoutSourceArtifactMail().processMessage();

    }

    public DivergedCheckoutSourceArtifactMail getDivergedCheckoutSourceArtifactMail() {
	DivergedCheckoutSourceArtifactMail divergedCheckoutSourceArtifactMail = new DivergedCheckoutSourceArtifactMail();
	divergedCheckoutSourceArtifactMail.setCheckedOutTargetSystem("WSP");
	divergedCheckoutSourceArtifactMail.setDeveloper("e738090");
	divergedCheckoutSourceArtifactMail.setSourceArtifact("aslol.asm");
	divergedCheckoutSourceArtifactMail.setTargetSystemAfter("OSS");
	divergedCheckoutSourceArtifactMail.setTargetSystemBefore("RES");
	return divergedCheckoutSourceArtifactMail;
    }
}
