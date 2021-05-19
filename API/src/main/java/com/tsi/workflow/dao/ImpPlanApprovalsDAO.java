package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.ImpPlanApprovals;
import com.tsi.workflow.exception.WorkflowException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class ImpPlanApprovalsDAO extends BaseDAO<ImpPlanApprovals> {

    public List<ImpPlanApprovals> findByImpPlan(String pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<ImpPlanApprovals> findByImpPlan(List<ImpPlan> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<ImpPlanApprovals> findByImpPlan(ImpPlan pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<ImpPlanApprovals> findByImpPlan(String[] pId) throws WorkflowException {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : pId) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", lImpPlan));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public ImpPlanApprovals findByPlanAndFileName(String planId, String fileName) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("planId", new ImpPlan(planId)));
	expressions.add(Restrictions.like("fileName", fileName));
	expressions.add(Restrictions.eq("active", "Y"));
	List<ImpPlanApprovals> impPlanApproval = findAll(expressions);
	return impPlanApproval != null ? impPlanApproval.get(0) : null;
    }

    public ImpPlanApprovals findByUniquePlan(String planId) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("planId", new ImpPlan(planId)));
	expressions.add(Restrictions.eq("active", "Y"));
	List<ImpPlanApprovals> impPlanApproval = findAll(expressions);
	return impPlanApproval != null ? impPlanApproval.get(0) : null;
    }
}
