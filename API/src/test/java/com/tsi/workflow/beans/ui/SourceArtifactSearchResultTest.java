package com.tsi.workflow.beans.ui;

import static org.junit.Assert.*;

import java.math.BigInteger;
import java.sql.Date;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SourceArtifactSearchResultTest {

    private SourceArtifactSearchResult sourceArtifactSearchResult;

    @Before
    public void setUp() throws Exception {
	sourceArtifactSearchResult = new SourceArtifactSearchResult();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testSourceArtifactSearchResultTest() {
	sourceArtifactSearchResult.setCheckindatetime(new Date(2019, 9, 21));
	assertEquals(new Date(2019, 9, 21), sourceArtifactSearchResult.getCheckindatetime());
	sourceArtifactSearchResult.setCommitid("89473HF");
	assertEquals("89473HF", sourceArtifactSearchResult.getCommitid());
	sourceArtifactSearchResult.setDevelopername("DeveloperName");
	assertEquals("DeveloperName", sourceArtifactSearchResult.getDevelopername());
	sourceArtifactSearchResult.setFallbackdatetime(new Date(2019, 9, 21));
	assertEquals(new Date(2019, 9, 21), sourceArtifactSearchResult.getFallbackdatetime());
	sourceArtifactSearchResult.setFilename("FileName");
	assertEquals("FileName", sourceArtifactSearchResult.getFilename());
	sourceArtifactSearchResult.setFiletype("FileType");
	assertEquals("FileType", sourceArtifactSearchResult.getFiletype());
	sourceArtifactSearchResult.setHighlightGroupFlag(Boolean.TRUE);
	assertTrue(sourceArtifactSearchResult.getHighlightGroupFlag());
	sourceArtifactSearchResult.setImpid("P12637");
	assertEquals("P12637", sourceArtifactSearchResult.getImpid());
	sourceArtifactSearchResult.setListingFile("ListingFile");
	assertEquals("ListingFile", sourceArtifactSearchResult.getListingFile());
	sourceArtifactSearchResult.setListingfilelink("Link");
	assertEquals("Link", sourceArtifactSearchResult.getListingfilelink());
	sourceArtifactSearchResult.setLoaddatetime(new Date(2019, 9, 21));
	assertEquals(new Date(2019, 9, 21), sourceArtifactSearchResult.getLoaddatetime());
	sourceArtifactSearchResult.setPlanid("P85347856");
	assertEquals("P85347856", sourceArtifactSearchResult.getPlanid());
	sourceArtifactSearchResult.setPlanstatus("ONLINE");
	assertEquals("ONLINE", sourceArtifactSearchResult.getPlanstatus());
	sourceArtifactSearchResult.setSourceartifactlink("Link");
	assertEquals("Link", sourceArtifactSearchResult.getSourceartifactlink());
	sourceArtifactSearchResult.setSourcerepo("SourceRepo");
	assertEquals("SourceRepo", sourceArtifactSearchResult.getSourcerepo());
	sourceArtifactSearchResult.setStatusrank(new BigInteger("875834"));
	assertEquals(new Integer("875834"), sourceArtifactSearchResult.getStatusrank());
	sourceArtifactSearchResult.setTargetsystem("XYZ");
	assertEquals("XYZ", sourceArtifactSearchResult.getTargetsystem());
	sourceArtifactSearchResult.setProgramname("ProgeamName");
	assertEquals("ProgeamName", sourceArtifactSearchResult.getProgramname());
    }

}
