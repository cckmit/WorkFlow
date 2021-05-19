package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SourceArtifactSearchFormTest {

    private SourceArtifactSearchForm sourceArtifactSearchForm;

    @Before
    public void setUp() throws Exception {
	sourceArtifactSearchForm = new SourceArtifactSearchForm();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSourceArtifactSearchForm() {
	sourceArtifactSearchForm.setFileType("File Type");
	assertEquals("File Type", sourceArtifactSearchForm.getFileType());
	sourceArtifactSearchForm.setSourceArtifactName("Source Artifact Name");
	assertEquals("Source Artifact Name", sourceArtifactSearchForm.getSourceArtifactName());
	sourceArtifactSearchForm.setTargetSys(new ArrayList<com.tsi.workflow.beans.dao.System>());
	assertNotNull(sourceArtifactSearchForm.getTargetSys());

    }

}
