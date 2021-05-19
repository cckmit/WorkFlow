package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.LoadCategories;
import com.tsi.workflow.beans.dao.System;
import com.tsi.workflow.exception.WorkflowException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class LoadCategoriesDAO extends BaseDAO<LoadCategories> {

    public List<LoadCategories> findBySystem(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", new System(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<LoadCategories> findBySystem(List<System> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<LoadCategories> findBySystem(System pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("systemId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<LoadCategories> findBySystem(Integer[] pId) throws WorkflowException {
	List<System> lSystem = new ArrayList<>();
	for (Integer lId : pId) {
	    lSystem.add(new System(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("systemId", lSystem));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public Boolean isExists(LoadCategories loadCategory) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("systemId", loadCategory.getSystemId()));
	lCriteria.add(Restrictions.eq("name", loadCategory.getName()));
	lCriteria.add(Restrictions.eq("active", "Y"));
	if (loadCategory.getId() != null) {
	    lCriteria.add(Restrictions.ne("id", loadCategory.getId()));
	}
	lCriteria.setProjection(Projections.rowCount());
	return ((Long) lCriteria.uniqueResult()) > 0;
    }

    public Long getCountBySystem(Integer systemId) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("systemId", new System(systemId)));
	lCriteria.add(Restrictions.eq("active", "Y"));
	lCriteria.setProjection(Projections.rowCount());
	return (Long) lCriteria.uniqueResult();
    }

    public List<LoadCategories> getLoadCategoriesBy(Integer pSystem, Date pDate) {
	String lQuery = "SELECT DISTINCT a FROM LoadCategories a, LoadFreeze b" + " WHERE a.id != b.loadCategoryId" + " AND a.systemId = :System" + " AND :Date between b.fromDate and b.toDate" + " AND b.active = 'Y'" + " AND a.active = 'Y'";

	List<LoadCategories> lLoadCategoriesList = getCurrentSession().createQuery(lQuery).setParameter("System", new System(pSystem)).setParameter("Date", pDate).list();

	return lLoadCategoriesList;
    }

    public List<LoadCategories> findByIdAndSystem(Integer pSystemId, List<String> pCategoryIds) {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.eq("systemId", new System(pSystemId)));
	lCriteria.add(Restrictions.in("id", pCategoryIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();

    }

    public List<String> getLoadCategoryByDate(Date pDate) {
	String lQuery = "SELECT DISTINCT load_category_id FROM load_window_date WHERE date = :Date";
	List<String> lCategoryList = getCurrentSession().createSQLQuery(lQuery).setParameter("Date", pDate).list();
	return lCategoryList;

    }

    public List<LoadCategories> getLoadCategoriesByDay(Integer pSystem, String pDay) {
	String lQuery = "SELECT DISTINCT cat FROM LoadCategories cat, LoadWindow win" + " WHERE cat.active = 'Y'" + " AND win.active = 'Y'" + " AND cat.systemId = :SystemId" + " AND cat.id = win.loadCategoryId" + " AND win.daysOfWeek = :Day";

	List<LoadCategories> lLoadCategoriesList = getCurrentSession().createQuery(lQuery).setParameter("SystemId", new System(pSystem)).setParameter("Day", pDay).list();

	return lLoadCategoriesList;
    }

    public List<LoadCategories> getLoadCategoriesBy(List<LoadCategories> pLoadCategories, Date pDate) {
	List<Integer> lCategoryId = new ArrayList();
	for (LoadCategories lCategories : pLoadCategories) {
	    lCategoryId.add(lCategories.getId());
	}

	String lQuery = "SELECT cat FROM LoadCategories cat, LoadFreeze fre" + " WHERE cat.active = 'Y'" + " AND fre.active = 'Y'" + " AND cat.id = fre.loadCategoryId" + " AND :Date BETWEEN fre.fromDate AND fre.toDate" + " AND cat.id IN (:CategoryId)";

	List<LoadCategories> lLoadCategoriesList = getCurrentSession().createQuery(lQuery).setParameterList("CategoryId", lCategoryId).setParameter("Date", pDate).list();

	return lLoadCategoriesList;

    }
}
