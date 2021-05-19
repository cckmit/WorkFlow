package com.tsi.workflow.report;

import static org.junit.Assert.*;

import com.tsi.workflow.beans.ui.RepoBasedSrcArtifacts;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RepoBasedSrcArtifactExportTest {

    private RepoBasedSrcArtifactExport repoBasedSrcArtifactExport;

    @Before
    public void setUp() throws Exception {
	repoBasedSrcArtifactExport = new RepoBasedSrcArtifactExport();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void RepoBasedSrcArtifactExport() {
	repoBasedSrcArtifactExport.addSearchResult(getPResults());
	assertEquals(null, repoBasedSrcArtifactExport.getName());
	assertNotNull(repoBasedSrcArtifactExport.getWorkBook());
    }

    private List<RepoBasedSrcArtifacts> getPResults() {
	List<RepoBasedSrcArtifacts> list = new ArrayList<>();
	RepoBasedSrcArtifacts RepoBasedSrcArtifacts = new RepoBasedSrcArtifacts();
	RepoBasedSrcArtifacts.setFileext("FileExt");
	RepoBasedSrcArtifacts.setFilename("FileName");
	RepoBasedSrcArtifacts.setProgname("ProgramName");
	RepoBasedSrcArtifacts.setSorucecommitid("SourceCommitId");
	RepoBasedSrcArtifacts.setSourcerepo("SourceRepo");
	RepoBasedSrcArtifacts.setSrcArtifactLink("SourceArtifactLink");
	RepoBasedSrcArtifacts.setSubsourcerepo("SubSourceRepo");
	RepoBasedSrcArtifacts.setTargetsystem("TargetSystem");
	list.add(RepoBasedSrcArtifacts);
	return list;
    }

}
