package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.PutLevel;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.exception.WorkflowException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class PutLevelDAO extends BaseDAO<PutLevel> {

    private static final Logger LOG = Logger.getLogger(PutLevelDAO.class.getName());

    public List<PutLevel> findBySystem(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", new System(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<PutLevel> findBySystem(List<System> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<PutLevel> findBySystem(System pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<PutLevel> findBySystem(Integer[] pId) throws WorkflowException {
	List<System> lSystem = new ArrayList<>();
	for (Integer lId : pId) {
	    lSystem.add(new System(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", lSystem));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public Boolean isExists(System system, String putLevelStatus) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.eq("systemId", system));
	lCriteria.add(Restrictions.eq("status", putLevelStatus));
	lCriteria.setProjection(Projections.rowCount());
	return ((Long) lCriteria.uniqueResult()) > 0;
    }

    public PutLevel getPutLevel(System system, String putLevelStatus) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.eq("systemId", system));
	lCriteria.add(Restrictions.eq("status", putLevelStatus));
	return ((PutLevel) lCriteria.uniqueResult());
    }

    public PutLevel findBySystemAndPutName(String putLevel, Integer systemId) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.ilike("putLevel", putLevel));
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.eq("systemId", new System(systemId)));
	return (PutLevel) lCriteria.uniqueResult();
    }

    public PutLevel findByPutLevelandSystem(String putLevel, Integer systemId, String putLevelStatus) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.ilike("putLevel", putLevel));
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.eq("systemId", new System(systemId)));
	lCriteria.add(Restrictions.eq("status", putLevelStatus));
	return (PutLevel) lCriteria.uniqueResult();
    }

    public List<PutLevel> findByStatus(List<String> status) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("status", status));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<PutLevel> findPutLevelBySys(String pCompany, String pSystem, List<String> status) {
	String lQuery = "SELECT put from PutLevel put" + " WHERE put.active = 'Y'" + " AND put.systemId.name = :system" + " AND put.status IN (:status)  " + " AND put.systemId.platformId.nickName = :Company";
	List<PutLevel> lPlanList = getCurrentSession().createQuery(lQuery).setParameter("system", pSystem.toUpperCase().trim()).setParameter("Company", pCompany.toLowerCase().trim()).setParameterList("status", status).list();
	return lPlanList;
    }

    public List<PutLevel> getPutLevelList(List<String> status) {
	String lQueryString = "SELECT put FROM PutLevel put" + " WHERE put.active = 'Y'" + " AND put.status in (:status)" + " AND put.deploymentDateMailFlag = :mailFlag " + " AND put.putDateTime < :currentDate";
	List<PutLevel> lPutLevelList = getCurrentSession().createQuery(lQueryString).setParameterList("status", status).setParameter("mailFlag", Boolean.FALSE).setParameter("currentDate", new Date()).list();

	return lPutLevelList;
    }

}
