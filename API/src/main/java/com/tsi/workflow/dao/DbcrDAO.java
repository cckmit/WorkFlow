/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.Dbcr;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.exception.WorkflowException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author
 */
@Repository
public class DbcrDAO extends BaseDAO<Dbcr> {

    public List<Dbcr> findByPlanId(String pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<Dbcr> findByPlanId(List<ImpPlan> pIds) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("planId", pIds));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<Dbcr> findByPlanId(ImpPlan pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<Dbcr> findBySystemId(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", new com.tsi.workflow.beans.dao.System(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<Dbcr> findBySystemId(List<com.tsi.workflow.beans.dao.System> pIds) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("systemId", pIds));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<Dbcr> findBySystemId(com.tsi.workflow.beans.dao.System pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<Dbcr> findByPlanSystemId(String pId, Integer systemId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	lFilter.put("systemId", new com.tsi.workflow.beans.dao.System(systemId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<Dbcr> findByPlanSystemEnvironment(String pId, Integer systemId, String env) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	lFilter.put("systemId", new com.tsi.workflow.beans.dao.System(systemId));
	lFilter.put("environment", env);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<Dbcr> findBySystemId(Integer[] pId) throws WorkflowException {
	List<com.tsi.workflow.beans.dao.System> lSystem = new ArrayList<>();
	for (Integer lId : pId) {
	    lSystem.add(new com.tsi.workflow.beans.dao.System(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("systemId", lSystem));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<Dbcr> findByPlanId(String[] pId) throws WorkflowException {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : pId) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("planId", lImpPlan));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<Dbcr> findByDBCRId(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("id", pId);
	return findAll(lFilter);
    }

    public List<Dbcr> findByPlanSystemId(List<ImpPlan> pId, Integer systemId) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("planId", pId));
	expressions.add(Restrictions.eq("systemId", new com.tsi.workflow.beans.dao.System(systemId)));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }
}
