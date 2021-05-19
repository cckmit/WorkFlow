package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.Vpars;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class VparsDAO extends BaseDAO<Vpars> {

    public List<Vpars> findBySystem(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", new System(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<Vpars> findBySystem(List<System> pIds, Constants.VPARSEnvironment pVparsType, String pUserId) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", pIds));
	lCriteria.add(Restrictions.eq("ownerId", pUserId));
	lCriteria.add(Restrictions.eq("type", pVparsType.name()));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<Vpars> findBySystem(List<System> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<Vpars> findBySystem(List<System> pIds, Constants.VPARSEnvironment pVparsType) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", pIds));
	lCriteria.add(Restrictions.eq("type", pVparsType.name()));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<Vpars> findBySystem(System pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<Vpars> findBySystem(Integer[] pId) throws WorkflowException {
	List<System> lSystem = new ArrayList<>();
	for (Integer lId : pId) {
	    lSystem.add(new System(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", lSystem));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<Vpars> findBySystem(List<System> pIds, Boolean vparsFlag, Constants.VPARSEnvironment pVparsType) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", pIds));
	lCriteria.add(Restrictions.eq("qaVpars", vparsFlag));
	lCriteria.add(Restrictions.eq("type", pVparsType.name()));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<Vpars> findBySystem(System system, String vparsName, List<Constants.VPARSEnvironment> pVparsTypes) throws WorkflowException {
	List<String> lVparsEnvList = new ArrayList();
	pVparsTypes.forEach((vparsType) -> {
	    lVparsEnvList.add(vparsType.name());
	});
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("systemId", system));
	lCriteria.add(Restrictions.eq("name", vparsName.toUpperCase()));
	lCriteria.add(Restrictions.in("type", lVparsEnvList));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<Vpars> findByVpars(Integer[] vparsIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("id", vparsIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<Vpars> findBySystem(List<System> pIds, String vparsName, Constants.VPARSEnvironment pVparsType) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", pIds));
	lCriteria.add(Restrictions.eq("name", vparsName));
	lCriteria.add(Restrictions.eq("type", pVparsType.name()));
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.add(Restrictions.eq("tssDeploy", Boolean.TRUE));
	return lCriteria.list();
    }

    public List<Vpars> findBySystemNotAutoDeployedTSS(List<System> pIds, Constants.VPARSEnvironment pVparsType) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", pIds));
	lCriteria.add(Restrictions.eq("type", pVparsType.name()));
	lCriteria.add(Restrictions.eq("tssDeploy", Boolean.FALSE));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

}
