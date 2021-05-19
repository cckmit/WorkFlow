package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.Platform;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.exception.WorkflowException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class SystemDAO extends BaseDAO<System> {

    public List<System> findByPlatform(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("platformId", new Platform(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<System> findByPlatform(List<Platform> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("platformId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<System> findByPlatform(Platform pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("platformId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<System> findByPlatform(Integer[] pId) throws WorkflowException {
	List<Platform> lPlatform = new ArrayList<>();
	for (Integer lId : pId) {
	    lPlatform.add(new Platform(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("platformId", lPlatform));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public System findByName(String lSystem) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.ilike("name", lSystem));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return (System) lCriteria.uniqueResult();
    }

    public List<System> findByIds(Integer[] ids) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("id", ids));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<String> getAliasSystemBySystemName(String systemName) {
	String lQuery = "select s.name from system s " + " where s.active = 'Y' " + " and s.alias_name in ( " + " select ss.alias_name from system ss " + "  where ss.name = :SystemName " + "  and ss.active = 'Y' " + "  ) and s.name <> :SystemName";
	List<String> lReturnlist = getCurrentSession().createSQLQuery(lQuery).setParameter("SystemName", systemName).list();
	return lReturnlist;

    }

}
