
package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class RepoUserViewTest {

    @Test(timeout = 4000)
    public void test0() throws Throwable {
	RepoUserView repoUserView0 = new RepoUserView();
	HashMap<String, String> hashMap0 = new HashMap<String, String>();
	hashMap0.put(".rHnw9;1M2~", ".rHnw9;1M2~");
	repoUserView0.setUserList(hashMap0);
	Map<String, String> map0 = repoUserView0.getUserList();
	assertFalse(map0.isEmpty());
    }

    @Test(timeout = 4000)
    public void test1() throws Throwable {
	RepoUserView repoUserView0 = new RepoUserView();
	Map<String, String> map0 = repoUserView0.getUserList();
	assertNull(map0);
    }

    @Test(timeout = 4000)
    public void test2() throws Throwable {
	RepoUserView repoUserView0 = new RepoUserView();
	HashMap<String, String> hashMap0 = new HashMap<String, String>();
	repoUserView0.setUserList(hashMap0);
	Map<String, String> map0 = repoUserView0.getUserList();
	assertTrue(map0.isEmpty());
    }
}
