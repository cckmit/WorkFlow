package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.OnlineBuild;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class OnlineBuildDAO extends BaseDAO<OnlineBuild> {

    public List<OnlineBuild> findByImpPlan(String pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<OnlineBuild> findByImpPlan(List<ImpPlan> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<OnlineBuild> findByImpPlan(ImpPlan pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<OnlineBuild> findAll(ImpPlan pId, Constants.BUILD_TYPE pBuildType) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", pId);
	lFilter.put("buildType", pBuildType.name());
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<OnlineBuild> findByImpPlan(String[] pId) throws WorkflowException {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : pId) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", lImpPlan));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<OnlineBuild> findBySystem(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", new System(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<OnlineBuild> findBySystem(List<System> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<OnlineBuild> findBySystem(System pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<OnlineBuild> findBySystem(Integer[] pId) throws WorkflowException {
	List<System> lSystem = new ArrayList<>();
	for (Integer lId : pId) {
	    lSystem.add(new System(lId));
	}
	Criteria lCriteria = getCriteria();

	lCriteria.add(Restrictions.in("systemId", lSystem));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public OnlineBuild findByBuild(String planId, System pSystem, Integer buildNumber, Constants.BUILD_TYPE buildType) {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(planId));
	lFilter.put("active", "Y");
	lFilter.put("systemId", pSystem);
	lFilter.put("buildNumber", buildNumber);
	lFilter.put("buildType", buildType.name());
	return find(lFilter);
    }

    public List<OnlineBuild> findLastBuild(String planId, List<System> pSystem, Constants.BUILD_TYPE buildType) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("planId", new ImpPlan(planId)));
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.in("systemId", pSystem));
	lCriteria.add(Restrictions.eq("buildType", buildType.name()));
	lCriteria.addOrder(Order.desc("id"));
	lCriteria.setMaxResults(pSystem.size());
	return lCriteria.list();
    }

    public Long findOnlineBuildInProgress(ImpPlan planId, Constants.BUILD_TYPE buildType) {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("planId", planId));
	expressions.add(Restrictions.eq("active", "Y"));
	expressions.add(Restrictions.eq("buildType", buildType.name()));
	expressions.add(Restrictions.eq("jobStatus", "P"));
	return count(expressions);
    }

    public Long findAllSuccessBuildForPlan(ImpPlan plan, Constants.BUILD_TYPE buildType) {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("planId", plan));
	expressions.add(Restrictions.eq("active", "Y"));
	expressions.add(Restrictions.eq("buildType", buildType.name()));
	expressions.add(Restrictions.in("jobStatus", Arrays.asList("S")));
	return count(expressions);

    }

    // fetch the build data inprogress details
    public List<OnlineBuild> findLastBuildInProgress(String planId, List<System> pSystem, Constants.BUILD_TYPE buildType) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("planId", new ImpPlan(planId)));
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.in("systemId", pSystem));
	lCriteria.add(Restrictions.eq("jobStatus", "P"));
	lCriteria.add(Restrictions.eq("buildType", buildType.name()));
	lCriteria.addOrder(Order.desc("id"));
	lCriteria.setMaxResults(pSystem.size());
	return lCriteria.list();
    }

    // 891 - Fetch records based upon system load and are active
    public List<OnlineBuild> findBySystemAndType(ImpPlan plan, System system, String buildType) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("planId", plan));
	lCriteria.add(Restrictions.eq("systemId", system));
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.eq("buildType", buildType));
	lCriteria.addOrder(Order.desc("id"));
	return lCriteria.list();
    }

}
