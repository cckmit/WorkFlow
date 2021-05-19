/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.git;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author USER
 */
public class GitMetaResultTest {

    public GitMetaResultTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetRepository() {
	Repository repository = mock(Repository.class);
	when(repository.getDirectory()).thenReturn(mock(File.class));
	GitMetaResult instance = new GitMetaResult(repository, null, null, null);

	String expResult = "";
	Ref result1 = instance.getBranch();
	assertNull(result1);
	Repository result2 = instance.getRepository();
	assertNotNull(result2);
	String result = instance.getFileHashCode();
	assertNull(result);
	result = instance.getFileName();
	assertNull(result);
	result = instance.getProgramName();
	assertNull(result);
	result = instance.getFileNameWithHash();
	assertNotNull(result);
    }

}
