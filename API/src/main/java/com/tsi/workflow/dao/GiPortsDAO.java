package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.GiPorts;
import com.tsi.workflow.exception.WorkflowException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class GiPortsDAO extends BaseDAO<GiPorts> {

    public List<GiPorts> findByUserId(String pUserId) throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("userId", pUserId);
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }

    public void hardDelete(GiPorts pObject) throws WorkflowException {
	getCurrentSession().delete(pObject);
    }
}
