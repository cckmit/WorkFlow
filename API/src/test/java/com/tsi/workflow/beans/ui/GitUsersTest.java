package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import org.junit.Test;

public class GitUsersTest {

    @Test
    public void test00() throws Throwable {
	GitUsers gitUsers = new GitUsers();
	gitUsers.setActive("Y");
	String status = gitUsers.getActive();
	assertEquals("Y", status);
    }

    @Test
    public void test01() throws Throwable {
	GitUsers gitUsers = new GitUsers();
	gitUsers.setDisplayname("displayname");
	String displayname = gitUsers.getDisplayname();
	assertEquals("displayname", displayname);
    }

    @Test
    public void test02() throws Throwable {
	GitUsers gitUsers = new GitUsers();
	gitUsers.setId(10);
	Integer id = gitUsers.getId();
	assertNotEquals("", id);
    }

    @Test
    public void test03() throws Throwable {
	GitUsers gitUsers = new GitUsers();
	gitUsers.setUsername("username");
	String username = gitUsers.getUsername();
	assertEquals("username", username);
    }
}
