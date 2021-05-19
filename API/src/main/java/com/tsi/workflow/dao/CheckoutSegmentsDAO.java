package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.CheckoutSegments;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.beans.dao.LegacyFallBackPlan;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.RepoSearchForm;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class CheckoutSegmentsDAO extends BaseDAO<CheckoutSegments> {

    private static final Logger LOG = Logger.getLogger(CheckoutSegmentsDAO.class.getName());

    public List<CheckoutSegments> findByImpPlan(String pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<CheckoutSegments> findByImpPlan(List<ImpPlan> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<CheckoutSegments> findByImpPlan(ImpPlan pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<CheckoutSegments> findByImpPlan(String[] pId) throws WorkflowException {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : pId) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", lImpPlan));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<CheckoutSegments> findByImplementation(String pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("impId", new Implementation(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<CheckoutSegments> findByImplementation(List<Implementation> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("impId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<CheckoutSegments> findByImplementation(Implementation pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("impId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<CheckoutSegments> findByImplementation(String[] pId) throws WorkflowException {
	List<Implementation> lImplementation = new ArrayList<>();
	for (String lId : pId) {
	    lImplementation.add(new Implementation(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("impId", lImplementation));
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.addOrder(Order.asc("programName"));
	return lCriteria.list();
    }

    public List<CheckoutSegments> findBySystemLoad(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemLoad", new SystemLoad(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<CheckoutSegments> findBySystemLoad(List<SystemLoad> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemLoad", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<CheckoutSegments> findPlanBySystem(String lPlanId, String system) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("planId.id", lPlanId));
	lCriteria.add(Restrictions.eq("targetSystem", system));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<CheckoutSegments> findNewFileCreationPlanBySystem(String lPlanId, String system) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("planId.id", lPlanId));
	lCriteria.add(Restrictions.eq("targetSystem", system.toUpperCase()));
	lCriteria.add(Restrictions.or(Restrictions.and(Restrictions.isNotNull("refStatus"), Restrictions.eqOrIsNull("refStatus", "newfile")), Restrictions.isNull("refStatus")));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<CheckoutSegments> findBySystemLoad(SystemLoad pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemLoad", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<CheckoutSegments> findBySystemLoad(Integer[] pId) throws WorkflowException {
	List<SystemLoad> lSystemLoad = new ArrayList<>();
	for (Integer lId : pId) {
	    lSystemLoad.add(new SystemLoad(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemLoad", lSystemLoad));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<CheckoutSegments> findByFileName(String fileName, String implId) {
	String lQuery = "SELECT seg FROM CheckoutSegments seg" + " WHERE seg.programName LIKE (:programName) AND " + " seg.planId.id LIKE (:nickName) AND " + " seg.planId.planStatus IN (:status) AND " + " seg.impId.checkinDateTime IS NOT NULL AND " + " seg.refStatus IS NOT NULL AND " + " seg.active LIKE 'Y' ORDER BY seg.fileName, seg.systemLoad.loadDateTime DESC";

	List<CheckoutSegments> lPlanList = getCurrentSession().createQuery(lQuery).setParameter("programName", fileName + "%").setParameter("nickName", implId.charAt(0) + "%").setParameterList("status", Constants.PlanStatus.getAfterSubmitStatus().keySet()).list();
	return lPlanList;
    }

    public List<CheckoutSegments> getSegmentWithNoAccess(String impId, Set<String> pRepoList) {
	String lQuery = "SELECT seg FROM CheckoutSegments seg" + " WHERE seg.active = 'Y' " + " AND seg.impId.id = :impId " + " AND seg.sourceUrl NOT IN (:repoList)";

	List<CheckoutSegments> lReturnList = getCurrentSession().createQuery(lQuery).setParameter("impId", impId).setParameterList("repoList", pRepoList).list();
	return lReturnList;
    }

    public List<CheckoutSegments> getSgementsByPlan(String impId) {
	String lQuery = "SELECT seg FROM CheckoutSegments seg" + " WHERE seg.active = 'Y' " + " AND seg.impId.id = :impId ";
	List<CheckoutSegments> lReturnList = getCurrentSession().createQuery(lQuery).setParameter("impId", impId).list();
	return lReturnList;
    }

    public List<CheckoutSegments> findByFileName(String fileName, String implId, Set<String> pAllowedRepoList) {
	String lQuery = "SELECT seg FROM CheckoutSegments seg, SystemLoad load WHERE load.id = seg.systemLoad.id and seg.systemLoad.loadDateTime is not null and LOWER(seg.programName) LIKE (:programName) AND " + " seg.planId.id LIKE (:nickName) AND " + " seg.planId.planStatus IN (:status) AND ";
	if (pAllowedRepoList != null && !pAllowedRepoList.isEmpty()) {
	    lQuery += " seg.sourceUrl IN (:repoList) AND";
	}
	lQuery += " seg.active LIKE 'Y' ORDER BY seg.fileName, seg.systemLoad.loadDateTime DESC";

	Query query = getCurrentSession().createQuery(lQuery).setParameter("programName", fileName + "%").setParameter("nickName", implId.charAt(0) + "%");
	if (pAllowedRepoList != null && !pAllowedRepoList.isEmpty()) {
	    query.setParameterList("repoList", pAllowedRepoList);
	}
	query.setParameterList("status", Constants.PlanStatus.getNonProdStatusMap().keySet());
	return query.list();
    }

    public List<CheckoutSegments> findByFileNameBySystem(String fileName, String implId, Set<String> pAllowedRepoList, String targetSystem) {
	String lQuery = "SELECT seg FROM CheckoutSegments seg" + " WHERE seg.programName LIKE (:programName) AND " + " seg.planId.id LIKE (:nickName) AND " + " seg.targetSystem LIKE (:system) AND " + " seg.planId.planStatus IN (:status) AND " + " seg.impId.checkinDateTime IS NOT NULL AND " + " seg.refStatus IS NOT NULL AND " + " seg.sourceUrl IN (:repoList) AND" + " seg.active LIKE 'Y' ORDER BY seg.fileName, seg.systemLoad.loadDateTime DESC";

	List<CheckoutSegments> lPlanList = getCurrentSession().createQuery(lQuery).setParameter("programName", fileName + "%").setParameter("system", targetSystem).setParameter("nickName", implId.charAt(0) + "%").setParameterList("repoList", pAllowedRepoList).setParameterList("status", Constants.PlanStatus.getApprovedAndAboveStatus().keySet()).list();
	return lPlanList;
    }

    public List<String> getDependentSegments(String pImplId, String pStatus) {
	String lQuery = "select imp.id from checkout_segments seg, checkout_segments seg1, system_load load1, system_load load2, implementation imp" + " where" + " seg.imp_id like ?" + " and seg.file_name = seg1.file_name" + " and seg.plan_id <> seg1.plan_id" + " and seg.target_system = seg1.target_system"
	// + " -- and seg.file_hash_code = seg1.file_hash_code"
		+ " and load1.id = seg.system_load" + " and load2.id = seg1.system_load" + " and seg.imp_id != seg1.imp_id" + " and imp.id = seg1.imp_id" + " and imp.imp_status <> ?" + " and seg.active='Y'" + " and seg1.active='Y'" + " and load1.active='Y'" + " and load2.active='Y'" + " and load2.load_date_time < load1.load_date_time";

	return getCurrentSession().createSQLQuery(lQuery).setParameter(0, pImplId).setParameter(1, pStatus).list();
    }

    public List<CheckoutSegments> getSourceArtifact(CheckoutSegments pSegmentMapping) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("programName", pSegmentMapping.getProgramName()));
	lCriteria.add(Restrictions.eq("fileName", pSegmentMapping.getFileName()));
	lCriteria.add(Restrictions.eq("targetSystem", pSegmentMapping.getTargetSystem()));
	lCriteria.add(Restrictions.eq("funcArea", pSegmentMapping.getFuncArea()));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<CheckoutSegments> findByFiles(List<String> pStatus, List<String> lMakFileList, Date pLoadDateTime, System pSystem, Integer pOffset, Integer pLimit, LinkedHashMap pOrderBy) {

	String lQuery = "SELECT Seg FROM CheckoutSegments Seg, ImpPlan IP, SystemLoad SL, System S" + " WHERE Seg.programName IN (:progNameList) AND" + " Seg.planId = IP.id AND" + " IP.planStatus IN (:planStatusList) AND" + " Seg.systemLoad = SL.id AND" + " SL.loadDateTime >= :loadDateTime AND" + " S.id = :systemId AND" + " SL.systemId = :systemId AND" + " Seg.active = 'Y'";
	List list = getCurrentSession().createQuery(lQuery).setParameter("loadDateTime", pLoadDateTime).setParameter("systemId", pSystem.getId()).setParameterList("progNameList", lMakFileList).setParameterList("planStatusList", pStatus).setFirstResult(pOffset * pLimit).setMaxResults(pLimit).list();

	return list;

    }

    public Long countByFiles(List<String> pStatus, List<String> lMakFileList, Date pLoadDateTime, System pSystem) {
	String lQuery = "SELECT count(Seg) FROM CheckoutSegments Seg, ImpPlan IP, SystemLoad SL, System S" + " WHERE Seg.programName IN (:progNameList) AND" + " Seg.planId = IP.id AND" + " IP.planStatus IN (:planStatusList) AND" + " Seg.systemLoad = SL.id AND" + " SL.loadDateTime >= :loadDateTime AND" + " S.id = :systemId AND" + " SL.systemId = :systemId AND" + " Seg.active = 'Y'";

	return (Long) (getCurrentSession().createQuery(lQuery).setParameter("loadDateTime", pLoadDateTime).setParameter("systemId", pSystem.getId()).setParameterList("progNameList", lMakFileList).setParameterList("planStatusList", pStatus).uniqueResult());

    }

    public Long uniqueSegmentCount(String planId, String systemName) {

	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("planId", new ImpPlan(planId)));
	lCriteria.add(Restrictions.eq("targetSystem", systemName));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return (long) lCriteria.setProjection(Projections.projectionList().add(Projections.groupProperty("programName")).add(Projections.rowCount())).list().size();

    }

    public HashMap<String, List<String>> getFileTypesByPlan(String[] ids) {

	List<ImpPlan> lPlans = new ArrayList<>();
	for (String lId : ids) {
	    lPlans.add(new ImpPlan(lId));
	}

	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", lPlans));
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.eq("fileType", Constants.FILE_TYPE.IBM.name()));
	lCriteria.setProjection(Projections.distinct(Projections.property("planId").as("planId")));
	List<String> lIBMPlans = lCriteria.list();

	lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", lPlans));
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.eq("fileType", Constants.FILE_TYPE.NON_IBM.name()));
	lCriteria.setProjection(Projections.distinct(Projections.property("planId").as("planId")));
	List<String> lNonIBMPlans = lCriteria.list();

	HashMap<String, List<String>> lReturn = new HashMap<>();
	Collection lOnlyIBM = CollectionUtils.subtract(lIBMPlans, lNonIBMPlans);
	Collection lOnlyNonIBM = CollectionUtils.subtract(lNonIBMPlans, lIBMPlans);
	Collection lBoth = CollectionUtils.intersection(lNonIBMPlans, lIBMPlans);

	for (Object lPlan : lOnlyIBM) {
	    lReturn.put(((ImpPlan) lPlan).getId(), Constants.lIBMTemplate);
	}

	for (Object lPlan : lOnlyNonIBM) {
	    lReturn.put(((ImpPlan) lPlan).getId(), Constants.lNONIBMTemplate);
	}

	for (Object lPlan : lBoth) {
	    lReturn.put(((ImpPlan) lPlan).getId(), Constants.lBothTemplate);
	}

	return lReturn;
    }

    public CheckoutSegments findByFileName(String fileName, String implId, String systemName, String funcArea) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("fileName", fileName));
	lCriteria.add(Restrictions.eq("impId.id", implId));
	lCriteria.add(Restrictions.eq("targetSystem", systemName));
	lCriteria.add(Restrictions.eq("funcArea", funcArea));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return (CheckoutSegments) lCriteria.uniqueResult();
    }

    public List<CheckoutSegments> getCheckoutListfindByFileName(String fileName, String implId, String systemName, String funcArea) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("fileName", fileName));
	lCriteria.add(Restrictions.eq("impId.id", implId));
	lCriteria.add(Restrictions.eq("targetSystem", systemName));
	lCriteria.add(Restrictions.eq("funcArea", funcArea));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public CheckoutSegments findByFileName(String fileName, String implId, String systemName) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("fileName", fileName));
	lCriteria.add(Restrictions.eq("impId.id", implId));
	lCriteria.add(Restrictions.eq("targetSystem", systemName));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return (CheckoutSegments) lCriteria.uniqueResult();
    }

    public List<CheckoutSegments> findByFileNameAndImpl(String fileName, String implId) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("fileName", fileName));
	lCriteria.add(Restrictions.eq("impId.id", implId));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<CheckoutSegments> findByFiles(List<String> pStatus, String pProgramName, Date pLoadDateTime, System pSystem, Integer pOffset, Integer pLimit, LinkedHashMap pOrderBy) {

	String lQuery = "SELECT Seg FROM CheckoutSegments Seg, ImpPlan IP, SystemLoad SL, System S" + " WHERE Seg.programName LIKE (:progNameList) AND" + " Seg.planId = IP.id AND" + " IP.planStatus IN (:planStatusList) AND" + " Seg.systemLoad = SL.id AND" + " SL.loadDateTime >= :loadDateTime AND" + " S.id = :systemId AND" + " SL.systemId = :systemId AND" + " Seg.active = 'Y'";
	List list = getCurrentSession().createQuery(lQuery).setParameter("loadDateTime", pLoadDateTime).setParameter("systemId", pSystem.getId()).setParameter("progNameList", pProgramName + "%").setParameterList("planStatusList", pStatus).setFirstResult(pOffset * pLimit).setMaxResults(pLimit).list();

	return list;

    }

    public Long countByFiles(List<String> pStatus, String pProgramName, Date pLoadDateTime, System pSystem) {
	String lQuery = "SELECT count(Seg) FROM CheckoutSegments Seg, ImpPlan IP, SystemLoad SL, System S" + " WHERE Seg.programName LIKE (:progNameList) AND" + " Seg.planId = IP.id AND" + " IP.planStatus IN (:planStatusList) AND" + " Seg.systemLoad = SL.id AND" + " SL.loadDateTime >= :loadDateTime AND" + " S.id = :systemId AND" + " SL.systemId = :systemId AND" + " Seg.active = 'Y'";

	return (Long) (getCurrentSession().createQuery(lQuery).setParameter("loadDateTime", pLoadDateTime).setParameter("systemId", pSystem.getId()).setParameter("progNameList", pProgramName + "%").setParameterList("planStatusList", pStatus).uniqueResult());

    }

    public List<String> findCommonFiles(String implId, String file) {
	String lQuery = "select concat(b.ipaddress,',',array_to_string(array_agg(concat(lower(a.target_system),'/',a.file_name)),',')) from checkout_segments a, system b " + " where a.imp_id like :implId" + " and a.file_name like :file" + " and a.active = 'Y'" + " and a.common_file = true" + " and b.name = a.target_system" + " group by a.target_system, b.ipaddress";
	return getCurrentSession().createSQLQuery(lQuery).setParameter("implId", implId.toUpperCase()).setParameter("file", file).list();
    }

    public List<String> findCommonFilesStatus(String implId, String file, String core) {
	String lQuery = "select a.file_name from checkout_segments a " + " where a.imp_id like :implId" + " and a.file_name like :file" + " and a.active = 'Y'" + " and a.common_file = true" + " and a.target_system = :core";
	return getCurrentSession().createSQLQuery(lQuery).setParameter("implId", implId.toUpperCase()).setParameter("file", file).setParameter("core", core.toUpperCase()).list();
    }

    public List<String> getBlockedSystemsByPlan(String planId) {
	String lQuery = "SELECT DISTINCT seg.targetSystem from CheckoutSegments seg" + " WHERE seg.planId.id = :PlanId" + " AND seg.active = 'Y'";
	return getCurrentSession().createQuery(lQuery).setParameter("PlanId", planId).list();
    }

    public Integer isMacroHeaderPlan(String planId) {
	String lQuery = "SELECT seg.program_name from checkout_segments seg " + "WHERE seg.plan_id = :PlanId " + "AND seg.active = 'Y' " + "AND seg.program_name NOT SIMILAR TO '%.mac|%.hpp|%.h|%.cpy|%.inc|%.incafs'";
	List<String> lProgramNameList = getCurrentSession().createSQLQuery(lQuery).setParameter("PlanId", planId).list();
	if (lProgramNameList != null) {
	    return lProgramNameList.size();
	} else {
	    return 0;
	}
    }

    public List<CheckoutSegments> getSameSegmentDevelopersByImpId(String impId, List<String> status) {
	String lQuery = "SELECT othSeg" + " FROM CheckoutSegments othSeg ,CheckoutSegments seg, ImpPlan othPlan, Implementation imp, SystemLoad load1, SystemLoad load2" + " WHERE seg.fileName = othSeg.fileName" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND load1.active = 'Y'" + " AND load2.active = 'Y'" + " AND seg.targetSystem = othSeg.targetSystem" + " AND seg.id <> othSeg.id" + " AND seg.planId.id <> othSeg.planId.id" + " AND othPlan.id = othSeg.planId.id"
		+ " AND seg.impId.id = :impId" + " AND othPlan.planStatus IN (:statuses)" + " AND imp.id = othSeg.impId.id" + " AND load1.id = othSeg.systemLoad.id" + " AND load2.id = seg.systemLoad.id" + " AND load1.loadDateTime IS NOT NULL" + " AND load2.loadDateTime IS NOT NULL" + " AND load2.loadDateTime < load1.loadDateTime";

	List<CheckoutSegments> lPlanList = getCurrentSession().createQuery(lQuery).setParameterList("statuses", status).setParameter("impId", impId).list();
	return lPlanList;
    }

    public List<CheckoutSegments> getSameSegmentDevelopersByFileName(String impId, String fileName, List<String> status) {
	String lQuery = "SELECT DISTINCT othSeg" + " FROM CheckoutSegments othSeg ,CheckoutSegments seg, ImpPlan othPlan, Implementation imp, SystemLoad load1, SystemLoad load2" + " WHERE seg.fileName = othSeg.fileName" + " AND seg.fileName = :FileName" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.targetSystem = othSeg.targetSystem" + " AND seg.id <> othSeg.id" + " AND seg.planId.id <> othSeg.planId.id" + " AND othPlan.id = othSeg.planId.id"
		+ " AND seg.impId.id <> othSeg.impId.id" + " AND othPlan.planStatus IN (:statuses)" + " AND load1.id = othSeg.systemLoad.id" + " AND load2.id = seg.systemLoad.id" + " AND load1.loadDateTime IS NOT NULL" + " AND load2.loadDateTime IS NOT NULL" + " AND load2.loadDateTime < load1.loadDateTime" + " AND othSeg.impId.id <> :ImpId";

	List<CheckoutSegments> lPlanList = getCurrentSession().createQuery(lQuery).setParameterList("statuses", status).setParameter("FileName", fileName).setParameter("ImpId", impId).list();
	return lPlanList;
    }

    public List<String> findCommonFilesBtwnPlan(String sourcePlan, String targetPlan) {
	String lQuery = "SELECT DISTINCT concat(seg1.file_name,'[',seg1.target_system,']') FROM checkout_segments seg1, checkout_segments seg2" + " WHERE seg1.file_name = seg2.file_name" + " AND seg1.active = 'Y'" + " AND seg2.active = 'Y'" + " AND seg1.file_name NOT LIKE '%.mak'" + " AND seg1.plan_id = :SourcePlan" + " AND seg2.plan_id = :TargetPlan";

	List<String> lSegments = getCurrentSession().createSQLQuery(lQuery).setParameter("SourcePlan", sourcePlan).setParameter("TargetPlan", targetPlan).list();
	return lSegments;

    }

    public List<String> findByFileName(List<String> pStatus, String pFileName, String pSystemName) {

	String lQuery = "SELECT DISTINCT Seg.planId.id FROM CheckoutSegments Seg, ImpPlan IP, SystemLoad SL, System S" + " WHERE Seg.fileName LIKE (:fileName) AND" + " Seg.active = 'Y' AND" + " Seg.planId.id = IP.id AND" + " IP.planStatus NOT IN (:planStatusList) AND" + " Seg.systemLoad = SL.id AND" + " Seg.targetSystem = :systemName AND" + " Seg.targetSystem = S.name AND" + " S.active = 'Y' AND" + " SL.active = 'Y'";
	List list = getCurrentSession().createQuery(lQuery).setParameter("systemName", pSystemName).setParameter("fileName", pFileName + "%").setParameterList("planStatusList", pStatus).list();
	return list;
    }

    public List<String> findByDependentPlans(LegacyFallBackPlan lFallBackImpPlan) {
	String lQuery = "SELECT segs.plan_id  FROM checkout_segments segs, imp_plan impPlan, system_load sysLoad " + " WHERE segs.program_name = :programName " + "AND segs.func_area = :funcArea " + "AND sysLoad.load_date_time >= to_date(:loadDate,'yyyyMMdd') " + "AND impPlan.id = sysLoad.plan_id " + "AND impPlan.id = segs.plan_id " + "AND segs.ref_plan = :legacyPlan " + "AND segs.active = 'Y' ";

	List<String> lPlanList = getCurrentSession().createSQLQuery(lQuery).setParameter("programName", lFallBackImpPlan.getProgramName()).setParameter("funcArea", lFallBackImpPlan.getFuncArea()).setParameter("loadDate", lFallBackImpPlan.getLoadDateTime()).setParameter("legacyPlan", lFallBackImpPlan.getPlanId()).list();

	return lPlanList;

    }

    public List<CheckoutSegments> findAllNonProd(String pCompany, String pFileFilter, String pBranch) {
	String lQuery = "SELECT segments from CheckoutSegments segments" + " WHERE segments.active = 'Y'" + " AND segments.targetSystem = :Branch" + " AND segments.programName LIKE :ProgramName" + " AND segments.systemLoad.active = 'Y'" + " AND segments.planId.planStatus IN (:status)  " + " AND segments.systemLoad.systemId.platformId.nickName = :Company";
	List<CheckoutSegments> lPlanList = getCurrentSession().createQuery(lQuery).setParameter("Branch", pBranch.toUpperCase()).setParameter("ProgramName", pFileFilter.toLowerCase() + "%").setParameter("Company", pCompany.toLowerCase()).setParameterList("status", Constants.PlanStatus.getNonProdStatusMap().keySet()).list();
	return lPlanList;
    }

    public HashMap<String, List<String>> getFileTypesByPlan(String[] ids, List<String> status) {

	List<ImpPlan> lPlans = new ArrayList<>();
	for (String lId : ids) {
	    lPlans.add(new ImpPlan(lId));
	}

	// List<String> status = new
	// ArrayList(Constants.PlanStatus.getDeployedInPreProdToOnline().keySet());
	String lQuery = "SELECT distinct segments.planId.id from CheckoutSegments segments, ImpPlan ip, PreProductionLoads pre " + " WHERE segments.active = 'Y'" + " AND segments.planId in (:planId) " + " AND segments.fileType = :fileType " + " AND ip.id = segments.planId.id and ((ip.planStatus in (:planStatus)) or (ip.planStatus in (:beforePreProdStatus) and pre.planId= ip.id and pre.systemLoadActionsId is not null" + " and pre.active='Y' and pre.status in (:preProdStatus)))";

	List<String> lIBMPlans = getCurrentSession().createQuery(lQuery).setParameterList("planId", lPlans).setParameter("fileType", Constants.FILE_TYPE.IBM.name()).setParameterList("planStatus", status).setParameterList("beforePreProdStatus", new ArrayList(Constants.PlanStatus.getSubmittedToPreProd().keySet())).setParameterList("preProdStatus", new ArrayList(Constants.LOAD_SET_STATUS.getLoadScenarios())).list();
	List<String> lNonIBMPlans = getCurrentSession().createQuery(lQuery).setParameterList("planId", lPlans).setParameter("fileType", Constants.FILE_TYPE.NON_IBM.name()).setParameterList("planStatus", status).setParameterList("beforePreProdStatus", new ArrayList(Constants.PlanStatus.getSubmittedToPreProd().keySet())).setParameterList("preProdStatus", new ArrayList(Constants.LOAD_SET_STATUS.getLoadScenarios())).list();
	HashMap<String, List<String>> lReturn = new HashMap<>();
	Collection lOnlyIBM = CollectionUtils.subtract(lIBMPlans, lNonIBMPlans);
	Collection lOnlyNonIBM = CollectionUtils.subtract(lNonIBMPlans, lIBMPlans);
	Collection lBoth = CollectionUtils.intersection(lNonIBMPlans, lIBMPlans);
	for (Object lPlan : lOnlyIBM) {
	    lReturn.put(lPlan.toString(), Constants.lIBMTemplate);
	}

	for (Object lPlan : lOnlyNonIBM) {
	    lReturn.put(lPlan.toString(), Constants.lNONIBMTemplate);
	}

	for (Object lPlan : lBoth) {
	    lReturn.put(lPlan.toString(), Constants.lBothTemplate);
	}
	return lReturn;
    }

    public HashMap<String, List<String>> getPreProdAndAbovePlanForDelta(String[] ids) {

	List<ImpPlan> lPlans = new ArrayList<>();
	for (String lId : ids) {
	    lPlans.add(new ImpPlan(lId));
	}

	// List<String> status = new
	// ArrayList(Constants.PlanStatus.getDeployedInPreProdToOnline().keySet());
	String lQuery = "SELECT distinct segments.planId.id from CheckoutSegments segments, ImpPlan ip, SystemLoadActions pre, Vpars vpar " + " WHERE segments.active = 'Y'" + " AND pre.planId.id = ip.id " + " AND vpar.id = pre.vparId.id " + " AND vpar.type = :vpartype" + " AND segments.planId in (:planId) " + " AND segments.fileType = :fileType " + " AND ip.id = segments.planId.id and ip.planStatus in (:planStatus)";

	List<String> lIBMPlans = getCurrentSession().createQuery(lQuery).setParameterList("planId", lPlans).setParameter("fileType", Constants.FILE_TYPE.IBM.name()).setParameter("vpartype", Constants.VPARSEnvironment.PRE_PROD.name()).setParameterList("planStatus", new ArrayList(Constants.PlanStatus.getDeployedInPreProdToOnline().keySet())).list();
	List<String> lNonIBMPlans = getCurrentSession().createQuery(lQuery).setParameterList("planId", lPlans).setParameter("fileType", Constants.FILE_TYPE.NON_IBM.name()).setParameter("vpartype", Constants.VPARSEnvironment.PRE_PROD.name()).setParameterList("planStatus", new ArrayList(Constants.PlanStatus.getDeployedInPreProdToOnline().keySet())).list();
	HashMap<String, List<String>> lReturn = new HashMap<>();
	Collection lOnlyIBM = CollectionUtils.subtract(lIBMPlans, lNonIBMPlans);
	Collection lOnlyNonIBM = CollectionUtils.subtract(lNonIBMPlans, lIBMPlans);
	Collection lBoth = CollectionUtils.intersection(lNonIBMPlans, lIBMPlans);
	for (Object lPlan : lOnlyIBM) {
	    lReturn.put(lPlan.toString(), Constants.lIBMTemplate);
	}

	for (Object lPlan : lOnlyNonIBM) {
	    lReturn.put(lPlan.toString(), Constants.lNONIBMTemplate);
	}

	for (Object lPlan : lBoth) {
	    lReturn.put(lPlan.toString(), Constants.lBothTemplate);
	}
	return lReturn;
    }

    public CheckoutSegments findByFileNameAndSystemAndFuncArea(String fileName, String systemName, String funcArea) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("fileName", fileName));
	lCriteria.add(Restrictions.eq("funcArea", funcArea));
	lCriteria.add(Restrictions.eq("targetSystem", systemName));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return (CheckoutSegments) lCriteria.uniqueResult();
    }

    public CheckoutSegments findByFileNameAndSystem(String fileName, String systemName) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("fileName", fileName));
	lCriteria.add(Restrictions.eq("targetSystem", systemName));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return (CheckoutSegments) lCriteria.uniqueResult();
    }

    public HashMap<String, List<String>> getFileTypesByPlanforTravelport(List<String> planIds, List<String> planStatuses, Integer systemId, String loadsetStatus) {

	String lQuery = "select distinct seg.planId.id from CheckoutSegments seg, ImpPlan ip, PreProductionLoads pp, System sys" + " where pp.active = 'Y'" + " and pp.planId.id in (:PlanIds)" + " and pp.systemId.id = :SystemId" + " and pp.status = :LoadsetStatus" + " and seg.active = 'Y'" + " and seg.planId.id = pp.planId.id" + " and seg.fileType = :fileType" + " and sys.id = :SystemId" + " and sys.name = seg.targetSystem" + " and pp.planId.id = ip.id" + " and ip.planStatus in (:PlanStatuses)";

	List<String> lIBMPlans = getCurrentSession().createQuery(lQuery).setParameterList("PlanIds", planIds).setParameter("fileType", Constants.FILE_TYPE.IBM.name()).setParameterList("PlanStatuses", planStatuses).setParameter("LoadsetStatus", loadsetStatus).setParameter("SystemId", systemId).list();
	List<String> lNonIBMPlans = getCurrentSession().createQuery(lQuery).setParameterList("PlanIds", planIds).setParameter("fileType", Constants.FILE_TYPE.NON_IBM.name()).setParameterList("PlanStatuses", planStatuses).setParameter("LoadsetStatus", loadsetStatus).setParameter("SystemId", systemId).list();
	HashMap<String, List<String>> lReturn = new HashMap<>();
	Collection lOnlyIBM = CollectionUtils.subtract(lIBMPlans, lNonIBMPlans);
	Collection lOnlyNonIBM = CollectionUtils.subtract(lNonIBMPlans, lIBMPlans);
	Collection lBoth = CollectionUtils.intersection(lNonIBMPlans, lIBMPlans);
	lOnlyIBM.forEach((lPlan) -> {
	    lReturn.put(lPlan.toString(), Constants.lIBMTemplate);
	});

	lOnlyNonIBM.forEach((lPlan) -> {
	    lReturn.put(lPlan.toString(), Constants.lNONIBMTemplate);
	});

	lBoth.forEach((lPlan) -> {
	    lReturn.put(lPlan.toString(), Constants.lBothTemplate);
	});
	return lReturn;
    }

    public HashMap<String, List<String>> getDeltaPlansDeployedInPreProd(List<String> planIds, Integer systemId, String loadsetStatus) {

	// List<String> status = new
	// ArrayList(Constants.PlanStatus.getDeployedInPreProdToOnline().keySet());
	String lQuery = "SELECT distinct segments.planId.id from CheckoutSegments segments, ImpPlan ip, SystemLoadActions pre, Vpars vpar, System sys" + " WHERE segments.active = 'Y'" + " AND pre.planId.id = ip.id" + " AND pre.status = :LoadsetStatus" + " AND vpar.id = pre.vparId.id " + " AND vpar.type = :vpartype" + " AND segments.planId.id in (:planId) " + " AND segments.fileType = :fileType " + " AND ip.id = segments.planId.id and ip.planStatus in (:planStatus)" + " AND sys.id = :SystemId"
		+ " AND sys.name = segments.targetSystem";

	List<String> lIBMPlans = getCurrentSession().createQuery(lQuery).setParameterList("planId", planIds).setParameter("fileType", Constants.FILE_TYPE.IBM.name()).setParameter("SystemId", systemId).setParameter("LoadsetStatus", loadsetStatus).setParameter("vpartype", Constants.VPARSEnvironment.PRE_PROD.name()).setParameterList("planStatus", new ArrayList(Constants.PlanStatus.getDeployedInPreProdToOnline().keySet())).list();
	List<String> lNonIBMPlans = getCurrentSession().createQuery(lQuery).setParameterList("planId", planIds).setParameter("fileType", Constants.FILE_TYPE.NON_IBM.name()).setParameter("vpartype", Constants.VPARSEnvironment.PRE_PROD.name()).setParameter("SystemId", systemId).setParameter("LoadsetStatus", loadsetStatus).setParameterList("planStatus", new ArrayList(Constants.PlanStatus.getDeployedInPreProdToOnline().keySet())).list();
	HashMap<String, List<String>> lReturn = new HashMap<>();
	Collection lOnlyIBM = CollectionUtils.subtract(lIBMPlans, lNonIBMPlans);
	Collection lOnlyNonIBM = CollectionUtils.subtract(lNonIBMPlans, lIBMPlans);
	Collection lBoth = CollectionUtils.intersection(lNonIBMPlans, lIBMPlans);
	for (Object lPlan : lOnlyIBM) {
	    lReturn.put(lPlan.toString(), Constants.lIBMTemplate);
	}

	for (Object lPlan : lOnlyNonIBM) {
	    lReturn.put(lPlan.toString(), Constants.lNONIBMTemplate);
	}

	for (Object lPlan : lBoth) {
	    lReturn.put(lPlan.toString(), Constants.lBothTemplate);
	}
	return lReturn;
    }

    public List<Object[]> getFunctionalAreaList(RepoSearchForm repoForm) {
	String lQuery = "select distinct  a.func_area, a.source_repo from git.repo_detail a, git.sub_repo_detail b, git.repo_file_list c, public.system sys " + "where sys.id in (:systemId) " + "and c.sub_repo_id = b.id " + "and sys.name=c.target_system " + "and a.id = b.repo_id " + "and c.is_deleted = 'N' " + "and c.program_name ilike :segment ";

	String lExt = FilenameUtils.getExtension(repoForm.getSegment());
	String lSearchSegment = "";
	if (lExt.isEmpty()) {
	    lSearchSegment = repoForm.getSegment() + ".%";
	} else {
	    lSearchSegment = repoForm.getSegment();
	}
	List<Object[]> functionalAreaList = getCurrentSession().createSQLQuery(lQuery).setParameter("segment", lSearchSegment).setParameterList("systemId", repoForm.getTargetSys()).list();
	return functionalAreaList;
    }

    public List<SystemLoad> getSystemLoadHavingDateNull(String planId) {
	String lQuery = "SELECT  load" + " FROM  SystemLoad load " + " WHERE  load.active = 'Y'" + " AND load.planId.id = :planId" + " AND load.loadDateTime IS  NULL";
	List<SystemLoad> lPlanList = getCurrentSession().createQuery(lQuery).setParameter("planId", planId).list();
	return lPlanList;
    }

    public List<String> getPlanByPutLevel(String putLevelSourceUrl, List<String> status, String systemName) {
	String lQuery = "SELECT DISTINCT ip.id FROM CheckoutSegments cs, ImpPlan ip , SystemLoad sl, System s " + " WHERE cs.active = 'Y' " + " AND cs.sourceUrl like :PutLevelURL " + " AND ip.active = 'Y' " + " AND ip.id = cs.planId.id " + " AND ip.planStatus IN (:PlanStatus)" + " AND cs.targetSystem = s.name" + " AND sl.planId.id = ip.id" + " AND sl.active = 'Y' " + " AND s.active = 'Y'" + " AND s.name = :TargetSystem" + " AND cs.targetSystem = s.name" + " AND sl.systemId.id = s.id"
		+ " AND (SELECT COUNT(pl.planId.id) FROM ProductionLoads pl WHERE pl.planId.id = ip.id) = 0";

	List<String> lPlanList = getCurrentSession().createQuery(lQuery).setParameterList("PlanStatus", status).setParameter("PutLevelURL", "%" + putLevelSourceUrl + "%").setParameter("TargetSystem", systemName).list();
	return lPlanList;
    }

    public List<CheckoutSegments> getSegmentsBySystemAndPut(String impId, String putLevelSourceUrl, String systemName) {
	String lQuery = "SELECT cs FROM CheckoutSegments cs" + " WHERE cs.active = 'Y'" + " AND cs.fileType = 'IBM'" + " AND cs.sourceUrl NOT like :PutLevelURL" + " AND cs.targetSystem = :TargetSystem" + " AND cs.impId.id = :ImpId";

	List<CheckoutSegments> lSegmentList = getCurrentSession().createQuery(lQuery).setParameter("PutLevelURL", "%" + putLevelSourceUrl + "%").setParameter("TargetSystem", systemName).setParameter("ImpId", impId).list();
	return lSegmentList;
    }

    public List<CheckoutSegments> getSegmentsBySystemAndPutAndPlan(String planId, String putLevelSourceUrl, String systemName) {
	String lQuery = "SELECT cs FROM CheckoutSegments cs" + " WHERE cs.active = 'Y'" + " AND cs.fileType = 'IBM'" + " AND cs.sourceUrl NOT like :PutLevelURL" + " AND cs.targetSystem = :TargetSystem" + " AND cs.planId.id = :PlanId";

	List<CheckoutSegments> lSegmentList = getCurrentSession().createQuery(lQuery).setParameter("PutLevelURL", "%" + putLevelSourceUrl + "%").setParameter("TargetSystem", systemName).setParameter("PlanId", planId).list();
	return lSegmentList;
    }

    public List<CheckoutSegments> findSOFileNameByImpPlan(List<String> planIds) throws WorkflowException {
	StringBuilder lQuery = new StringBuilder("");
	lQuery.append("select a.id from (SELECT cs.id, replace(LOWER(SUBSTRING (cs.programName FROM  '%#\".%#\"%' FOR '#')),'.','') as file_ext FROM Checkout_Segments cs, Imp_Plan ip").append(" WHERE cs.active = 'Y'").append(" AND cs.plan_id in (:PlanId)").append(" AND cs.plan_id = ip.id").append(" AND ip.macro_Header = false").append(" AND (cs.so_name is null or cs.so_name = '')) as a where a.file_ext in ('cpp','asm', 'sbt','c')");
	// .append(" AND (cs.programName ilike '%.c' OR cs.programName ilike '%.cpp' OR
	// cs.programName ilike '%.asm' OR cs.ProgramName ilike '%.sbt')");

	List<Object[]> lSegmentList = getCurrentSession().createSQLQuery(lQuery.toString()).setParameterList("PlanId", planIds).list();
	List<Integer> lIdList = new ArrayList();
	for (Object[] lSegment : lSegmentList) {
	    lIdList.add(Integer.valueOf(lSegment[0].toString()));
	}
	String lObjQuery = "Select cs from CheckoutSegments where cs.id in (:Ids)";
	List<CheckoutSegments> lReturnData = getCurrentSession().createQuery(lObjQuery).setParameterList("Ids", lIdList).list();

	return lReturnData;
    }

    public Long findSOCountByImpPlan(List<String> planIds, String systemName) throws WorkflowException {
	StringBuilder lQuery = new StringBuilder("");
	lQuery.append("SELECT count(distinct cs.so_name || cs.plan_id || cs.target_system) FROM checkout_segments cs").append(" WHERE cs.active = 'Y'").append(" AND cs.plan_id in (:PlanId)").append(" AND cs.target_system = :SystemName").append(" AND (cs.so_name IS NOT NULL or cs.so_name != '')");

	Object lCnt = getCurrentSession().createSQLQuery(lQuery.toString()).setParameterList("PlanId", planIds).setParameter("SystemName", systemName).uniqueResult();
	return Long.parseLong(lCnt.toString());
    }

    public Long findSOCountByImpPlanAndFuncArea(List<String> planIds, String funcArea) throws WorkflowException {
	StringBuilder lQuery = new StringBuilder("");
	lQuery.append("SELECT count(distinct cs.so_name) FROM checkout_segments cs").append(" WHERE cs.active = 'Y'").append(" AND cs.plan_id in (:PlanId)").append(" AND (cs.so_name IS NOT NULL or cs.so_name != '')").append(" AND cs.func_area = :FuncArea");

	Object lCnt = getCurrentSession().createSQLQuery(lQuery.toString()).setParameterList("PlanId", planIds).setParameter("FuncArea", funcArea).uniqueResult();
	return Long.parseLong(lCnt.toString());
    }

    public String getSegmentsByPlanAndSystem(String pPlanId, String systemName) {
	String lQuery = "select distinct string_agg(a.file_name,',') from checkout_segments a, imp_plan b where a.active='Y' and b.active='Y' and a.plan_id = b.id and b.full_build_dt is not null and a.last_changed_time >= b.full_build_dt and b.id = :planId ";

	if (systemName != null && !systemName.isEmpty()) {
	    lQuery = lQuery + " and a.target_system = :systemName";
	}

	Query query = getCurrentSession().createSQLQuery(lQuery);
	if (systemName != null && !systemName.isEmpty()) {
	    query.setParameter("systemName", systemName);
	}
	Object lPlanList = query.setParameter("planId", pPlanId).uniqueResult();
	return lPlanList != null ? lPlanList.toString() : null;
    }

    public Boolean isChangedSegAvailable(String pPlanId, String systemName) {
	String lQuery = "select distinct string_agg(a.file_name,',') from checkout_segments a, imp_plan b where b.active='Y' and a.plan_id = b.id and b.full_build_dt is not null and a.last_changed_time >= b.full_build_dt and b.id = :planId ";

	if (systemName != null && !systemName.isEmpty()) {
	    lQuery = lQuery + " and a.target_system = :systemName";
	}

	Query query = getCurrentSession().createSQLQuery(lQuery);
	if (systemName != null && !systemName.isEmpty()) {
	    query.setParameter("systemName", systemName);
	}
	Object lPlanList = query.setParameter("planId", pPlanId).uniqueResult();
	return lPlanList != null ? true : false;
    }

    /**
     * ZTPFM-2014 Activity Log implemented
     */
    public List<CheckoutSegments> getMacroSegemntsByPlanId(String planId, String fileName) {
	String lQuery = "SELECT cs FROM CheckoutSegments cs" + " WHERE cs.active = 'Y'" + " AND cs.fileName  like :fileName" + " AND cs.planId.id = :planId";
	List<CheckoutSegments> lSegmentList = getCurrentSession().createQuery(lQuery).setParameter("fileName", "%" + fileName + "%").setParameter("planId", planId).list();
	return lSegmentList;
    }

    public List<CheckoutSegments> getChangedFilesInWorkspace(String impId) {
	String lQuery = "select cs.* from checkout_segments cs, " + "( " + " select im.checkin_date_time as checkinTime, MAX(bl.modified_dt) as buildTime from implementation im, build bl " + "  where im.id = :ImpId " + "  and im.active = 'Y' " + "  and im.plan_id = bl.plan_id " + "  and bl.build_type = :BuildType " + "  and bl.job_status = 'S' " + "  group by checkinTime " + ") as cd " + " where cs.last_changed_time >= cd.checkinTime " + " and cs.last_changed_time >= cd.buildTime "
		+ " and cs.imp_id = :ImpId and cd.checkInTime is not null " + " and ((cs.active = 'N' and cs.created_dt >= cd.checkInTime and cs.modified_dt >= cd.checkInTime) = false) " + " UNION ALL select cs.* from checkout_segments cs, implementation im, imp_plan ip " + " where cs.plan_id = ip.id " + " and im.plan_id = ip.id " + " and im.active = 'Y' " + " and im.checkin_date_time is null " + " and ip.rejected_date_time is not null " + " and cs.last_changed_time >= ip.rejected_date_time "
		+ " and im.id = :ImpId";

	List<CheckoutSegments> lReturnData = new ArrayList();
	lReturnData = getCurrentSession().createSQLQuery(lQuery).setParameter("ImpId", impId).setParameter("BuildType", Constants.BUILD_TYPE.DVL_BUILD.name()).list();
	return lReturnData;
    }

    public List<CheckoutSegments> findSystemBasedSegs(String lPlanId, String system, List<String> fileNames) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("planId.id", lPlanId));
	lCriteria.add(Restrictions.eq("targetSystem", system));
	lCriteria.add(Restrictions.eq("active", "Y"));
	if (fileNames != null && !fileNames.isEmpty()) {
	    lCriteria.add(Restrictions.in("fileName", fileNames));
	}
	return lCriteria.list();
    }

}
