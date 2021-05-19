package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.PreProductionLoads;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class PreProductionLoadsDAO extends BaseDAO<PreProductionLoads> {

    public List<PreProductionLoads> findByPlanId(String pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<PreProductionLoads> findByPlanId(List<ImpPlan> pIds) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("planId", pIds));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<PreProductionLoads> findByPlanId(ImpPlan pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<PreProductionLoads> findByPlanId(String[] pId) throws WorkflowException {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : pId) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("planId", lImpPlan));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<PreProductionLoads> findByPreProdLoadPlanId(String[] pId) throws WorkflowException {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : pId) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("planId", lImpPlan));
	expressions.add(Restrictions.isNotNull("systemLoadActionsId"));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<PreProductionLoads> findBySystemId(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", new System(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<PreProductionLoads> findBySystemId(List<System> pIds) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("systemId", pIds));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<PreProductionLoads> findBySystemId(System pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<PreProductionLoads> findBySystemId(Integer[] pId) throws WorkflowException {
	List<System> lSystem = new ArrayList<>();
	for (Integer lId : pId) {
	    lSystem.add(new System(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("systemId", lSystem));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<PreProductionLoads> findBySystemLoadId(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemLoadId", new SystemLoad(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<PreProductionLoads> findBySystemLoadId(List<SystemLoad> pIds) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("systemLoadId", pIds));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<PreProductionLoads> findBySystemLoadId(SystemLoad pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemLoadId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<PreProductionLoads> findBySystemLoadId(Integer[] pId) throws WorkflowException {
	List<SystemLoad> lSystemLoad = new ArrayList<>();
	for (Integer lId : pId) {
	    lSystemLoad.add(new SystemLoad(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("systemLoadId", lSystemLoad));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public PreProductionLoads findByPlanId(ImpPlan lImpPlan, System system) {
	String lQuery = "SELECT prodLoad FROM ProductionLoads prodLoad" + " WHERE prodLoad.planId = :PlanId" + " AND prodLoad.active LIKE 'Y'" + " AND prodLoad.systemId = :systemId" + " AND prodLoad.cpuId IS NULL";
	PreProductionLoads lPlanList = (PreProductionLoads) getCurrentSession().createQuery(lQuery).setParameter("PlanId", lImpPlan).setParameter("systemId", system).uniqueResult();
	return lPlanList;
    }

    public List<PreProductionLoads> findByPlanIdAndSystemId(ImpPlan lImpPlan, System system) {
	String lQuery = "SELECT ppl FROM PreProductionLoads ppl" + " WHERE ppl.planId = :PlanId" + " AND ppl.active LIKE 'Y'" + " AND ppl.systemId = :systemId";
	List<PreProductionLoads> lPlanList = getCurrentSession().createQuery(lQuery).setParameter("PlanId", lImpPlan).setParameter("systemId", system).list();
	return lPlanList;
    }

    public PreProductionLoads findByLoadset(Integer id, String loadset) {
	String lQuery = "SELECT prodLoad FROM PreProductionLoads prodLoad, SystemLoad load" + " WHERE prodLoad.systemLoadId = load.id" + " AND prodLoad.active LIKE 'Y'" + " AND load.active LIKE 'Y'" + " AND (load.loadSetName LIKE :loadset" + " OR load.fallbackLoadSetName LIKE :loadset)"
	// + " AND prodLoad.cpuId IS NULL"
		+ " AND prodLoad.id = :ID";
	PreProductionLoads lPlanList = (PreProductionLoads) getCurrentSession().createQuery(lQuery).setParameter("ID", id).setParameter("loadset", loadset).uniqueResult();
	return lPlanList;
    }

    public List<PreProductionLoads> findBySystemLoadAndStatus(List<SystemLoad> systemLoads, List<String> status) {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("systemLoadId", systemLoads));
	expressions.add(Restrictions.in("status", status));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public PreProductionLoads findByPlanIdByLoad(ImpPlan lImpPlan, System system) {
	String lQuery = "SELECT prodLoad FROM PreProductionLoads prodLoad" + " WHERE prodLoad.planId = :PlanId" + " AND prodLoad.active LIKE 'Y'" + " AND prodLoad.systemId = :systemId" + " AND prodLoad.systemLoadActionsId IS NOT NULL";
	PreProductionLoads lPlanList = (PreProductionLoads) getCurrentSession().createQuery(lQuery).setParameter("PlanId", lImpPlan).setParameter("systemId", system).uniqueResult();
	return lPlanList;
    }

    public PreProductionLoads findByPlanIdAndSystemAndCpu(String lImpPlanId, Integer systemId, Integer cpuId) {
	String lQuery = "SELECT prodLoad FROM PreProductionLoads prodLoad" + " WHERE prodLoad.planId.id = :PlanId" + " AND prodLoad.active LIKE 'Y'" + " AND prodLoad.systemId.id = :systemId" + " AND prodLoad.cpuId.id = :cpuId";
	PreProductionLoads lPlanList = (PreProductionLoads) getCurrentSession().createQuery(lQuery).setParameter("PlanId", lImpPlanId).setParameter("systemId", systemId).setParameter("cpuId", cpuId).uniqueResult();
	return lPlanList;
    }

    public List<Object[]> plansNotInPreProd(String[] ids) {
	String lQuery = "select sysload.plan_id as planId, sysload.system_id as systemId, syscpu.id as cpu from system_load as sysload " + "join system_cpu as syscpu on sysload.system_id = syscpu.system_id " + "left join pre_production_loads preprod on preprod.system_id = syscpu.system_id " + "and preprod.plan_id = sysload.plan_id " + "and preprod.active='Y' " + "where sysload.plan_id in (:PlanId) " + "and syscpu.active = 'Y' " + "and syscpu.default_cpu='Y' " + "and sysload.active='Y' "
		+ "and preprod.id is null ";

	List<Object[]> lPlanList = getCurrentSession().createSQLQuery(lQuery).setParameterList("PlanId", ids).list();
	return lPlanList;
    }

    public List<Object[]> getPreProdSystemByPlanStatus(String[] ids) {
	String lQuery = "select A.ID, B.SYSTEM_ID,A.PLAN_STATUS from imp_plan a, pre_production_loads b where a.id = b.plan_id and a.plan_status in (:planStatus) " + " and a.active='Y' and b.active='Y' and a.id in (:PlanId)";

	List<Object[]> lPlanList = getCurrentSession().createSQLQuery(lQuery).setParameterList("PlanId", ids).setParameterList("planStatus", Constants.PlanStatus.getQAPlanStatusBetweenApprovedAndRegDepl().keySet()).list();
	return lPlanList;
    }
}
