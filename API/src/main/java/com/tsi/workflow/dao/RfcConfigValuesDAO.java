package com.tsi.workflow.dao;

import com.tsi.workflow.base.BaseDAO;
import com.tsi.workflow.beans.dao.RfcConfigValues;
import com.tsi.workflow.exception.WorkflowException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class RfcConfigValuesDAO extends BaseDAO<RfcConfigValues> {

    private static final Logger LOG = Logger.getLogger(RfcConfigValues.class.getName());

    public List<RfcConfigValues> getConfigValues() throws WorkflowException {
	HashMap<String, Serializable> lFilter = new HashMap<>();
	lFilter.put("active", "Y");
	return findAll(lFilter);
    }
}
