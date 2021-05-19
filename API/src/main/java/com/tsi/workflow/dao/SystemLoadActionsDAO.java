package com.tsi.workflow.dao;

import com.tsi.workflow.User;
import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemLoadActions;
import com.tsi.workflow.beans.dao.Vpars;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.VPARSEnvironment;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class SystemLoadActionsDAO extends BaseDAO<SystemLoadActions> {

    public List<SystemLoadActions> findByPlanId(String pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<SystemLoadActions> findByPlanId(List<ImpPlan> pIds) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("planId", pIds));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<SystemLoadActions> findByPlanId(ImpPlan pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<SystemLoadActions> findByPlanId(String[] pId) throws WorkflowException {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : pId) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("planId", lImpPlan));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<SystemLoadActions> findBySystemId(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", new System(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<SystemLoadActions> findBySystemId(List<System> pIds) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("systemId", pIds));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<SystemLoadActions> findBySystemId(System pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<SystemLoadActions> findBySystemId(Integer[] pId) throws WorkflowException {
	List<System> lSystem = new ArrayList<>();
	for (Integer lId : pId) {
	    lSystem.add(new System(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("systemId", lSystem));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<SystemLoadActions> findBySystemLoadId(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemLoadId", new SystemLoad(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<SystemLoadActions> findBySystemLoadId(List<SystemLoad> pIds) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("systemLoadId", pIds));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<SystemLoadActions> findBySystemLoadId(SystemLoad pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemLoadId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<SystemLoadActions> findBySystemLoadId(Integer[] pId) throws WorkflowException {
	List<SystemLoad> lSystemLoad = new ArrayList<>();
	for (Integer lId : pId) {
	    lSystemLoad.add(new SystemLoad(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("systemLoadId", lSystemLoad));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<SystemLoadActions> findByVparId(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("vparId", new Vpars(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<SystemLoadActions> findByVparId(List<Vpars> pIds) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("vparId", pIds));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<SystemLoadActions> findByVparId(Vpars pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("vparId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<SystemLoadActions> findByVparId(Integer[] pId) throws WorkflowException {
	List<Vpars> lVpars = new ArrayList<>();
	for (Integer lId : pId) {
	    lVpars.add(new Vpars(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("vparId", lVpars));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<SystemLoadActions> findByPlan(String[] pId, List<Constants.VPARSEnvironment> pVpars, User pUser) throws WorkflowException {
	List<String> vparsType = pVpars.stream().map(Constants.VPARSEnvironment::name).collect(Collectors.toList());
	if (pUser.getCurrentRole().equals(Constants.UserGroup.Lead.name())) {
	    String lQuery = "SELECT loadAction FROM SystemLoadActions loadAction, Vpars pars" + " Where loadAction.vparId=pars.id " + " AND loadAction.active = 'Y'" + " AND pars.active='Y'" + " AND loadAction.planId.id in (:Planids)" + " AND ((pars.type ='INTEGRATION') or (pars.type='PRIVATE' AND pars.ownerId = :ownerId))";
	    return getCurrentSession().createQuery(lQuery).setParameterList("Planids", pId).setParameter("ownerId", pUser.getId()).list();
	} else {
	    String lQuery = "SELECT loadAction FROM SystemLoadActions loadAction" + " Where loadAction.active = 'Y'"
	    // + " AND loadAction.isAutoDeploy = :isAutoDeploy"
		    + " AND loadAction.planId.id in (:Planids)" + " AND loadAction.vparId.type in :VparsType";
	    return getCurrentSession().createQuery(lQuery).setParameterList("Planids", pId)
		    // .setParameter("isAutoDeploy", Boolean.FALSE)
		    .setParameterList("VparsType", vparsType).list();
	}
    }

    public List<SystemLoadActions> findBySystemLoadEnv(SystemLoad pSystemLoad, Constants.VPARSEnvironment pVpars) throws WorkflowException {
	String lQuery = "SELECT loadAction FROM SystemLoadActions loadAction" + " Where loadAction.active = 'Y'" + " AND loadAction.systemLoadId = :pSystemLoad" + " AND loadAction.vparId.type = :VparsType" + " AND loadAction.isAutoDeploy = :isAutoDeploy";
	return getCurrentSession().createQuery(lQuery).setParameter("pSystemLoad", pSystemLoad).setParameter("VparsType", pVpars.name()).setParameter("isAutoDeploy", Boolean.FALSE).list();
    }

    public List<SystemLoadActions> findByPlanSystemEnv(ImpPlan pPlan, System pSystem, Constants.VPARSEnvironment pVpars) throws WorkflowException {
	String lQuery = "SELECT loadAction FROM SystemLoadActions loadAction" + " Where loadAction.active = 'Y'" + " AND loadAction.systemId = :System" + " AND loadAction.planId = :Plan" + " AND loadAction.vparId.type = :VparsType";
	return getCurrentSession().createQuery(lQuery).setParameter("System", pSystem).setParameter("Plan", pPlan).setParameter("VparsType", pVpars.name()).list();
    }

    public List<SystemLoadActions> findBy(String[] pId, Integer vparid[]) throws WorkflowException {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	List<Vpars> lVpar = new ArrayList<>();
	for (String lId : pId) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	for (Integer lId : vparid) {
	    lVpar.add(new Vpars(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("planId", lImpPlan));
	expressions.add(Restrictions.in("vparId", lVpar));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<SystemLoadActions> findByPlanAndVpars(String planId, Integer[] vparid) {
	String lQuery = " SELECT action FROM systemLoadActions action, SystemLoad sysLoad " + " WHERE action.planId = :PlanId " + " AND action.vparId in (:VparsId) " + " AND action.active = 'Y' " + " AND action.systemLoadId = sysLoad.id " + " AND sysLoad.active = 'Y' " + " AND sysLoad.qaTestingStatus = 'NONE' ";

	List<SystemLoadActions> lReturnList = getCurrentSession().createQuery(lQuery).setParameter("PlanId", planId).setParameterList("VparsId", vparid).list();

	return lReturnList;
    }

    public List<SystemLoadActions> findByPlanAndEnv(String pPlanId, Constants.VPARSEnvironment pVpars) throws WorkflowException {
	String lQuery = "SELECT loadAction FROM SystemLoadActions loadAction" + " Where loadAction.active = 'Y'" + " AND loadAction.planId.id = :PlanId" + " AND loadAction.vparId.type = :VparsType";
	return getCurrentSession().createQuery(lQuery).setParameter("PlanId", pPlanId).setParameter("VparsType", pVpars.name()).list();
    }

    public SystemLoadActions findByPlanAndVpars(String planId, Integer vparid, boolean isAutoDeploy) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("planId", new ImpPlan(planId)));
	expressions.add(Restrictions.eq("vparId", new Vpars(vparid)));
	expressions.add(Restrictions.eq("active", "Y"));
	expressions.add(Restrictions.eq("isAutoDeploy", isAutoDeploy));
	List<SystemLoadActions> systemLoadActions = findAll(expressions);
	return systemLoadActions != null ? systemLoadActions.get(0) : null;
    }

    public List<Object[]> plansNotInSystemLoadActions(String[] ids, List<VPARSEnvironment> lVparsType) {
	List<String> vparsType = lVparsType.stream().map(Constants.VPARSEnvironment::name).collect(Collectors.toList());
	String lQuery = "select distinct sysload.plan_id as planId, sysload.system_id as systemId, vPar.id as vParId from system_load as sysload " + "join vpars as vPar on sysload.system_id = vPar.system_id " + "left join system_load_actions loadAction on loadAction.system_id = vPar.system_id " + "and loadAction.plan_id = sysload.plan_id and loadAction.active='Y' " + "where sysload.plan_id in (:PlanId) " + "and vPar.active = 'Y' " + "and vPar.default_cpu='Y' and vPar.type in (:lVparsType) "
		+ "and sysload.active='Y' ";

	List<Object[]> lPlanList = getCurrentSession().createSQLQuery(lQuery).setParameterList("PlanId", ids).setParameterList("lVparsType", vparsType).list();
	return lPlanList;
    }

    public List<SystemLoadActions> findByPlanForDSLEnable(ImpPlan pImpPlan) throws WorkflowException {
	String lQuery = "SELECT loadAction FROM SystemLoadActions loadAction , Vpars vpar " + " Where loadAction.active = 'Y'" + " AND loadAction.vparId = vpar.id " + " AND loadAction.planId.id = :PlanId" + " AND vpar.active = 'Y'" + " AND loadAction.dslUpdate = 'Y'" + " AND loadAction.status = :loadSetStatus";
	return getCurrentSession().createQuery(lQuery).setParameter("PlanId", pImpPlan.getId()).setParameter("loadSetStatus", Constants.LOAD_SET_STATUS.ACTIVATED.name()).list();
    }

}
