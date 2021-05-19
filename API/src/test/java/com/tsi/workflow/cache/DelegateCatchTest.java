package com.tsi.workflow.cache;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DelegateCatchTest {

    public DelegateCatchTest() {
    }

    @Test
    public void delegateCatchTest() {
	DelegateCache instance = new DelegateCache("tp", "Added", "travelPort", "delta");
	instance.setAction("Added");
	assertEquals("Added", instance.getAction());
	instance.setProfile("tp");
	assertEquals("tp", instance.getProfile());
	instance.setFrom("travelPort");
	assertEquals("travelPort", instance.getFrom());
	instance.setTo("delta");
	assertEquals("delta", instance.getTo());

    }

}
