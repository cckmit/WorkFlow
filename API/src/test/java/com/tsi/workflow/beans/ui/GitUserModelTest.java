package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;

public class GitUserModelTest {

    @Test
    public void test00() throws Throwable {
	GitUserModel gitUserModel = new GitUserModel();
	gitUserModel.setDefultpermission("defultpermission");
	String defultpermission = gitUserModel.getDefultpermission();
	assertEquals("defultpermission", defultpermission);
    }

    @Test
    public void test01() throws Throwable {
	GitUserModel gitUserModel = new GitUserModel();
	gitUserModel.setDerivedrepo("derivedrepo");
	String derivedrepo = gitUserModel.getDerivedrepo();
	assertEquals("derivedrepo", derivedrepo);
    }

    @Test
    public void test02() throws Throwable {
	GitUserModel gitUserModel = new GitUserModel();
	gitUserModel.setDescription("description");
	String description = gitUserModel.getDescription();
	assertEquals("description", description);
    }

    @Test
    public void test03() throws Throwable {
	GitUserModel gitUserModel = new GitUserModel();
	gitUserModel.setDisplayname("displayname");
	String displayname = gitUserModel.getDisplayname();
	assertEquals("displayname", displayname);
    }

    @Test
    public void test04() throws Throwable {
	GitUserModel gitUserModel = new GitUserModel();
	gitUserModel.setPermission("permission");
	;
	String permission = gitUserModel.getPermission();
	assertEquals("permission", permission);
    }

    @Test
    public void test05() throws Throwable {
	GitUserModel gitUserModel = new GitUserModel();
	gitUserModel.setRepoid(10);
	Integer repoId = gitUserModel.getRepoid();
	assertNotEquals("", repoId);
    }

    @Test
    public void test06() throws Throwable {
	GitUserModel gitUserModel = new GitUserModel();
	gitUserModel.setSourcerepo("sourcerepo");
	String sourcerepo = gitUserModel.getSourcerepo();
	assertEquals("sourcerepo", sourcerepo);
    }

    @Test
    public void test07() throws Throwable {
	GitUserModel gitUserModel = new GitUserModel();
	gitUserModel.setTargetsystems("WSP");
	List<String> targetsystems = gitUserModel.getTargetsystems();
	assertNotNull(targetsystems);
    }

    @Test
    public void test08() throws Throwable {
	GitUserModel gitUserModel = new GitUserModel();
	gitUserModel.setUsername("username");
	String username = gitUserModel.getUsername();
	assertEquals("username", username);
    }

    @Test
    public void test09() throws Throwable {
	GitUserModel gitUserModel = new GitUserModel();
	gitUserModel.setSubsourcerepo("subsourcerepo");
	String subsourcerepo = gitUserModel.getSubsourcerepo();
	assertEquals("subsourcerepo", subsourcerepo);
    }

    @Test
    public void test10() throws Throwable {
	GitUserModel gitUserModel = new GitUserModel();
	gitUserModel.setSubderivedrepo("subderivedrepo");
	String subderivedrepo = gitUserModel.getSubderivedrepo();
	assertEquals("subderivedrepo", subderivedrepo);
    }

}
