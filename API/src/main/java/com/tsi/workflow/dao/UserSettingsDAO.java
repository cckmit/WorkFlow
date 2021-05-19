package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.UserSettings;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class UserSettingsDAO extends BaseDAO<UserSettings> {

    private static final Logger LOG = Logger.getLogger(UserSettingsDAO.class.getName());

    public List<UserSettings> findAll(String pUserId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("active", "Y");
	lFilter.put("userId", pUserId);
	return findAll(lFilter);
    }

    public UserSettings find(String pUserId, String pSettingsName) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("active", "Y");
	lFilter.put("name", pSettingsName);
	lFilter.put("userId", pUserId);
	return find(lFilter);
    }

    // public List<UserSettings> findUsersDelegatedThemselfTo(String pUserId) {
    // String lQuery = "select a from UserSettings a, UserSettings b"
    // + " where a.value like ?"
    // + " and a.name like ?"
    // + " and b.userId = a.userId"
    // + " and b.name like ?"
    // + " and b.value like 'TRUE'";
    // List<UserSettings> lSettings = (List<UserSettings>)
    // getCurrentSession().createQuery(lQuery)
    // .setParameter(0, pUserId)
    // .setParameter(1, Constants.UserSettings.DELEGATE_USER.name())
    // .setParameter(2, Constants.UserSettings.DELEGATION.name())
    // .list();
    // return lSettings;
    // }
    public List<UserSettings> findAllActiveDelegations() {
	String lQuery = "select a from UserSettings a, UserSettings b" + " where a.name like ?" + " and b.userId = a.userId" + " and b.name like ?" + " and b.value like 'TRUE'";
	List<UserSettings> lSettings = (List<UserSettings>) getCurrentSession().createQuery(lQuery).setParameter(0, Constants.UserSettings.DELEGATE_USER.name()).setParameter(1, Constants.UserSettings.DELEGATION.name()).list();
	return lSettings;
    }

    public List<UserSettings> findAnyDelegatedUsersFor(Collection<String> pUserId) {
	String lQuery = "select a from UserSettings a, UserSettings b" + " where a.userId in (:userList)" + " and a.name like :type1" + " and b.userId = a.userId" + " and b.name like :type2" + " and b.value like 'TRUE'";
	List<UserSettings> lSettings = (List<UserSettings>) getCurrentSession().createQuery(lQuery).setParameterList("userList", pUserId).setParameter("type1", Constants.UserSettings.DELEGATE_USER.name()).setParameter("type2", Constants.UserSettings.DELEGATION.name()).list();
	return lSettings;
    }
}
