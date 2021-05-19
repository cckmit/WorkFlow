package com.tsi.workflow.schedular;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.tsi.workflow.DataWareHouse;
import com.tsi.workflow.beans.dao.Project;
import com.tsi.workflow.dao.ImplementationDAO;
import com.tsi.workflow.gitblit.GitBlitClientUtils;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

public class GitblitIndexerTest {

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
	GitblitIndexer realInstance = new GitblitIndexer();
	instance = spy(realInstance);
    }

    GitblitIndexer instance;

    @Test
    public void testReindexTickets() throws IOException {
	ReflectionTestUtils.setField(instance, "lGitBlitClientUtils", mock(GitBlitClientUtils.class));
	ReflectionTestUtils.setField(instance, "implementationDAO", mock(ImplementationDAO.class));
	when(instance.implementationDAO.findAll()).thenReturn(Arrays.asList(DataWareHouse.getPlan().getImplementationList().get(0)));
	Map<String, Project> lMap = new HashMap<>();
	lMap.put(DataWareHouse.getPlan().getProjectId().getProjectNumber(), DataWareHouse.getPlan().getProjectId());
	instance.reindexTickets();
    }

}
