/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.git;

import com.tsi.workflow.utils.Constants;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 *
 * @author prabhu.prabhakaran
 */
public interface IJGITSearchUtils {

    public Collection<GitSearchResult> SearchAllRepos(String pCompany, String pFileFilter, Boolean pMacroHeader, Constants.PRODSearchType pPendingStatusReq, Collection<String> pAllowedRepos) throws IOException;

    public Collection<GitSearchResult> SearchAllRepos(String pCompany, String pFileFilter, Boolean pMacroHeader, Constants.PRODSearchType pPendingStatusReq, Collection<String> pAllowedRepos, List<String> systems) throws IOException;

    public Collection<GitSearchResult> SearchAllRepos(String pCompany, String pFileFilter, Boolean pMacroHeader, Constants.PRODSearchType pPendingStatusReq, Collection<String> pAllowedRepos, List<String> systems, Set<String> SystemAndFuncBasedFiles) throws IOException;

}
