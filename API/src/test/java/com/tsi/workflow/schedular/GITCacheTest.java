package com.tsi.workflow.schedular;

import static org.junit.Assert.*;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.git.GitUserDetails;
import com.tsi.workflow.helper.GITHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

public class GITCacheTest {
    GITCache instance;

    @Before
    public void setUp() throws Exception {
	GITCache gITCache = new GITCache();
	instance = Mockito.spy(gITCache);

	ReflectionTestUtils.setField(instance, "gitHelper", Mockito.mock(GITHelper.class));
	ReflectionTestUtils.setField(instance, "wFConfig", Mockito.mock(WFConfig.class));
	ReflectionTestUtils.setField(instance, "gitUserDetails", Mockito.mock(GitUserDetails.class));

    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
	Mockito.when(instance.wFConfig.getPrimary()).thenReturn(true);
	instance.populateGitInfo();
    }

}
