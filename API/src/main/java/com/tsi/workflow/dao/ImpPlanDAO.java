package com.tsi.workflow.dao;

import com.google.gson.Gson;
import com.tsi.workflow.User;
import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Project;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.ui.BuildQueueForm;
import com.tsi.workflow.beans.ui.DependenciesForm;
import com.tsi.workflow.beans.ui.ProdDeactivateResult;
import com.tsi.workflow.beans.ui.ProductionLoadDetailsForm;
import com.tsi.workflow.beans.ui.ReportModel;
import com.tsi.workflow.beans.ui.SegmentBasedActionDetail;
import com.tsi.workflow.beans.ui.SegmentSearchForm;
import com.tsi.workflow.beans.ui.SourceArtifactSearchForm;
import com.tsi.workflow.beans.ui.SourceArtifactSearchResult;
import com.tsi.workflow.beans.ui.SystemBasedMetaData;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

@Repository
public class ImpPlanDAO extends BaseDAO<ImpPlan> {

    private static final Logger LOG = Logger.getLogger(ImpPlanDAO.class.getName());

    public List<ImpPlan> findByProject(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("projectId", new Project(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<ImpPlan> findByProject(List<Project> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("projectId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<ImpPlan> findByProject(Project pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("projectId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<ImpPlan> findByProject(Integer[] pId) throws WorkflowException {
	List<Project> lProject = new ArrayList<>();
	for (Integer lId : pId) {
	    lProject.add(new Project(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("projectId", lProject));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<ImpPlan> find(List<String> pId) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("id", pId));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public void setPlanDelegate(User pApprover, Set<String> pDevManagerUsers) {
	String lQuery = "UPDATE imp_plan SET delegate_id = :approver, delegate_name = :approverName " + "WHERE dev_manager IN ( :devmanager ) AND plan_status IN (:status)";
	getCurrentSession().createSQLQuery(lQuery).setParameter("approver", pApprover.getId()).setParameter("approverName", pApprover.getDisplayName()).setParameterList("devmanager", pDevManagerUsers).setParameterList("status", Constants.PlanStatus.getNonProdStatusMap().keySet()).executeUpdate();
    }

    public void removePlanDelegate(String pUserId) {
	String lQuery = "UPDATE imp_plan SET delegate_id = null, delegate_name = null " + "WHERE dev_manager LIKE :devmanager AND plan_status IN (:status)";
	getCurrentSession().createSQLQuery(lQuery).setParameter("devmanager", pUserId).setParameterList("status", Constants.PlanStatus.getNonProdStatusMap().keySet()).executeUpdate();
    }

    public List<String> getInvalidRelatedPlans(List<String> pPlanId, System pSystem, Date pLoadDateTime) {
	String lQuery = "SELECT plan.id FROM imp_plan plan, system_load load" + " WHERE plan.id = load.plan_id" + " AND load.load_date_time > :loaddatetime" + " AND load.system_id = :systemid" + " AND plan.active = 'Y'" + " AND load.active = 'Y'" + " AND plan.id IN (:plans)";
	List list = getCurrentSession().createSQLQuery(lQuery).setParameter("loaddatetime", pLoadDateTime).setParameter("systemid", pSystem.getId()).setParameterList("plans", pPlanId).list();
	LOG.info(list.size());
	return list;
    }

    public List<DependenciesForm> getAllRelatedDependntPlanDetail(String pPlanId, List<String> pRelPlanId) {
	String lQuery = "SELECT OthPlan.id as planid ,othsys.name as targetsystem , OthPlan.plan_status as status ,othLoad.load_date_time as loaddatetime ," + "OthPlan.load_type as loadtype ,seg.program_name as segments" + " FROM imp_plan OthPlan, system_load othLoad, system_load loadl, system othsys,checkout_segments seg" + " WHERE loadl.plan_id LIKE :planid" + " AND othLoad.plan_id = OthPlan.id" + " AND othsys.id = othLoad.system_id" + " AND loadl.system_id = othLoad.system_id"
		+ " AND seg.plan_id = othLoad.plan_id" + " AND OthPlan.id IN (:plans)";

	List<DependenciesForm> lReturnList = getCurrentSession().createSQLQuery(lQuery.toString()).setParameter("planid", pPlanId).setParameterList("plans", pRelPlanId).setResultTransformer(new AliasToBeanResultTransformer(DependenciesForm.class)).list();
	return lReturnList;
    }

    public List<DependenciesForm> getPreSegmentDepententRelatedPlans(String pPlanId, String pFilter, Integer offset, Integer limit) {
	String lQuery = "SELECT DISTINCT(othSeg.plan_id) as planid ,string_agg(DISTINCT othSeg.target_system, ', ')  as targetsystem , othPlan.plan_status as status , othLoad.load_date_time as loaddatetime, " + " othPlan.load_type as loadtype , string_agg(DISTINCT seg.program_name, ', ') as segments " + " FROM checkout_segments othSeg " + " join checkout_segments seg on seg.file_name = othSeg.file_name AND seg.target_system = othSeg.target_system"
		+ "	join system_load othLoad on othLoad.plan_id = othSeg.plan_id " + "	join system_load loadl on loadl.system_id = othLoad.system_id AND loadl.plan_id = seg.plan_id" + "	join imp_plan othPlan on othPlan.id = othLoad.plan_id" + "	join system sys on sys.name = seg.target_system AND sys.id = othLoad.system_id" + "	left join pre_production_loads preProd on othLoad.system_id = preProd.system_id and othSeg.plan_id = preProd.plan_id" + " WHERE seg.active = 'Y'"
		+ " AND othSeg.active = 'Y'" + " AND othLoad.active = 'Y'" + " AND loadl.active = 'Y'" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id" + " AND loadl.plan_id <> othLoad.plan_id" + " AND (othLoad.load_date_time >= loadl.load_date_time)" + " AND seg.plan_id LIKE ?" + "  GROUP BY othSeg.plan_id,othSeg.target_system , othPlan.plan_status, othLoad.load_date_time, othPlan.load_type" + " ORDER BY othLoad.load_date_time";

	Query query = getCurrentSession().createSQLQuery(lQuery).setParameter(0, pPlanId);
	if (limit > 0) {
	    query.setFirstResult(offset * limit).setMaxResults(limit);
	}
	query.setResultTransformer(new AliasToBeanResultTransformer(DependenciesForm.class));
	return query.list();
    }

    public List<Object[]> getAllRelatedPlanDetail(String pPlanId, List<String> pRelPlanId) {
	String lQuery = "SELECT concat(OthPlan.id,'/',othsys.name), OthPlan.plan_status" + " FROM imp_plan OthPlan, system_load othLoad, system_load loadl, system othsys" + " WHERE loadl.plan_id LIKE :planid" + " AND othLoad.plan_id = OthPlan.id" + " AND othsys.id = othLoad.system_id" + " AND loadl.system_id = othLoad.system_id" + " AND OthPlan.id IN (:plans)" + " AND loadl.active = 'Y'" + " AND othLoad.active = 'Y'";

	return getCurrentSession().createSQLQuery(lQuery).setParameter("planid", pPlanId).setParameterList("plans", pRelPlanId).list();
    }

    public List<Object[]> getPostSegmentRelatedPlansProdLoad(String pPlanId) {
	String lQuery = "SELECT concat(othSeg.plan_id,'/',othSeg.target_system) , othPlan.plan_status, othPlan.id " + " FROM checkout_segments othSeg ,checkout_segments seg, system_load othLoad, " + " system_load loadl, imp_plan othPlan, system sys" + " WHERE seg.file_name = othSeg.file_name" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.target_system = othSeg.target_system" + " AND sys.name = seg.target_system" + " AND seg.id <> othSeg.id"
		+ " AND seg.plan_id <> othSeg.plan_id" + " AND loadl.plan_id <> othLoad.plan_id" + " AND loadl.system_id = othLoad.system_id" + " AND othLoad.prod_load_status LIKE 'ACTIVATED%'" + " AND othLoad.plan_id = othSeg.plan_id" + " AND loadl.active = 'Y'" + " AND othLoad.active = 'Y'" + " AND othLoad.load_date_time is not null" + " AND loadl.load_date_time is not null" + " AND othLoad.load_date_time > loadl.load_date_time " + " AND othPlan.id = othLoad.plan_id"
		+ " AND sys.id = othLoad.system_id" + " AND loadl.plan_id = seg.plan_id" + " AND seg.plan_id LIKE ?";

	return getCurrentSession().createSQLQuery(lQuery).setParameter(0, pPlanId).list();
    }

    public List<Object[]> getPostSegmentRelatedPlansProdLoadFallbackLoad(String pPlanId, boolean executeProdLoadStatus) {
	String lQuery = "SELECT concat(othSeg.plan_id,'/',othSeg.target_system) , othPlan.plan_status, othPlan.id,othLoad.prod_load_status " + " FROM checkout_segments othSeg ,checkout_segments seg, system_load othLoad, " + " system_load loadl, imp_plan othPlan, system sys" + " WHERE seg.file_name = othSeg.file_name" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.target_system = othSeg.target_system" + " AND sys.name = seg.target_system" + " AND seg.id <> othSeg.id"
		+ " AND seg.plan_id <> othSeg.plan_id" + " AND loadl.plan_id <> othLoad.plan_id" + " AND loadl.system_id = othLoad.system_id" + " AND othLoad.plan_id = othSeg.plan_id" + " AND loadl.active = 'Y'" + " AND othLoad.active = 'Y'" + " AND othLoad.load_date_time is not null" + " AND loadl.load_date_time is not null" + " AND othLoad.load_date_time > loadl.load_date_time " + " AND othPlan.id = othLoad.plan_id" + " AND sys.id = othLoad.system_id" + " AND loadl.plan_id = seg.plan_id"
		+ " AND seg.plan_id LIKE ?";
	if (executeProdLoadStatus) {
	    lQuery = lQuery + " AND othLoad.prod_load_status in ('ACTIVATED_ON_ALL_CPU','ACCEPTED','FALLBACK_LOADED','FALLBACK_ACTIVATED','FALLBACK_DEACTIVATED','FALLBACK_DELETED','ACTIVATED_ON_SINGLE_CPU','ACTIVATED_ON_MULTIPLE_CPU')";
	}

	return getCurrentSession().createSQLQuery(lQuery).setParameter(0, pPlanId).list();
    }

    public List<Object[]> getPostSegmentRelatedPlansProdLoadFallback(String pPlanId) {
	String lQuery = "SELECT concat(othSeg.plan_id,'/',othSeg.target_system) , othPlan.plan_status, othPlan.id " + " FROM checkout_segments othSeg ,checkout_segments seg, system_load othLoad, " + " system_load loadl, imp_plan othPlan, system sys" + " WHERE seg.file_name = othSeg.file_name" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.target_system = othSeg.target_system" + " AND sys.name = seg.target_system" + " AND seg.id <> othSeg.id"
		+ " AND seg.plan_id <> othSeg.plan_id" + " AND loadl.plan_id <> othLoad.plan_id" + " AND loadl.system_id = othLoad.system_id" + " AND othLoad.prod_load_status is not null" + " AND othLoad.plan_id = othSeg.plan_id" + " AND loadl.active = 'Y'" + " AND othLoad.active = 'Y'" + " AND othLoad.load_date_time is not null" + " AND loadl.load_date_time is not null" + " AND othLoad.load_date_time > loadl.load_date_time " + " AND othPlan.id = othLoad.plan_id"
		+ " AND sys.id = othLoad.system_id" + " AND loadl.plan_id = seg.plan_id" + " AND seg.plan_id LIKE ?";

	return getCurrentSession().createSQLQuery(lQuery).setParameter(0, pPlanId).list();
    }

    private List<Object[]> getRelatedPlanDetail(String pPlanId, List<String> pRelPlanId) {
	String lQuery = "SELECT concat(OthPlan.id,'/',othsys.name), OthPlan.plan_status" + " FROM imp_plan OthPlan, system_load othLoad, system_load loadl, system othsys" + " WHERE loadl.plan_id LIKE :planid" + " AND (othLoad.load_date_time < loadl.load_date_time)" + " AND othLoad.plan_id = OthPlan.id" + " AND othsys.id = othLoad.system_id" + " AND loadl.system_id = othLoad.system_id" + " AND OthPlan.id IN (:plans)";
	return getCurrentSession().createSQLQuery(lQuery).setParameter("planid", pPlanId).setParameterList("plans", pRelPlanId).list();
    }

    public List<Object[]> getPreSegmentRelatedPlans(String pPlanId) {
	// String lQuery = "SELECT concat(othSeg.plan_id,'/',othSeg.target_system) ,
	// othPlan.plan_status"
	// + " FROM checkout_segments othSeg on seg.file_name = othSeg.file_name "
	// + ",checkout_segments seg, system_load othLoad, system_load loadl, imp_plan
	// othPlan, system sys"
	// + " WHERE seg.file_name = othSeg.file_name"
	// + " AND seg.active = 'Y'"
	// + " AND othSeg.active = 'Y'"
	// + " AND othLoad.active = 'Y'"
	// + " AND loadl.active = 'Y'"
	// + " AND seg.target_system = othSeg.target_system"
	// + " AND sys.name = seg.target_system"
	// + " AND seg.id <> othSeg.id"
	// + " AND seg.plan_id <> othSeg.plan_id"
	// + " AND loadl.plan_id <> othLoad.plan_id"
	// + " AND loadl.system_id = othLoad.system_id"
	// + " AND othLoad.plan_id = othSeg.plan_id"
	// + " AND (othLoad.load_date_time <= loadl.load_date_time)"
	// + " AND othPlan.id = othLoad.plan_id"
	// + " AND sys.id = othLoad.system_id"
	// + " AND loadl.plan_id = seg.plan_id"
	// + " AND seg.plan_id LIKE ?";
	String lQuery = "SELECT concat(othSeg.plan_id,'/',othSeg.target_system) , othPlan.plan_status," + " preProd.status, preProd.active, preProd.system_load_action_id, preProd.last_action_status, prodLoad.status as prodStatus" + " FROM checkout_segments othSeg " + " join checkout_segments seg on seg.file_name = othSeg.file_name AND seg.target_system = othSeg.target_system" + "	join system_load othLoad on othLoad.plan_id = othSeg.plan_id "
		+ "	join system_load loadl on loadl.system_id = othLoad.system_id AND loadl.plan_id = seg.plan_id" + "	join imp_plan othPlan on othPlan.id = othLoad.plan_id" + "	join system sys on sys.name = seg.target_system AND sys.id = othLoad.system_id" + "	left join pre_production_loads preProd on othLoad.system_id = preProd.system_id and othSeg.plan_id = preProd.plan_id"
		+ "	left join production_loads prodLoad on othLoad.system_id = prodLoad.system_id and othSeg.plan_id = prodLoad.plan_id  and prodLoad.active='Y' and prodLoad.last_action_status='SUCCESS'" + " WHERE seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND othLoad.active = 'Y'" + " AND loadl.active = 'Y'" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id" + " AND loadl.plan_id <> othLoad.plan_id" + " AND (othLoad.load_date_time <= loadl.load_date_time)"
		+ " AND seg.plan_id LIKE ?";
	return getCurrentSession().createSQLQuery(lQuery).setParameter(0, pPlanId).list();
    }

    public List<Object[]> getPostSegmentRelatedPlans(String pPlanId, Boolean approvalTime) {
	String lQuery = "SELECT concat(othSeg.plan_id,'/',othSeg.target_system) , othPlan.plan_status, othPlan.id, othLoad.load_date_time " + " FROM checkout_segments othSeg ,checkout_segments seg, system_load othLoad, " + " system_load loadl, imp_plan othPlan, system sys, imp_plan plan" + " WHERE seg.file_name = othSeg.file_name" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.target_system = othSeg.target_system" + " AND sys.name = seg.target_system"
		+ " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id" + " AND loadl.plan_id <> othLoad.plan_id" + " AND loadl.system_id = othLoad.system_id" + " AND othLoad.plan_id = othSeg.plan_id" + " AND loadl.active = 'Y'" + " AND othLoad.active = 'Y'" + " AND othLoad.load_date_time is not null" + " AND loadl.load_date_time is not null" + " AND othLoad.load_date_time > loadl.load_date_time " + " AND othPlan.id = othLoad.plan_id" + " AND sys.id = othLoad.system_id"
		+ " AND loadl.plan_id = seg.plan_id" + " AND plan.id = seg.plan_id" + " AND seg.plan_id LIKE :planId";
	if (approvalTime) {
	    lQuery = lQuery.concat(" AND othPlan.approve_date_time is not null AND othPlan.approve_date_time < plan.approve_date_time ");
	}
	return getCurrentSession().createSQLQuery(lQuery).setParameter("planId", pPlanId).list();
    }

    public List<Object[]> getBtwnSegmentRelatedPlansForLaterDate(String pPlanId, int systemId, Date loaDate, List<String> statuses) {
	String lQuery = "SELECT concat(othSeg.plan_id,'/',othSeg.target_system) , othPlan.plan_status, othPlan.id" + " FROM checkout_segments othSeg ,checkout_segments seg, system_load othLoad, system_load loadl, imp_plan othPlan, system sys" + " WHERE seg.file_name = othSeg.file_name" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.target_system = othSeg.target_system" + " AND sys.name = seg.target_system" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id"
		+ " AND loadl.plan_id <> othLoad.plan_id" + " AND loadl.system_id = othLoad.system_id" + " AND loadl.active = 'Y'" + " AND othLoad.active = 'Y'" + " AND othLoad.plan_id = othSeg.plan_id" + " AND (" + "         othLoad.load_date_time is not null " + "         AND loadl.load_date_time is not null " + "         AND othLoad.load_date_time BETWEEN loadl.load_date_time AND :loadDate" + "     )" + " AND othPlan.id = othLoad.plan_id" + " AND sys.id = othLoad.system_id"
		+ " AND loadl.plan_id = seg.plan_id" + " AND seg.plan_id LIKE :planId" + " AND othPlan.plan_status IN (:statuses)";

	LOG.info(lQuery);
	return getCurrentSession().createSQLQuery(lQuery).setParameter("loadDate", loaDate).setParameter("planId", pPlanId).setParameterList("statuses", statuses).list();
    }

    public List<Object[]> getBtwnSegmentRelatedPlansForBeforeDate(String pPlanId, int systemId, Date loaDate, List<String> statuses) {
	String lQuery = "SELECT concat(othSeg.plan_id,'/',othSeg.target_system) , othPlan.plan_status, othPlan.id" + " FROM checkout_segments othSeg ,checkout_segments seg, system_load othLoad, system_load loadl, imp_plan othPlan, system sys" + " WHERE seg.file_name = othSeg.file_name" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.target_system = othSeg.target_system" + " AND sys.name = seg.target_system" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id"
		+ " AND loadl.plan_id <> othLoad.plan_id" + " AND loadl.system_id = othLoad.system_id" + " AND loadl.active = 'Y'" + " AND othLoad.active = 'Y'" + " AND othLoad.plan_id = othSeg.plan_id" + " AND (" + "         othLoad.load_date_time is not null " + "         AND loadl.load_date_time is not null " + "         AND othLoad.load_date_time BETWEEN :loadDate AND loadl.load_date_time" + "     )" + " AND othPlan.id = othLoad.plan_id" + " AND sys.id = othLoad.system_id"
		+ " AND loadl.plan_id = seg.plan_id" + " AND seg.plan_id LIKE :planId" + " AND othPlan.plan_status IN (:statuses)";

	LOG.info(lQuery);
	return getCurrentSession().createSQLQuery(lQuery).setParameter("loadDate", loaDate).setParameter("planId", pPlanId).setParameterList("statuses", statuses).list();
    }

    public List<Object[]> getPostSegmentRelatedPlansBySystem(String pPlanId, String targetSystem, List<String> statuses) {
	String lQuery = "SELECT othLoad.load_date_time, othPlan.plan_status, othPlan.id " + " FROM checkout_segments othSeg ,checkout_segments seg, system_load othLoad, " + " system_load loadl, imp_plan othPlan, system sys" + " WHERE seg.file_name = othSeg.file_name" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND loadl.active = 'Y'" + " AND othLoad.active = 'Y'" + " AND seg.target_system = othSeg.target_system" + " AND sys.name = seg.target_system" + " AND seg.id <> othSeg.id"
		+ " AND seg.plan_id <> othSeg.plan_id" + " AND loadl.plan_id <> othLoad.plan_id" + " AND loadl.system_id = othLoad.system_id" + " AND othLoad.plan_id = othSeg.plan_id" + " AND othLoad.load_date_time > loadl.load_date_time" + " AND othPlan.id = othLoad.plan_id" + " AND sys.id = othLoad.system_id" + " AND loadl.plan_id = seg.plan_id" + " AND seg.plan_id = :planId" + " AND seg.target_system = :targetSystem" + " AND othPlan.plan_status IN (:statuses)";

	return getCurrentSession().createSQLQuery(lQuery).setParameter("planId", pPlanId).setParameter("targetSystem", targetSystem).setParameterList("statuses", statuses).list();
    }

    public List<Object[]> getBtwnSegmentRelatedPlans(String pPlanId, int systemId, Date loaDate) {
	String lQuery = "SELECT concat(othSeg.plan_id,'/',othSeg.target_system) , othPlan.plan_status" + " FROM checkout_segments othSeg ,checkout_segments seg, system_load othLoad, system_load loadl, imp_plan othPlan, system sys" + " WHERE seg.file_name = othSeg.file_name" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.target_system = othSeg.target_system" + " AND sys.name = seg.target_system" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id"
		+ " AND loadl.plan_id <> othLoad.plan_id" + " AND loadl.system_id = othLoad.system_id" + " AND othLoad.plan_id = othSeg.plan_id" + " AND (" + "         (othLoad.load_date_time BETWEEN loadl.load_date_time AND  ?)" + "         OR(othLoad.load_date_time BETWEEN ? AND loadl.load_date_time)" + "     )" + " AND othPlan.id = othLoad.plan_id" + " AND sys.id = othLoad.system_id" + " AND loadl.plan_id = seg.plan_id" + " AND seg.plan_id LIKE ?" + " AND sys.id = ?";
	return getCurrentSession().createSQLQuery(lQuery).setParameter(0, loaDate).setParameter(1, loaDate).setParameter(2, pPlanId).setParameter(3, systemId).list();
    }

    public List<String> getStagingDepedendentPlans(String pPlanId, Integer systemId, Date loaDate) {
	// TODO: need check Query
	String lQuery = "SELECT DISTINCT concat(othSeg.plan_id,'_',to_char(othLoad.load_date_time,'YYMMDDHHMISS'))" + " FROM checkout_segments othSeg ,checkout_segments seg, system_load othLoad, system_load loadl, imp_plan othPlan, system sys" + " WHERE seg.file_name = othSeg.file_name" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.target_system = othSeg.target_system" + " AND sys.name = seg.target_system" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id"
		+ " AND loadl.plan_id <> othLoad.plan_id" + " AND othPlan.approve_date_time is not null" + " AND loadl.system_id = othLoad.system_id" + " AND othLoad.plan_id = othSeg.plan_id" + " AND (" + "         OR(othLoad.load_date_time BETWEEN loadl.load_date_time AND  ?)" + "         OR(othLoad.load_date_time BETWEEN ? AND loadl.load_date_time)" + "     )" + " AND othPlan.id = othLoad.plan_id" + " AND sys.id = othLoad.system_id" + " AND loadl.plan_id = seg.plan_id"
		+ " AND seg.plan_id LIKE ?" + " AND sys.id = ?";
	return getCurrentSession().createSQLQuery(lQuery).setParameter(0, loaDate).setParameter(1, loaDate).setParameter(2, pPlanId).setParameter(3, systemId).list();
    }

    public String doPlanAuditForUpdate(String planId, Integer systemId, Date loadDateTime) {
	SortedSet<String> lSet = new TreeSet<>();
	StringBuilder lReturn = new StringBuilder("");
	List<Object[]> relatedPlanDetails = getBtwnSegmentRelatedPlans(planId, systemId, loadDateTime);
	for (Object[] plan : relatedPlanDetails) {
	    String planStatus = plan[1].toString();
	    if (Constants.PlanStatus.getAfterSubmitStatus().containsKey(planStatus)) {
		lSet.add(plan[0].toString());
	    }
	}
	if (!lSet.isEmpty()) {
	    lReturn.append("Plans (").append(StringUtils.join(lSet, ", ")).append(") are submitted with in the change period.");
	}
	return lReturn.toString();
    }

    public String doPlanAudit(String pPlanId, String pPlanStatus) {
	SortedSet<String> lSet = new TreeSet<String>();
	StringBuilder lReturn = new StringBuilder("");
	ImpPlan load = (ImpPlan) getCurrentSession().load(ImpPlan.class, pPlanId);
	String relatedPlans = load.getRelatedPlans();
	Set<String> lPlanStatusList = new TreeSet();
	if (pPlanStatus.equalsIgnoreCase(Constants.PlanStatus.SUBMITTED.name())) {
	    lPlanStatusList = Constants.PlanStatus.getSecuredStatusMap().keySet();
	} else if (pPlanStatus.equalsIgnoreCase(Constants.PlanStatus.APPROVED.name())) {
	    lPlanStatusList = Constants.PlanStatus.getApprovedStatusMap().keySet();
	}

	if (relatedPlans != null && !relatedPlans.isEmpty()) {
	    List<String> split = Arrays.asList(relatedPlans.split(","));
	    List<Object[]> relatedPlanDetails = getRelatedPlanDetail(pPlanId, split);
	    for (Object[] plan : relatedPlanDetails) {
		String planStatus = plan[1].toString();
		if (!(lPlanStatusList.contains(planStatus))) {
		    lSet.add(plan[0].toString());
		}
	    }
	    if (!lSet.isEmpty()) {
		lReturn.append("Plans (").append(StringUtils.join(lSet, ", ")).append(") has to be loaded before Plan ").append(pPlanId).append(". ");
	    }
	    lSet.clear();

	    relatedPlanDetails = getAllRelatedPlanDetail(pPlanId, split);
	    for (Object[] plan : relatedPlanDetails) {
		String planStatus = plan[1].toString();
		if (!(lPlanStatusList.contains(planStatus))) {
		    lSet.add(plan[0].toString());
		}
	    }
	    if (!lSet.isEmpty()) {
		lReturn.delete(0, lReturn.length());
		lReturn.append("Plans (").append(StringUtils.join(lSet, ", ")).append(") has to be " + Constants.PlanStatus.SUBMITTED.name() + "before Plan ").append(pPlanId).append(". ");
	    }
	    lSet.clear();
	}

	List<Object[]> segmentRelatedPlans = getPreSegmentRelatedPlans(pPlanId);
	for (Object[] plan : segmentRelatedPlans) {
	    String planStatus = plan[1].toString();
	    if (!(lPlanStatusList.contains(planStatus))) {
		lSet.add(plan[0].toString());
	    }
	}
	if (!lSet.isEmpty()) {
	    lReturn.delete(0, lReturn.length());
	    lReturn.append("Plans (").append(StringUtils.join(lSet, ", ")).append(") has to be " + Constants.PlanStatus.SUBMITTED.name() + " before Plan ").append(pPlanId).append(". ");
	}
	lSet.clear();

	segmentRelatedPlans = getPostSegmentRelatedPlans(pPlanId, false);
	for (Object[] plan : segmentRelatedPlans) {
	    String planStatus = plan[1].toString();
	    if ((lPlanStatusList.contains(planStatus))) {
		lSet.add(plan[0].toString());
	    }
	}
	if (!lSet.isEmpty()) {
	    lReturn.delete(0, lReturn.length());
	    lReturn.append("Segment dependency found between the plan ").append(pPlanId).append(" and ").append(StringUtils.join(lSet, ", ")).append(", load date in Plan ").append(pPlanId).append("should be later than plan ").append(StringUtils.join(lSet, ", "));
	    // lReturn.append("<br/>Plans (").append(StringUtils.join(lSet, ", ")).append(")
	    // should not be approved
	    // before Plan ").append(pPlanId).append(". ");
	}
	lSet.clear();

	return lReturn.toString();
    }

    public List findById(String filter, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.ilike("id", filter, MatchMode.ANYWHERE));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions, pOffset, pLimit, pOrderBy);
    }

    public List findById(String filter) {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.ilike("id", filter, MatchMode.ANYWHERE));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public Long countById(String filter) {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.ilike("id", filter, MatchMode.ANYWHERE));
	expressions.add(Restrictions.eq("active", "Y"));
	return count(expressions);
    }

    public List<ImpPlan> findDependentPlans(String pPlanId) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.ilike("relatedPlans", pPlanId, MatchMode.ANYWHERE));
	return lCriteria.list();
    }

    public List<ImpPlan> findBySO(List<String> pStatuses, List<String> pSegments, Date pLoadDate, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	String lQuery = "SELECT ip FROM ImpPlan ip, CheckoutSegments seg" + " WHERE seg.programName IN (:segments) AND " + " seg.planId.planStatus IN (:statuses) AND " + " seg.systemLoad.loadDateTime >= :loadDate";

	List<ImpPlan> lPlanList = getCurrentSession().createQuery(lQuery).setParameterList("segments", pSegments).setParameterList("statuses", pStatuses).setParameter("loadDate", pLoadDate).list();
	return lPlanList;
    }

    public List<ImpPlan> findByPlanStatus(Collection<String> planIds, List<String> status) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.in("id", planIds));
	lCriteria.add(Restrictions.in("planStatus", status));
	return lCriteria.list();
    }

    public Long countByPlanStatus(Collection<String> planIds, List<String> status) {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("active", "Y"));
	expressions.add(Restrictions.in("id", planIds));
	expressions.add(Restrictions.in("planStatus", status));
	return count(expressions);
    }

    public Map<String, Integer> getSecuredPassedLoadDate(List<String> status) {
	String lQuery = "SELECT a.id, count(a.id) FROM imp_plan a, system_load b, build c" + " WHERE a.plan_status IN (:statuses)" + " AND b.load_date_time <= (now() - INTERVAL '2 days')" + " AND b.active = 'Y'" + " AND b.prod_load_status is null" + " AND a.active = 'Y'" + " AND c.build_type = '" + Constants.BUILD_TYPE.STG_LOAD.name() + "'" + " AND c.load_set_type = '" + Constants.LoaderTypes.E + "'" + " AND c.active ='Y'" + " AND c.job_status = 'S'" + " AND c.plan_id = a.id"
		+ " AND b.plan_id = a.id" + " AND c.system_id = b.system_id" + " GROUP BY a.id";

	List<Object[]> lPlanList = getCurrentSession().createSQLQuery(lQuery).setParameterList("statuses", status).list();
	Map<String, Integer> lReturn = new HashMap<>();
	for (Object[] lPlan : lPlanList) {
	    lReturn.put(lPlan[0].toString(), Integer.parseInt(lPlan[1].toString()));
	}
	return lReturn;
    }

    public List<ImpPlan> findByStatusList(List<String> status, boolean isDelta, boolean leadCheck, String pLeadName, String pFilter, Constants.BUILD_TYPE build_type, Constants.LoaderTypes loaderTypes, Integer offset, Integer limit, LinkedHashMap<String, String> lOrderBy, boolean doFilter) {

	String lQueryString = "SELECT plan FROM ImpPlan plan, Build build" + " WHERE plan.id = build.planId " + " AND plan.active = 'Y'" + " AND plan.macroHeader = false" + " AND plan.planStatus in (:planStatus)" + " AND build.buildType = :buildType" + " AND build.loadSetType = :loadType" + " AND build.active ='Y'" + " AND build.jobStatus = 'S'";
	String lAppendQuery = "";

	if (leadCheck && pLeadName != null && !pLeadName.isEmpty()) {
	    lAppendQuery = lAppendQuery + " AND plan.leadId  = '" + pLeadName + "'";
	} else if (!leadCheck && pLeadName != null && !pLeadName.isEmpty()) {
	    lAppendQuery = lAppendQuery + " AND plan.leadId <> '" + pLeadName + "'";
	}

	// This is only for Delta TSS, So NO ELSE
	if (isDelta) {
	    lAppendQuery = lAppendQuery + " AND plan.id LIKE 'D%'";
	}

	if (pFilter != null && !pFilter.isEmpty()) {
	    lAppendQuery = lAppendQuery + " AND plan.id LIKE '%" + pFilter.toUpperCase() + "%'";
	}
	String lGroupByString = " GROUP BY plan ";
	String lOrderByString = "";

	if (lOrderBy != null && !lOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> entrySet : lOrderBy.entrySet()) {
		String key = entrySet.getKey();
		String value = entrySet.getValue();
		if ("planStatus".equals(key)) {
		    key = Constants.PlanStatus.getAllPlanStatusMap();
		    lOrderByString = " ORDER BY CASE " + key + " " + value;
		} else {
		    lOrderByString = " ORDER BY plan." + key + " " + value;
		}
	    }
	}

	if (doFilter) {
	    return getCurrentSession().createQuery(lQueryString + lAppendQuery + lGroupByString + lOrderByString).setParameterList("planStatus", status).setParameter("buildType", build_type.name()).setParameter("loadType", loaderTypes.name()).setFirstResult(offset).setMaxResults(limit).list();
	} else {
	    return getCurrentSession().createQuery(lQueryString + lAppendQuery + lGroupByString + lOrderByString).setParameterList("planStatus", status).setParameter("buildType", build_type.name()).setParameter("loadType", loaderTypes.name()).list();
	}
    }

    public Long countByStatusList(List<String> status, boolean isDelta, String pFilter, Constants.BUILD_TYPE build_type, Constants.LoaderTypes loaderTypes) {
	return countByStatusListUser(status, null, isDelta, pFilter, build_type, loaderTypes);
    }

    public Long countByStatusListUser(List<String> status, String pUser, boolean isDelta, String pFilter, Constants.BUILD_TYPE build_type, Constants.LoaderTypes loaderTypes) {
	String lQueryString = "SELECT count(DISTINCT plan) FROM ImpPlan plan, Build build" + " WHERE plan.id = build.planId " + " AND plan.active = 'Y'" + " AND plan.macroHeader = false" + " AND plan.planStatus in (:planStatus)" + " AND build.buildType = :buildType" + " AND build.loadSetType = :loadType" + " AND build.active ='Y'" + " AND build.jobStatus = 'S'";
	String lAppendQuery = "";

	if (pUser != null && !pUser.isEmpty()) {
	    lAppendQuery = lAppendQuery + " AND plan.leadId = '" + pUser + "'";
	}

	if (isDelta) {
	    lAppendQuery = lAppendQuery + " AND plan LIKE 'D%'";
	}
	if (pFilter != null && !pFilter.isEmpty()) {
	    lAppendQuery = lAppendQuery + " AND plan.id LIKE '%" + pFilter.toUpperCase() + "%'";
	}
	Long lCount = (Long) getCurrentSession().createQuery(lQueryString + lAppendQuery).setParameterList("planStatus", status).setParameter("buildType", build_type.name()).setParameter("loadType", loaderTypes.name()).uniqueResult();
	return lCount;
    }

    public List<Object[]> getSameSegmentDevelopers(String pPlanId, List<String> status) {
	String lQuery = "SELECT DISTINCT othSeg.imp_id, othSeg.plan_id, othPlan.lead_id, imp.dev_id , concat(othSeg.program_name,'[',othSeg.target_system,']' )" + " FROM checkout_segments othSeg ,checkout_segments seg, imp_plan othPlan, implementation imp" + " WHERE seg.file_name = othSeg.file_name" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.target_system = othSeg.target_system" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id"
		+ " AND seg.plan_id = :planId" + " AND othPlan.plan_status IN (:statuses)" + " AND othPlan.id = othSeg.plan_id" + " AND imp.id = othSeg.imp_id";

	List<Object[]> lPlanList = getCurrentSession().createSQLQuery(lQuery).setParameterList("statuses", status).setParameter("planId", pPlanId).list();

	return lPlanList;
    }

    public List<Object[]> getDevelopersBySegment(String pPlanId, List<String> status, String segmentName, String targetSystem) {
	String lQuery = "SELECT DISTINCT othSeg.imp_id, othSeg.plan_id, othPlan.lead_id, imp.dev_id , concat(othSeg.file_name,'[',othSeg.target_system,']' )," + " othPlan.load_type, othPlan.plan_status, imp.id , sysLoad.load_date_time" + " FROM checkout_segments othSeg ,checkout_segments seg, imp_plan othPlan, implementation imp," + " system_load othSysLoad, system_load sysLoad" + " WHERE seg.file_name = othSeg.file_name" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'"
		+ " AND seg.target_system = othSeg.target_system" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id" + " AND othPlan.id = othSeg.plan_id" + " AND othPlan.plan_status IN (:statuses)" + " AND imp.id = othSeg.imp_id" + " AND seg.plan_id = :planId" + " AND seg.target_system = :targetSystem " + " AND seg.file_name = :segmentName" + " AND sysLoad.id = othSeg.system_load" + " AND othSysLoad.id = seg.system_load" + " AND sysLoad.load_date_time IS NOT NULL "
		+ " AND othSysLoad.load_date_time IS NOT NULL " + " AND sysLoad.load_date_time < othSysLoad.load_date_time" + " ORDER BY sysLoad.load_date_time DESC LIMIT 1";

	List<Object[]> lPlanList = getCurrentSession().createSQLQuery(lQuery).setParameterList("statuses", status).setParameter("planId", pPlanId).setParameter("targetSystem", targetSystem).setParameter("segmentName", segmentName).list();
	return lPlanList;
    }

    public List<Object[]> getAllDependentDevelopersBySegment(String pPlanId, List<String> status, String segmentName, String targetSystem) {
	String lQuery = "SELECT DISTINCT othSeg.imp_id, othSeg.plan_id, othPlan.lead_id, imp.dev_id , concat(othSeg.file_name,'[',othSeg.target_system,']' )," + " othPlan.load_type, othPlan.plan_status, imp.id , sysLoad.load_date_time" + " FROM checkout_segments othSeg ,checkout_segments seg, imp_plan othPlan, implementation imp," + " system_load othSysLoad, system_load sysLoad" + " WHERE seg.file_name = othSeg.file_name" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'"
		+ " AND seg.target_system = othSeg.target_system" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id" + " AND othPlan.id = othSeg.plan_id" + " AND othPlan.plan_status IN (:statuses)" + " AND imp.id = othSeg.imp_id" + " AND seg.plan_id = :planId" + " AND seg.target_system = :targetSystem " + " AND seg.file_name = :segmentName" + " AND sysLoad.id = othSeg.system_load" + " AND othSysLoad.id = seg.system_load" + " AND sysLoad.load_date_time IS NOT NULL "
		+ " AND othSysLoad.load_date_time IS NOT NULL " + " AND sysLoad.load_date_time > othSysLoad.load_date_time" + " ORDER BY sysLoad.load_date_time LIMIT 1";

	List<Object[]> lPlanList = getCurrentSession().createSQLQuery(lQuery).setParameterList("statuses", status).setParameter("planId", pPlanId).setParameter("targetSystem", targetSystem).setParameter("segmentName", segmentName).list();
	return lPlanList;
    }

    public List<Object[]> getDevelopersBySegmentForDelete(String pPlanId, List<String> status, String segmentName, List<String> targetSystem) {
	String lQuery = "SELECT DISTINCT othSeg.imp_id, othSeg.plan_id, othPlan.lead_id, imp.dev_id , concat(othSeg.file_name,'[',othSeg.target_system,']' )," + " othPlan.load_type, othPlan.plan_status, imp.id " + " FROM checkout_segments othSeg ,checkout_segments seg, imp_plan othPlan, implementation imp," + " system_load othSysLoad, system_load sysLoad" + " WHERE seg.file_name = othSeg.file_name" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'"
		+ " AND seg.target_system = othSeg.target_system" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id" + " AND othPlan.id = othSeg.plan_id" + " AND othPlan.plan_status IN (:statuses)" + " AND imp.id = othSeg.imp_id" + " AND seg.plan_id = :planId" + " AND seg.target_system IN (:targetSystem)" + " AND seg.program_name = :segmentName" + " AND sysLoad.id = seg.system_load" + " AND othSysLoad.id = othSeg.system_load"
		+ " AND ((sysLoad.load_date_time IS NOT NULL) AND (othSysLoad.load_date_time IS NOT NULL) " + " AND (sysLoad.load_date_time < othSysLoad.load_date_time))";

	List<Object[]> lPlanList = getCurrentSession().createSQLQuery(lQuery).setParameterList("statuses", status).setParameter("planId", pPlanId).setParameterList("targetSystem", targetSystem).setParameter("segmentName", segmentName).list();
	return lPlanList;
    }

    public List<Object[]> getDevelopersByTargetSystem(String pPlanId, String targetSystem) {
	String lQuery = "SELECT DISTINCT othSeg.imp_id, othSeg.plan_id, othPlan.lead_id, imp.dev_id , concat(othSeg.program_name,'[',othSeg.target_system,']' )," + " othPlan.load_type, othPlan.plan_status, imp.id , sysLoad.load_date_time" + " FROM checkout_segments othSeg ,checkout_segments seg, imp_plan othPlan, implementation imp," + " system_load othSysLoad, system_load sysLoad" + " WHERE seg.file_name = othSeg.file_name" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'"
		+ " AND seg.target_system = othSeg.target_system" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id" + " AND othPlan.id = othSeg.plan_id" + " AND imp.id = othSeg.imp_id" + " AND seg.plan_id = :planId" + " AND seg.target_system = :targetSystem " + " AND sysLoad.id = othSeg.system_load" + " AND othSysLoad.id = seg.system_load" + " AND sysLoad.load_date_time IS NOT NULL " + " AND othSysLoad.load_date_time IS NOT NULL "
		+ " AND sysLoad.load_date_time > othSysLoad.load_date_time" + " ORDER BY sysLoad.load_date_time ";

	List<Object[]> lPlanList = getCurrentSession().createSQLQuery(lQuery).setParameter("planId", pPlanId).setParameter("targetSystem", targetSystem).list();
	return lPlanList;
    }

    public List<Object[]> getSameSegmentDevelopersByImpId(String impId, List<String> status) {
	String lQuery = "SELECT DISTINCT othSeg.imp_id, othSeg.plan_id, othPlan.lead_id, imp.dev_id, concat(othSeg.program_name,'[',othSeg.target_system,']' )" + " FROM checkout_segments othSeg ,checkout_segments seg, imp_plan othPlan, implementation imp" + " WHERE seg.file_name = othSeg.file_name" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.target_system = othSeg.target_system" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id"
		+ " AND othPlan.id = othSeg.plan_id" + " AND seg.imp_id = :impId" + " AND othPlan.plan_status IN (:statuses)" + " AND imp.id = othSeg.imp_id";

	List<Object[]> lPlanList = getCurrentSession().createSQLQuery(lQuery).setParameterList("statuses", status).setParameter("impId", impId).list();
	return lPlanList;
    }

    // public List<ImpPlan> findFallbackPlans(Integer pOffset, Integer pLimit,
    // LinkedHashMap<String, String> orderBy) {
    // Calendar c = Calendar.getInstance();
    // c.add(Calendar.WEEK_OF_MONTH, -2);
    // String lQueryString = "SELECT plan FROM ImpPlan plan, SystemLoad loads"
    // + " WHERE plan.planStatus = :status"
    // + " AND plan.active = 'Y'"
    // + " AND loads.planId = plan.id"
    // + " AND plan.acceptedDateTime BETWEEN :fromTime AND :toTime"
    // + " GROUP BY plan";
    //
    // String lOrderByString = "";
    //
    // if (orderBy != null && !orderBy.isEmpty()) {
    // for (Map.Entry<String, String> entrySet : orderBy.entrySet()) {
    // String key = entrySet.getKey();
    // String value = entrySet.getValue();
    // lOrderByString = " ORDER BY " + key + " " + value;
    // }
    // } else {
    // lOrderByString = " ORDER BY MIN(loads.loadDateTime) DESC";
    // }
    //
    // Query lQuery = getCurrentSession().createQuery(lQueryString + lOrderByString)
    // .setParameter("status", Constants.PlanStatus.ONLINE.name())
    // .setParameter("fromTime", c.getTime())
    // .setParameter("toTime", new Date())
    // .setFirstResult(pOffset * pLimit)
    // .setMaxResults(pLimit);
    //
    // List<ImpPlan> lPlanList = lQuery.list();
    // return lPlanList;
    // }
    //
    // public Long countFallbackPlans() {
    // Calendar c = Calendar.getInstance();
    // c.add(Calendar.WEEK_OF_MONTH, -2);
    // String lQuery = "SELECT COUNT(plan) from ImpPlan plan"
    // + " WHERE plan.planStatus = :status"
    // + " AND plan.active = 'Y'"
    // + " AND plan.acceptedDateTime BETWEEN :fromTime AND :toTime";
    // Long lPlanList = (Long) getCurrentSession().createQuery(lQuery)
    // .setParameter("status", Constants.PlanStatus.ONLINE.name())
    // .setParameter("fromTime", c.getTime())
    // .setParameter("toTime", new Date())
    // .uniqueResult();
    // return lPlanList;
    // }
    public List<ImpPlan> findByStatusListAndOwner(List<String> status, String leadId, Integer offset, Integer limit, LinkedHashMap<String, String> lOrderBy, String planFilter) {
	List<Criterion> lRestrictions = new ArrayList<>();
	lRestrictions.add(Restrictions.in("planStatus", status));
	lRestrictions.add(Restrictions.eq("leadId", leadId));
	lRestrictions.add(Restrictions.eq("active", "Y"));
	if (planFilter != null && !planFilter.isEmpty()) {
	    lRestrictions.add(Restrictions.ilike("id", planFilter, MatchMode.ANYWHERE));
	}
	return findAll(lRestrictions, offset, limit, lOrderBy);
    }

    public List<ImpPlan> findByStatusListAndOwner(List<String> status, String leadId, Integer offset, Integer limit, String planFilter) {
	List<Criterion> lRestrictions = new ArrayList<>();
	lRestrictions.add(Restrictions.in("planStatus", status));
	lRestrictions.add(Restrictions.eq("leadId", leadId));
	lRestrictions.add(Restrictions.eq("active", "Y"));
	if (planFilter != null && !planFilter.isEmpty()) {
	    lRestrictions.add(Restrictions.ilike("id", planFilter, MatchMode.ANYWHERE));
	}
	return findAll(lRestrictions);
    }

    public Long countByStatusListAndOwner(List<String> status, String leadId, String planFilter) {
	List<Criterion> lRestrictions = new ArrayList<>();
	lRestrictions.add(Restrictions.eq("leadId", leadId));
	lRestrictions.add(Restrictions.in("planStatus", status));
	lRestrictions.add(Restrictions.eq("active", "Y"));
	if (planFilter != null && !planFilter.isEmpty()) {
	    lRestrictions.add(Restrictions.ilike("id", planFilter, MatchMode.ANYWHERE));
	}
	return count(lRestrictions);
    }

    public List<ImpPlan> passedAcceptanceList(List<String> status) {
	List<Criterion> lRestrictions = new ArrayList<>();
	lRestrictions.add(Restrictions.in("planStatus", status));
	lRestrictions.add(Restrictions.eq("active", "Y"));
	return findAll(lRestrictions);
    }

    public List<ImpPlan> getPlansByLoadDateTime(String planStatus, String devManagerId, Integer offset, Integer limit, HashMap<String, String> lOrderBy) {

	StringBuilder sb = new StringBuilder();

	sb.append("SELECT plan FROM SystemLoad sysLoad, ImpPlan plan");
	sb.append(" Where sysLoad.active = 'Y'");
	sb.append(" AND sysLoad.loadDateTime is not null");
	sb.append(" AND sysLoad.planId.id = plan.id");
	sb.append(" AND plan.planStatus = :PlanStatus");
	sb.append(" AND plan.devManager = :DevManagerId");
	sb.append(" AND plan.macroHeader = false");
	sb.append(" GROUP BY plan");
	if (lOrderBy != null && !lOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> order : lOrderBy.entrySet()) {
		if (order.getKey().equals("loaddatetime") && order.getValue().equals("asc")) {
		    sb.append(" ORDER BY MIN(sysLoad.loadDateTime) ASC ");
		} else if (order.getKey().equals("loaddatetime") && order.getValue().equals("desc")) {
		    sb.append(" ORDER BY MIN(sysLoad.loadDateTime) DESC ");
		}

	    }
	} else {
	    sb.append(" ORDER BY MIN(sysLoad.loadDateTime) ASC");
	}

	return getCurrentSession().createQuery(sb.toString()).setParameter("PlanStatus", planStatus).setParameter("DevManagerId", devManagerId).setFirstResult(offset * limit).setMaxResults(limit).list();
    }

    public List<Object[]> getToBeApprovedList(String planStatus, String devManagerId, Boolean devMangFlag) {

	StringBuilder sb = new StringBuilder();

	sb.append("select * from imp_plan where id in (");
	sb.append("SELECT plan.id FROM imp_plan plan, system_load sysLoad");
	sb.append(" where sysLoad.active = 'Y'");
	sb.append(" AND sysLoad.load_date_time is not null");
	sb.append(" AND sysLoad.plan_id = plan.id");
	sb.append(" AND plan.plan_status = :PlanStatus");
	sb.append(" AND plan.dev_manager = :DevManagerId");
	sb.append(" AND plan.macro_header = false");
	sb.append(devMangFlag ? " AND plan.approve_request_date_time <= (Now() - interval '1 days') AND approve_mail_flag = false)" : " AND plan.approve_request_date_time is not null )");

	return getCurrentSession().createSQLQuery(sb.toString()).setParameter("PlanStatus", planStatus).setParameter("DevManagerId", devManagerId).list();
    }

    public List<ImpPlan> getPlansByLoadDateTime(String devManagerId, Integer offset, Integer limit, String planId, String planStatus, HashMap<String, String> lOrderBy) {

	StringBuilder sb = new StringBuilder();
	sb.append("SELECT plan FROM SystemLoad sysLoad, ImpPlan plan");
	sb.append(" Where sysLoad.active = 'Y'");
	sb.append(" AND sysLoad.loadDateTime is not null");
	sb.append(" AND sysLoad.planId.id = plan.id");
	sb.append(" AND plan.devManager = :DevManagerId");
	if (planId != null && !planId.trim().isEmpty()) {
	    sb.append(" AND LOWER(plan.id) LIKE LOWER('%" + planId + "%')");
	}
	if (planStatus != null && !planStatus.trim().isEmpty()) {
	    sb.append(" AND plan.planStatus Like :planStatus");
	}
	sb.append(" GROUP BY plan");
	if (lOrderBy != null && !lOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> order : lOrderBy.entrySet()) {
		if (order.getKey().equals("loaddatetime") && order.getValue().equals("asc")) {
		    sb.append(" ORDER BY MIN(sysLoad.loadDateTime) ASC ");
		} else if (order.getKey().equals("loaddatetime") && order.getValue().equals("desc")) {
		    sb.append(" ORDER BY MIN(sysLoad.loadDateTime) DESC ");
		}

	    }
	} else {
	    sb.append(" ORDER BY MIN(sysLoad.loadDateTime) ASC");
	}

	Query lQuery = getCurrentSession().createQuery(sb.toString()).setParameter("DevManagerId", devManagerId);

	if (planStatus != null && !planStatus.trim().isEmpty()) {
	    lQuery.setParameter("planStatus", planStatus);
	}
	return lQuery.setFirstResult(offset * limit).setMaxResults(limit).list();
    }

    public List<ImpPlan> getMacroHeaderPlansByLoadDateTime(String planStatus, String devManagerId, Integer offset, Integer limit, HashMap<String, String> lOrderBy) {

	StringBuilder sb = new StringBuilder();

	sb.append("SELECT plan FROM SystemLoad sysLoad, ImpPlan plan");
	sb.append(" Where sysLoad.active = 'Y'");
	sb.append(" AND sysLoad.loadDateTime is not null");
	sb.append(" AND sysLoad.planId.id = plan.id");
	sb.append(" AND plan.planStatus = :PlanStatus");
	sb.append(" AND plan.devManager = :DevManagerId");
	sb.append(" AND plan.macroHeader = true");
	sb.append(" GROUP BY plan");
	if (lOrderBy != null && !lOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> order : lOrderBy.entrySet()) {
		if (order.getKey().equals("loaddatetime") && order.getValue().equals("asc")) {
		    sb.append(" ORDER BY MIN(sysLoad.loadDateTime) ASC ");
		} else if (order.getKey().equals("loaddatetime") && order.getValue().equals("desc")) {
		    sb.append(" ORDER BY MIN(sysLoad.loadDateTime) DESC ");
		}

	    }
	} else {
	    sb.append(" ORDER BY MIN(sysLoad.loadDateTime) ASC");
	}

	return getCurrentSession().createQuery(sb.toString()).setParameter("PlanStatus", planStatus).setParameter("DevManagerId", devManagerId).setFirstResult(offset * limit).setMaxResults(limit).list();
    }

    public List<ImpPlan> getPlansByMacroHeader(String devManagerId, String planStatus, Boolean pMacroHeader, Integer offset, Integer limit, LinkedHashMap<String, String> lOrderBy) {
	List<Criterion> lRestrictions = new ArrayList<>();
	lRestrictions.add(Restrictions.eq("planStatus", planStatus));
	lRestrictions.add(Restrictions.eq("active", "Y"));
	lRestrictions.add(Restrictions.eq("macroHeader", pMacroHeader));
	lRestrictions.add(Restrictions.eq("devManager", devManagerId));
	return findAll(lRestrictions, offset, limit, lOrderBy);
    }

    public List<Object[]> getPlansByMacroHeaderBasedOnTime(String devManagerId, String planStatus, Boolean pMacroHeader, Boolean devManMacroFlag) throws WorkflowException {
	String lQuery = "select * from imp_plan where id in (select plan.id from imp_plan plan, system_load sys where plan.plan_status = :PlanStatus and plan.active = 'Y' and sys.active = 'Y' and plan.id = sys.plan_id and Date(sys.load_date_time) <= date (Now()) and plan.macro_header = :flag and plan.dev_manager ilike :devManager ";
	String ldevMgrMacro = devManMacroFlag ? " plan.approve_request_date_time <= (Now() - interval '1 days') AND approve_mail_flag = false) " : "plan.approve_request_date_time is not null ) ";
	lQuery = lQuery + " and " + ldevMgrMacro;
	return getCurrentSession().createSQLQuery(lQuery).setParameter("devManager", devManagerId).setParameter("PlanStatus", planStatus).setParameter("flag", pMacroHeader).list();
    }

    public Long getPlansCountByMacroHeader(String devManagerId, String planStatus, Boolean pMacroHeader) {
	List<Criterion> lRestrictions = new ArrayList<>();
	lRestrictions.add(Restrictions.eq("planStatus", planStatus));
	lRestrictions.add(Restrictions.eq("active", "Y"));
	lRestrictions.add(Restrictions.eq("macroHeader", Boolean.TRUE));
	lRestrictions.add(Restrictions.eq("devManager", devManagerId));
	return count(lRestrictions);
    }

    public ImpPlan findByPlanStatus(String planId, List<String> status) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.eq("id", planId));
	lCriteria.add(Restrictions.in("planStatus", status));
	return (ImpPlan) lCriteria.uniqueResult();
    }

    public List<ImpPlan> findByLoad(List<String> planStatus, String pFilter, boolean isDelta, String loadSetType, Integer offset, Integer limit, boolean doFilter) {
	StringBuilder lQuery = new StringBuilder("select plan from  Build b, SystemLoad load ,ImpPlan plan " + " left join plan.preProductionLoadsList as preProd " + " where " + " (" + " (plan.planStatus in (:Statuses))" + "  or (plan.planStatus in (:ImpPlanStatus) and preProd.systemLoadActionsId is not null" + " and preProd.active = 'Y' )" + ")" + " and b.active = 'Y'" + " and b.planId = plan.id" + " and load.systemId = b.systemId" + " and b.buildType = :BuildType"
		+ " and b.loadSetType = :LoadSetType" + " and load.planId = plan.id");
	if (pFilter != null && !pFilter.isEmpty()) {
	    lQuery.append(" AND plan.id LIKE :Planid");
	}
	// This is Mutualy Exculsive, So ELSE
	if (isDelta) {
	    lQuery.append(" AND plan.id LIKE 'D%'");
	} else {
	    lQuery.append(" AND plan.id LIKE 'T%'");
	}
	lQuery.append(" group by plan.id");
	lQuery.append(" order by min(load.loadDateTime),1");

	Query setMaxResults = getCurrentSession().createQuery(lQuery.toString()).setParameterList("Statuses", planStatus).setParameter("BuildType", Constants.BUILD_TYPE.STG_LOAD.name()).setParameterList("ImpPlanStatus", Constants.PlanStatus.getQAPlanStatusBetweenApprovedAndRegDepl().keySet()).setParameter("LoadSetType", loadSetType);

	if (pFilter != null && !pFilter.isEmpty()) {
	    setMaxResults.setParameter("Planid", "%" + pFilter + "%");
	}
	if (doFilter) {
	    setMaxResults.setFirstResult(offset * limit).setMaxResults(limit);
	}
	return setMaxResults.list();
    }

    public Long getCountOfLoad(List<String> planStatus, String pFilter, boolean isDelta, String loadSetType) {
	// Long lPlanCount;
	Long lPlanCount = 0L;
	StringBuilder lQuery = new StringBuilder("select count( DISTINCT plan.id) from imp_plan plan, build b, system_load load,pre_production_loads preProd" + " where " + " (" + " (plan.plan_status in (:Statuses))" + "  or (plan.plan_status in (:ImpPlanStatus) and preProd.system_load_action_id is not null and plan.id = preProd.plan_id " + " and preProd.active = 'Y' )" + ")" + " and b.active = 'Y'" + " and b.plan_id = plan.id" + " and load.system_id = b.system_id"
		+ " and b.build_type = :BuildType" + " and b.load_set_type = :LoadSetType" + " and load.plan_id = plan.id");

	if (pFilter != null && !pFilter.isEmpty()) {
	    lQuery.append(" AND plan.id LIKE :Planid");
	}
	// This is Mutualy Exculsive, So ELSE
	if (isDelta) {
	    lQuery.append(" AND plan.id LIKE 'D%'");
	} else {
	    lQuery.append(" AND plan.id LIKE 'T%'");
	}

	Query setMaxResults = getCurrentSession().createSQLQuery(lQuery.toString()).setParameterList("Statuses", planStatus).setParameter("BuildType", Constants.BUILD_TYPE.STG_LOAD.name()).setParameterList("ImpPlanStatus", Constants.PlanStatus.getQAFunctionalDeploymentStatus().keySet()).setParameter("LoadSetType", loadSetType);

	if (pFilter != null && !pFilter.isEmpty()) {
	    setMaxResults.setParameter("Planid", "%" + pFilter + "%");
	}

	final List<BigInteger> obj = setMaxResults.list();
	for (BigInteger l : obj) {
	    if (l != null) {
		lPlanCount = l.longValue();
	    }
	}
	return lPlanCount;
	// lPlanCount = Long.parseLong(setMaxResults.uniqueResult().toString());
	// return lPlanCount;
    }

    public List<ImpPlan> findLoadByPassedAcceptance(String planStatus, String pFilter, Integer offset, Integer limit) {
	List<ImpPlan> lPlanList = new ArrayList<>();
	StringBuilder lQuery = new StringBuilder("select plan from ImpPlan plan, Build b, SystemLoad load" + " where " + " (plan.planStatus = :Statuses)" + " and b.active = 'Y'" + " and b.planId = plan.id" + " and load.systemId = b.systemId" + " and b.buildType = :BuildType" + " and load.planId = plan.id");
	if (pFilter != null && !pFilter.isEmpty()) {
	    lQuery.append(" AND plan.id LIKE :Planid");
	}

	lQuery.append(" group by plan.id");
	lQuery.append(" order by min(load.loadDateTime),1");

	Query setMaxResults = getCurrentSession().createQuery(lQuery.toString()).setParameter("Statuses", planStatus).setParameter("BuildType", Constants.BUILD_TYPE.STG_LOAD.name());

	if (pFilter != null && !pFilter.isEmpty()) {
	    setMaxResults.setParameter("Planid", "%" + pFilter + "%");
	}
	lPlanList = setMaxResults.setFirstResult(offset * limit).setMaxResults(limit).list();
	return lPlanList;
    }

    public Long getCountOfLoadByPassedAcceptance(String planStatus, String pFilter) {
	Long lPlanCount = 0L;
	StringBuilder lQuery = new StringBuilder("select count( DISTINCT plan.id) from imp_plan plan, build b, system_load load" + " where " + " (plan.plan_status = :Statuses)" + " and b.active = 'Y'" + " and b.plan_id = plan.id" + " and load.system_id = b.system_id" + " and b.build_type = :BuildType" + " and load.plan_id = plan.id");

	if (pFilter != null && !pFilter.isEmpty()) {
	    lQuery.append(" AND plan.id LIKE :Planid");
	}

	Query setMaxResults = getCurrentSession().createSQLQuery(lQuery.toString()).setParameter("Statuses", planStatus).setParameter("BuildType", Constants.BUILD_TYPE.STG_LOAD.name());

	if (pFilter != null && !pFilter.isEmpty()) {
	    setMaxResults.setParameter("Planid", "%" + pFilter + "%");
	}

	final List<BigInteger> obj = setMaxResults.list();
	for (BigInteger l : obj) {
	    if (l != null) {
		lPlanCount = l.longValue();
	    }
	}
	return lPlanCount;
    }

    public List<ImpPlan> findByLoad(List<String> planStatus, String loadSetType, Integer offset, Integer limit, Map<String, String> orderBy, String filter) {
	List<ImpPlan> lPlanList = new ArrayList();
	StringBuilder lQuery = new StringBuilder("SELECT DISTINCT plan FROM ImpPlan plan, Build build1" + " WHERE plan.active = 'Y'" + " AND plan.planStatus IN (:Statuses)" + " AND build1.active = 'Y'" + " AND build1.planId.id = plan.id" + " AND build1.buildType = :BuildType" + " AND build1.loadSetType = :LoadSetType");
	if (filter != null && !filter.isEmpty()) {
	    lQuery.append(" AND plan.id LIKE '%" + filter.toUpperCase() + "%'");
	}

	if (orderBy != null && !orderBy.isEmpty()) {
	    lQuery.append(" ORDER BY");
	    for (Map.Entry<String, String> order : orderBy.entrySet()) {
		lQuery.append(" plan.").append(order.getKey());
		lQuery.append(" ").append(order.getValue());
	    }
	}

	lPlanList = getCurrentSession().createQuery(lQuery.toString()).setParameterList("Statuses", planStatus).setParameter("BuildType", Constants.BUILD_TYPE.STG_LOAD.name()).setParameter("LoadSetType", loadSetType).setFirstResult(offset * limit).setMaxResults(limit).list();

	return lPlanList;
    }

    public Long getCountOfLoad(List<String> planStatus, String loadSetType, String filter) {
	Long lPlanCount;
	String lQuery = "SELECT COUNT(DISTINCT plan) FROM ImpPlan plan, Build build1" + " WHERE plan.active = 'Y'" + " AND plan.planStatus IN (:Statuses)" + " AND build1.active = 'Y'" + " AND build1.planId.id = plan.id" + " AND build1.buildType = :BuildType" + " AND build1.loadSetType = :LoadSetType";
	if (filter != null && !filter.isEmpty()) {
	    lQuery = lQuery + " AND plan.id LIKE '%" + filter.toUpperCase() + "%'";
	}
	lPlanCount = (Long) getCurrentSession().createQuery(lQuery).setParameterList("Statuses", planStatus).setParameter("BuildType", Constants.BUILD_TYPE.STG_LOAD.name()).setParameter("LoadSetType", loadSetType).uniqueResult();

	return lPlanCount;
    }

    public List<ImpPlan> findByStatusOnLoadTypeList(String status, String loadType, Integer offset, Integer limit, LinkedHashMap<String, String> lOrderBy) {
	List<Criterion> lRestrictions = new ArrayList<>();
	lRestrictions.add(Restrictions.eq("planStatus", status));
	lRestrictions.add(Restrictions.eq("loadType", loadType));
	lRestrictions.add(Restrictions.eq("active", "Y"));
	lRestrictions.add(Restrictions.eq("macroHeader", Boolean.FALSE));
	return findAll(lRestrictions, offset, limit, lOrderBy);
    }

    public Long countByStatusOnLoadTypeList(String status, String loadType) {
	List<Criterion> lRestrictions = new ArrayList<>();
	lRestrictions.add(Restrictions.eq("planStatus", status));
	lRestrictions.add(Restrictions.eq("loadType", loadType));
	lRestrictions.add(Restrictions.eq("active", "Y"));
	lRestrictions.add(Restrictions.eq("macroHeader", Boolean.FALSE));
	return count(lRestrictions);
    }

    public List<ImpPlan> findQAPlanList(List<String> status, String pFilter, Constants.BUILD_TYPE build_type, Constants.LoaderTypes loaderTypes, List<String> qaFilter, Integer offset, Integer limit, LinkedHashMap<String, String> lOrderBy) {
	String lQueryString = "SELECT plan FROM ImpPlan plan, Build build, SystemLoad sysLoad" + " WHERE plan.id = build.planId " + " AND plan.active = 'Y'" + " AND plan.macroHeader = false" + " AND plan.planStatus in (:planStatus)" + " AND build.buildType = :buildType" + " AND build.loadSetType = :loadType" + " AND build.active ='Y'" + " AND build.jobStatus = 'S'" + " AND plan.id = sysLoad.planId " + " AND sysLoad.active = 'Y' " + " AND sysLoad.qaBypassStatus IN (:qaFilter) ";
	if (pFilter != null && !pFilter.isEmpty()) {
	    lQueryString = lQueryString + " AND plan.id LIKE '%" + pFilter.toUpperCase() + "%'";
	}
	String lOrderByString = "";
	String lGroupByString = " GROUP BY plan ";

	if (lOrderBy != null && !lOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> entrySet : lOrderBy.entrySet()) {
		String key = entrySet.getKey();
		String value = entrySet.getValue();
		lOrderByString = " ORDER BY plan." + key + " " + value;
	    }
	} else {
	    lOrderByString = " ORDER BY MIN(sysLoad.loadDateTime) ";
	}
	List<ImpPlan> list = getCurrentSession().createQuery(lQueryString + lGroupByString + lOrderByString).setParameterList("planStatus", status).setParameter("buildType", build_type.name()).setParameter("loadType", loaderTypes.name()).setParameterList("qaFilter", qaFilter).setFirstResult(offset * limit).setMaxResults(limit).list();
	return list;
    }

    public Long countByQAStatusList(List<String> status, String pFilter, Constants.BUILD_TYPE build_type, Constants.LoaderTypes loaderTypes, List<String> qaStatusFilter) {
	String lQueryString = "SELECT count(DISTINCT plan) FROM ImpPlan plan, Build build, SystemLoad sysLoad " + " WHERE plan.id = build.planId " + " AND plan.active = 'Y'" + " AND plan.macroHeader = false" + " AND plan.planStatus in (:planStatus)" + " AND build.buildType = :buildType" + " AND build.loadSetType = :loadType" + " AND build.active ='Y'" + " AND build.jobStatus = 'S'" + " AND plan.id = sysLoad.planId " + " AND sysLoad.active = 'Y' " + " AND sysLoad.qaBypassStatus IN (:qaFilter) ";
	if (pFilter != null && !pFilter.isEmpty()) {
	    lQueryString = lQueryString + " AND plan.id LIKE '%" + pFilter.toUpperCase() + "%'";
	}
	Long lCount = (Long) getCurrentSession().createQuery(lQueryString).setParameterList("planStatus", status).setParameter("buildType", build_type.name()).setParameter("loadType", loaderTypes.name()).setParameterList("qaFilter", qaStatusFilter).uniqueResult();
	return lCount;
    }

    public List<Object[]> getImpPlanFromLoadDateTime(List<String> status) {
	String lQuery = "SELECT impPlan.id,impPlan.lead_id,impPlan.dev_manager,sysLoad.load_date_time, string_agg(DISTINCT sys.name, ', ') AS name,imp.peer_reviewers,imp.dev_id,impPlan.plan_desc " + " FROM imp_plan impPlan, system_load sysLoad ,system sys,implementation imp, build c " + " WHERE impPlan.plan_status IN (:planStatus) " + " AND date(sysLoad.load_date_time) = date (now()+ INTERVAL '4 days')  " + " AND sysLoad.prod_load_status is null" + " AND sysLoad.active = 'Y'"
		+ " AND impPlan.active = 'Y'" + " AND c.system_id = sysLoad.system_id" + " AND c.plan_id = impPlan.id" + " AND c.build_type = '" + Constants.BUILD_TYPE.STG_LOAD.name() + "'" + " AND c.load_set_type = '" + Constants.LoaderTypes.E + "'" + " AND sysLoad.plan_id = impPlan.id " + " AND sys.id=sysLoad.system_id" + " AND imp.plan_id=impPlan.id " + " GROUP BY  impPlan.id,sysLoad.load_date_time,imp.peer_reviewers,imp.dev_id " + " ORDER BY impPlan.id DESC";

	List<Object[]> lPlanList = getCurrentSession().createSQLQuery(lQuery).setParameterList("planStatus", status).list();

	return lPlanList;

    }

    public List<ImpPlan> getPlansByLoadDateTime(String pFilter, List<String> planStatus, Integer offset, Integer limit) {
	String lQuery = "SELECT plan FROM SystemLoad sysLoad, ImpPlan plan" + " Where sysLoad.active = 'Y'" + " AND sysLoad.loadDateTime is not null" + " AND sysLoad.planId.id = plan.id" + " AND plan.planStatus in :PlanStatus";
	if (!pFilter.isEmpty()) {
	    lQuery = lQuery + " AND plan.id LIKE :PlanId";
	}
	lQuery = lQuery + " GROUP BY plan" + " ORDER BY MIN(sysLoad.loadDateTime)";

	Query lQuery1 = getCurrentSession().createQuery(lQuery);
	if (!pFilter.isEmpty()) {
	    lQuery1.setParameter("PlanId", "%" + pFilter + "%");
	}
	return lQuery1.setParameterList("PlanStatus", planStatus).setFirstResult(offset * limit).setMaxResults(limit).list();
    }

    public Long countPlansByLoadDateTime(String pFilter, List<String> planStatus) {
	String lQuery = "SELECT Count(plan) FROM ImpPlan plan" + " Where plan.planStatus in :PlanStatus";
	if (!pFilter.isEmpty()) {
	    lQuery = lQuery + " AND plan.id LIKE :PlanId";
	}

	Query lQuery1 = getCurrentSession().createQuery(lQuery);
	if (!pFilter.isEmpty()) {
	    lQuery1.setParameter("PlanId", "%" + pFilter + "%");
	}
	return (Long) lQuery1.setParameterList("PlanStatus", planStatus).uniqueResult();
    }

    public List<Object[]> getImpPlanFromMacroHeaderPlan(String status, Boolean macroHeader) {

	String lQuery = "select aa.planid, aa.maxloaddatetime , aa.devmanager, aa.leadid from (SELECT PLAN.id as planid, plan.dev_manager as devmanager ,plan.lead_id as leadid, " + "  Max(LOAD.load_date_time)  as maxloaddatetime " + "FROM imp_plan PLAN, " + "  system_load LOAD, " + "   system sys " + "WHERE  PLAN.id = LOAD.plan_id " + "   AND LOAD.system_id = sys.id " + "    AND LOAD.active = 'Y' " + "   AND PLAN.active = 'Y' " + "   AND PLAN.macro_header = :macroHeader "
		+ "  AND PLAN.plan_status = :planStatus " + "   AND PLAN.load_date_macro_mail_flag = false " + "GROUP  BY PLAN.id )as aa where to_char(aa.maxloaddatetime, 'YYYY-MM-DD HH24:MI') = to_char(now(), 'YYYY-MM-DD HH24:MI') ";
	List<Object[]> lPlanList = getCurrentSession().createSQLQuery(lQuery).setParameter("planStatus", status).setParameter("macroHeader", macroHeader).list();
	return lPlanList;
    }

    public List<String> getSecuredSegmentList(String planId, String lPlanId) {
	String lQuery = "" + "select distinct seg.program_name from checkout_segments seg " + "join checkout_segments seg1 on seg.program_name = seg1.program_name " + "where seg.plan_id = :planId and seg1.plan_id = :lPlanId " + "and seg.active='Y' and seg1.active='Y'";
	List<String> lSegmentList = getCurrentSession().createSQLQuery(lQuery).setParameter("planId", planId).setParameter("lPlanId", lPlanId).list();
	return lSegmentList;
    }

    public Object findFallBackMacroHeaderPlan(String userId, Integer pOffset, Integer pLimit) {
	Calendar c = Calendar.getInstance();
	c.add(Calendar.WEEK_OF_MONTH, Constants.fallBackMacroHeaderDateGap);
	String lQuery = "SELECT plan1 FROM ImpPlan plan1" + " WHERE  plan1.acceptedDateTime BETWEEN :fromTime AND :toTime" + " AND plan1.planStatus = :PlanStatus" + " AND plan1.macroHeader = :MacroHeader" + " AND plan1.active = 'Y'";
	if (!userId.isEmpty()) {
	    lQuery = lQuery + " AND plan1.devManager = :devMgrId";

	}

	Query lQuery1 = getCurrentSession().createQuery(lQuery);
	if (!userId.isEmpty()) {
	    lQuery1.setParameter("devMgrId", userId);
	}
	return lQuery1.setParameter("PlanStatus", Constants.PlanStatus.ONLINE.name()).setParameter("MacroHeader", true).setParameter("fromTime", c.getTime()).setParameter("toTime", new Date()).setFirstResult(pOffset * pLimit).setMaxResults(pLimit).list();

    }

    public Long countFallBackMacroHeaderPlan(String userId) throws WorkflowException {
	Calendar c = Calendar.getInstance();
	c.add(Calendar.WEEK_OF_MONTH, Constants.fallBackMacroHeaderDateGap);
	String lQuery = "SELECT count(plan1) FROM ImpPlan plan1" + " WHERE plan1.acceptedDateTime BETWEEN :fromTime AND :toTime" + " AND plan1.planStatus = :PlanStatus" + " AND plan1.macroHeader = :MacroHeader" + " AND plan1.active = 'Y'";
	if (!userId.isEmpty()) {
	    lQuery = lQuery + " AND plan1.devManager = :devMgrId";

	}
	Query lQuery1 = getCurrentSession().createQuery(lQuery);
	if (!userId.isEmpty()) {
	    lQuery1.setParameter("devMgrId", userId);
	}
	return (Long) lQuery1.setParameter("PlanStatus", Constants.PlanStatus.ONLINE.name()).setParameter("MacroHeader", true).setParameter("fromTime", c.getTime()).setParameter("toTime", new Date()).uniqueResult();

    }

    public List<Object[]> getPostSegmentRelatedPlansPreProdLoad(String pPlanId) {
	String lQuery = "SELECT concat(othSeg.plan_id,'/',othSeg.target_system) , othPlan.plan_status, othPlan.id, preProd.status " + " FROM checkout_segments othSeg ,checkout_segments seg, system_load othLoad, " + " system_load loadl, imp_plan othPlan, system sys, pre_production_loads preProd" + " WHERE seg.file_name = othSeg.file_name" + " AND preProd.active='Y'" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.target_system = othSeg.target_system"
		+ " AND sys.name = seg.target_system" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id" + " AND loadl.plan_id <> othLoad.plan_id" + " AND loadl.system_id = othLoad.system_id" + " AND othLoad.plan_id = othSeg.plan_id" + " AND othLoad.system_id = preProd.system_id" + " AND othSeg.plan_id = preProd.plan_id" + " AND loadl.active = 'Y'" + " AND othLoad.active = 'Y'" + " AND othLoad.load_date_time is not null" + " AND loadl.load_date_time is not null"
		+ " AND othLoad.load_date_time > loadl.load_date_time " + " AND othPlan.id = othLoad.plan_id" + " AND sys.id = othLoad.system_id" + " AND loadl.plan_id = seg.plan_id" + " AND seg.plan_id LIKE ?";

	return getCurrentSession().createSQLQuery(lQuery).setParameter(0, pPlanId).list();
    }

    public List<SegmentBasedActionDetail> getAllDeploymentActivities(SegmentSearchForm segmentSearchForm, Integer offset, Integer limit, LinkedHashMap<String, String> lOrderBy, boolean isExcelExport, boolean getFullCount) {
	LOG.info(new Gson().toJson(segmentSearchForm));

	StringBuilder sb = new StringBuilder();
	if (!isExcelExport) {
	    sb.append("SELECT distinct preSeg.id, preSeg.planstatus, preSeg.devname, preSeg.programname, preSeg.targetsystem, preSeg.catname, TO_TIMESTAMP(to_char(preSeg.modifieddate, 'YYYY-MM-DD HH24:MI'),'YYYY-MM-DD HH24:MI') as modifieddate, preSeg.productionstatus, preSeg.funcarea FROM ");

	} else {
	    sb.append(
		    "SELECT distinct preSeg.id, preSeg.planstatus, preSeg.devname, preSeg.programname, preSeg.targetsystem, preSeg.catname, TO_TIMESTAMP(to_char(preSeg.modifieddate, 'YYYY-MM-DD HH24:MI'),'YYYY-MM-DD HH24:MI') as modifieddate, preSeg.productionstatus, preSeg.loaddatetime," + " preSeg.devmanagername, preSeg.leadname, preSeg.loadattendee, preSeg.peerreviewer, preSeg.funcarea,  preSeg.csrno," + " preSeg.projectname, preSeg.qastatus,preSeg.loadinstruction , preSeg.plandesc  FROM ");
	}

	if (segmentSearchForm.getEnvironment().equalsIgnoreCase("Pre-Production")) {
	    sb.append("PRE_PROD_SEGMENT_BASED_ACTION_VIEW preSeg ");
	} else {
	    sb.append("PROD_SEGMENT_BASED_ACTION_VIEW preSeg ");
	}
	sb.append("where sysid in (:systems) ");

	List<String> actionList = new ArrayList<>();
	if (segmentSearchForm.getActionList().contains("Deployed")) {
	    actionList.add(Constants.LOAD_SET_STATUS.ACTIVATED.name());
	}

	if (segmentSearchForm.getActionList().contains("Removed")) {
	    actionList.add(Constants.LOAD_SET_STATUS.DELETED.name());
	    actionList.add(Constants.LOAD_SET_STATUS.FALLBACK_ACTIVATED.name());
	}
	sb.append("and productionstatus in (:preProdStatus) ");

	String fileExten = "";
	if (segmentSearchForm.getProgramNames() != null && !segmentSearchForm.getProgramNames().isEmpty()) {
	    for (String programData : segmentSearchForm.getProgramNames()) {
		fileExten = getExtensionByApacheCommonLib(programData);
		LOG.info("File Name and  Extension " + programData + " Exten >>>" + fileExten);
		if (fileExten.isEmpty()) {
		    sb.append("and programname like '%" + programData.toLowerCase() + "%'");
		}
	    }
	    StringBuilder sb1 = new StringBuilder();
	    for (String s2 : segmentSearchForm.getProgramNames()) {
		sb1.append(s2.toLowerCase());
		sb1.append(",");
	    }
	    if (!fileExten.isEmpty()) {
		List<String> list = Stream.of(sb1.toString().split(",")).collect(Collectors.toList());
		sb.append("and  programname  in  " + "('" + StringUtils.join(list, "','") + "')");

		LOG.info("and  programname  in  " + "('" + StringUtils.join(list, "','") + "')");
	    }
	}

	// Function area
	if (segmentSearchForm.getFunctionPackage() != null && !segmentSearchForm.getFunctionPackage().isEmpty()) {
	    sb.append("and funcarea in (:functionArea) ");
	}
	sb.append("and lastactionstatus in (:lastActionStatus) ");
	sb.append("and modifiedDate between :startDate and :endDate ");
	sb.append("order by preSeg.id, modifieddate asc");
	LOG.info(sb.toString());
	Query lQuery = getCurrentSession().createSQLQuery(sb.toString()).setParameterList("systems", segmentSearchForm.getTargetSys()).setParameterList("preProdStatus", actionList);

	if (segmentSearchForm.getFunctionPackage() != null && !segmentSearchForm.getFunctionPackage().isEmpty()) {
	    lQuery.setParameterList("functionArea", segmentSearchForm.getFunctionPackage());
	}

	List<String> statusList = new ArrayList<>();
	statusList.add(Constants.CheckinStatus.SUCCESS.name());
	statusList.add(Constants.CheckinStatus.FAILED.name());

	lQuery.setParameterList("lastActionStatus", statusList);
	lQuery.setParameter("startDate", segmentSearchForm.getStartDate());
	lQuery.setParameter("endDate", segmentSearchForm.getEndDate());

	lQuery.setResultTransformer(new AliasToBeanResultTransformer(SegmentBasedActionDetail.class));
	if (!getFullCount) {
	    lQuery.setFirstResult(offset * limit);
	    lQuery.setMaxResults(limit);
	}

	return lQuery.list();
    }

    public String getExtensionByApacheCommonLib(String filename) {
	return FilenameUtils.getExtension(filename);
    }

    public List<ImpPlan> findQAFunPlanList(User lUser, List<String> status, String pFilter, Constants.BUILD_TYPE build_type, Constants.LoaderTypes loaderTypes, List<String> qaFilter, Integer offset, Integer limit, LinkedHashMap<String, String> lOrderBy) {
	String lQueryString = "SELECT plan FROM ImpPlan plan, Build build, SystemLoad sysLoad" + " WHERE plan.id = build.planId " + " AND plan.active = 'Y'" + " AND plan.macroHeader = false" + " AND plan.planStatus in (:planStatus)" + " AND build.buildType = :buildType" + " AND build.loadSetType = :loadType" + " AND build.active ='Y'" + " AND build.jobStatus = 'S'" + " AND plan.id = sysLoad.planId " + " AND sysLoad.active = 'Y' " + " AND sysLoad.qaBypassStatus IN (:qaFilter) ";
	if (pFilter != null && !pFilter.isEmpty()) {
	    lQueryString = lQueryString + " AND plan.id LIKE '%" + pFilter + "%'";
	}
	if (lUser.getCurrentRole().equalsIgnoreCase(Constants.UserGroup.QADeployLead.name())) {
	    if (lUser.getCurrentDelegatedUser() == null) {
		lQueryString = lQueryString + " AND  (sysLoad.qaFunctionalTesters LIKE '%" + lUser.getId() + "%' or plan.leadId LIKE '%" + lUser.getId() + "%' ) ";
	    } else {
		lQueryString = lQueryString + " AND  (sysLoad.qaFunctionalTesters LIKE '%" + lUser.getCurrentDelegatedUser().getId() + "%' or plan.leadId LIKE '%" + lUser.getCurrentDelegatedUser().getId() + "%') ";
	    }
	} else {
	    if (lUser.getCurrentDelegatedUser() == null) {
		lQueryString = lQueryString + " AND  sysLoad.qaFunctionalTesters LIKE '%" + lUser.getId() + "%' ";
	    } else {
		lQueryString = lQueryString + " AND  sysLoad.qaFunctionalTesters LIKE '%" + lUser.getCurrentDelegatedUser().getId() + "%' ";
	    }
	}
	String lOrderByString = "";

	if (lOrderBy != null && !lOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> entrySet : lOrderBy.entrySet()) {
		String key = entrySet.getKey();
		String value = entrySet.getValue();
		lOrderByString = " GROUP BY plan." + key + " ORDER BY plan." + key + " " + value;
	    }
	} else {
	    lQueryString = lQueryString + "GROUP BY plan ORDER BY MIN(sysLoad.loadDateTime)";
	}
	List<ImpPlan> list = getCurrentSession().createQuery(lQueryString + lOrderByString).setParameterList("planStatus", status).setParameter("buildType", build_type.name()).setParameter("loadType", loaderTypes.name()).setParameterList("qaFilter", qaFilter).setFirstResult(offset * limit).setMaxResults(limit).list();
	return list;
    }

    public Long countByQAFunStatusList(User lUser, List<String> status, String pFilter, Constants.BUILD_TYPE build_type, Constants.LoaderTypes loaderTypes, List<String> qaStatusFilter) {
	String lQueryString = "SELECT count(DISTINCT plan) FROM ImpPlan plan, Build build, SystemLoad sysLoad " + " WHERE plan.id = build.planId " + " AND plan.active = 'Y'" + " AND plan.macroHeader = false" + " AND plan.planStatus in (:planStatus)" + " AND build.buildType = :buildType" + " AND build.loadSetType = :loadType" + " AND build.active ='Y'" + " AND build.jobStatus = 'S'" + " AND plan.id = sysLoad.planId " + " AND sysLoad.active = 'Y' " + " AND sysLoad.qaBypassStatus IN (:qaFilter) ";
	if (pFilter != null && !pFilter.isEmpty()) {
	    lQueryString = lQueryString + " AND plan.id LIKE '%" + pFilter + "%'";
	}
	if (lUser.getCurrentRole().equalsIgnoreCase(Constants.UserGroup.QADeployLead.name())) {
	    if (lUser.getCurrentDelegatedUser() == null) {
		lQueryString = lQueryString + " AND  (sysLoad.qaFunctionalTesters LIKE '%" + lUser.getId() + "%' or plan.leadId LIKE '%" + lUser.getId() + "%' ) ";
	    } else {
		lQueryString = lQueryString + " AND  (sysLoad.qaFunctionalTesters LIKE '%" + lUser.getCurrentDelegatedUser().getId() + "%' or plan.leadId LIKE '%" + lUser.getCurrentDelegatedUser().getId() + "%') ";
	    }
	} else {
	    if (lUser.getCurrentDelegatedUser() == null) {
		lQueryString = lQueryString + " AND  sysLoad.qaFunctionalTesters LIKE '%" + lUser.getId() + "%' ";
	    } else {
		lQueryString = lQueryString + " AND  sysLoad.qaFunctionalTesters LIKE '%" + lUser.getCurrentDelegatedUser().getId() + "%' ";
	    }
	}
	Long lCount = (Long) getCurrentSession().createQuery(lQueryString).setParameterList("planStatus", status).setParameter("buildType", build_type.name()).setParameter("loadType", loaderTypes.name()).setParameterList("qaFilter", qaStatusFilter).uniqueResult();
	return lCount;
    }

    public List<String> getPlanListToReject(String pPlanId) {
	String lQuery = "select imp.id from imp_plan imp " + " left join production_loads prod on imp.id = prod.plan_id " + " where imp.active='Y' and prod.active='Y' " + " and ((imp.plan_status = :partialPlanToInclude " + " and prod.status is null) or imp.plan_status in (:planToBeIncluded)) " + " and imp.id = :planIds";
	List list = getCurrentSession().createSQLQuery(lQuery).setParameter("partialPlanToInclude", Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name()).setParameterList("planToBeIncluded", new ArrayList(Constants.PlanStatus.getSecuredBeforeReadyForProduction().keySet())).setParameter("planIds", pPlanId).list();
	return list;
    }

    public List<Object[]> getYodaPreProdRelatedPlans(String pPlanId) {

	String lQuery = "SELECT concat(othSeg.plan_id,'/',othSeg.target_system) , othPlan.plan_status, preProd.status, preProd.active, preProd.id as system_load_action_id, othSeg.plan_id, othSeg.target_system" + " FROM checkout_segments othSeg " + " join checkout_segments seg on seg.file_name = othSeg.file_name AND seg.target_system = othSeg.target_system" + "	join system_load othLoad on othLoad.plan_id = othSeg.plan_id "
		+ "	join system_load loadl on loadl.system_id = othLoad.system_id AND loadl.plan_id = seg.plan_id" + "	join imp_plan othPlan on othPlan.id = othLoad.plan_id" + "	join system sys on sys.name = seg.target_system AND sys.id = othLoad.system_id" + "	join vpars vpar on vpar.system_id = othLoad.system_id" + "	left join system_load_actions preProd on othLoad.system_id = preProd.system_id and othSeg.plan_id = preProd.plan_id and vpar.id = preProd.vpar_id"
		+ " WHERE seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND othLoad.active = 'Y'" + " AND loadl.active = 'Y'" + " AND vpar.active= 'Y'" + " AND vpar.type=:preProdVpar" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id" + " AND loadl.plan_id <> othLoad.plan_id" + " AND (othLoad.load_date_time <= loadl.load_date_time)" + " AND seg.plan_id =:planId";
	return getCurrentSession().createSQLQuery(lQuery).setParameter("preProdVpar", Constants.VPARSEnvironment.PRE_PROD.name()).setParameter("planId", pPlanId).list();
    }

    public List<Object[]> getYodaPostSegmentRelatedPlansPreProdLoad(String pPlanId) {
	String lQuery = "SELECT distinct concat(othSeg.plan_id,'/',othSeg.target_system) , othPlan.plan_status, othPlan.id, preProd.status " + " FROM checkout_segments othSeg ,checkout_segments seg, system_load othLoad, " + " system_load loadl, imp_plan othPlan, system sys,vpars vpar, system_load_actions preProd" + " WHERE seg.file_name = othSeg.file_name" + " AND preProd.active='Y'" + " AND seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND seg.target_system = othSeg.target_system"
		+ " AND sys.name = seg.target_system" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id" + " AND loadl.plan_id <> othLoad.plan_id" + " AND loadl.system_id = othLoad.system_id" + " AND othLoad.plan_id = othSeg.plan_id" + " AND othLoad.system_id = preProd.system_id" + " AND othSeg.plan_id = preProd.plan_id" + " AND vpar.system_id = othLoad.system_id" + " AND othLoad.system_id = preProd.system_id " + " and vpar.id = preProd.vpar_id" + " AND loadl.active = 'Y'"
		+ " AND othLoad.active = 'Y'" + " AND vpar.active= 'Y'" + " AND vpar.type=:preProdVpar" + " AND othLoad.load_date_time is not null" + " AND loadl.load_date_time is not null" + " AND othLoad.load_date_time > loadl.load_date_time " + " AND othPlan.id = othLoad.plan_id" + " AND sys.id = othLoad.system_id" + " AND loadl.plan_id = seg.plan_id" + " AND seg.plan_id =:planId";

	return getCurrentSession().createSQLQuery(lQuery).setParameter("preProdVpar", Constants.VPARSEnvironment.PRE_PROD.name()).setParameter("planId", pPlanId).list();
    }

    public List<SourceArtifactSearchResult> getHistoricalVersionsBySourceArtifact(SourceArtifactSearchForm searchForm, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	StringBuilder lBuilder = new StringBuilder();
	lBuilder.append("select final.* from (select checkout.program_name as programname, checkout.func_area as funcpackage, checkout.repo_desc as repodesc, checkout.plan_id as planid, checkout.target_system as targetsystem, ");
	lBuilder.append("sysload.load_date_time as loaddatetime, impPlan.plan_status AS planstatus, impPlan.fallback_date_time AS fallbackdatetime, ");
	lBuilder.append(" impl.dev_name AS developername, '' as sourcerepo, '' as commitid, checkout.file_name as filename, checkout.file_type as filetype,");
	lBuilder.append(" 0 as statusrank, checkout.ref_status as checkoutrefstatus, checkout.source_url as sourceurl from checkout_segments checkout,system sys, ");
	lBuilder.append("system_load sysload, implementation impl, imp_plan impPlan ");
	lBuilder.append("where sys.name = checkout.target_system and sysload.system_id = sys.id and sysload.plan_id = checkout.plan_id ");
	lBuilder.append("and impl.id = checkout.imp_id and impPlan.id = impl.plan_id and checkout.active='Y' and impl.active='Y' and impPlan.active='Y' ");
	lBuilder.append("and sys.active='Y' and sysload.active='Y' and sys.id in (:systemId) AND checkout.program_name ILIKE :programNames ");
	if (searchForm.getFileType() != null && !searchForm.getFileType().isEmpty()) {
	    lBuilder.append(" AND checkout.program_name ILIKE :dbFileExt ");
	}
	lBuilder.append(" and impPlan.plan_status not in('ONLINE', 'FALLBACK', 'PENDING') UNION ALL ");
	lBuilder.append(" select  fin.programname, fin.funcpackage, fin.repodesc, fin.planid, fin.targetsystem,fin.loaddatetime, fin.planstatus, ");
	lBuilder.append(" fin.fallbackdatetime,  fin.developername,fin.sourcerepo, fin.commitid, fin.filename, filetype,");
	lBuilder.append(" RANK() over (partition by fin.filename, fin.sourcerepo, fin.planstatus,fin.targetsystem ");
	lBuilder.append(" order by fin.loaddatetime desc) as statusrank, 'prod' as checkoutrefstatus, fin.sourceurl as sourceurl  from (select fileList.program_name AS programname, rep.repo_description as repodesc,  rep.func_area as funcpackage,");
	lBuilder.append(" upper(commit.ref_plan) AS planid, fileList.target_system AS targetsystem, commit.ref_load_date_time  AS loaddatetime,");
	lBuilder.append(" commit.ref_status AS planstatus, impPlan.fallback_date_time AS fallbackdatetime, impl.dev_name AS developername,sub.sub_source_repo as sourcerepo,commit.source_commit_id as ");
	lBuilder.append(" commitid, fileList.file_name as filename, ");
	lBuilder.append(" commit.id as repo_commit_id, CASE WHEN ( delete_detail.repo_commit_id > 0 ) THEN delete_detail.repo_commit_id ELSE 0 END AS DELETE_ID, ");
	lBuilder.append(" rep.file_type as filetype, rep.source_repo as sourceurl from git.repo_file_list fileList ");
	lBuilder.append(" join system sys on sys.name = fileList.target_system join git.repo_commit commit on commit.file_id = fileList.id ");
	lBuilder.append(" join git.sub_repo_detail sub on sub.id = fileList.sub_repo_id join git.repo_detail rep on rep.id = sub.repo_id ");
	lBuilder.append(" left join imp_plan impPlan on impPlan.id = upper(commit.ref_plan) and impPlan.active='Y' ");
	lBuilder.append(" left join checkout_segments checkout on checkout.plan_id = impPlan.id and checkout.program_name = fileList.program_name ");
	lBuilder.append(" and checkout.target_system = fileList.target_system and checkout.active='Y' left join implementation impl on impl.id = checkout.imp_id ");
	lBuilder.append(" left join (SELECT rc.file_id, Max(rc.id) AS repo_commit_id FROM   git.repo_commit rc, git.repo_file_list fl");
	lBuilder.append(" WHERE  rc.file_id = fl.id and fl.program_Name ILIKE :programNames ");
	if (searchForm.getFileType() != null && !searchForm.getFileType().isEmpty()) {
	    lBuilder.append("AND fl.file_ext = :gitFileExt ");
	}
	lBuilder.append(" and rc.ref_status = 'Deleted' GROUP  BY file_id) as delete_detail on delete_detail.file_id = fileList.id");
	lBuilder.append(" where sys.id in (:systemId) ");
	lBuilder.append(" AND fileList.program_name ILIKE :programNames ");
	if (searchForm.getFileType() != null && !searchForm.getFileType().isEmpty()) {
	    lBuilder.append("AND fileList.file_ext = :gitFileExt ");
	}
	lBuilder.append(") as fin where fin.repo_commit_id> delete_id) as final order by loaddatetime desc, programname asc ");
	LOG.info("Query " + lBuilder.toString());
	Query lQuery = getCurrentSession().createSQLQuery(lBuilder.toString());

	if (searchForm.getFileType() != null && !searchForm.getFileType().isEmpty()) {
	    lQuery.setParameter("dbFileExt", "%." + searchForm.getFileType());
	    lQuery.setParameter("gitFileExt", searchForm.getFileType());
	}
	lQuery.setParameter("programNames", "%" + searchForm.getSourceArtifactName() + "%");
	lQuery.setParameterList("systemId", searchForm.getTargetSys());
	// lQuery.setFirstResult(pOffset * pLimit);
	// lQuery.setMaxResults(pLimit);
	lQuery.setResultTransformer(new AliasToBeanResultTransformer(SourceArtifactSearchResult.class));
	return lQuery.list();
    }

    public Long getCountHistoricalVersionsBySourceArtifact(SourceArtifactSearchForm searchForm) {
	Long sourceArtifactCount = 0L;
	StringBuilder lBuilder = new StringBuilder();
	lBuilder.append("select count(final.*) from (select checkout.program_name as programname, checkout.func_area as funcpackage, checkout.plan_id as planid, checkout.target_system as targetsystem, ");
	lBuilder.append("sysload.load_date_time as loaddatetime, impPlan.plan_status AS planstatus, impPlan.fallback_date_time AS fallbackdatetime, ");
	lBuilder.append("impl.dev_name AS developername, checkout.file_type as filetype from checkout_segments checkout,system sys, system_load sysload, implementation impl, imp_plan impPlan ");
	lBuilder.append("where sys.name = checkout.target_system and sysload.system_id = sys.id and sysload.plan_id = checkout.plan_id ");
	lBuilder.append("and impl.id = checkout.imp_id and impPlan.id = impl.plan_id and checkout.active='Y' and impl.active='Y' and impPlan.active='Y' ");
	lBuilder.append("and sys.active='Y' and sysload.active='Y' and sys.id in (:systemId) AND checkout.program_name ILIKE :programName ");
	if (searchForm.getFileType() != null && !searchForm.getFileType().isEmpty()) {
	    lBuilder.append(" AND checkout.program_name ILIKE :dbFileExt ");
	}
	lBuilder.append(" and impPlan.plan_status not in('ONLINE', 'FALLBACK', 'PENDING') UNION ALL ");
	lBuilder.append(" select  fin.programname, fin.funcpackage, fin.planid, fin.targetsystem,fin.loaddatetime, fin.planstatus, ");
	lBuilder.append(" fin.fallbackdatetime,  fin.developername, filetype from (select fileList.program_name AS programname,  rep.func_area as funcpackage,");
	lBuilder.append(" upper(commit.ref_plan) AS planid, fileList.target_system AS targetsystem, commit.ref_load_date_time  AS loaddatetime,");
	lBuilder.append(" commit.ref_status AS planstatus, impPlan.fallback_date_time AS fallbackdatetime, impl.dev_name AS developername,sub.sub_source_repo as sourcerepo,commit.source_commit_id as ");
	lBuilder.append(" commitid, fileList.file_name as filename, ");
	lBuilder.append(" commit.id as repo_commit_id, CASE WHEN ( delete_detail.repo_commit_id > 0 ) THEN delete_detail.repo_commit_id ELSE 0 END AS DELETE_ID, ");
	lBuilder.append(" rep.file_type as filetype from git.repo_file_list fileList ");
	lBuilder.append(" join system sys on sys.name = fileList.target_system join git.repo_commit commit on commit.file_id = fileList.id ");
	lBuilder.append(" join git.sub_repo_detail sub on sub.id = fileList.sub_repo_id join git.repo_detail rep on rep.id = sub.repo_id");
	lBuilder.append(" left join imp_plan impPlan on impPlan.id = upper(commit.ref_plan) and impPlan.active='Y' ");
	lBuilder.append(" left join checkout_segments checkout on checkout.plan_id = impPlan.id and checkout.program_name = fileList.program_name ");
	lBuilder.append(" and checkout.target_system = fileList.target_system and checkout.active='Y' left join implementation impl on impl.id = checkout.imp_id ");
	lBuilder.append(" left join (SELECT rc.file_id, Max(rc.id) AS repo_commit_id FROM   git.repo_commit rc, git.repo_file_list fl");
	lBuilder.append(" WHERE  rc.file_id = fl.id and fl.program_Name ILIKE :programName ");
	if (searchForm.getFileType() != null && !searchForm.getFileType().isEmpty()) {
	    lBuilder.append(" AND fl.file_ext = :gitFileExt ");
	}
	lBuilder.append(" and rc.ref_status = 'Deleted' GROUP  BY file_id) as delete_detail on delete_detail.file_id = fileList.id");
	lBuilder.append(" where sys.id in (:systemId) ");
	lBuilder.append(" AND fileList.program_name ILIKE :programName ");
	if (searchForm.getFileType() != null && !searchForm.getFileType().isEmpty()) {
	    lBuilder.append("AND fileList.file_ext = :gitFileExt ");
	}
	lBuilder.append(") as fin where fin.repo_commit_id> delete_id) as final");
	Query lQuery = getCurrentSession().createSQLQuery(lBuilder.toString());

	if (searchForm.getFileType() != null && !searchForm.getFileType().isEmpty()) {
	    lQuery.setParameter("dbFileExt", "%." + searchForm.getFileType());
	    lQuery.setParameter("gitFileExt", searchForm.getFileType());
	}
	lQuery.setParameter("programName", searchForm.getSourceArtifactName() + "%");
	lQuery.setParameterList("systemId", searchForm.getTargetSys());
	final List<BigInteger> obj = lQuery.list();
	for (BigInteger l : obj) {
	    if (l != null) {
		sourceArtifactCount = l.longValue();
	    }
	}
	return sourceArtifactCount;
    }

    public List<ImpPlan> getPlanBySystemAndStatus(Integer systemId, String status) {
	String lQuery = "SELECT ip FROM ImpPlan ip, SystemLoad sl, System sy" + " WHERE ip.planStatus = :Status " + " AND ip.active = 'Y'" + " AND sl.active = 'Y'" + " AND sl.planId.id = ip.id" + " AND sl.systemId.id = sy.id" + " AND sy.id = :SystemId" + " AND sy.active = 'Y' ";

	List<ImpPlan> lPlanList = getCurrentSession().createQuery(lQuery).setParameter("Status", status).setParameter("SystemId", systemId).list();
	return lPlanList;
    }

    public List<SystemBasedMetaData> getSystemBasedMetaData(String targetSystem, List<String> progNames, String startLoadDate, String endLoadDate) {

	StringBuilder lBuilder = new StringBuilder();

	lBuilder.append(" select b.* from ( select a.*, rank() over (partition by (progname, targetsys, planstatus) order by loaddatetime desc ) as statusrank");
	lBuilder.append(" from (select distinct impPlan.id as planid, sys.name as targetsys, impl.id as impid, seg.func_area as funcarea, ");
	lBuilder.append(" sysload.load_date_time as loaddatetime, lc.name as loadcategory, impPlan.plan_status as planstatus, impl.dev_id as devid, sysload.qa_bypass_status ");
	lBuilder.append(" as qabypassstatus, impPlan.load_type as loadtype, impPlan.lead_id as leadid, pro.project_number as projnumber, pro.line_of_business as lineofbusiness, impPlan.sdm_tkt_num ");
	lBuilder.append(" as sdmtktnum, impl.pr_tkt_num as prtktnum ,db.dbcr_name as dbcrname, impPlan.plan_desc as plandesc ,impPlan.dev_manager as devmanager, ");
	lBuilder.append(" sysload.load_attendee_id as loadattendeeid, seg.program_name as progname, impPlan.approver, sysload.qa_functional_tester_name as qafunctionaltester, ");
	lBuilder.append(" impl.peer_reviewers as peerreviewerids, seg.file_type as filetype, '' as commitid, '' as repodetail, seg.file_name as filename, impPlan.dev_manager_name as devmanagername, impl.dev_name as developername, bld.load_set_type as loadsetType, rfc.rfc_number as rfcnumber ");
	lBuilder.append(" from checkout_segments seg join  system sys on sys.name = seg.target_system join system_load sysload on sysload.system_id = sys.id ");
	lBuilder.append(" and sysload.plan_id = seg.plan_id join implementation impl on impl.id = seg.imp_id");
	lBuilder.append(" join imp_plan impPlan on impPlan.id = impl.plan_id");
	lBuilder.append(" join load_categories lc on lc.id = sysload.load_category_id and lc.active='Y'");
	lBuilder.append(" left join project pro on pro.id = impPlan.project_id");
	lBuilder.append(" left join dbcr db on db.system_id = sys.id and db.plan_id = impPlan.id and db.active='Y'");
	lBuilder.append(" left join build bld on bld.plan_id = seg.plan_id and bld.active = 'Y' and bld.load_set_type is not null");
	lBuilder.append(" left join rfc_details rfc on rfc.system_load_id = sysload.id and rfc.active = 'Y' ");
	lBuilder.append(" where seg.active='Y' and impl.active='Y' and impPlan.active='Y' ");
	lBuilder.append(" and sys.active='Y' and sysload.active='Y' and sys.name in (:targetSystem) ");
	// Program Name
	if (progNames != null && progNames.size() == 1) {
	    lBuilder.append(" AND seg.program_name ILIKE :programName");
	} else if (progNames != null && progNames.size() > 1) {
	    lBuilder.append(" AND seg.program_name in (:programName)");
	}
	lBuilder.append(" and impPlan.plan_status not in ('ONLINE','PENDING','FALLBACK') union all ");
	lBuilder.append(" select planid, targetsys, impid, funcarea, loaddatetime, loadcategory, planstatus, devid, qabypassstatus, loadtype, leadid, ");
	lBuilder.append(" projnumber, lineofbusiness, sdmtktnum, prtktnum, dbcrname, plandesc, devmanager, loadattendeeid, progname, approver, qafunctionaltester, peerreviewerids, filetype, commitid, ");
	lBuilder.append(" repodetail, filename, devmanagername, developerName, loadsetType, rfcnumber from ( ");
	lBuilder.append(" select distinct rc.ref_plan as planid, sys.name as targetsys, impl.id  as impid, rd.func_area as funcarea, rc.ref_load_date_time as ");
	lBuilder.append(" loaddatetime, lc.name as loadcategory, rc.ref_status as planstatus, impl.dev_id as devid, impl.dev_name as developername, load.qa_bypass_status as qabypassstatus ");
	lBuilder.append(" ,impPlan.load_type as loadtype, impPlan.lead_id as leadid, pro.project_number as projnumber, pro.line_of_business as lineofbusiness, impPlan.sdm_tkt_num as sdmtktnum ");
	lBuilder.append(" , impl.pr_tkt_num as prtktnum, db.dbcr_name as dbcrname, impPlan.plan_desc as plandesc, impPlan.dev_manager as devmanager, impPlan.dev_manager_name as devmanagername, ");
	lBuilder.append(" load.load_attendee_id as loadattendeeid,fl.program_name as progname, impPlan.approver, load.qa_functional_tester_name as qafunctionaltester, impl.peer_reviewers as peerreviewerids, ");
	lBuilder.append(" rd.file_type as filetype, rc.source_commit_id as commitid, sr.sub_source_repo as repodetail, fl.file_name as filename, bld.load_set_type as loadsetType, rfc.rfc_number as rfcnumber,");
	lBuilder.append("CASE WHEN ( dd.repo_commit_id > 0 ) THEN dd.repo_commit_id ELSE 0 END AS deleteid, rc.id as repocommitid from git.repo_commit rc ");
	lBuilder.append(" join git.repo_file_list fl on fl.id = rc.file_id ");
	lBuilder.append(" join git.sub_repo_detail sr on sr.id = fl.sub_repo_id ");
	lBuilder.append(" join git.repo_detail rd on rd.id = sr.repo_id ");
	lBuilder.append(" join system sys on sys.name = fl.target_system ");
	lBuilder.append(" left join system_load load on load.system_id = sys.id and load.plan_id = upper(rc.ref_plan) and load.active='Y' ");
	lBuilder.append(" left join imp_plan impPlan on impPlan.id = load.plan_id and impPlan.active='Y' ");
	lBuilder.append(" left join implementation impl on impl.plan_id = impPlan.id and impl.active='Y' ");
	lBuilder.append(" left join load_categories lc on lc.id = load.load_category_id and lc.active='Y' ");
	lBuilder.append(" left join project pro on pro.id = impPlan.project_id ");
	lBuilder.append(" left join dbcr db on db.system_id = sys.id and db.plan_id = impPlan.id and db.active='Y' ");
	lBuilder.append(" left join build bld on bld.plan_id = impPlan.id and bld.active = 'Y' and bld.load_set_type is not null ");
	lBuilder.append(" left join rfc_details rfc on rfc.system_load_id = load.id and rfc.active = 'Y' ");
	lBuilder.append(" left join (SELECT rc.file_id, Max(rc.id) AS repo_commit_id FROM git.repo_commit rc, git.repo_file_list fl  WHERE  rc.file_id = fl.id ");
	if (progNames != null && progNames.size() == 1) {
	    lBuilder.append("AND fl.program_name ILIKE :programName");
	} else if (progNames != null && progNames.size() > 1) {
	    lBuilder.append("AND fl.program_name in (:programName) ");
	}
	lBuilder.append(" and rc.ref_status = 'Deleted' GROUP BY file_id) as dd on dd.file_id = fl.id");
	lBuilder.append(" where sys.active='Y'  and sys.name in (:targetSystem) ");
	if (progNames != null && progNames.size() == 1) {
	    lBuilder.append("AND fl.program_name ILIKE :programName");
	} else if (progNames != null && progNames.size() > 1) {
	    lBuilder.append("AND fl.program_name in (:programName) ");
	}
	lBuilder.append(" ) as fin where fin.repocommitid > fin.deleteid ) as a) as b ");

	if (startLoadDate != null && endLoadDate != null) {
	    lBuilder.append("where to_char(loaddatetime,'yyyyMMdd') >= :startDate AND to_char(loaddatetime,'yyyyMMdd') <= :endDate ");
	} else if (startLoadDate != null && endLoadDate == null) {
	    lBuilder.append("where to_char(loaddatetime,'yyyyMMdd') >= :startDate");
	} else if (startLoadDate == null && endLoadDate != null) {
	    lBuilder.append("where to_char(loaddatetime,'yyyyMMdd') <= :endDate");
	}

	Query lQuery = getCurrentSession().createSQLQuery(lBuilder.toString());
	lQuery.setParameter("targetSystem", targetSystem.toUpperCase());
	if (progNames != null && progNames.size() == 1) {
	    lQuery.setParameter("programName", progNames.get(0).trim() + "%");
	} else if (progNames != null && progNames.size() > 1) {
	    // LOG.info("Prog Name size: " + progNames.size());
	    // progNames.forEach(val -> {
	    // LOG.info("Prog Name:" + val);
	    // });
	    lQuery.setParameterList("programName", progNames);
	}

	// Date
	if (startLoadDate != null && endLoadDate != null) {
	    lQuery.setParameter("startDate", startLoadDate);
	    lQuery.setParameter("endDate", endLoadDate);
	} else if (startLoadDate != null && endLoadDate == null) {
	    lQuery.setParameter("startDate", startLoadDate);
	} else if (startLoadDate == null && endLoadDate != null) {
	    lQuery.setParameter("endDate", endLoadDate);
	}
	// LOG.info("Query: " + lQuery.toString());
	lQuery.setResultTransformer(new AliasToBeanResultTransformer(SystemBasedMetaData.class));
	return lQuery.list();
    }

    public List<Object[]> getEqualDateSegmentRelatedPlans(String pPlanId) {
	String lQuery = "SELECT concat(othSeg.plan_id,'/',othSeg.target_system) , othPlan.plan_status, preProd.status, preProd.active, preProd.system_load_action_id, preProd.last_action_status" + " FROM checkout_segments othSeg " + " join checkout_segments seg on seg.file_name = othSeg.file_name AND seg.target_system = othSeg.target_system" + "	join system_load othLoad on othLoad.plan_id = othSeg.plan_id "
		+ "	join system_load loadl on loadl.system_id = othLoad.system_id AND loadl.plan_id = seg.plan_id" + "	join imp_plan othPlan on othPlan.id = othLoad.plan_id" + "	join system sys on sys.name = seg.target_system AND sys.id = othLoad.system_id" + "	left join pre_production_loads preProd on othLoad.system_id = preProd.system_id and othSeg.plan_id = preProd.plan_id" + " WHERE seg.active = 'Y'" + " AND othSeg.active = 'Y'" + " AND othLoad.active = 'Y'"
		+ " AND loadl.active = 'Y'" + " AND seg.id <> othSeg.id" + " AND seg.plan_id <> othSeg.plan_id" + " AND loadl.plan_id <> othLoad.plan_id" + " AND (othLoad.load_date_time = loadl.load_date_time)" + " AND seg.plan_id LIKE ?";
	return getCurrentSession().createSQLQuery(lQuery).setParameter(0, pPlanId).list();
    }

    public List<DependenciesForm> getAllManualPlanForEqualLoadDate(String pPlanId, List<String> pRelPlanId) {
	String lQuery = "SELECT OthPlan.id as planid ,othsys.name as targetsystem , OthPlan.plan_status as status ,othLoad.load_date_time as loaddatetime ," + "OthPlan.load_type as loadtype ,seg.program_name as segments" + " FROM imp_plan OthPlan, system_load othLoad, system_load loadl, system othsys,checkout_segments seg" + " WHERE loadl.plan_id LIKE :planid" + " AND othLoad.plan_id = OthPlan.id" + " AND othsys.id = othLoad.system_id" + " AND loadl.system_id = othLoad.system_id"
		+ " AND seg.plan_id = othLoad.plan_id" + " AND OthPlan.id IN (:plans)" + " AND loadl.load_date_time = othLoad.load_date_time";

	List<DependenciesForm> lReturnList = getCurrentSession().createSQLQuery(lQuery.toString()).setParameter("planid", pPlanId).setParameterList("plans", pRelPlanId).setResultTransformer(new AliasToBeanResultTransformer(DependenciesForm.class)).list();
	return lReturnList;
    }

    public List<DependenciesForm> getManualDependentPlans(String planId) {
	String lQuery = "SELECT ip.id as planid , string_agg(DISTINCT seg.target_system, ', ') as targetsystem, ip.plan_status as status ," + " sl.load_date_time as loaddatetime ,ip.load_type as loadtype, string_agg(DISTINCT seg.program_name, ', ') as segments  " + " FROM imp_plan ip, system_load sl, system s, checkout_segments seg " + " WHERE ip.related_plans ILIKE :planid " + " AND sl.plan_id = ip.id " + " AND sl.system_id = s.id " + " AND sl.active = 'Y' " + " AND s.active = 'Y' "
		+ " AND seg.plan_id = ip.id " + " AND seg.target_system = s.name " + " AND seg.active = 'Y' " + " GROUP BY ip.id,seg.target_system , ip.plan_status, sl.load_date_time, ip.load_type " + " ORDER BY sl.load_date_time";

	List<DependenciesForm> lReturnList = getCurrentSession().createSQLQuery(lQuery).setParameter("planid", "%" + planId + "%").setResultTransformer(new AliasToBeanResultTransformer(DependenciesForm.class)).list();
	return lReturnList;
    }

    public List<String> getListOfPlansDeactivatedInProd() {
	String lQuery = "select DISTINCT ip.id from imp_plan ip, production_loads pl " + " where ip.id = pl.plan_id" + " and ip.plan_status in (:PlanStatuses)" + " and pl.status = :LoadsetStatus" + " and pl.last_action_status = 'SUCCESS'" + " and pl.active = 'Y'";

	List<String> lReturnList = getCurrentSession().createSQLQuery(lQuery).setParameter("LoadsetStatus", Constants.LOAD_SET_STATUS.DEACTIVATED.name()).setParameterList("PlanStatuses", Arrays.asList(Constants.PlanStatus.READY_FOR_PRODUCTION_DEPLOYMENT.name())).list();

	return lReturnList;
    }

    /*
     * ZTPFM-2125 Get plan List DeActivated in Production Loads
     */
    public List<ProdDeactivateResult> getImpPlanFromProductionLoadDeactivate(String status) {
	String lQuery = "select DISTINCT(prodLoad.plan_id) AS planid ,string_agg(DISTINCT sys.name, ', ') AS sysname ,load.load_date_time AS loaddate , " + " impl.peer_reviewers AS reviewers ,plan.lead_id  AS leadid ,plan.dev_manager AS devmanager ,impl.dev_id AS developerid ,plan.plan_status AS planstatus" + " FROM imp_plan plan,production_loads prodLoad,implementation impl,system_load load, system sys " + " WHERE plan.id=prodLoad.plan_id " + " AND impl.plan_id=prodLoad.plan_id "
		+ " AND plan.id=impl.plan_id " + " AND load.plan_id=prodLoad.plan_id " + " AND load.system_id=sys.id " + " AND load.active='Y' " + " AND prodLoad.active='Y' " + " AND impl.active='Y' " + " AND prodLoad.status = :loadStatus " + " AND prodLoad.last_action_status='SUCCESS' " + " AND plan.plan_status in(:planStatus) " + " GROUP BY prodLoad.plan_id,impl.peer_reviewers,plan.lead_id,plan.dev_manager,impl.dev_id,load.load_date_time,plan.plan_status" + " ORDER BY prodLoad.plan_id DESC";

	List<ProdDeactivateResult> lPlanList = getCurrentSession().createSQLQuery(lQuery).setParameter("planStatus", status).setParameter("loadStatus", Constants.LOAD_SET_STATUS.DEACTIVATED.name()).setResultTransformer(new AliasToBeanResultTransformer(ProdDeactivateResult.class)).list();

	return lPlanList;

    }

    public List<ProdDeactivateResult> getDependentPlanFromDeactivatePlanList(List<String> status, String planId) {
	String lQuery = "SELECT DISTINCT(othplan.id) AS planid ,string_agg(DISTINCT sys.name, ', ') AS sysname ,othload.load_date_time AS loaddate , " + "impl.peer_reviewers AS reviewers ,othplan.lead_id AS leadid ,othplan.dev_manager AS devmanager ,impl.dev_id AS developerid ,othplan.plan_status AS planstatus " + "FROM   checkout_segments othSeg , " + "       checkout_segments seg, " + "       system_load othload, " + "       system_load loadl, " + "       imp_plan othplan, "
		+ "       implementation impl, " + "       system sys " + "WHERE  seg.file_name = othseg.file_name " + "AND    seg.active = 'Y' " + "AND    othseg.active = 'Y' " + "AND    othload.active = 'Y' " + "AND    loadl.active = 'Y' " + "AND    impl.plan_id=othplan.id " + "AND    seg.target_system = othseg.target_system " + "AND    sys.NAME = seg.target_system " + "AND    seg.id <> othseg.id " + "AND    seg.plan_id <> othseg.plan_id " + "AND    loadl.plan_id <> othload.plan_id "
		+ "AND    loadl.system_id = othload.system_id " + "AND    othload.plan_id = othseg.plan_id " + "AND    (othload.load_date_time > loadl.load_date_time) " + "AND    othplan.id = othload.plan_id " + "AND    sys.id = othload.system_id " + "AND    loadl.plan_id = seg.plan_id " + "AND    seg.plan_id  = :planId " + "AND    othplan.plan_status  in(:planStatus) "
		+ "GROUP BY othplan.id,impl.peer_reviewers,othplan.lead_id,othplan.dev_manager,impl.dev_id,othload.load_date_time,othplan.plan_status " + "ORDER BY othplan.id";

	List<ProdDeactivateResult> lPlanList = getCurrentSession().createSQLQuery(lQuery).setParameterList("planStatus", status).setParameter("planId", planId).setResultTransformer(new AliasToBeanResultTransformer(ProdDeactivateResult.class)).list();

	return lPlanList;

    }

    public List<Object[]> getActivePlanUserDetails(List<String> plansIds) {
	String query = "SELECT a.id, a.lead_id, b.peer_reviewers, b.dev_id, b.imp_status FROM imp_plan a left join implementation b on a.id=b.plan_id and b.active= 'Y'" + " where a.active = 'Y' and a.plan_status = :status";

	if (plansIds != null && !plansIds.isEmpty()) {
	    query = query + " and a.id in (:planIds) ";
	}
	Query lQuery = getCurrentSession().createSQLQuery(query).setParameter("status", Constants.PlanStatus.ACTIVE.name());
	if (plansIds != null && !plansIds.isEmpty()) {
	    lQuery.setParameterList("planIds", plansIds);
	}

	return lQuery.list();
    }

    /*
     * Created by : Vinoth Ponurangan Modified by : Dinesh Ramanathan Date
     * :08/12/2019 JIRA : 2037 Exporting RepoReport data
     */
    public List<Object[]> getSegemntDetailsBasedOnSystem(List<String> systemNameList, List<String> fileExtn, Date startDate, Date endDate) {
	StringBuilder lBuilder = new StringBuilder();
	lBuilder.append(" select program_name , case when (ref_status in ('Online', 'Fallback','DEPLOYED_IN_PRODUCTION')) then 'PROD' when (ref_status in ('ACTIVE')) then 'NON_SECURED' else 'SECURED' END as status , count(*) noofPlans  from ")
		.append(" (select aa.program_name, aa.ref_status,aa.ref_plan  from (select b.id as file_id, a.id as repo_Commit_id,  CASE WHEN ( c.id > 0 ) THEN c.id ELSE 0 END AS DELETE_ID, b.program_name,a.ref_status,a.ref_plan  from git.repo_commit a, git.repo_file_list b ")
		.append(" left join (SELECT rc.file_id, Max(rc.id) AS id FROM git.repo_commit rc, git.repo_file_list fl WHERE  fl.file_ext in (:fileExtn)  and fl.target_system  similar to (:systemNameList)  and rc.ref_load_date_time between :StartDate and :EndDate and rc.ref_status = 'Deleted' GROUP  BY file_id) as c on c.file_id = b.id  where a.sub_repo_id = b.sub_repo_id  and b.id = a.file_id   and b.file_ext in (:fileExtn)  and b.target_system  similar to (:systemNameList)  and a.ref_load_date_time between :StartDate and :EndDate  and a.ref_status in ('Online', 'Fallback')) as aa  where aa.repo_Commit_id> aa.DELETE_ID")
		.append(" union( select aa.program_name, aa.plan_status,aa.plan_id from ( select a1.program_name , replace(LOWER(SUBSTRING (a1.program_name FROM  '%#\".%#\"%' FOR '#')),'.','') as file_ext , a1.plan_status,a1.plan_id from  ").append(" ( select a.program_name, b.plan_status, a.id,a.plan_id from checkout_segments a, imp_plan b , system_load c , system d ,git.repo_file_list fl")
		.append(" where a.plan_id = c.plan_id and a.target_system = d.name and b.id = c.plan_id  and d.id = c.system_id and a.program_name=fl.program_name and a.active='Y' and b.active='Y' and c.active='Y' and d.active='Y'  and c.load_date_time between :StartDate and :EndDate and a.target_system similar to (:systemNameList) and fl.file_ext in (:fileExtn) and b.plan_status not in ('ONLINE', 'FALLBACK', 'DELETED','ONLINE_RELOAD')) as a1) as aa )) as bb  group by program_name, status ");

	List<Object[]> lPlanList = getCurrentSession().createSQLQuery(lBuilder.toString()).setParameterList("fileExtn", fileExtn).setParameter("systemNameList", (systemNameList == null || systemNameList.isEmpty()) ? "%%" : "%(" + String.join("|", systemNameList) + ")%").setParameter("StartDate", startDate).setParameter("EndDate", endDate).list();
	return lPlanList;
    }

    public List<ProductionLoadDetailsForm> getProductionLoadsBeforeDayDetails(String companyName) {

	String sb = "SELECT plan.id as planid, string_agg(DISTINCT plan.plan_desc, ', ')  as plandescription, string_agg(DISTINCT imp.dev_id, ', ') as developerid ," + " string_agg(DISTINCT imp.dev_name, ', ') as developername, string_agg(DISTINCT plan.dev_manager, ', ') as devmanagerid, string_agg(DISTINCT plan.dev_manager_name, ', ') as devmamangername ,"
		+ " string_agg(DISTINCT plan.load_type, ', ') as loadtype, string_agg(DISTINCT plan.plan_status, ', ') as planstatus , prod.activated_date_time as activateddatetime ," + " sys.name as targetsystem , load.prod_load_status as prodstatus , string_agg(DISTINCT seg.program_name, ', ') as programname ," + " load.load_date_time as loaddatetime " + " FROM production_loads prod , imp_plan plan , checkout_segments seg , system_load load ,"
		+ " system sys , implementation imp , platform plat " + " WHERE  prod.plan_id = plan.id  AND plan.id = seg.plan_id  AND seg.system_load = prod.system_load_id  AND load.plan_id = prod.plan_id " + " AND prod.system_load_id = load.id  AND sys.id = load.system_id AND sys.platform_id=plat.id  AND imp.plan_id = plan.id  AND imp.id = seg.imp_id AND load.prod_load_status IS NOT NULL "
		+ " AND prod.active = 'Y'   AND seg.active = 'Y'  AND plan.active = 'Y'  AND load.active = 'Y' AND plan.macro_header = (:macroHeader)  AND  Date(prod.activated_date_time) = date (Now() - interval '1 days')  AND plat.nick_name LIKE (:companyName) " + " GROUP BY plan.id , sys.name , load.prod_load_status , load.load_date_time , prod.activated_date_time  " + "ORDER BY plan.id ";

	List<ProductionLoadDetailsForm> planList = getCurrentSession().createSQLQuery(sb).setParameter("macroHeader", Boolean.FALSE).setParameter("companyName", companyName).setResultTransformer(new AliasToBeanResultTransformer(ProductionLoadDetailsForm.class)).list();
	return planList;

    }

    public List<ProductionLoadDetailsForm> getProductionLoadsNextDayDetails(String companyName) {

	String sb = "SELECT plan.id as planid, string_agg(DISTINCT plan.plan_desc, ', ')  as plandescription, string_agg(DISTINCT imp.dev_id, ', ') as developerid ," + " string_agg(DISTINCT imp.dev_name, ', ') as developername, string_agg(DISTINCT plan.dev_manager, ', ') as devmanagerid, string_agg(DISTINCT plan.dev_manager_name, ', ') as devmamangername ," + " string_agg(DISTINCT plan.load_type, ', ') as loadtype, string_agg(DISTINCT plan.plan_status, ', ') as planstatus , "
		+ " sys.name as targetsystem ,  string_agg(DISTINCT seg.program_name, ', ') as programname ," + " load.load_date_time as loaddatetime  ," + " CASE   WHEN load.prod_load_status IS NULL  THEN 'NOT_LOADED'  ELSE load.prod_load_status END as prodstatus " + " FROM imp_plan plan , checkout_segments seg , system_load load ," + " system sys , implementation imp , platform plat " + " WHERE  load.plan_id = plan.id  AND plan.id = seg.plan_id  "
		+ "  AND sys.id = load.system_id  AND imp.plan_id = plan.id  AND imp.id = seg.imp_id " + "  AND sys.platform_id=plat.id  AND seg.active = 'Y'  AND plan.active = 'Y'  AND load.active = 'Y' AND  Date(load.load_date_time) = date (Now() + interval '1 days') AND plan.macro_header = (:macroHeader)   AND load.prod_load_status IS NULL  AND plat.nick_name LIKE (:companyName)  " + " GROUP BY plan.id , sys.name , load.prod_load_status , load.load_date_time " + "ORDER BY plan.id ";

	List<ProductionLoadDetailsForm> planList = getCurrentSession().createSQLQuery(sb).setParameter("macroHeader", Boolean.FALSE).setParameter("companyName", companyName).setResultTransformer(new AliasToBeanResultTransformer(ProductionLoadDetailsForm.class)).list();
	return planList;

    }

    public List<Object[]> getUserReportPlans(Boolean isLeadRole, List<String> users, List<String> planStatus, List<String> systemNameList, Date startDate, Date endDate) {
	String lQuery = "select " + (isLeadRole ? "ip.lead_name" : "ip.dev_manager_name") + " as leadName, ip.id as planId, ip.plan_status as planStatus from imp_plan ip, system_load sl, system s " + " where ip.plan_status in (:PlanStatus) and ip.macro_header = false " + " and sl.plan_id = ip.id" + " and sl.system_id = s.id" + " and s.name in (:SystemNameList)" + " and s.active = 'Y'" + " and sl.active = 'Y'" + " and sl.load_date_time between :StartDate and :EndDate";
	String role = isLeadRole ? "ip.lead_id" : "ip.dev_manager";
	lQuery = lQuery + " and " + role + " in (:UsersList)";
	return getCurrentSession().createSQLQuery(lQuery).setParameterList("PlanStatus", planStatus).setParameterList("SystemNameList", systemNameList).setParameterList("UsersList", users).setParameter("StartDate", startDate).setParameter("EndDate", endDate).list();
    }

    public List<ReportModel> getFuncAndSegmentsCount(List<String> planStatus, List<String> targetSystems, List<String> funcAreas, Date startDate, Date endDate) {
	String lQuery = "select cs.target_system as systemName, cs.func_area as funcArea, ip.id as planId, ip.plan_status as planStatus, count(cs.program_name) as segmentsCount from imp_plan ip, system_load sl, checkout_segments cs " + " where ip.id = sl.plan_id" + " and sl.active = 'Y'" + " and ip.id = cs.plan_id" + " and sl.id = cs.system_load" + " and cs.active = 'Y'" + " and ip.plan_status in (:PlanStatus)" + " and cs.func_area similar to :FuncArea"
		+ " and cs.target_system in (:TargetSystem)" + " and sl.load_date_time between :StartDate and :EndDate and ip.macro_header = false " + " group by cs.target_system, cs.func_area, ip.plan_status, ip.id";

	List<Object[]> lObjects = getCurrentSession().createSQLQuery(lQuery).setParameterList("PlanStatus", planStatus).setParameter("FuncArea", (funcAreas == null || funcAreas.isEmpty()) ? "%%" : "%(" + String.join("|", funcAreas) + ")%").setParameterList("TargetSystem", targetSystems).setParameter("StartDate", startDate).setParameter("EndDate", endDate).list();

	List<ReportModel> lReturnData = new ArrayList();
	lObjects.stream().map((lObject) -> {
	    ReportModel lReport = new ReportModel();
	    lReport.setSystemName(lObject[0].toString());
	    lReport.setFuncArea(lObject[1].toString());
	    lReport.setPlanId(lObject[2].toString());
	    lReport.setPlanStatus(lObject[3].toString());
	    lReport.setSegmentsCount(Integer.parseInt(lObject[4].toString()));
	    return lReport;
	}).forEachOrdered((lReport) -> {
	    lReturnData.add(lReport);
	});
	return lReturnData;
    }

    public Set<String> getPostDependentPlanList(String planId) {
	Set<String> postDependentList = new HashSet<>();
	ImpPlan load = (ImpPlan) getCurrentSession().load(ImpPlan.class, planId);
	String relatedPlans = load.getRelatedPlans();

	Set<String> lPlanStatusList = new TreeSet();
	lPlanStatusList = Constants.PlanStatus.getSecuredStatusMap().keySet();

	if (relatedPlans != null && !relatedPlans.isEmpty()) {
	    List<String> split = Arrays.asList(relatedPlans.split(","));
	    List<Object[]> relatedPlanDetails = getRelatedPlanDetail(planId, split);
	    for (Object[] plan : relatedPlanDetails) {
		String planStatus = plan[1].toString();
		if (!(lPlanStatusList.contains(planStatus))) {
		    postDependentList.add(plan[0].toString());
		}
	    }
	}
	return lPlanStatusList;
    }

    /*
     * JIRA Ticket: ZTPFM-2038 Created By: Radhakrishnan Created Dt: 06-Aug-2019
     * Description: Get the list of ONLINE/FALLBACK plans which has load date
     * between the range
     */
    public List<ImpPlan> getPlanByLoadDateRange(List<String> devMangers, List<String> status, Date startDate, Date endDate) {
	String lQuery = "SELECT ip FROM ImpPlan ip " + " WHERE ip.planStatus IN (:Status)" + " AND ip.id IN (SELECT DISTINCT sl.planId.id FROM SystemLoad sl WHERE sl.active = 'Y' AND (sl.loadDateTime BETWEEN :StartDate AND :EndDate ))" + " AND ip.macroHeader = false" + " AND ip.devManager IN :DevManagers";

	List<ImpPlan> lPlanList = getCurrentSession().createQuery(lQuery).setParameterList("Status", status).setParameter("StartDate", startDate).setParameter("EndDate", endDate).setParameterList("DevManagers", devMangers).list();
	return lPlanList;
    }

    public void resetRfcMailFlag() {
	String lQuery = "UPDATE imp_plan SET rfc_mail_flag='false' WHERE rfc_mail_flag='true'";
	getCurrentSession().createSQLQuery(lQuery).executeUpdate();
    }

    /**
     * ZTPFM-2533 get plan status by implementation
     *
     * @param impId
     * @return planStatus
     */
    public String getPlanStatusByImp(String impId) {
	String lQuery = "select plan.plan_status from imp_plan plan, implementation imp where plan.id=imp.plan_id and imp.active='Y' " + "and imp.id in (:impid)";
	return (String) getCurrentSession().createSQLQuery(lQuery).setParameter("impid", impId).list().get(0);
    }

    /**
     * ZTPFM-2502 get the list of plans which are in Accepted in Production
     *
     */
    public List<ImpPlan> getAcceptedInProductionPlans(Integer offset, Integer limit, Map<String, String> orderBy, String filter) {
	String lQuery = "SELECT ip from ImpPlan ip where ip.planStatus = :PlanStatus and ip.active = 'Y' and ip.macroHeader = false";
	if (filter != null && !filter.isEmpty()) {
	    lQuery = lQuery + " and ip.id like '%" + filter.toUpperCase() + "%'";
	}
	StringBuilder lOrderString = new StringBuilder(" order by ");
	if (orderBy != null && !orderBy.isEmpty()) {
	    for (Map.Entry<String, String> orders : orderBy.entrySet()) {
		lOrderString.append(" ip.").append(orders.getKey()).append(" ").append(orders.getValue());
	    }
	} else {
	    lOrderString.append(" ip.tpfAcceptedDateTime asc");
	}
	List<ImpPlan> lReturnPlanList = getCurrentSession().createQuery(lQuery + lOrderString.toString()).setParameter("PlanStatus", Constants.PlanStatus.ACCEPTED_IN_PRODUCTION.name()).setFirstResult(offset * limit).setMaxResults(limit).list();
	return lReturnPlanList;
    }

    public ImpPlan getAcceptedInProductionPlan(List<String> planIds) {
	String lQuery = "SELECT ip from ImpPlan ip where ip.planStatus = :PlanStatus and ip.active = 'Y' and ip.macroHeader = false and ip.id in (:PlanList) order by ip.tpfAcceptedDateTime asc";

	ImpPlan lReturnPlanList = (ImpPlan) getCurrentSession().createQuery(lQuery).setParameter("PlanStatus", Constants.PlanStatus.ACCEPTED_IN_PRODUCTION.name()).setParameterList("PlanList", planIds).setMaxResults(1).uniqueResult();
	return lReturnPlanList;
    }

    public List<ImpPlan> getAcceptedInProductionPlan() {
	String lQuery = "SELECT ip from ImpPlan ip where ip.planStatus = :PlanStatus and ip.active = 'Y' and ip.macroHeader = false order by ip.tpfAcceptedDateTime asc";

	List<ImpPlan> lReturnPlanList = getCurrentSession().createQuery(lQuery).setParameter("PlanStatus", Constants.PlanStatus.ACCEPTED_IN_PRODUCTION.name()).list();
	return lReturnPlanList;
    }

    public Long getAcceptedInProductionPlansCount() {
	String lQuery = "SELECT count(ip) from ImpPlan ip where ip.planStatus = :PlanStatus and ip.active = 'Y' and ip.macroHeader = false";

	Long lReturnPlanListCount = (Long) getCurrentSession().createQuery(lQuery).setParameter("PlanStatus", Constants.PlanStatus.ACCEPTED_IN_PRODUCTION.name()).uniqueResult();
	return lReturnPlanListCount;
    }

    /**
     * ZTPFM-2502 check, is there any OnlineFeedback in progress
     *
     */
    public boolean isOnlineFeedbackInprogress() {
	String lQuery = " SELECT ip from ImpPlan ip where ip.planStatus = :PlanStatus and ip.active = 'Y' and ip.tpfAcceptedDateTime is NOT null and ip.inprogressStatus = :InprogressStatus";
	List<ImpPlan> lReturnPlans = getCurrentSession().createQuery(lQuery).setParameter("PlanStatus", Constants.PlanStatus.ACCEPTED_IN_PRODUCTION.name()).setParameter("InprogressStatus", Constants.PlanInProgressStatus.ONLINE.name()).list();
	if (lReturnPlans == null || lReturnPlans.isEmpty()) {
	    return false;
	}
	return true;
    }

    /**
     * Build Queue
     */
    public List<BuildQueueForm> getBuildQueuePlan(User pUser, Integer offset, Integer limit, String filter, Map<String, String> orderBy) {
	String sb = "SELECT b.plan_id as planid, " + " sys.NAME as targetsystem, " + " b.build_type as buildtype, " + " b.tdx_running_status as buildstatus, " + " CASE WHEN ( b.job_status IN ( 'P' ) AND  b.tdx_running_status in ('In progress') ) THEN  to_char(b.build_date_time, 'YYYY-MM-DD HH24:MI:SS')  " + " ELSE CASE   WHEN ( b.build_number < 0 " + "  AND b.job_status IN ( 'P' ) ) THEN '' " + " END " + " END AS buildstartdate, " + "  split_part(b.jenkins_url,':', 2)    AS servername, "
		+ " CASE WHEN (b.created_dt is null) THEN '' " + " ELSE to_char(b.created_dt, 'YYYY-MM-DD HH24:MI:SS') END AS buildtriggerdate " + "FROM build b, system sys " + "WHERE  sys.id = b.system_id " + "  AND b.active = 'Y' " + "  AND b.job_status = 'P' " + " AND b.build_type IN ( 'DVL_BUILD', 'STG_BUILD' ) and b.tdx_running_status <> :RunningStatus";
	if (filter != null && !filter.isEmpty()) {
	    sb = sb + " and split_part(b.jenkins_url,':', 2)  like '%" + filter.toLowerCase() + "%'";
	}
	sb = sb + " order by buildstatus asc , b.created_dt ASC ";
	List<BuildQueueForm> planList = getCurrentSession().createSQLQuery(sb).setParameter("RunningStatus", Constants.TDXRunningStatus.COMPLETED.getTDXRunningStatus()).setResultTransformer(new AliasToBeanResultTransformer(BuildQueueForm.class)).list();
	return planList;

    }

    public List<ImpPlan> findPlansByLoadDateTime(List<String> status, String id, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy, String planFilter) {
	StringBuffer query = new StringBuffer();
	query.append("SELECT PLAN ");
	query.append("FROM SystemLoad sysLoad ");
	query.append(",ImpPlan PLAN ");
	query.append("WHERE sysLoad.active = 'Y' ");
	query.append("AND sysLoad.loadDateTime IS NOT NULL ");
	query.append("AND sysLoad.planId.id = PLAN.id ");
	query.append("AND PLAN.active='Y' ");
	if (status != null && !status.isEmpty()) {
	    query.append("AND PLAN.planStatus in(:PlanStatus) ");
	}
	if (id != null && !id.isEmpty()) {
	    query.append("AND PLAN.leadId = :LeadId ");
	}
	if (planFilter != null && !planFilter.isEmpty()) {
	    query.append("AND PLAN.id  Like '%" + planFilter.toUpperCase() + "%'");
	}
	query.append("GROUP BY PLAN ");

	if (pOrderBy != null && !pOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> order : pOrderBy.entrySet()) {
		if (order.getKey().equals("loaddatetime") && order.getValue().equals("asc")) {
		    query.append("ORDER BY MIN(sysLoad.loadDateTime) ASC ");
		} else if (order.getKey().equals("loaddatetime") && order.getValue().equals("desc")) {
		    query.append("ORDER BY MIN(sysLoad.loadDateTime) DESC ");
		}

	    }
	}
	Query lQuery = getCurrentSession().createQuery(query.toString());
	if (status != null && !status.isEmpty()) {
	    lQuery.setParameterList("PlanStatus", status);
	}
	if (id != null && !id.isEmpty()) {
	    lQuery.setParameter("LeadId", id);
	}

	return lQuery.setFirstResult(pOffset * pLimit).setMaxResults(pLimit).list();
    }

    public List<ImpPlan> getPlanDetailsActiveToOnline() {
	String lQuery = "SELECT ip from ImpPlan ip where ip.planStatus in (:PlanStatus) and ip.active = 'Y' and ip.leadEmail is null  ";
	List<ImpPlan> lReturnPlanList = getCurrentSession().createQuery(lQuery).setParameterList("PlanStatus", Constants.PlanStatus.getPlanStatusActiveToOnline().keySet()).list();
	return lReturnPlanList;
    }

    public List<Object[]> getSegmentListByCompany(String company) {
	StringBuilder sb = new StringBuilder();
	sb.append("select  distinct seg.id , repo.repo_description from checkout_segments seg,imp_plan plan , git.repo_detail repo , system_load load , system sys , platform plat where plan.id=seg.plan_id  and load.id=seg.system_load ");
	sb.append("and load.system_id=sys.id and load.plan_id=plan.id and  plat.id=sys.platform_id and load.active='Y' ");
	sb.append("and plat.nick_name=repo.company ");
	sb.append("and seg.active='Y'  and seg.repo_desc is  null and repo.repo_description is not null ");
	sb.append("and repo.func_area=seg.func_area ");
	sb.append("and repo.company= :company ");
	sb.append("and plan.plan_status not in('ONLINE','FALLBACK','DELETED')");
	List<Object[]> lSegList = getCurrentSession().createSQLQuery(sb.toString()).setParameter("company", company).list();
	return lSegList;
    }

    public List<ImpPlan> getSubmittedPlansForGitOperation(Date startDate, Date endDate) {
	String lQuery = "SELECT ip from ImpPlan ip" + " where ip.planStatus in (:PlanStatus) " + " and ip.loadType = :LoadType" + " and ip.approveDateTime is not null" + " and (ip.approveDateTime between :StartDate and :EndDate)" + " and ip.active = 'Y'";
	List<ImpPlan> lReturnPlanList = getCurrentSession().createQuery(lQuery).setParameter("LoadType", Constants.LoadTypes.STANDARD.name()).setParameter("StartDate", startDate).setParameter("EndDate", endDate).setParameterList("PlanStatus", Constants.PlanStatus.getApprovedAndAboveStatus().keySet()).list();
	return lReturnPlanList;
    }
}
