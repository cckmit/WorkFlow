package com.tsi.workflow.dao;

import com.tsi.workflow.User;
import com.tsi.workflow.base.ActivityLogMessage;
import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.ActivityLog;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.exception.WorkflowException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class ActivityLogDAO extends BaseDAO<ActivityLog> {

    private static final Logger LOG = Logger.getLogger(ActivityLogDAO.class.getName());

    public List<ActivityLog> findByPlanId(String pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<ActivityLog> findByPlanId(List<ImpPlan> pIds) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("planId", pIds));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<ActivityLog> findByPlanId(ImpPlan pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<ActivityLog> findByPlanId(String[] pId) throws WorkflowException {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : pId) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("planId", lImpPlan));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<ActivityLog> findByImpId(String pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("impId", new Implementation(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<ActivityLog> findByImpId(List<Implementation> pIds) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("impId", pIds));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public List<ActivityLog> findByImpId(Implementation pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("impId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<ActivityLog> findByImpId(String[] pId) throws WorkflowException {
	List<Implementation> lImplementation = new ArrayList<>();
	for (String lId : pId) {
	    lImplementation.add(new Implementation(lId));
	}
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.in("impId", lImplementation));
	expressions.add(Restrictions.eq("active", "Y"));
	return findAll(expressions);
    }

    public void save(User pUser, ActivityLogMessage pActivityMessage) {
	try {
	    pActivityMessage.setUser(pUser);
	    ActivityLog activityLog = pActivityMessage.getActivityLog();
	    LOG.info(activityLog.getMessage());
	    save(pUser, activityLog);
	} catch (Exception ex) {
	    LOG.warn("Error in Logging Activity", ex);
	}
    }

    public Long countByPlanId(String pPlanId, String pFilter) {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("planId", new ImpPlan(pPlanId)));
	expressions.add(Restrictions.eq("active", "Y"));
	expressions.add(Restrictions.ilike("message", "%" + pFilter + "%"));
	return count(expressions);
    }

    public List<ActivityLog> findByPlanId(String pPlanId, String pFilter, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("planId", new ImpPlan(pPlanId)));
	expressions.add(Restrictions.eq("active", "Y"));
	expressions.add(Restrictions.ilike("message", "%" + pFilter + "%"));
	return findAll(expressions, pOffset, pLimit, pOrderBy);
    }
}
