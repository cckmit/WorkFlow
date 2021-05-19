/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.PdddsLibrary;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.exception.WorkflowException;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

/**
 *
 * @author sreenubab.kasamsetti
 */
@Repository
public class PdddsLibraryDAO extends BaseDAO<PdddsLibrary> {

    private static final Logger LOG = Logger.getLogger(PdddsLibraryDAO.class.getName());

    public List<PdddsLibrary> findBySystem(System pSystem) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("systemId", pSystem));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<PdddsLibrary> findByIds(List<Integer> ids) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("id", ids));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<PdddsLibrary> findBySystemLoadId(Integer id) {
	String lQuery = "SELECT pddds FROM SystemPdddsMapping system, PdddsLibrary pddds" + " WHERE system.pdddsLibraryId.id = pddds.id AND " + " system.systemLoadId.id = :systemLoadId AND " + " system.active = 'Y' AND  pddds.active = 'Y' ORDER BY system.id ASC";

	List<PdddsLibrary> lPdddsList = getCurrentSession().createQuery(lQuery).setParameter("systemLoadId", id).list();
	return lPdddsList;
    }
}
