package com.tsi.workflow.dao;

import com.tsi.workflow.User;
import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ProductionLoads;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.LOAD_SET_STATUS;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class ProductionLoadsDAO extends BaseDAO<ProductionLoads> {

    public List<ProductionLoads> findByPlanId(String pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<ProductionLoads> findByPlanId(List<ImpPlan> pIds) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("planId", pIds));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<ProductionLoads> findByPlanId(ImpPlan pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<ProductionLoads> findByPlanId(String[] pId) throws WorkflowException {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : pId) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("planId", lImpPlan));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<ProductionLoads> findBySystemId(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", new System(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<ProductionLoads> findBySystemId(List<System> pIds) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("systemId", pIds));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<ProductionLoads> findBySystemId(System pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<ProductionLoads> findBySystemId(Integer[] pId) throws WorkflowException {
	List<System> lSystem = new ArrayList<>();
	for (Integer lId : pId) {
	    lSystem.add(new System(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("systemId", lSystem));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<ProductionLoads> findBySystemLoadId(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemLoadId", new SystemLoad(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<ProductionLoads> findBySystemLoadId(List<SystemLoad> pIds) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("systemLoadId", pIds));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<ProductionLoads> findBySystemLoadId(SystemLoad pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemLoadId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<ProductionLoads> findBySystemLoadId(Integer[] pId) throws WorkflowException {
	List<SystemLoad> lSystemLoad = new ArrayList<>();
	for (Integer lId : pId) {
	    lSystemLoad.add(new SystemLoad(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("systemLoadId", lSystemLoad));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public Long getAllActivated(String pPlanId) {
	String lQuery = "select count(plan.id) from production_loads loads, system_load sysload, imp_plan plan" + " where plan.id like ?" + " and plan.accepted_date_time is null" + " and plan.fallback_date_time is null" + " and loads.cpu_id is null" + " and loads.status like '" + LOAD_SET_STATUS.ACTIVATED.name() + "'" + " and loads.last_action_status like 'SUCCESS'" + " and loads.active = 'Y'" + " and plan.id = loads.plan_id" + " and sysload.id = loads.system_load_id"
		+ " and sysload.active = 'Y'" + " and sysload.prod_load_status like '" + Constants.PROD_LOAD_STATUS.ACTIVATED_ON_ALL_CPU.name() + "'";
	Long lCount = Long.parseLong(getCurrentSession().createSQLQuery(lQuery).setParameter(0, pPlanId).uniqueResult().toString());
	return lCount;
    }

    public Long getAllDeActivated(String pPlanId) {
	String lQuery = "select count(plan.id) from production_loads loads, system_load sysload, imp_plan plan" + " where plan.id like ?" + " and plan.accepted_date_time is null" + " and plan.fallback_date_time is null" + " and loads.cpu_id is null" + " and loads.status like '" + LOAD_SET_STATUS.DEACTIVATED.name() + "'" + " and loads.last_action_status like 'SUCCESS'" + " and loads.active = 'Y'" + " and plan.id = loads.plan_id" + " and sysload.id = loads.system_load_id"
		+ " and sysload.active = 'Y'";
	Long lCount = Long.parseLong(getCurrentSession().createSQLQuery(lQuery).setParameter(0, pPlanId).uniqueResult().toString());
	return lCount;
    }

    public Long getAllAccepted(String pPlanId) {
	String lQuery = "select count(plan.id) from production_loads loads, system_load sysload, imp_plan plan" + " where plan.id like ?" + " and plan.plan_status like '" + Constants.PlanStatus.DEPLOYED_IN_PRODUCTION.name() + "'" + " and plan.accepted_date_time is null" + " and plan.fallback_date_time is null" + " and loads.cpu_id is null" + " and loads.status like '" + LOAD_SET_STATUS.ACCEPTED.name() + "'" + " and loads.last_action_status like 'SUCCESS'" + " and loads.active = 'Y'"
		+ " and plan.id = loads.plan_id" + " and sysload.id = loads.system_load_id" + " and sysload.active = 'Y'" + " and sysload.prod_load_status like '" + Constants.PROD_LOAD_STATUS.ACCEPTED.name() + "'";
	Long lCount = Long.parseLong(getCurrentSession().createSQLQuery(lQuery).setParameter(0, pPlanId).uniqueResult().toString());
	return lCount;
    }

    public Long getAllFallBackActivated(String pPlanId) {
	String lQuery = "select count(plan.id) from production_loads loads, system_load sysload, imp_plan plan" + " where plan.id like ?" + " and plan.plan_status like '" + Constants.PlanStatus.ONLINE.name() + "'" + " and plan.accepted_date_time is not null" + " and plan.fallback_date_time is null" + " and loads.cpu_id is null" + " and loads.status like '" + LOAD_SET_STATUS.FALLBACK_ACTIVATED.name() + "'" + " and loads.last_action_status like 'SUCCESS'" + " and loads.active = 'Y'"
		+ " and plan.id = loads.plan_id" + " and sysload.id = loads.system_load_id" + " and sysload.active = 'Y'" + " and sysload.prod_load_status like '" + Constants.PROD_LOAD_STATUS.FALLBACK_ACTIVATED.name() + "'";

	Long lCount = Long.parseLong(getCurrentSession().createSQLQuery(lQuery).setParameter(0, pPlanId).uniqueResult().toString());
	return lCount;
    }

    public Long getAllFallBackAccepted(String pPlanId) {
	String lQuery = "select count(plan.id) from production_loads loads, system_load sysload, imp_plan plan" + " where plan.id like ?" + " and plan.plan_status in ('" + Constants.PlanStatus.ONLINE.name() + "','" + Constants.PlanStatus.FALLBACK_DEPLOYED_IN_PRODUCTION.name() + "')" + " and plan.accepted_date_time is not null" + " and plan.fallback_date_time is null" + " and loads.cpu_id is null" + " and loads.status like '" + LOAD_SET_STATUS.FALLBACK_ACCEPTED.name() + "'"
		+ " and loads.last_action_status like 'SUCCESS'" + " and loads.active = 'Y'" + " and plan.id = loads.plan_id" + " and sysload.id = loads.system_load_id" + " and sysload.active = 'Y'" + " and sysload.prod_load_status like '" + Constants.PROD_LOAD_STATUS.FALLBACK_ACCEPTED.name() + "'";

	Long lCount = Long.parseLong(getCurrentSession().createSQLQuery(lQuery).setParameter(0, pPlanId).uniqueResult().toString());
	return lCount;
    }
    // TO Accept

    public List<ProductionLoads> findTobeLoaded(Map<String, String> orderBy) {
	StringBuilder lQuery = new StringBuilder("SELECT loads FROM ProductionLoads loads, SystemLoad sysload").append(" WHERE loads.planId.planStatus LIKE ?").append(" AND loads.planId.acceptedDateTime IS NULL").append(" AND loads.planId.fallbackDateTime IS NULL").append(" AND loads.cpuId IS NULL").append(" AND loads.active = 'Y'").append(" AND sysload.id = loads.systemLoadId.id").append(" AND sysload.active = 'Y'");

	if (orderBy != null && !orderBy.isEmpty()) {
	    lQuery.append(" ORDER BY ");
	    for (Map.Entry<String, String> entrySet : orderBy.entrySet()) {
		String key = entrySet.getKey();
		String value = entrySet.getValue();
		lQuery.append(key).append(" ").append(value.toUpperCase());
	    }
	}

	List<ProductionLoads> lPlanList = getCurrentSession().createQuery(lQuery.toString()).setParameter(0, Constants.PlanStatus.DEPLOYED_IN_PRODUCTION.name()).list();
	return lPlanList;
    }
    // To Fallback Accept

    public List<ProductionLoads> findTobeFallbackLoaded(Map<String, String> orderBy) {
	StringBuilder lQuery = new StringBuilder("SELECT loads FROM ProductionLoads loads, SystemLoad sysload").append(" WHERE loads.planId.planStatus LIKE ?").append(" AND loads.planId.acceptedDateTime IS NOT NULL").append(" AND loads.planId.fallbackDateTime IS NULL").append(" AND loads.cpuId IS NULL").append(" AND loads.active = 'Y'").append(" AND sysload.id = loads.systemLoadId.id").append(" AND sysload.active = 'Y'");

	if (orderBy != null && !orderBy.isEmpty()) {
	    lQuery.append(" ORDER BY ");
	    for (Map.Entry<String, String> entrySet : orderBy.entrySet()) {
		String key = entrySet.getKey();
		String value = entrySet.getValue();
		lQuery.append(key).append(" ").append(value.toUpperCase());
	    }
	}

	List<ProductionLoads> lPlanList = getCurrentSession().createQuery(lQuery.toString()).setParameter(0, Constants.PlanStatus.FALLBACK_DEPLOYED_IN_PRODUCTION.name()).list();
	return lPlanList;
    }

    public List<ProductionLoads> findByPlanId(String[] ids, System system) {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : ids) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("planId", lImpPlan));
	expressions.add(Restrictions.eq("systemId", system));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<ProductionLoads> findByFallbackPlanId(String[] ids, System system) {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : ids) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("planId", lImpPlan));
	expressions.add(Restrictions.eq("systemId", system));
	expressions.add(Restrictions.isNull("cpuId"));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public ProductionLoads findByPlanId(ImpPlan lImpPlan, System system) {
	String lQuery = "SELECT prodLoad FROM ProductionLoads prodLoad" + " WHERE prodLoad.planId = :PlanId" + " AND prodLoad.active LIKE 'Y'" + " AND prodLoad.systemId = :systemId" + " AND prodLoad.cpuId IS NULL";
	ProductionLoads lPlanList = (ProductionLoads) getCurrentSession().createQuery(lQuery).setParameter("PlanId", lImpPlan).setParameter("systemId", system).uniqueResult();
	return lPlanList;
    }

    public List<ProductionLoads> findByPlanIdWithCpu(ImpPlan lImpPlan, System system) {
	String lQuery = "SELECT prodLoad FROM ProductionLoads prodLoad" + " WHERE prodLoad.planId = :PlanId" + " AND prodLoad.active LIKE 'Y'" + " AND prodLoad.systemId = :systemId";
	List<ProductionLoads> lPlanList = getCurrentSession().createQuery(lQuery).setParameter("PlanId", lImpPlan).setParameter("systemId", system).list();
	return lPlanList;
    }

    public List<ProductionLoads> findALLByPlanId(ImpPlan lImpPlan, System system) {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("planId", lImpPlan));
	expressions.add(Restrictions.eq("systemId", system));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<ProductionLoads> findAllLastSuccessfulBuild(String pPlanId) {
	String lQuery = "SELECT prodLoad FROM ProductionLoads prodLoad" + " WHERE prodLoad.planId.id = :PlanId" + " AND prodLoad.active LIKE 'Y'" + " AND prodLoad.lastActionStatus LIKE 'SUCCESS'" + " ORDER BY prodLoad.id DESC";

	List<ProductionLoads> lBuild = getCurrentSession().createQuery(lQuery).setParameter("PlanId", pPlanId).list();
	return lBuild;
    }

    public List<Object[]> getLoadsCountByLoadStatus(String pPlanId, Integer pSystemId) {
	String lQuery = "SELECT prodLoad.status, COUNT(*) FROM ProductionLoads prodLoad" + " WHERE prodLoad.planId.id = :PlanId" + " AND prodLoad.systemId.id = :SystemId" + " AND prodLoad.cpuId is not NULL" + " AND prodLoad.active LIKE 'Y' GROUP BY prodLoad.status";

	List<Object[]> lBuild = getCurrentSession().createQuery(lQuery).setParameter("PlanId", pPlanId).setParameter("SystemId", pSystemId).list();
	return lBuild;
    }

    public List<Object[]> getAllLoadsCountByLoadStatus(String pPlanId, Integer pSystemId) {
	String lQuery = "SELECT prodLoad.status, COUNT(*) FROM ProductionLoads prodLoad" + " WHERE prodLoad.planId.id = :PlanId" + " AND prodLoad.systemId.id = :SystemId" + " AND prodLoad.cpuId is NULL" + " AND prodLoad.active LIKE 'Y' GROUP BY prodLoad.status";

	List<Object[]> lBuild = getCurrentSession().createQuery(lQuery).setParameter("PlanId", pPlanId).setParameter("SystemId", pSystemId).list();
	return lBuild;
    }

    public Long findLoadSetInProgress(ImpPlan planId, LOAD_SET_STATUS pStatus) {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("planId", planId));
	expressions.add(Restrictions.eq("active", "Y"));
	expressions.add(Restrictions.eq("status", pStatus.name()));
	expressions.add(Restrictions.eq("lastActionStatus", "INPROGRESS"));
	return count(expressions);
    }

    public List<ProductionLoads> findPlanLoadSet(ImpPlan planId, LOAD_SET_STATUS pStatus, List<String> lastActionStatus) {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("planId", planId));
	expressions.add(Restrictions.eq("active", "Y"));
	expressions.add(Restrictions.eq("status", pStatus.name()));
	expressions.add(Restrictions.in("lastActionStatus", lastActionStatus));
	return findAll(expressions);
    }

    public void updateAllDeActivated(User user, ProductionLoads lLoad) {
	String lQuery = "UPDATE production_loads SET status = ? " + " WHERE plan_id = ?" + " AND system_load_id = ?" + " AND system_id = ?" + " AND cpu_id IS NOT NULL" + " AND active = 'Y'";

	getCurrentSession().createSQLQuery(lQuery).setParameter(0, Constants.LOAD_SET_STATUS.DEACTIVATED.name()).setParameter(1, lLoad.getPlanId().getId()).setParameter(2, lLoad.getSystemLoadId().getId()).setParameter(3, lLoad.getSystemId().getId()).executeUpdate();
    }

    public void updateAllActivated(User user, ProductionLoads lLoad) {
	String lQuery = "UPDATE production_loads SET status = ? , de_activated_date_time = null " + " WHERE plan_id = ?" + " AND system_load_id = ?" + " AND system_id = ?" + " AND cpu_id IS NOT NULL" + " AND active = 'Y'";

	getCurrentSession().createSQLQuery(lQuery).setParameter(0, Constants.LOAD_SET_STATUS.ACTIVATED.name()).setParameter(1, lLoad.getPlanId().getId()).setParameter(2, lLoad.getSystemLoadId().getId()).setParameter(3, lLoad.getSystemId().getId()).executeUpdate();
    }

    public ProductionLoads findByLoadset(Integer id, String loadset) {
	String lQuery = "SELECT prodLoad FROM ProductionLoads prodLoad, SystemLoad load" + " WHERE prodLoad.systemLoadId = load.id" + " AND prodLoad.active LIKE 'Y'" + " AND load.active LIKE 'Y'" + " AND (load.loadSetName LIKE :loadset" + " OR load.fallbackLoadSetName LIKE :loadset)" + " AND prodLoad.id = :ID";
	ProductionLoads lPlanList = (ProductionLoads) getCurrentSession().createQuery(lQuery).setParameter("ID", id).setParameter("loadset", loadset).uniqueResult();
	return lPlanList;
    }

    public List<ProductionLoads> findByPlanIdAndSystemId(ImpPlan lImpPlan, System system) {
	String lQuery = "SELECT pl FROM ProductionLoads pl" + " WHERE pl.planId = :PlanId" + " AND pl.active LIKE 'Y'" + " AND pl.systemId = :systemId";
	List<ProductionLoads> lPlanList = getCurrentSession().createQuery(lQuery).setParameter("PlanId", lImpPlan).setParameter("systemId", system).list();
	return lPlanList;
    }

    public List<ProductionLoads> findInfoByPlanAndSystemId(ImpPlan lImpPlan, System system) {
	String lQuery = "SELECT pl FROM ProductionLoads pl" + " WHERE pl.planId = :PlanId" + " AND pl.active LIKE 'Y'" + " AND pl.systemId = :systemId" + " AND pl.cpuId is null";
	List<ProductionLoads> lPlanList = getCurrentSession().createQuery(lQuery).setParameter("PlanId", lImpPlan).setParameter("systemId", system).list();
	return lPlanList;
    }

    public List<ProductionLoads> getProdLoadDeactivatedPlans(Date date) {
	String lQuery = "SELECT prod FROM ProductionLoads prod where prod.active='Y' and prod.planId in (SELECT pl.planId FROM ProductionLoads pl where pl.active = 'Y' and pl.deActivatedDateTime is not null and pl.deActivatedDateTime <= :dateTime and pl.status = :status)";
	List<ProductionLoads> lPlanList = getCurrentSession().createQuery(lQuery).setParameter("dateTime", date).setParameter("status", Constants.LOAD_SET_STATUS.DEACTIVATED.name()).list();
	return lPlanList;
    }

    public List<ProductionLoads> getRCatPlansProdLoads(List<String> planStatus, List<String> prodLoadStatus) {
	String lQuery = "select pl from SystemLoad sl, LoadCategories lc, ProductionLoads pl, ImpPlan ip" + " where ip.id = sl.planId.id" + " and sl.loadCategoryId.id = lc.id" + " and lc.name = 'R'" + " and sl.active = 'Y'" + " and lc.active = 'Y'" + " and ip.id = pl.planId.id" + " and pl.systemLoadId.id = sl.id" + " and pl.status in (:ProdLoadStatus)" + " and pl.active = 'Y'" + " and pl.cpuId is null" + " and ip.planStatus in (:PlanStatus)";
	List<ProductionLoads> lPlanList = getCurrentSession().createQuery(lQuery).setParameterList("PlanStatus", planStatus).setParameterList("ProdLoadStatus", prodLoadStatus).list();

	return lPlanList;
    }

    public List<ProductionLoads> findInfoByPlanId(String planId) {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("planId", new ImpPlan(planId)));
	expressions.add(Restrictions.isNull("cpuId"));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public void updateAllAccepted(User user, ProductionLoads lLoad) {
	String lQuery = "UPDATE production_loads SET status = ? WHERE plan_id = ? AND system_load_id = ? AND system_id = ? AND cpu_id IS NOT NULL AND active = 'Y'";

	getCurrentSession().createSQLQuery(lQuery).setParameter(0, Constants.LOAD_SET_STATUS.ACCEPTED.name()).setParameter(1, lLoad.getPlanId().getId()).setParameter(2, lLoad.getSystemLoadId().getId()).setParameter(3, lLoad.getSystemId().getId()).executeUpdate();
    }
}
