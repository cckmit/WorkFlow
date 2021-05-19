/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.hazelcast.core.IMap;
import com.tsi.workflow.activity.DbcrActivityMessage;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.LoadWindow;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.Project;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.UserSettings;
import com.tsi.workflow.beans.external.ProblemTicket;
import com.tsi.workflow.beans.ui.RepositoryView;
import com.tsi.workflow.bpm.model.TaskReponse;
import com.tsi.workflow.datawarehouse.ActivityLogDAODataHub;
import com.tsi.workflow.datawarehouse.BPMClientUtilsDataHub;
import com.tsi.workflow.datawarehouse.BaseControllerDataHub;
import com.tsi.workflow.datawarehouse.BuildDAODataHub;
import com.tsi.workflow.datawarehouse.CSRNumberDAODataHub;
import com.tsi.workflow.datawarehouse.CheckoutSegmentsDAODataHub;
import com.tsi.workflow.datawarehouse.CommonBaseControllerDataHub;
import com.tsi.workflow.datawarehouse.CommonBaseServiceDataHub;
import com.tsi.workflow.datawarehouse.CommonControllerDataHub;
import com.tsi.workflow.datawarehouse.CommonServiceDataHub;
import com.tsi.workflow.datawarehouse.DSLFileHelperDataHub;
import com.tsi.workflow.datawarehouse.DateAuditCrossCheckDataHub;
import com.tsi.workflow.datawarehouse.DbcrDAODataHub;
import com.tsi.workflow.datawarehouse.DbcrHelperDataHub;
import com.tsi.workflow.datawarehouse.DeveloperControllerDataHub;
import com.tsi.workflow.datawarehouse.DeveloperLeadControllerDataHub;
import com.tsi.workflow.datawarehouse.DeveloperLeadServiceDataHub;
import com.tsi.workflow.datawarehouse.DeveloperManagerControllerDataHub;
import com.tsi.workflow.datawarehouse.DeveloperManagerServiceDataHub;
import com.tsi.workflow.datawarehouse.DeveloperServiceDataHub;
import com.tsi.workflow.datawarehouse.GITSSHUtilsDataHub;
import com.tsi.workflow.datawarehouse.GitHookUpdateServiceDataHub;
import com.tsi.workflow.datawarehouse.ImpPlanApprovalsDAODataHub;
import com.tsi.workflow.datawarehouse.ImpPlanDAODataHub;
import com.tsi.workflow.datawarehouse.ImplementationDAODataHub;
import com.tsi.workflow.datawarehouse.JGitClientUtilsDataHub;
import com.tsi.workflow.datawarehouse.JenkinsClientDataHub;
import com.tsi.workflow.datawarehouse.LDAPAuthenticatorImplDataHub;
import com.tsi.workflow.datawarehouse.LDAPServiceDataHub;
import com.tsi.workflow.datawarehouse.LoadCategoriesDAODataHub;
import com.tsi.workflow.datawarehouse.LoadFreezeDAODataHub;
import com.tsi.workflow.datawarehouse.LoadWindowDAODataHub;
import com.tsi.workflow.datawarehouse.LoadsControlControllerDataHub;
import com.tsi.workflow.datawarehouse.LoadsControlServiceDataHub;
import com.tsi.workflow.datawarehouse.MailMessageFactoryDataHub;
import com.tsi.workflow.datawarehouse.PlatformDAODataHub;
import com.tsi.workflow.datawarehouse.PreProductionLoadsDAODataHub;
import com.tsi.workflow.datawarehouse.ProductionLoadsDAODataHub;
import com.tsi.workflow.datawarehouse.ProjectDAODataHub;
import com.tsi.workflow.datawarehouse.ProtectedControllerDataHub;
import com.tsi.workflow.datawarehouse.ProtectedServiceDataHub;
import com.tsi.workflow.datawarehouse.PutLevelDAODataHub;
import com.tsi.workflow.datawarehouse.QualityAssuranceControllerDataHub;
import com.tsi.workflow.datawarehouse.QualityAssuranceServiceDataHub;
import com.tsi.workflow.datawarehouse.ReviewerControllerDataHub;
import com.tsi.workflow.datawarehouse.ReviewerServiceDataHub;
import com.tsi.workflow.datawarehouse.SSHUtilDataHub;
import com.tsi.workflow.datawarehouse.STAGEBuildMonitorDataHub;
import com.tsi.workflow.datawarehouse.SequenceGeneratorDataHub;
import com.tsi.workflow.datawarehouse.SystemCpuDAODataHub;
import com.tsi.workflow.datawarehouse.SystemDAODataHub;
import com.tsi.workflow.datawarehouse.SystemLoadActionsDAODataHub;
import com.tsi.workflow.datawarehouse.SystemLoadDAODataHub;
import com.tsi.workflow.datawarehouse.TOSHelperDataHub;
import com.tsi.workflow.datawarehouse.TSDControllerDataHub;
import com.tsi.workflow.datawarehouse.TSDServiceDataHub;
import com.tsi.workflow.datawarehouse.TestSystemSupportControllerDataHub;
import com.tsi.workflow.datawarehouse.TestSystemSupportServiceDataHub;
import com.tsi.workflow.datawarehouse.UserControllerDataHub;
import com.tsi.workflow.datawarehouse.UserSettingsDAODataHub;
import com.tsi.workflow.datawarehouse.VparsDAODataHub;
import com.tsi.workflow.git.GitSearchResult;
import com.tsi.workflow.gitblit.model.Repository;
import com.tsi.workflow.gitblit.model.UserModel;
import com.tsi.workflow.tos.model.TOSResult;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.JSONResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Level;

/**
 *
 * @author USER
 */
public class DataWareHouse {

    private static ImpPlan lPlan;
    private static Implementation lImplementation;
    private static System lSystem;
    private static SystemLoad lSystemLoad;
    private static UserSettings lUserSetting;
    private static PreProductionLoads preProductionLoads;
    private static ProblemTicket problemTicket;
    private static GitSearchResult lGitSearchResult;
    private static TaskReponse lBPMResponse;
    private static Repository repository;
    private static DbcrActivityMessage dbcrActivityMessage;

    public static Integer offset = 0;
    public static Integer limit = 10;
    public static String RepoName = "/tpf/tp/source/" + getPlan().getId().toLowerCase() + ".git";
    public static String RepoLFSName = "/tpf/tp/derived/" + getPlan().getId().toLowerCase() + ".git";
    public static String PlanSSHUrl = "ssh://" + getUser().getId() + "@vhldvztdt001.tvlport.net:8445/tpf/tp/source/" + getPlan().getId().toLowerCase() + ".git";

    public static List<ImpPlan> planList = new ArrayList<>();
    public static List<System> systemList = new ArrayList<>();
    public static List<PutLevel> putlevelList = new ArrayList();
    public static List<Implementation> implementationList = new ArrayList<>();
    public static Map<String, Project> lProjectResult = new HashMap();
    public static List<String> lBranchList = new ArrayList();
    public static Level LogLevel = Level.ERROR;

    public static String lJobName = Constants.DEVL.BLD_DVL_.name() + getPlan().getSystemLoadList().get(0).getSystemId().getName();
    public static String lInputString = "example";
    public static Integer lInputInt = 1;
    public static Boolean initIvoke = false;
    public static Date lInputDate = new Date();
    private static ProductionLoads productionLoads;
    private static TOSResult tOSResult;
    private static LoadWindow loadWindow;

    // CacheClient for Repository
    public static IMap<String, UserModel> lRepoWiseUserList;
    public static IMap<String, List<Repository>> lAllRepositoryMap;
    public static IMap<String, UserModel> lAllRepoUsersMap;
    public static IMap<String, RepositoryView> lFilteredRepository;

    public static void init() {
	if (!initIvoke) {
	    ActivityLogDAODataHub.init();
	    BuildDAODataHub.init();
	    CheckoutSegmentsDAODataHub.init();
	    CommonBaseControllerDataHub.init();
	    CommonBaseServiceDataHub.init();
	    CommonControllerDataHub.init();
	    CommonServiceDataHub.init();
	    DbcrDAODataHub.init();
	    DeveloperControllerDataHub.init();
	    DeveloperLeadControllerDataHub.init();
	    DeveloperLeadServiceDataHub.init();
	    DeveloperManagerControllerDataHub.init();
	    DeveloperManagerServiceDataHub.init();
	    DeveloperServiceDataHub.init();
	    GitHookUpdateServiceDataHub.init();
	    ImpPlanApprovalsDAODataHub.init();
	    ImpPlanDAODataHub.init();
	    ImplementationDAODataHub.init();
	    LDAPServiceDataHub.init();
	    LoadCategoriesDAODataHub.init();
	    LoadFreezeDAODataHub.init();
	    LoadWindowDAODataHub.init();
	    LoadsControlControllerDataHub.init();
	    LoadsControlServiceDataHub.init();
	    PlatformDAODataHub.init();
	    PreProductionLoadsDAODataHub.init();
	    ProductionLoadsDAODataHub.init();
	    ProjectDAODataHub.init();
	    ProtectedControllerDataHub.init();
	    ProtectedServiceDataHub.init();
	    PutLevelDAODataHub.init();
	    QualityAssuranceControllerDataHub.init();
	    QualityAssuranceServiceDataHub.init();
	    ReviewerControllerDataHub.init();
	    ReviewerServiceDataHub.init();
	    SystemCpuDAODataHub.init();
	    SystemDAODataHub.init();
	    SystemLoadActionsDAODataHub.init();
	    SystemLoadDAODataHub.init();
	    TSDControllerDataHub.init();
	    TSDServiceDataHub.init();
	    TestSystemSupportControllerDataHub.init();
	    TestSystemSupportServiceDataHub.init();
	    UserControllerDataHub.init();
	    UserSettingsDAODataHub.init();
	    VparsDAODataHub.init();
	    BPMClientUtilsDataHub.init();
	    DbcrHelperDataHub.init();
	    LDAPAuthenticatorImplDataHub.init();
	    SequenceGeneratorDataHub.init();
	    MailMessageFactoryDataHub.init();
	    JenkinsClientDataHub.init();
	    DateAuditCrossCheckDataHub.init();
	    SSHUtilDataHub.init();
	    JGitClientUtilsDataHub.init();
	    BaseControllerDataHub.init();
	    TOSHelperDataHub.init();
	    GITSSHUtilsDataHub.init();
	    CSRNumberDAODataHub.init();
	    DSLFileHelperDataHub.init();
	    STAGEBuildMonitorDataHub.init();
	}
    }

    public static User user;
    public static User userWithDelegation;

    public static User getUser() {
	if (user == null) {
	    try (InputStream stream = Resources.getResource("user.txt").openStream()) {
		user = new ObjectMapper().readValue(IOUtils.toString(stream), User.class);
	    } catch (Exception e) {
		user = null;
	    }
	}
	return user;
    }

    public static User getUserWithDelagated() {
	if (userWithDelegation == null) {
	    try (InputStream stream = Resources.getResource("userWithDelegation.txt").openStream()) {
		userWithDelegation = new ObjectMapper().readValue(IOUtils.toString(stream), User.class);
	    } catch (Exception e) {
		userWithDelegation = null;
	    }
	}
	return userWithDelegation;
    }

    public static JSONResponse getPositiveResponse() {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.TRUE);
	return lResponse;
    }

    public static JSONResponse getNegativeResponse() {
	JSONResponse lResponse = new JSONResponse();
	lResponse.setStatus(Boolean.FALSE);
	return lResponse;
    }

    public static ImpPlan getPlan() {
	if (lPlan == null) {
	    try (InputStream stream = Resources.getResource("data.txt").openStream()) {
		lPlan = new ObjectMapper().readValue(IOUtils.toString(stream), ImpPlan.class);
	    } catch (Exception e) {
		lPlan = null;
	    }
	}
	return lPlan;
    }

    public static PreProductionLoads getPreProductionLoads() {
	if (preProductionLoads == null) {
	    try (InputStream stream = Resources.getResource("preprodload.txt").openStream()) {
		preProductionLoads = new ObjectMapper().readValue(IOUtils.toString(stream), PreProductionLoads.class);
	    } catch (Exception e) {
		preProductionLoads = null;
	    }
	}
	return preProductionLoads;
    }

    public static ProductionLoads getProductionLoads() {
	if (productionLoads == null) {
	    try (InputStream stream = Resources.getResource("productionloads.txt").openStream()) {
		productionLoads = new ObjectMapper().readValue(IOUtils.toString(stream), ProductionLoads.class);
	    } catch (Exception e) {
		productionLoads = null;
	    }
	}
	return productionLoads;
    }

    public static UserSettings getUserSettings() {
	if (lUserSetting == null) {
	    try (InputStream stream = Resources.getResource("usersettings.txt").openStream()) {
		lUserSetting = new ObjectMapper().readValue(IOUtils.toString(stream), UserSettings.class);
	    } catch (Exception e) {
		lUserSetting = null;
	    }
	}
	return lUserSetting;
    }

    public static ProblemTicket getProblemTicket() {
	if (problemTicket == null) {
	    try (InputStream stream = Resources.getResource("problemticket.txt").openStream()) {
		problemTicket = new ObjectMapper().readValue(IOUtils.toString(stream), ProblemTicket.class);
	    } catch (Exception e) {
		problemTicket = null;
	    }
	}
	return problemTicket;
    }

    public static GitSearchResult getGitSearchResult() {
	if (lGitSearchResult == null) {
	    try (InputStream stream = Resources.getResource("gitsearchresult.txt").openStream()) {
		lGitSearchResult = new ObjectMapper().readValue(IOUtils.toString(stream), GitSearchResult.class);
	    } catch (Exception e) {
		lGitSearchResult = null;
	    }
	}
	return lGitSearchResult;
    }

    public static Object getAsList(Object get) {
	List arrayList = new ArrayList();
	if (get.getClass().isArray()) {
	    Object[] get1 = (Object[]) get;
	    for (Object get2 : get1) {
		arrayList.add(get2);
	    }
	} else {
	    arrayList.add(get);
	}
	return (List) arrayList;
    }

    public static TaskReponse getTaskResponse() {
	if (lBPMResponse == null) {
	    try (InputStream stream = Resources.getResource("taskresponse.txt").openStream()) {
		lBPMResponse = new ObjectMapper().readValue(IOUtils.toString(stream), TaskReponse.class);
	    } catch (Exception e) {
		lBPMResponse = null;
	    }
	}
	return lBPMResponse;
    }

    public static TOSResult getTOSResult() {
	if (tOSResult == null) {
	    try (InputStream stream = Resources.getResource("tosresult.txt").openStream()) {
		tOSResult = new ObjectMapper().readValue(IOUtils.toString(stream), TOSResult.class);
	    } catch (Exception e) {
		tOSResult = null;
	    }
	}
	return tOSResult;
    }

    public static LoadWindow getLoadWindow() {
	if (loadWindow == null) {
	    try (InputStream stream = Resources.getResource("loadwindow.txt").openStream()) {
		loadWindow = new ObjectMapper().readValue(IOUtils.toString(stream), LoadWindow.class);
	    } catch (Exception e) {
		loadWindow = null;
	    }
	}
	return loadWindow;
    }

    public static List<System> getSystemList() {
	List<System> lSysList = new ArrayList();
	for (int i = 0; i < getPlan().getSystemLoadList().size(); i++) {
	    lSysList.add(getPlan().getSystemLoadList().get(i).getSystemId());
	}
	return lSysList;
    }

    public static Set<PutLevel> getPutLevelList() {
	Set<PutLevel> lPutLevelList = new HashSet<>();
	for (int i = 0; i < getPlan().getSystemLoadList().size(); i++) {
	    lPutLevelList.add(getPlan().getSystemLoadList().get(i).getPutLevelId());
	}
	return lPutLevelList;
    }

    public static List<String> getBranchList() {
	List<String> lBranchList = new ArrayList();
	for (System lSystem : getSystemList()) {
	    lBranchList.add(getPlan().getImplementationList().get(0).getId().toLowerCase() + "_" + lSystem.getName().toLowerCase());
	}
	return lBranchList;
    }

    public static User user() {
	throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
								       // Tools | Templates.
    }

    public static ImpPlan setRefreshPlan() {
	try (InputStream stream = Resources.getResource("data.txt").openStream()) {
	    lPlan = new ObjectMapper().readValue(IOUtils.toString(stream), ImpPlan.class);
	} catch (Exception e) {
	    lPlan = null;
	}
	return lPlan;
    }

    public static GitSearchResult refreshGitSearchResult() {
	try (InputStream stream = Resources.getResource("gitsearchresult.txt").openStream()) {
	    lGitSearchResult = new ObjectMapper().readValue(IOUtils.toString(stream), GitSearchResult.class);
	} catch (Exception e) {
	    lGitSearchResult = null;
	}
	return lGitSearchResult;
    }

    public static Repository getRepositoryDetails() {
	if (repository == null) {
	    try (InputStream stream = Resources.getResource("repository.txt").openStream()) {
		repository = new ObjectMapper().readValue(IOUtils.toString(stream), Repository.class);
	    } catch (Exception e) {
		repository = null;
	    }
	}
	return repository;
    }

    public static DbcrActivityMessage getDbcrActivityMessage() {
	if (dbcrActivityMessage == null) {
	    try (InputStream stream = Resources.getResource("dbcrActivityMessage.txt").openStream()) {
		dbcrActivityMessage = new ObjectMapper().readValue(IOUtils.toString(stream), DbcrActivityMessage.class);
	    } catch (Exception e) {
		dbcrActivityMessage = null;
	    }
	}
	return dbcrActivityMessage;
    }

    public static IMap<String, UserModel> getRepoWiseUserMap() {
	if (lRepoWiseUserList == null) {
	    try (InputStream stream = Resources.getResource("RepoWiseUserList.txt").openStream()) {
		lRepoWiseUserList = new ObjectMapper().readValue(IOUtils.toString(stream), IMap.class);
	    } catch (Exception e) {
		lRepoWiseUserList = null;
	    }
	}
	return lRepoWiseUserList;
    }

    public static IMap<String, List<Repository>> getAllRepositoryMap() {
	if (lAllRepositoryMap == null) {
	    try (InputStream stream = Resources.getResource("AllRepoUserList.txt").openStream()) {
		lAllRepositoryMap = new ObjectMapper().readValue(IOUtils.toString(stream), IMap.class);
	    } catch (Exception e) {
		lAllRepositoryMap = null;
	    }
	}
	return lAllRepositoryMap;
    }

    public static IMap<String, UserModel> getAllRepoUsersMap() {
	if (lAllRepoUsersMap == null) {
	    try (InputStream stream = Resources.getResource("AllRepoUserList.txt").openStream()) {
		lAllRepoUsersMap = new ObjectMapper().readValue(IOUtils.toString(stream), IMap.class);
	    } catch (Exception e) {
		lAllRepoUsersMap = null;
	    }
	}
	return lAllRepoUsersMap;
    }

    public static IMap<String, RepositoryView> getFilteredRepositoryMap() {
	if (lFilteredRepository == null) {
	    try (InputStream stream = Resources.getResource("FilteredRepositoryMap.txt").openStream()) {
		lFilteredRepository = new ObjectMapper().readValue(IOUtils.toString(stream), IMap.class);
	    } catch (Exception e) {
		lFilteredRepository = null;
	    }
	}
	return lFilteredRepository;
    }

}
