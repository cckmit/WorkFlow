package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.LoadCategories;
import com.tsi.workflow.beans.dao.LoadFreeze;
import com.tsi.workflow.beans.dao.LoadFreezeGrouped;
import com.tsi.workflow.beans.ui.LoadFreezeCalendar;
import com.tsi.workflow.beans.ui.LoadFreezeForm;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.stereotype.Repository;

@Repository
public class LoadFreezeDAO extends BaseDAO<LoadFreeze> {
    private static final Logger LOG = Logger.getLogger(LoadFreezeDAO.class.getName());

    public List<LoadFreeze> findByLoadCategories(Integer pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("loadCategoryId", new LoadCategories(pId));
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<LoadFreeze> findByLoadCategories(List<LoadCategories> pIds) throws WorkflowException {
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("loadCategoryId", pIds));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<LoadFreeze> findByLoadCategories(LoadCategories pId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("loadCategoryId", pId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public List<LoadFreeze> findByLoadCategories(Integer[] pId) throws WorkflowException {
	List<LoadCategories> lLoadCategories = new ArrayList<>();
	for (Integer lId : pId) {
	    lLoadCategories.add(new LoadCategories(lId));
	}
	Criteria lCriteria = getCriteria();
	lCriteria.add(Restrictions.in("loadCategoryId", lLoadCategories));
	lCriteria.add(Restrictions.eq("active", "Y"));
	return lCriteria.list();
    }

    public List<LoadFreezeCalendar> findCommonDateBySystem(Integer systemId, Date date, Integer loadCategoryCount) {
	String lQuery = "SELECT to_char( date, '" + Constants.LOAD_FREEZE_DATE_TIME_FORMAT + "') as date FROM load_freeze_date" + " WHERE system_id = :SystemId" + " AND date BETWEEN :fromDate AND :toDate" + " AND category_count = :Count";
	Date fromDate = DateUtils.addDays(DateUtils.truncate(date, Calendar.MONTH), -7);
	Date toDate = DateUtils.addDays(DateUtils.ceiling(date, Calendar.MONTH), 7);
	List<String> lDateList = getCurrentSession().createSQLQuery(lQuery).setParameter("SystemId", systemId).setParameter("fromDate", fromDate).setParameter("toDate", toDate).setParameter("Count", loadCategoryCount).list();
	List<LoadFreezeCalendar> lDateObjList = new ArrayList<>();
	for (String lDate : lDateList) {
	    lDateObjList.add(new LoadFreezeCalendar(lDate));
	}
	return lDateObjList;

    }

    public Collection findCommonDateByLoadCategory(Integer loadCategoryId, Date date) {
	String lQuery = "SELECT to_char( date, '" + Constants.LOAD_FREEZE_DATE_TIME_FORMAT + "') as date FROM load_window_date" + " WHERE date BETWEEN :fromDate AND :toDate" + " AND load_category_id = :Category";
	Date fromDate = DateUtils.addDays(DateUtils.truncate(date, Calendar.MONTH), -7);
	Date toDate = DateUtils.addDays(DateUtils.ceiling(date, Calendar.MONTH), 7);
	List<String> lDateList = getCurrentSession().createSQLQuery(lQuery).setParameter("fromDate", fromDate).setParameter("toDate", toDate).setParameter("Category", loadCategoryId).list();
	List<LoadFreezeCalendar> lDateObjList = new ArrayList<>();
	for (String lDate : lDateList) {
	    lDateObjList.add(new LoadFreezeCalendar(lDate));
	}
	return lDateObjList;
    }

    public List<LoadFreezeGrouped> getLoadFreezeGrouped(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy) {
	String lQuery = "SELECT * from load_freeze_group";

	String lOrderByString = "";
	if (pOrderBy != null && !pOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> entrySet : pOrderBy.entrySet()) {
		String key = entrySet.getKey();
		String value = entrySet.getValue();
		lOrderByString = " ORDER BY " + key + " " + value;
	    }
	}

	List<LoadFreezeGrouped> lReturnList = getCurrentSession().createSQLQuery(lQuery + lOrderByString).setFirstResult(pOffset * pLimit).setMaxResults(pLimit).setResultTransformer(new AliasToBeanResultTransformer(LoadFreezeGrouped.class)).list();
	return lReturnList;
    }

    public List<LoadFreezeGrouped> getLoadFreezeGroupedBySystem(Integer pOffset, Integer pLimit, LinkedHashMap<String, String> pOrderBy, String pFilter) {
	StringBuilder lQuery = new StringBuilder("SELECT * from load_freeze_group where ");
	if (pFilter != null && !pFilter.isEmpty()) {
	    lQuery.append(" systemid = " + pFilter);
	}
	String lOrderByString = "";
	if (pOrderBy != null && !pOrderBy.isEmpty()) {
	    for (Map.Entry<String, String> entrySet : pOrderBy.entrySet()) {
		String key = entrySet.getKey();
		String value = entrySet.getValue();
		lOrderByString = " ORDER BY " + key + " " + value;
	    }
	}

	List<LoadFreezeGrouped> lReturnList = getCurrentSession().createSQLQuery(lQuery + lOrderByString).setFirstResult(pOffset * pLimit).setMaxResults(pLimit).setResultTransformer(new AliasToBeanResultTransformer(LoadFreezeGrouped.class)).list();
	return lReturnList;
    }

    public Long getLoadFreezeGroupedBySystemCount(String pFilter) {
	StringBuilder lQuery = new StringBuilder("SELECT COUNT(ids) from load_freeze_group where ");
	if (pFilter != null && !pFilter.isEmpty()) {
	    lQuery.append(" systemid = " + pFilter);
	}

	Long count = Long.valueOf(getCurrentSession().createSQLQuery(lQuery.toString()).uniqueResult().toString());
	return count;
    }

    public Long getLoadFreezeGroupedCount() {
	String lQuery = "SELECT COUNT(ids) from load_freeze_group";

	Long count = Long.valueOf(getCurrentSession().createSQLQuery(lQuery).uniqueResult().toString());
	return count;
    }

    public List<LoadFreezeGrouped> getLoadFreezeGroupedByDateAndSystem(String date, Integer systemId) {
	String lQuery = "SELECT DISTINCT a.* FROM load_freeze_group a, system b" + " WHERE :Date BETWEEN to_char(a.from_date,'YYYY-MM-DD') AND to_char(a.to_date,'YYYY-MM-DD')" + " AND b.id = :SystemId" + " AND b.active = 'Y'" + " AND a.name = b.name ";
	List<LoadFreezeGrouped> lReturnList = getCurrentSession().createSQLQuery(lQuery).setParameter("Date", date).setParameter("SystemId", systemId).setResultTransformer(new AliasToBeanResultTransformer(LoadFreezeGrouped.class)).list();
	return lReturnList;
    }

    public List<LoadFreezeCalendar> findCommonDateBySystem(Integer systemId, Date date) {
	String lQuery = "SELECT DISTINCT to_char( date, '" + Constants.LOAD_FREEZE_DATE_TIME_FORMAT + "') as date FROM load_freeze_date" + " WHERE system_id = :SystemId" + " AND date BETWEEN :fromDate AND :toDate";

	Date fromDate = DateUtils.addDays(DateUtils.truncate(date, Calendar.MONTH), -7);
	Date toDate = DateUtils.addDays(DateUtils.ceiling(date, Calendar.MONTH), 7);
	List<String> lDateList = getCurrentSession().createSQLQuery(lQuery).setParameter("SystemId", systemId).setParameter("fromDate", fromDate).setParameter("toDate", toDate).list();
	List<LoadFreezeCalendar> lDateObjList = new ArrayList<>();
	for (String lDate : lDateList) {
	    lDateObjList.add(new LoadFreezeCalendar(lDate));
	}
	return lDateObjList;

    }

    public List<String> lLoadFreezeList(LoadFreezeForm loadFreezeForm, LoadCategories lLoadCategories) {
	String lQuery = "SELECT load FROM LoadFreeze load" + " WHERE load.active = 'Y'" + " AND load.loadCategoryId.systemId.id = :systemId " + " AND load.loadCategoryId.id = :loadCategoryId " + " AND load.toDate  = :toDate" + " AND load.fromDate = :fromDate";

	return getCurrentSession().createQuery(lQuery).setParameter("systemId", loadFreezeForm.getSystem().getId()).setParameter("loadCategoryId", lLoadCategories.getId()).setParameter("toDate", loadFreezeForm.getLoadFreeze().getToDate()).setParameter("fromDate", loadFreezeForm.getLoadFreeze().getFromDate()).list();
    }

    public List<String> lLoadFreezeListByCategories(LoadFreeze loadFreeze) {
	String lQuery = "SELECT load FROM LoadFreeze load" + " WHERE load.active = 'Y' " + " AND load.loadCategoryId.systemId.id = :systemId " + " AND load.loadCategoryId.id  = :loadCategoryId" + " AND load.toDate  = :toDate" + " AND load.fromDate = :fromDate";

	return getCurrentSession().createQuery(lQuery).setParameter("systemId", loadFreeze.getLoadCategoryId().getSystemId().getId()).setParameter("loadCategoryId", loadFreeze.getLoadCategoryId().getId()).setParameter("toDate", loadFreeze.getToDate()).setParameter("fromDate", loadFreeze.getFromDate()).list();
    }

    public List<String> lLoadFreezeUpdateByDate(LoadFreeze loadFreeze, Date fromDate, Date toDate) {
	String lQuery = "SELECT load FROM LoadFreeze load" + " WHERE load.active = 'Y' " + " AND load.loadCategoryId.systemId.id = :systemId " + " AND load.loadCategoryId.id  = :loadCategoryId" + " AND load.toDate  = :toDate" + " AND load.fromDate = :fromDate";

	return getCurrentSession().createQuery(lQuery).setParameter("systemId", loadFreeze.getLoadCategoryId().getSystemId().getId()).setParameter("loadCategoryId", loadFreeze.getLoadCategoryId().getId()).setParameter("toDate", toDate).setParameter("fromDate", fromDate).list();
    }

}
