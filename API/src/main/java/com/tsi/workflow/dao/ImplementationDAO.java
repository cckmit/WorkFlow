package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.ImpPlan;
import com.tsi.workflow.beans.dao.Implementation;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class ImplementationDAO extends BaseDAO<Implementation> {
    private static final Logger LOG = Logger.getLogger(ImpPlanDAO.class.getName());

    public List<Implementation> findByImpPlan(String pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<Implementation> getImplementationList(String pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", new ImpPlan(pId));
	return findAll(lFilter);
    }

    public List<Implementation> findByImpPlan(List<ImpPlan> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<Implementation> findByImpPlan(ImpPlan pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("planId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<Implementation> findByImpPlan(String[] pId) throws WorkflowException {
	List<ImpPlan> lImpPlan = new ArrayList<>();
	for (String lId : pId) {
	    lImpPlan.add(new ImpPlan(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("planId", lImpPlan));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<Implementation> findByDeveloper(String pUserId, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("devId", pUserId);
	lFilter.put("active", "Y");
	return findAll(lFilter, pOffset, pLimit, pOrderBy);
    }

    public Long countByDeveloper(String pDeveloperId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("devId", pDeveloperId);
	lFilter.put("active", "Y");
	return count(lFilter);
    }

    public Long countBy(ImpPlan pPlanid, String pDeveloperId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("devId", pDeveloperId);
	lFilter.put("planId", pPlanid);
	lFilter.put("active", "Y");
	return count(lFilter);
    }

    public Long countByReviewer(String pReveiwerId) throws WorkflowException {
	StringBuilder sb = new StringBuilder();
	sb.append("SELECT count(imp) FROM Implementation imp  WHERE imp.peerReviewers like (:peerReviewers)");
	sb.append(" AND imp.active = 'Y'  AND tktNum IS NOT NULL  AND imp.substatus IN (:subStatus)");
	sb.append(" and id not in (select id from Implementation where reviewersDone like (:peerReviewers )) ");

	Long count = (Long) getCurrentSession().createQuery(sb.toString()).setParameter("peerReviewers", "%" + pReveiwerId + "%").setParameter("subStatus", Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name()).uniqueResult();
	return count;
    }

    public List<Implementation> findByReviewer1(String pReveiwerId, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("substatus", Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name()));
	expressions.add(Restrictions.eq("active", "Y"));
	expressions.add(Restrictions.isNotNull("tktNum"));
	expressions.add(Restrictions.ilike("peerReviewers", pReveiwerId, MatchMode.ANYWHERE));
	return findAll(expressions, pOffset, pLimit, pOrderBy);
    }

    public List<Implementation> findByReviewer(String pReveiwerId, Integer pOffset, Integer pLimit, Map<String, String> pOrderBy) throws WorkflowException {

	StringBuilder sb = new StringBuilder();
	if (pOrderBy == null) {
	    pOrderBy = new LinkedHashMap();
	}
	sb.append("SELECT imp FROM Implementation imp  WHERE imp.peerReviewers like (:peerReviewers)");
	sb.append(" AND imp.active = 'Y'  AND tktNum IS NOT NULL  AND imp.substatus IN (:subStatus)");
	sb.append(" and id not in (select id from Implementation where reviewersDone like (:peerReviewers )) ");

	if (!pOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> entrySet : pOrderBy.entrySet()) {
		String key = entrySet.getKey();
		String value = entrySet.getValue();
		LOG.info(key + " " + value);
		sb.append(" ORDER BY imp." + key + " " + value);
	    }
	} else {
	    sb.append(" ORDER BY imp.id DESC ");
	}

	return getCurrentSession().createQuery(sb.toString()).setParameter("peerReviewers", "%" + pReveiwerId + "%").setParameter("subStatus", Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name()).setFirstResult(pOffset * pLimit).setMaxResults(pLimit).list();
    }

    public List<Object[]> findByReviewerBasedOnTime(String pReveiwerId, Boolean lreviwerFlag) throws WorkflowException {
	String lQuery = "select * from implementation  where substatus = :PlanStatus and active = 'Y' and tkt_num is not null  and peer_reviewers like :pReveiwerId";
	String lReviewerNotDone = "  id not in (select id from implementation where reviewers_done like (:pReveiwerId )) ";
	String lreviewer = lreviwerFlag ? "review_request_date_time <=  (Now() - interval '1 days') and review_mail_flag = false " : "review_request_date_time is not null ";
	lQuery = lQuery + " and " + lReviewerNotDone + " and " + lreviewer;
	return getCurrentSession().createSQLQuery(lQuery).setParameter("pReveiwerId", "%" + pReveiwerId + "%").setParameter("PlanStatus", Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name()).list();
    }

    public List<String> findAllReviewPlans() throws WorkflowException {
	String lQuery = "SELECT impl.plan_id from implementation impl, imp_plan plan" + " WHERE impl.substatus = '" + Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name() + "'" + " AND impl.active = 'Y'" + " AND plan.active = 'Y'" + " AND plan.id = impl.plan_id" + " AND plan.plan_status = '" + Constants.PlanStatus.ACTIVE.name() + "'" + " AND impl.tkt_num is not null";
	List<String> lReturn = getCurrentSession().createSQLQuery(lQuery).list();
	return lReturn;
    }

    public List<String> findAllPlansAndReviewers() throws WorkflowException {
	String lQuery = "SELECT impl.id from implementation impl, imp_plan plan" + " WHERE impl.substatus = '" + Constants.ImplementationSubStatus.UNIT_TESTING_COMPLETED.name() + "'" + " AND impl.active = 'Y'" + " AND plan.active = 'Y'" + " AND plan.id = impl.plan_id" + " AND plan.plan_status = '" + Constants.PlanStatus.ACTIVE.name() + "'" + " AND impl.tkt_num is not null";
	List<String> lReturn = getCurrentSession().createSQLQuery(lQuery).list();
	return lReturn;
    }

    public Long countByReviewerHistory(String pReveiwerId) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("peerReview", "Y"));
	expressions.add(Restrictions.eq("active", "Y"));
	expressions.add(Restrictions.ilike("reviewersDone", pReveiwerId, MatchMode.ANYWHERE));
	return count(expressions);
    }

    public List<Implementation> findByReviewerHistory(String pReveiwerId, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	List<Criterion> expressions = new ArrayList<>();
	expressions.add(Restrictions.eq("peerReview", "Y"));
	expressions.add(Restrictions.eq("active", "Y"));
	expressions.add(Restrictions.ilike("reviewersDone", pReveiwerId, MatchMode.ANYWHERE));
	return findAll(expressions, pOffset, pLimit, pOrderBy);
    }

    public List<Implementation> find(List<String> pId) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("id", pId));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
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

    public List<Implementation> findImpByDeveloper(String pUserId, List<String> planStatuses, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy, String pFilter) throws WorkflowException {

	StringBuilder sb = new StringBuilder();
	String lQuery = "";
	if (pOrderBy == null) {
	    pOrderBy = new LinkedHashMap();
	}
	sb.append("SELECT imp FROM Implementation imp, ImpPlan ip  WHERE imp.devId = :DevId");
	sb.append(" AND imp.active = 'Y'  AND imp.planId.id = ip.id  AND ip.active = 'Y'  AND ip.planStatus IN (:PlanStatus)");
	if (pFilter != null && !pFilter.isEmpty()) {
	    sb.append(" AND ip.id LIKE '%" + pFilter.toUpperCase() + "%'");
	}
	if (pOrderBy.isEmpty()) {
	    sb.append(" ORDER BY COALESCE(imp.modifiedDt, imp.createdDt) DESC");
	    lQuery = sb.toString();
	} else {
	    sb.append(" ORDER BY");
	    for (Map.Entry<String, String> lOrderBy : pOrderBy.entrySet()) {
		sb.append(" " + "imp." + lOrderBy.getKey() + " " + lOrderBy.getValue() + ",");
	    }
	    lQuery = sb.toString().substring(0, sb.toString().length() - 1);
	}

	return getCurrentSession().createQuery(lQuery).setParameter("DevId", pUserId).setParameterList("PlanStatus", planStatuses).setFirstResult(pOffset * pLimit).setMaxResults(pLimit).list();
    }

    public List<Implementation> findImpByLoadDateTime(String pUserId, List<String> planStatuses, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy, String pFilter) throws WorkflowException {

	StringBuffer query = new StringBuffer();
	query.append("SELECT imp FROM Implementation imp,ImpPlan ip,SystemLoad sysLoad ");
	query.append(" WHERE imp.active = 'Y' ");
	query.append(" AND imp.planId.id = ip.id ");
	query.append(" AND ip.active = 'Y' ");
	query.append(" AND sysLoad.active = 'Y' ");
	query.append(" AND sysLoad.loadDateTime IS NOT NULL ");
	query.append(" AND sysLoad.planId = ip.id ");

	if (pUserId != null && !pUserId.isEmpty()) {
	    query.append(" AND imp.devId = :DevId ");
	}
	if (planStatuses != null && !planStatuses.isEmpty()) {
	    query.append(" AND ip.planStatus IN (:PlanStatus) ");
	}
	if (pFilter != null && !pFilter.isEmpty()) {
	    query.append(" AND ip.id LIKE '%" + pFilter.toUpperCase() + "%'");
	}
	query.append(" GROUP BY ip,imp ");
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

	if (pUserId != null && !pUserId.isEmpty()) {
	    lQuery.setParameter("DevId", pUserId);
	}
	if (planStatuses != null && !planStatuses.isEmpty()) {
	    lQuery.setParameterList("PlanStatus", planStatuses);
	}

	return lQuery.setFirstResult(pOffset * pLimit).setMaxResults(pLimit).list();
    }

    public Long countImpByDeveloper(String pUserId, List<String> planStatuses, String pFilter) {
	String lQuery = "SELECT COUNT(imp) FROM Implementation imp, ImpPlan ip" + " WHERE imp.devId = :DevId" + " AND imp.active = 'Y'" + " AND imp.planId.id = ip.id" + " AND ip.active = 'Y'" + " AND ip.planStatus IN (:PlanStatus)";
	if (pFilter != null && !pFilter.isEmpty()) {
	    lQuery = lQuery + " AND ip.id LIKE '%" + pFilter.toUpperCase() + "%'";
	}

	return (Long) getCurrentSession().createQuery(lQuery).setParameter("DevId", pUserId).setParameterList("PlanStatus", planStatuses).uniqueResult();
    }

}
