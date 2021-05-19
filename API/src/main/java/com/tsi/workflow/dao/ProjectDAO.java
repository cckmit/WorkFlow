package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.Project;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectDAO extends BaseDAO<Project> {

    public Map<String, Project> findActiveProjectNumbers() {
	List<Criterion> criterions = new ArrayList<>();
	criterions.add(Restrictions.eq("active", "Y"));
	criterions.add(Restrictions.eq("isDelta", Boolean.FALSE));
	List<Project> lProjects = findAll(criterions);
	Map<String, Project> lMap = new HashMap<>();
	for (Project lProject : lProjects) {
	    lMap.put(lProject.getProjectNumber(), lProject);
	}
	return lMap;
    }

    public Map<String, Project> findNonActiveProjectNumbers() {
	List<Criterion> criterions = new ArrayList<>();
	criterions.add(Restrictions.eq("active", "N"));
	criterions.add(Restrictions.eq("isDelta", Boolean.FALSE));
	List<Project> lProjects = findAll(criterions);
	Map<String, Project> lMap = new HashMap<>();
	for (Project lProject : lProjects) {
	    lMap.put(lProject.getProjectNumber(), lProject);
	}
	return lMap;
    }

    public List findFiltered(String filter) {
	List<Criterion> criterions = new ArrayList<>();
	criterions.add(Restrictions.eq("active", "Y"));
	criterions.add(Restrictions.ilike("projectNumber", filter, MatchMode.START));
	return findAll(criterions);
    }

    public List findFiltered(String filter, Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy, String searchField, Boolean isDelta) {
	List<Criterion> criterions = new ArrayList<>();
	criterions.add(Restrictions.eq("active", "Y"));
	criterions.add(Restrictions.eq("isDelta", isDelta));
	if (filter != null && !filter.equals("")) {
	    criterions.add(Restrictions.ilike(searchField, "%" + filter + "%", MatchMode.ANYWHERE));
	}
	return findAll(criterions, pOffset, pLimit, pOrderBy);
    }

    public Long countBy(String filter, Boolean isDelta, String searchField) {
	List<Criterion> criterions = new ArrayList<>();
	criterions.add(Restrictions.eq("active", "Y"));
	criterions.add(Restrictions.eq("isDelta", isDelta));
	if (filter != null && !filter.equals("")) {
	    criterions.add(Restrictions.ilike(searchField, "%" + filter + "%", MatchMode.ANYWHERE));
	}
	return count(criterions);
    }

}
