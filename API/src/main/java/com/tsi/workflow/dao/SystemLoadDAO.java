package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.LoadCategories;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.ui.DSLDetailsForm;
import com.tsi.workflow.beans.ui.ProdLoadStatusDetails;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.NullPrecedence;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

@Repository
public class SystemLoadDAO extends BaseDAO<SystemLoad> {

    public List<SystemLoad> findByLoadCategories(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("loadCategoryId", new LoadCategories(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<SystemLoad> findByLoadCategories(List<LoadCategories> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("loadCategoryId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<SystemLoad> findByLoadCategories(LoadCategories pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("loadCategoryId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<SystemLoad> findByLoadCategories(Integer[] pId) throws WorkflowException {
	List<LoadCategories> lLoadCategories = new ArrayList<>();
	for (Integer lId : pId) {
	    lLoadCategories.add(new LoadCategories(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("loadCategoryId", lLoadCategories));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<SystemLoad> findByPutLevel(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("putLevelId", new PutLevel(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<SystemLoad> findByPutLevel(List<PutLevel> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("putLevelId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<SystemLoad> findByPutLevel(PutLevel pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("putLevelId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<SystemLoad> findByPutLevel(Integer[] pId) throws WorkflowException {
	List<PutLevel> lPutLevel = new ArrayList<>();
	for (Integer lId : pId) {
	    lPutLevel.add(new PutLevel(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("putLevelId", lPutLevel));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<SystemLoad> findByImpPlan(String pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<SystemLoad> findByImpPlan(List<ImpPlan> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<SystemLoad> findByImpPlan(ImpPlan pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public Long countByImpPlan(ImpPlan pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", pId);
	lFilter.put("active", "Y");
	return count(lFilter);
    }

    public List<SystemLoad> findByImpPlan(String[] pId) throws WorkflowException {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : pId) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", lImpPlan));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<SystemLoad> findByImpPlanSorted(String[] pId) throws WorkflowException {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : pId) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", lImpPlan));
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.addOrder(Order.asc("loadDateTime").nulls(NullPrecedence.FIRST));
	return lCriteria.list();
    }

    public List<SystemLoad> findBySystem(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", new System(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<SystemLoad> findBySystem(Integer pId, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", new System(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter, pOffset, pLimit, pOrderBy);
    }

    public Long getCountBySystem(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", new System(pId));
	lFilter.put("active", "Y");
	return count(lFilter);
    }

    public SystemLoad findBy(ImpPlan plan, System system) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", system);
	lFilter.put("planId", plan);
	lFilter.put("active", "Y");
	return find(lFilter);
    }

    public List<SystemLoad> findBySystem(List<System> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<SystemLoad> findBySystem(System pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public SystemLoad find(ImpPlan plan, System system) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", system);
	lFilter.put("planId", plan);
	lFilter.put("active", "Y");
	return find(lFilter);
    }

    public List<SystemLoad> findBySystem(Integer[] pId) throws WorkflowException {
	List<System> lSystem = new ArrayList<>();
	for (Integer lId : pId) {
	    lSystem.add(new System(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", lSystem));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<SystemLoad> findBySystem(Integer pId, String pStatus, Integer pOffset, Integer pLimit) throws WorkflowException {
	String lQuery = "SELECT sl FROM SystemLoad sl, ImpPlan ip, System s" + " WHERE sl.systemId.id = :SystemId AND " + " sl.planId.id = ip.id AND " + " ip.planStatus = :PlanStatus AND " + " sl.active = 'Y' AND " + " s.id = :SystemId ORDER BY sl.loadDateTime DESC, sl.loadCategoryId.name ASC";

	List<SystemLoad> lSystemLoadList = getCurrentSession().createQuery(lQuery).setParameter("SystemId", pId).setParameter("PlanStatus", pStatus).setFirstResult(pOffset * pLimit).setMaxResults(pLimit).list();
	return lSystemLoadList;
    }

    public Long getCountBySystem(Integer pId, String pStatus) throws WorkflowException {
	String lQuery = "SELECT COUNT(sl) FROM SystemLoad sl, ImpPlan ip, System s" + " WHERE sl.systemId.id = :SystemId AND " + " sl.planId.id = ip.id AND " + " ip.planStatus = :PlanStatus AND " + " sl.active = 'Y' AND " + " s.id = :SystemId";

	Long lReturnCount = (Long) getCurrentSession().createQuery(lQuery).setParameter("SystemId", pId).setParameter("PlanStatus", pStatus).uniqueResult();
	return lReturnCount;
    }

    public List<SystemLoad> getStagingDepedendentPlansWithDate(String pPlanId, Integer systemId, Date loadDate, Set<String> status, boolean isforward) {
	String lQuery = "SELECT sysLoad FROM SystemLoad sysLoad, ImpPlan impPlan , System sys" + " WHERE sysLoad.systemId = sys.id " + " AND sysLoad.active = 'Y'" + " AND sys.id = :systemId" + " AND (sysLoad.loadDateTime " + (isforward ? ">=" : "<=") + " :loadDateTime ) " + " AND sysLoad.planId = impPlan.id" + " AND impPlan.id <> :planId" + " AND impPlan.approveDateTime IS NOT NULL" + " AND impPlan.planStatus IN :planStatus" + " ORDER BY sysLoad.loadDateTime desc";

	return getCurrentSession().createQuery(lQuery).setParameter("systemId", systemId).setParameter("planId", pPlanId).setParameter("loadDateTime", loadDate).setParameterList("planStatus", status).list();
    }

    public List<SystemLoad> getStagingDepedendentPlans(String pPlanId, Integer systemId, Date loaDate) {
	// TODO: need check Query
	String lQuery = "SELECT othLoad FROM CheckoutSegments othSeg ,CheckoutSegments seg, SystemLoad othLoad, SystemLoad loadl, ImpPlan othPlan, System sys" + " WHERE seg.fileName = othSeg.fileName" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.targetSystem = othSeg.targetSystem" + " AND sys.name = seg.targetSystem" + " AND seg.id <> othSeg.id" + " AND seg.planId <> othSeg.planId" + " AND loadl.planId <> othLoad.planId" + " AND loadl.active = 'Y'"
		+ " AND othLoad.active = 'Y'" + " AND othPlan.approveDateTime is not null" + " AND loadl.systemId = othLoad.systemId" + " AND othLoad.planId = othSeg.planId" + " AND (" + "         OR(othLoad.loadDateTime BETWEEN loadl.loadDateTime AND  :loadDateTime)" + "         OR(othLoad.loadDateTime BETWEEN :loadDateTime AND loadl.loadDateTime)" + "     )" + " AND othPlan.id = othLoad.planId" + " AND sys.id = othLoad.systemId" + " AND loadl.planId = seg.planId"
		+ " AND seg.planId LIKE :planId" + " AND sys.id = :systemId order by othLoad.loadDateTime desc";

	return getCurrentSession().createQuery(lQuery).setParameter("loadDateTime", loaDate).setParameter("planId", new ImpPlan(pPlanId)).setParameter("systemId", systemId).list();
    }

    public List<SystemLoad> getFallbackLoadSetPlanIds(String pPlanId, Set<String> status, Integer systemId) {
	String lQuery = "SELECT othLoad FROM CheckoutSegments othSeg ,CheckoutSegments seg, SystemLoad othLoad, SystemLoad loadl, ImpPlan othPlan, System sys, LoadCategories cat" + " WHERE seg.fileName = othSeg.fileName" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.targetSystem = othSeg.targetSystem" + " AND sys.name = seg.targetSystem" + " AND seg.id <> othSeg.id" + " AND seg.planId <> othSeg.planId" + " AND loadl.planId <> othLoad.planId"
		+ " AND loadl.systemId = othLoad.systemId" + " AND othLoad.planId = othSeg.planId" + " AND othLoad.active = 'Y' " + " AND loadl.active = 'Y' " + " AND (othLoad.loadDateTime < loadl.loadDateTime)" + " AND othPlan.id = othLoad.planId" + " AND sys.id = othLoad.systemId" + " AND sys.id = :SystemId" + " AND othLoad.loadCategoryId = cat.id" + " AND cat.name NOT LIKE 'P'" + " AND loadl.planId = seg.planId" + " AND seg.planId LIKE :planid" + " AND othPlan.planStatus IN (:status)"
		+ " ORDER BY othLoad.loadDateTime desc";
	return getCurrentSession().createQuery(lQuery).setParameter("planid", new ImpPlan(pPlanId)).setParameter("SystemId", systemId).setParameterList("status", status).list();
    }

    public List<SystemLoad> getSystemLoadsFromImp(String ImplId) {
	String lQuery = "SELECT DISTINCT seg.systemLoad from CheckoutSegments seg" + " WHERE seg.impId.id = :ImplId" + " AND seg.active = 'Y'";
	return getCurrentSession().createQuery(lQuery).setParameter("ImplId", ImplId).list();
    }

    public List<SystemLoad> findBySystem(Integer pId, Set<String> pStatus, Boolean pMacroHeader, Integer pOffset, Integer pLimit, String pFilter) throws WorkflowException {
	String lQuery = "SELECT load1 FROM SystemLoad load1, ImpPlan plan1, Build build1" + " WHERE load1.systemId.id = :SystemId" + " AND load1.planId.id = plan1.id" + " AND plan1.macroHeader = :MacroHeader" + " AND load1.active = 'Y'" + " AND build1.active = 'Y'" + " AND build1.planId.id = plan1.id" + " AND build1.systemId.id = load1.systemId.id" + " AND build1.buildType = :BuildType" + " AND build1.loadSetType <> :LoadSetType" + " AND plan1.planStatus in (:PlanStatus)"
		+ " AND ((select count(load2) from SystemLoad load2 where load2.planId.id = plan1.id AND load2.prodLoadStatus IN (:StatusList)) = 0" + " OR (load1.loadCategoryId.name = :RCategory AND (load1.prodLoadStatus is null OR load1.prodLoadStatus NOT IN (:StatusList))))";
	if (pFilter != null && !pFilter.isEmpty()) {
	    lQuery = lQuery + " AND plan1.id LIKE '%" + pFilter.toUpperCase() + "%'";
	}
	lQuery = lQuery + " ORDER BY load1.loadDateTime DESC, load1.loadCategoryId.name ASC, load1.planId DESC";

	List<SystemLoad> lSystemLoadList = getCurrentSession().createQuery(lQuery).setParameter("RCategory", Constants.RestrictedCatForAcceptPlan.R.name()).setParameter("SystemId", pId).setParameterList("StatusList", Constants.LOAD_SET_STATUS.getAcceptAndFallbackList()).setParameterList("PlanStatus", pStatus).setParameter("MacroHeader", pMacroHeader).setParameter("BuildType", Constants.BUILD_TYPE.STG_LOAD.name()).setParameter("LoadSetType", Constants.LoaderTypes.A.name())
		.setFirstResult(pOffset * pLimit).setMaxResults(pLimit).list();
	return lSystemLoadList;
    }

    public Long getCountBySystem(Integer pId, Set<String> pStatus, Boolean pMacroHeader, String pFilter) throws WorkflowException {
	String lQuery = "SELECT count(load1) FROM SystemLoad load1, ImpPlan plan1, Build build1" + " WHERE load1.systemId.id = :SystemId" + " AND load1.planId.id = plan1.id" + " AND plan1.planStatus in (:PlanStatus)" + " AND plan1.macroHeader = :MacroHeader" + " AND load1.active = 'Y'" + " AND build1.active = 'Y'" + " AND build1.planId.id = plan1.id" + " AND build1.systemId.id = load1.systemId.id" + " AND build1.buildType = :BuildType" + " AND build1.loadSetType <> :LoadSetType"
		+ " AND ((select count(load2) from SystemLoad load2 where load2.planId.id = plan1.id AND load2.prodLoadStatus IN (:StatusList)) = 0" + " OR (load1.loadCategoryId.name = :RCategory AND (load1.prodLoadStatus is null OR load1.prodLoadStatus NOT IN (:StatusList))))";
	if (pFilter != null && !pFilter.isEmpty()) {
	    lQuery = lQuery + " AND plan1.id LIKE '%" + pFilter.toUpperCase() + "%'";
	}
	Long lReturnCount = (Long) getCurrentSession().createQuery(lQuery).setParameter("RCategory", Constants.RestrictedCatForAcceptPlan.R.name()).setParameter("SystemId", pId).setParameterList("StatusList", Constants.LOAD_SET_STATUS.getAcceptAndFallbackList()).setParameterList("PlanStatus", pStatus).setParameter("MacroHeader", pMacroHeader).setParameter("BuildType", Constants.BUILD_TYPE.STG_LOAD.name()).setParameter("LoadSetType", Constants.LoaderTypes.A.name()).uniqueResult();
	return lReturnCount;
    }

    public List<Object[]> getDependentPlanByApproveDate(String planId, Integer systemId, Date loadDate, Set<String> status) {
	return getDependentPlanByApproveDate(planId, systemId, loadDate, status, null);
    }

    // ZTPFM-2795 Get Dependent Plans based upon the Put Level
    public List<Object[]> getDependentPlanByApproveDate(String pPlanId, Integer systemId, Date loadDate, Set<String> status, Integer putLevelId) {
	String lQuery = "SELECT sysLoad, impPlan FROM SystemLoad sysLoad, ImpPlan impPlan , System sys" + " WHERE sysLoad.systemId = sys.id " + " AND sys.id = :systemId " + " AND sysLoad.putLevelId.id = :PutLevelId " + " AND ((sysLoad.loadDateTime is NOT null AND sysLoad.loadDateTime <= :loadDateTime ) " + " or (sysLoad.loadDateTime is NOT null AND sysLoad.loadDateTime > :loadDateTime and impPlan.planStatus = :status)) " + " AND sysLoad.planId = impPlan.id " + " AND impPlan.id <> :planId "
		+ " AND impPlan.planStatus IN :planStatus " + " order by sysLoad.loadDateTime desc, impPlan.approveDateTime desc";
	return getCurrentSession().createQuery(lQuery).setParameter("systemId", systemId).setParameter("planId", pPlanId).setParameter("loadDateTime", loadDate).setParameterList("planStatus", status).setParameter("status", Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name()).setParameter("PutLevelId", putLevelId).list();
    }

    public List<SystemLoad> findFallbackLoadsBySystem(Integer pId, Integer pOffset, Integer pLimit) {
	Calendar c = Calendar.getInstance();
	c.add(Calendar.WEEK_OF_MONTH, Constants.fallbackLoadDateGap);
	// String lQuery = "SELECT load1 FROM SystemLoad load1, ImpPlan plan1, Build
	// build1" + " WHERE load1.systemId.id = :SystemId" + " AND load1.planId.id =
	// plan1.id" + " AND plan1.acceptedDateTime BETWEEN :fromTime AND :toTime" + "
	// AND (select count(load2) from SystemLoad load2 where load2.planId.id =
	// plan1.id AND load2.prodLoadStatus IN (:StatusList)) = 0" + " AND
	// plan1.planStatus in (:PlanStatus)" + " AND plan1.macroHeader = :MacroHeader"
	// + " AND load1.active = 'Y'"
	// + " AND build1.active = 'Y'" + " AND build1.planId.id = plan1.id" + " AND
	// build1.systemId.id = load1.systemId.id" + " AND build1.buildType =
	// :BuildType" + " AND build1.loadSetType <> :LoadSetType" + " ORDER BY
	// load1.loadDateTime DESC, load1.loadCategoryId.name ASC";

	String lQuery = "SELECT load1 FROM SystemLoad load1, ImpPlan plan1, Build build1" + " WHERE load1.systemId.id = :SystemId" + " AND load1.planId.id = plan1.id" + " AND plan1.macroHeader = :MacroHeader" + " AND load1.active = 'Y'" + " AND build1.active = 'Y'" + " AND build1.planId.id = plan1.id" + " AND build1.systemId.id = load1.systemId.id" + " AND build1.buildType = :BuildType" + " AND build1.loadSetType <> :LoadSetType"
		+ " AND ((plan1.acceptedDateTime BETWEEN :fromTime AND :toTime AND plan1.planStatus in (:PlanStatus)" + " AND (SELECT count(load2) from SystemLoad load2 where load2.planId.id = plan1.id AND load2.prodLoadStatus IN (:StatusList)) = 0)" + " OR (load1.loadCategoryId.name = 'R' AND plan1.planStatus in (:RCatPlanStatus)" + " AND (SELECT count(prod2) from ProductionLoads prod2 WHERE prod2.systemId.id = :SystemId AND prod2.planId.id = plan1.id" + " AND prod2.active = 'Y'"
		+ " AND prod2.cpuId is null" + " AND (plan1.acceptedDateTime is null OR (plan1.acceptedDateTime BETWEEN :fromTime AND :toTime))" + " AND prod2.status IN (:RCatProdLoadStatusList)) <> 0))" + " ORDER BY load1.loadDateTime DESC, load1.loadCategoryId.name ASC, load1.planId DESC";
	List<SystemLoad> lSystemLoadList = getCurrentSession().createQuery(lQuery).setParameter("SystemId", pId).setParameterList("StatusList", Constants.LOAD_SET_STATUS.getFallbackFinalList()).setParameterList("PlanStatus", Arrays.asList(Constants.PlanStatus.ONLINE.name(), Constants.PlanStatus.FALLBACK_DEPLOYED_IN_PRODUCTION.name())).setParameter("MacroHeader", false).setParameter("BuildType", Constants.BUILD_TYPE.STG_LOAD.name()).setParameter("LoadSetType", Constants.LoaderTypes.A.name())
		.setParameter("fromTime", c.getTime()).setParameter("toTime", new Date()).setParameterList("RCatPlanStatus", Arrays.asList(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name(), Constants.PlanStatus.DEPLOYED_IN_PRODUCTION.name(), Constants.PlanStatus.ONLINE.name(), Constants.PlanStatus.FALLBACK_DEPLOYED_IN_PRODUCTION.name())).setParameterList("RCatProdLoadStatusList", Constants.LOAD_SET_STATUS.getLoadsetStatusForRCatPlan()).setFirstResult(pOffset * pLimit)
		.setMaxResults(pLimit).list();
	return lSystemLoadList;
    }

    public Long countFallbackLoadsBySystem(Integer pId) throws WorkflowException {
	Calendar c = Calendar.getInstance();
	c.add(Calendar.WEEK_OF_MONTH, Constants.fallbackLoadDateGap);
	String lQuery = "SELECT count(load1) FROM SystemLoad load1, ImpPlan plan1, Build build1" + " WHERE load1.systemId.id = :SystemId" + " AND load1.planId.id = plan1.id" + " AND plan1.acceptedDateTime BETWEEN :fromTime AND :toTime" + " AND (select count(load2) from SystemLoad load2 where load2.planId.id = plan1.id AND load2.prodLoadStatus IN (:StatusList)) = 0" + " AND plan1.planStatus = :PlanStatus" + " AND plan1.macroHeader = :MacroHeader" + " AND load1.active = 'Y'"
		+ " AND build1.active = 'Y'" + " AND build1.planId.id = plan1.id" + " AND build1.systemId.id = load1.systemId.id" + " AND build1.buildType = :BuildType" + " AND build1.loadSetType <> :LoadSetType";

	Long lReturnCount = (Long) getCurrentSession().createQuery(lQuery).setParameter("SystemId", pId).setParameterList("StatusList", Constants.LOAD_SET_STATUS.getFallbackFinalList()).setParameter("PlanStatus", Constants.PlanStatus.ONLINE.name()).setParameter("MacroHeader", false).setParameter("BuildType", Constants.BUILD_TYPE.STG_LOAD.name()).setParameter("LoadSetType", Constants.LoaderTypes.A.name()).setParameter("fromTime", c.getTime()).setParameter("toTime", new Date()).uniqueResult();
	return lReturnCount;
    }

    public List<SystemLoad> findPlanByQATestingStatus(String planId, List<String> qaTestingStatus) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("planId", new ImpPlan(planId)));
	expressions.add(Restrictions.in("qaBypassStatus", qaTestingStatus));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<SystemLoad> findBySystemLoadId(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("id", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<DSLDetailsForm> getDslDetails(String systemName) {

	StringBuilder sbDSL = new StringBuilder();
	sbDSL.append("select a.name as systemname, b.load_set_name as loadsetname, b.fallback_load_set_name as fallbackloadsetname, b.load_date_time as loaddatetime, d.name as vparname , e.plan_status as planstatus  ");
	sbDSL.append(" from system a, system_load b, system_load_actions c, vpars d, imp_plan e ");
	sbDSL.append(" where b.system_id = a.id and c.system_id = a.id and c.system_load_id = b.id and d.id = c.vpar_id and d.system_id = a.id and e.id = b.plan_id ");
	sbDSL.append(" and a.name=:systemName and c.dsl_update='Y' and a.active='Y' and b.active='Y' and c.active='Y' and d.active='Y' and e.active='Y'");
	sbDSL.append(" and (b.load_set_name is not null or b.fallback_load_set_name is not null ) and  ( e.plan_status NOT IN (:planStatus) or   (e.plan_status in ('ONLINE','ONLINE_RELOAD')");
	sbDSL.append(" and Date(b.load_date_time) >= date (Now() - interval '30 days')))");

	List<String> statusList = new ArrayList<>();
	statusList.add(Constants.PlanStatus.DELETED.name());
	statusList.add(Constants.PlanStatus.FALLBACK.name());
	statusList.add(Constants.PlanStatus.ONLINE.name());
	statusList.add(Constants.PlanStatus.ONLINE_RELOAD.name());
	List<DSLDetailsForm> dslDetails = getCurrentSession().createSQLQuery(sbDSL.toString()).setParameter("systemName", systemName.toUpperCase()).setParameterList("planStatus", statusList).setResultTransformer(new AliasToBeanResultTransformer(DSLDetailsForm.class)).list();

	return dslDetails;
    }

    public List<String> getSystemsToAcceptForR(String planId, List<String> planStatus, String prodLoadStatus) throws WorkflowException {
	String lQuery = "select s.name from production_loads pl, load_categories lc, system_load sl, imp_plan ip, system s" + " where ip.id = :PlanId" + " and ip.is_accept_enabled = true" + " and sl.plan_id = ip.id" + " and sl.active = 'Y'" + " and sl.load_category_id = lc.id" + " and lc.active = 'Y'" + " and lc.name = 'R'" + " and pl.system_load_id = sl.id" + " and pl.active = 'Y'" + " and pl.status = :ProdLoadStatus" + " and ip.plan_status in (:PlanStatus)" + " and s.id = sl.system_id"
		+ " and s.active = 'Y' and pl.cpu_id IS NULL";

	List<String> lReturnlist = getCurrentSession().createSQLQuery(lQuery).setParameter("PlanId", planId).setParameterList("PlanStatus", planStatus).setParameter("ProdLoadStatus", prodLoadStatus).list();
	return lReturnlist;
    }

    public SystemLoad getSystemLoadByPlanAndSystem(String pPlanId, Integer systemId) {
	String lQuery = "SELECT sysLoad FROM SystemLoad sysLoad, ImpPlan impPlan , System sys" + " WHERE sysLoad.systemId = sys.id " + " AND sysLoad.active = 'Y'" + " AND sys.id = :systemId" + " AND sysLoad.planId = impPlan.id" + " AND impPlan.id = :planId";

	return (SystemLoad) getCurrentSession().createQuery(lQuery).setParameter("systemId", systemId).setParameter("planId", pPlanId).list().get(0);

    }

    public List<ProdLoadStatusDetails> findSystemsAndProdLoadStatusByPlan(String planId) {
	String lQuery = "select a.plan_id as planid, c.id as systemid, a.status as loadsetstatus from production_loads a, system_load b, system c where a.active = 'Y' and b.active = 'Y' and c.active = 'Y' and a.plan_id = b.plan_id and a.system_id = b.system_id " + "and b.system_id = c.id and a.plan_id = :planId";
	List<ProdLoadStatusDetails> lReturnlist = getCurrentSession().createSQLQuery(lQuery).setParameter("planId", planId).setResultTransformer(new AliasToBeanResultTransformer(ProdLoadStatusDetails.class)).list();
	return lReturnlist;

    }

    public List<String> getLoadNotAllowedPlans(List<String> planIds, String systemName) {
	String lQuery = "select distinct sl.plan_id from system_load sl, system s   " + " where sl.active = 'Y'   " + " and sl.system_id = s.id   " + " and s.active = 'Y'   " + " and s.name = :SystemName   " + " and sl.plan_id in (:PlanList)   " + " and ((now() < (sl.load_date_time - interval '12 hours'))  "
		+ " or EXISTS (select pl from production_loads pl where sl.id = pl.system_load_id and pl.plan_id = sl.plan_id and pl.active = 'Y' and pl.status = :ActionStatus and pl.last_action_status = 'SUCCESS')       " + ")";
	List<String> lReturnlist = getCurrentSession().createSQLQuery(lQuery).setParameterList("PlanList", planIds).setParameter("SystemName", systemName).setParameter("ActionStatus", Constants.LOAD_SET_STATUS.DELETED.name()).list();
	return lReturnlist;

    }

    public List<String> getAllowedPlansToActivate(List<String> planIds, String systemName) {
	String lQuery = "select distinct sl.plan_id from system_load sl, system s   " + " where sl.active = 'Y'   " + " and sl.system_id = s.id   " + " and s.active = 'Y'   " + " and s.name = :SystemName   " + " and (Now() < sl.load_date_time or EXISTS (select pl from production_loads pl where sl.id = pl.system_load_id and pl.plan_id = sl.plan_id and pl.active = 'Y' and pl.status = :ActionStatus and pl.last_action_status = 'SUCCESS' )) " + " and sl.plan_id in (:PlanList)  ";
	List<String> lReturnlist = getCurrentSession().createSQLQuery(lQuery).setParameterList("PlanList", planIds).setParameter("SystemName", systemName).setParameter("ActionStatus", Constants.LOAD_SET_STATUS.ACTIVATED.name()).list();
	return lReturnlist;

    }

}
