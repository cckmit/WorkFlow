/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.beans.dao.SystemCpu;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import com.tsi.workflow.utils.Constants.TOSEnvironment;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class SystemCpuDAO extends BaseDAO<SystemCpu> {

    public List<SystemCpu> findBySystem(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", new System(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public SystemCpu findBySystemAndFlag(System pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", pId);
	lFilter.put("active", "Y");
	lFilter.put("cpuType", TOSEnvironment.PRE_PROD_TOS.name());
	lFilter.put("default_cpu", "Y");
	return findAll(lFilter).get(0);
    }

    public List<SystemCpu> findBySystem(Integer pId, String pType) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", new System(pId));
	lFilter.put("cpuType", pType);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<SystemCpu> findBySystem(List<System> pIds, Constants.TOSEnvironment pVparsType) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", pIds));
	lCriteria.add(Restrictions.eq("cpuType", pVparsType.name()));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<SystemCpu> findCpusOtherthan(System lSystem, List<Integer> lCpuIds, String name, Boolean needAll) {
	List<SystemCpu> cpuList = new ArrayList<>();
	String lQuery;
	if (!lCpuIds.isEmpty()) {
	    lQuery = "SELECT cpu FROM SystemCpu cpu" + " WHERE cpu.id NOT IN (:loads)" + " AND cpu.systemId = :system" + " AND cpu.cpuType LIKE :type" + " AND cpu.active = 'Y' ORDER BY cpu.displayName DESC ";
	    cpuList = getCurrentSession().createQuery(lQuery).setParameterList("loads", lCpuIds).setParameter("system", lSystem).setParameter("type", name).list();
	} else {
	    lQuery = "SELECT cpu FROM SystemCpu cpu" + " WHERE cpu.systemId = :system" + " AND cpu.cpuType LIKE :type" + " AND cpu.active = 'Y' ORDER BY cpu.displayName DESC ";
	    cpuList = getCurrentSession().createQuery(lQuery).setParameter("system", lSystem).setParameter("type", name).list();
	}
	if (needAll) {
	    SystemCpu systemCpu = new SystemCpu();
	    systemCpu.setDisplayName("ALL");
	    cpuList.add(systemCpu);
	}
	return cpuList;
    }

}
