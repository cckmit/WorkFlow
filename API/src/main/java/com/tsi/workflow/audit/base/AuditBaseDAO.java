/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.base;

import com.tsi.workflow.User;
import com.tsi.workflow.base.CoalesceOrder;
import com.tsi.workflow.base.IBeans;
import com.tsi.workflow.exception.WorkflowException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.Criteria;
import org.hibernate.NullPrecedence;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;
import org.springframework.beans.factory.annotation.Autowired;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

/**
 *
 * @author Radha.Adhimoolam
 */
public abstract class AuditBaseDAO<T extends AuditIBeans> {

    @Autowired
    private SessionFactory sessionFactory;

    public final SessionFactory getSessionFactory() {
	return sessionFactory;
    }

    public void save(T pObject) {
	pObject.setCreatedDt(new Date());
	getCurrentSession().save(pObject);
    }

    public final Session getCurrentSession() {
	Session currentSession = getSessionFactory().getCurrentSession();
	return currentSession;
    }

    public final Criteria getCriteria() {
	return getCurrentSession().createCriteria(getDAOClass());
    }

    public Class getDAOClass() throws WorkflowException {
	Type lType = this.getClass().getGenericSuperclass();
	if (lType instanceof ParameterizedTypeImpl) {
	    ParameterizedTypeImpl lImpl = (ParameterizedTypeImpl) lType;
	    try {
		return Class.forName(lImpl.getActualTypeArguments()[0].getTypeName());
	    } catch (ClassNotFoundException ex) {
		throw new WorkflowException("Invalid DAO Class", ex);
	    }
	}
	return null;
	// throw new WorkflowException("Invalid DAO Class");
    }

    public T find(Integer pObject) throws WorkflowException {
	return (T) getCriteria().add(Restrictions.eq("id", pObject)).uniqueResult();
    }

    public T findE(List<Criterion> pFilter) {
	Criteria lCriteria = getCriteria();
	if (pFilter != null && !pFilter.isEmpty()) {
	    if (pFilter.size() > 1) {
		lCriteria.add(Restrictions.and(pFilter.toArray(new Criterion[0])));
	    } else if (pFilter.size() == 1) {
		lCriteria.add(pFilter.get(0));
	    }
	}
	return (T) lCriteria.uniqueResult();
    }

    public T find(String pObject) throws WorkflowException {
	return (T) getCriteria().add(Restrictions.eq("id", pObject)).uniqueResult();
    }

    public T find(HashMap<String, Serializable> pFilter) throws WorkflowException {
	Criteria lCriteria = getCriteria();

	if (pFilter == null || pFilter.isEmpty()) {
	    return null;
	}

	SimpleExpression[] lRestrictions = new SimpleExpression[pFilter.size()];
	int i = 0;
	for (Map.Entry<String, Serializable> entrySet : pFilter.entrySet()) {
	    String key = entrySet.getKey();
	    Serializable value = entrySet.getValue();
	    lRestrictions[i++] = (Restrictions.eq(key, value));
	}
	if (lRestrictions.length > 1) {
	    lCriteria.add(Restrictions.and(lRestrictions));
	} else if (lRestrictions.length == 1) {
	    lCriteria.add(lRestrictions[0]);
	}
	return (T) lCriteria.uniqueResult();
    }
    // <editor-fold defaultstate="collapsed" desc="Find All Methods">

    public List<T> findAllActive() throws WorkflowException {
	return getCriteria().add(Restrictions.eq("active", "Y")).list();
    }

    public List<T> findAll() throws WorkflowException {
	return getCriteria().list();
    }

    public List<T> findAll(HashMap<String, Serializable> pFilter) throws WorkflowException {
	return findAll(pFilter, 0, 0, true, null);
    }

    public List<T> findAll(Integer pOffset, Integer pLimit) throws WorkflowException {
	return findAll(pOffset, pLimit, true);
    }

    public List<T> findAll(Integer pOffset, Integer pLimit, Boolean isPage) throws WorkflowException {
	HashMap<String, Serializable> pFilter = new HashMap<>();
	pFilter.put("active", "Y");
	return findAll(pFilter, pOffset, pLimit, isPage, null);
    }

    public List<T> findAll(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	HashMap<String, Serializable> pFilter = new HashMap<>();
	pFilter.put("active", "Y");
	return findAll(pFilter, pOffset, pLimit, true, pOrderBy);
    }

    public List<T> findAll(HashMap<String, Serializable> pFilter, Integer pOffset, Integer pLimit) throws WorkflowException {
	return findAll(pFilter, pOffset, pLimit, true, null);
    }

    public List<T> findAll(List<Criterion> pFilter) throws WorkflowException {
	return findAll(pFilter, 0, 0, true, null);
    }

    public List<T> findAll(List<Criterion> pFilter, Integer pOffset, Integer pLimit) throws WorkflowException {
	return findAll(pFilter, pOffset, pLimit, true, null);
    }

    public List<T> findAll(List<Criterion> pFilter, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	return findAll(pFilter, pOffset, pLimit, true, pOrderBy);
    }

    public List<T> findAll(Integer pOffset, Integer pLimit, Boolean isPage, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	HashMap<String, Serializable> pFilter = new HashMap<>();
	pFilter.put("active", "Y");
	return findAll(pFilter, pOffset, pLimit, isPage, pOrderBy);
    }

    public List<T> findAll(HashMap<String, Serializable> pFilter, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	return findAll(pFilter, pOffset, pLimit, true, pOrderBy);
    }

    public List<T> findAll(HashMap<String, Serializable> pFilter, Integer pOffset, Integer pLimit, Boolean isPage, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	List<Criterion> lRestrictions = new ArrayList<>();
	if (pFilter != null && !pFilter.isEmpty()) {
	    for (Map.Entry<String, Serializable> entrySet : pFilter.entrySet()) {
		String key = entrySet.getKey();
		Serializable value = entrySet.getValue();
		lRestrictions.add(Restrictions.eq(key, value));
	    }
	}
	return findAll(lRestrictions, pOffset, pLimit, isPage, pOrderBy);
    }

    public List<T> findAll(List<Criterion> pFilter, Integer pOffset, Integer pLimit, Boolean isPage, LinkedHashMap<String, String> pOrderBy) throws WorkflowException {
	Criteria lCriteria = null;
	lCriteria = getCriteria();
	if (pLimit > 0) {
	    if (isPage) {
		lCriteria.setFirstResult((pOffset * pLimit));
	    } else {
		lCriteria.setFirstResult(pOffset);
	    }
	    lCriteria.setMaxResults(pLimit);
	}
	if (pOrderBy != null) {
	    int lSortCount = 0;
	    for (Map.Entry<String, String> lOrder : pOrderBy.entrySet()) {
		lSortCount++;
		String lSortKey = lOrder.getKey();
		String lSortDir = lOrder.getValue().toLowerCase();
		if (lSortKey.contains(".")) {
		    int lastIndexOf = lSortKey.lastIndexOf(".");
		    String Alias = lSortKey.substring(0, lastIndexOf);
		    String Column = lSortKey.substring(lastIndexOf);
		    lCriteria.createAlias(Alias, "Alias_" + lSortCount);
		    lSortKey = "Alias_" + lSortCount + Column;
		}
		if (lSortDir.contains("asc")) {
		    lCriteria.addOrder(Order.asc(lSortKey).nulls(NullPrecedence.FIRST));
		}
		if (lSortDir.contains("desc")) {
		    lCriteria.addOrder(Order.desc(lSortKey).nulls(NullPrecedence.FIRST));
		}
	    }
	} else {
	    lCriteria.addOrder(CoalesceOrder.desc("modifiedDt", "createdDt"));
	}
	if (pFilter != null && !pFilter.isEmpty()) {
	    if (pFilter.size() > 1) {
		lCriteria.add(Restrictions.and(pFilter.toArray(new Criterion[0])));
	    } else if (pFilter.size() == 1) {
		lCriteria.add(pFilter.get(0));
	    }
	}
	return lCriteria.list();
    }

    // </editor-fold>
    public Long count() throws WorkflowException {
	Criteria criteriaCount = getCriteria();
	criteriaCount.add(Restrictions.eq("active", "Y"));
	criteriaCount.setProjection(Projections.rowCount());
	return (Long) criteriaCount.uniqueResult();
    }

    public Long count(List<Criterion> pFilter) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.setProjection(Projections.rowCount());
	if (pFilter != null) {
	    if (pFilter.size() > 1) {
		lCriteria.add(Restrictions.and(pFilter.toArray(new Criterion[0])));
	    } else if (pFilter.size() == 1) {
		lCriteria.add(pFilter.get(0));
	    }
	}
	return (Long) lCriteria.uniqueResult();
    }

    public Long count(HashMap<String, Serializable> pFilter) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.setProjection(Projections.rowCount());

	if (pFilter == null || pFilter.isEmpty()) {
	    return (Long) lCriteria.uniqueResult();
	}

	SimpleExpression[] lRestrictions = new SimpleExpression[pFilter.size()];
	int i = 0;
	for (Map.Entry<String, Serializable> entrySet : pFilter.entrySet()) {
	    String key = entrySet.getKey();
	    Serializable value = entrySet.getValue();
	    lRestrictions[i++] = (Restrictions.eq(key, value));
	}
	if (lRestrictions.length > 1) {
	    lCriteria.add(Restrictions.and(lRestrictions));
	} else if (lRestrictions.length == 1) {
	    lCriteria.add(lRestrictions[0]);
	}
	return (Long) lCriteria.uniqueResult();
    }

    public void update(User User, T pObject) throws WorkflowException {
	getCurrentSession().merge(pObject);
	getCurrentSession().flush();
    }

    public void update(T pObject) throws WorkflowException {
	pObject.setModifiedDt(new Date());
	getCurrentSession().merge(pObject);
	getCurrentSession().flush();
    }

    public void delete(User User, T pObject) throws WorkflowException {
	getCurrentSession().update(pObject);
    }

    public void delete(T pObject) throws WorkflowException {
	getCurrentSession().update(pObject);
    }

    public void delete(User User, Integer pId) throws WorkflowException {
	IBeans pObject = (IBeans) getCurrentSession().load(getDAOClass(), pId);
	if (pObject != null) {
	    getCurrentSession().update(pObject);
	}
    }

    public void delete(User User, String pId) throws WorkflowException {
	IBeans pObject = (IBeans) getCurrentSession().load(getDAOClass(), pId);
	if (pObject != null) {
	    getCurrentSession().update(pObject);
	}
    }

}
