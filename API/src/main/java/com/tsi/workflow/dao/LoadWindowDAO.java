package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.LoadCategories;
import com.tsi.workflow.beans.dao.LoadWindow;
import com.tsi.workflow.exception.WorkflowException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class LoadWindowDAO extends BaseDAO<LoadWindow> {

    public List<LoadWindow> findByLoadCategories(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("loadCategoryId", new LoadCategories(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<LoadWindow> findByLoadCategories(List<LoadCategories> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("loadCategoryId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<LoadWindow> findByLoadCategories(LoadCategories pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("loadCategoryId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<LoadWindow> findByLoadCategories(Integer[] pId) throws WorkflowException {
	List<LoadCategories> lLoadCategories = new ArrayList<>();
	for (Integer lId : pId) {
	    lLoadCategories.add(new LoadCategories(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("loadCategoryId", lLoadCategories));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<LoadWindow> findByLoadCategoriesAndDay(Integer pId, String day) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("loadCategoryId", new LoadCategories(pId));
	lFilter.put("daysOfWeek", day);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

}
