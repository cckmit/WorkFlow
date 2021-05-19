package com.tsi.workflow.route;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class GITDBSearchConditionTest {

    GITDBSearchCondition instance;

    @Test
    public void test00() {
	GITDBSearchCondition dao = new GITDBSearchCondition();
	instance = Mockito.spy(dao);
	ConditionContext conditionContext = mock(ConditionContext.class);
	AnnotatedTypeMetadata annotatedTypeMetadata = mock(AnnotatedTypeMetadata.class);
	System.setProperty("wf.git.search.type", "GIT_DB");
	String value = System.getProperty("wf.git.search.type");
	assertTrue(value.equalsIgnoreCase("GIT_DB"));
	instance.matches(conditionContext, annotatedTypeMetadata);
    }

    @Test
    public void test01() {
	GITDBSearchCondition dao = new GITDBSearchCondition();
	instance = Mockito.spy(dao);
	ConditionContext conditionContext = mock(ConditionContext.class);
	AnnotatedTypeMetadata annotatedTypeMetadata = mock(AnnotatedTypeMetadata.class);
	System.setProperty("wf.git.search.type", "GIT_DB");
	String value = System.getProperty("wf.git.search.type");
	assertFalse(value.equalsIgnoreCase(""));
	instance.matches(conditionContext, annotatedTypeMetadata);
    }

}
