/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.SystemLoad;
import com.tsi.workflow.beans.dao.SystemPdddsMapping;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Radha.Adhimoolam
 */
@Repository
public class SystemPdddsMappingDAO extends BaseDAO<SystemPdddsMapping> {

    private static final Logger LOG = Logger.getLogger(SystemPdddsMappingDAO.class.getName());

    public List<SystemPdddsMapping> findBySystemLoadId(Integer id) {
	String lQuery = "SELECT system FROM SystemPdddsMapping system, PdddsLibrary pddds" + " WHERE system.pdddsLibraryId.id = pddds.id AND " + " system.systemLoadId.id = :systemLoadId AND " + " system.active = 'Y' ORDER BY system.id ASC";

	List<SystemPdddsMapping> lPdddsList = getCurrentSession().createQuery(lQuery).setParameter("systemLoadId", id).list();
	return lPdddsList;
    }

    public List<SystemPdddsMapping> findBySystemLoadId(Integer[] ids) {
	List<SystemLoad> lSysLoads = new ArrayList();
	for (Integer id : ids) {
	    lSysLoads.add(new SystemLoad(id));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemLoadId", lSysLoads));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

}
