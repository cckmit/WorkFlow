package com.tsi.workflow.mail;

import static org.junit.Assert.*;

import com.tsi.workflow.DataWareHouse;
import java.util.HashSet;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class StackHolderLoadsetMailTest {

    private StackHolderLoadsetMail stackHolderLoadsetMail;

    @Before
    public void setUp() throws Exception {
	stackHolderLoadsetMail = new StackHolderLoadsetMail();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testStackHolderLoadsetMail() {
	stackHolderLoadsetMail.setActivationDateTime(DataWareHouse.getPlan().getApproveDateTime());
	assertEquals(DataWareHouse.getPlan().getApproveDateTime(), stackHolderLoadsetMail.getActivationDateTime());
	stackHolderLoadsetMail.setCpuName(DataWareHouse.getSystemList().get(0).getSystemCpuList().get(0).getCpuName());
	assertEquals(DataWareHouse.getSystemList().get(0).getSystemCpuList().get(0).getCpuName(), stackHolderLoadsetMail.getCpuName());
	stackHolderLoadsetMail.setDbcr(DataWareHouse.getSystemList().get(0).getName());
	assertEquals(DataWareHouse.getSystemList().get(0).getName(), stackHolderLoadsetMail.getDbcr());
	stackHolderLoadsetMail.setPlanDescription(DataWareHouse.getPlan().getPlanDesc());
	assertEquals(DataWareHouse.getPlan().getPlanDesc(), stackHolderLoadsetMail.getPlanDescription());
	stackHolderLoadsetMail.setPlanId(DataWareHouse.getPlan().getId());
	assertEquals(DataWareHouse.getPlan().getId(), stackHolderLoadsetMail.getPlanId());
	stackHolderLoadsetMail.setProblemTicketSet(getProblemTicketSet());
	assertNotNull(stackHolderLoadsetMail.getProblemTicketSet());
	stackHolderLoadsetMail.setProdSystemName(DataWareHouse.getSystemList().get(0).getName());
	assertEquals(DataWareHouse.getSystemList().get(0).getName(), stackHolderLoadsetMail.getProdSystemName());
	stackHolderLoadsetMail.setStatus(DataWareHouse.getPlan().getPlanStatus());
	assertEquals(DataWareHouse.getPlan().getPlanStatus(), stackHolderLoadsetMail.getStatus());
	stackHolderLoadsetMail.setTargetSystems(DataWareHouse.getSystemList().get(0).getName());
	// Plan status other than Activated
	stackHolderLoadsetMail.processMessage();
	// Plan status Activated
	stackHolderLoadsetMail.setStatus("ACTIVATED");
	stackHolderLoadsetMail.processMessage();

    }

    private Set<String> getProblemTicketSet() {
	Set<String> set = new HashSet<String>();
	set.add(DataWareHouse.getProblemTicket().getRefNum());
	return set;
    }

}
