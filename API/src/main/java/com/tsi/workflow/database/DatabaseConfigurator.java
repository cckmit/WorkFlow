/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.database;

import com.tsi.workflow.WFConfig;
import com.tsi.workflow.exception.WorkflowException;
import com.tsi.workflow.utils.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author User
 */
@Component
public final class DatabaseConfigurator {

    @Autowired
    WFConfig wFConfig;

    protected JdbcTemplate jdbcTemplate;

    private static final Logger LOG = Logger.getLogger(DatabaseConfigurator.class.getName());

    public void setDataSource(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getJdbcTemplate() {
	return jdbcTemplate;
    }

    public void checkDatabase() throws SQLException {
	checkUpdates();
    }

    public boolean doesTableExist(String pTableName) {
	String lValue = this.getJdbcTemplate().queryForObject("SELECT to_regclass('" + pTableName + "')", String.class);
	if (lValue == null) {
	    return false;
	}
	boolean lExist = lValue.equalsIgnoreCase(pTableName);
	return lExist;
    }

    public void clearDatabase() throws SQLException {
	Collection<String> listTables = listTables();
	for (String mListTable : listTables) {
	    if (!mListTable.equalsIgnoreCase("TABLE_META")) {
		try {
		    this.getJdbcTemplate().execute("DELETE FROM " + mListTable);
		} catch (Exception e) {
		    LOG.error("Error Deleting Queries", e);
		}
	    }
	}
    }

    private List<String> readQueries(String pFileName) {
	List<String> lQueries = new ArrayList<String>();
	InputStream stream = null;
	try {
	    stream = this.getClass().getClassLoader().getResourceAsStream(pFileName);
	    lQueries = IOUtils.readLines(stream);
	} catch (IOException ex) {
	    LOG.error("Error reading Queries", ex);
	} finally {
	    IOUtils.closeQuietly(stream);
	}
	return lQueries;
    }

    private Collection<String> listTables() throws SQLException {
	return this.getJdbcTemplate().queryForList("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA LIKE 'public' ORDER BY TABLE_NAME", String.class);
    }

    private void checkUpdates() throws SQLException {
	if (doesTableExist("TABLE_META")) {
	    List<String> lExecutedQueries = this.getJdbcTemplate().queryForList("SELECT QUERY FROM TABLE_META ORDER BY ID", String.class);
	    LOG.info("Executed Queries Count : " + lExecutedQueries.size());
	    List<String> lNewQueries = readQueries(Constants.APP_DB_SCRIPTPATH);
	    LOG.info("File Queries Count : " + lNewQueries.size());
	    if (lNewQueries.size() >= lExecutedQueries.size()) {
		List<String> lOldQueries = lNewQueries.subList(0, lExecutedQueries.size());
		LOG.info("Old Queries Count  " + lOldQueries.size());
		for (int i = 0; i < lExecutedQueries.size(); i++) {
		    Boolean x = lExecutedQueries.get(i).equals(lOldQueries.get(i));
		    if (!x) {
			LOG.info("DB Query Line : " + i + " : " + lExecutedQueries.get(i));
			LOG.info("SQL File Query: " + i + " : " + lOldQueries.get(i));
		    }
		}
		if (CollectionUtils.isEqualCollection(lExecutedQueries, lOldQueries)) {
		    for (int mI = lExecutedQueries.size(); mI < lNewQueries.size(); mI++) {
			LOG.info("Executing Query No : " + mI);
			if (!lNewQueries.get(mI).isEmpty()) {
			    this.getJdbcTemplate().execute(lNewQueries.get(mI));
			    this.getJdbcTemplate().update("INSERT INTO TABLE_META (QUERY, EXECUTEDAT, EXECUTED) VALUES (?,?,?)", lNewQueries.get(mI), new Date(), true);
			}
		    }
		} else {
		    throw new WorkflowException("Old Queries is not matching with the DB Executed, Application is not Starting UP.");
		}
	    } else {
		throw new WorkflowException("New File Queries is less than with the DB Executed, Application is not Starting UP.");
	    }
	    if (lNewQueries.size() > 0) {
		if (wFConfig.getProfileName().startsWith("DL")) {
		    LOG.info("Deleting Travelport related Records");
		    List<String> lDelQueries = readQueries(Constants.APP_DB_DEL_TP);
		    for (String lDelQuery : lDelQueries) {
			this.getJdbcTemplate().execute(lDelQuery);
		    }
		} else if (wFConfig.getProfileName().startsWith("TP")) {
		    LOG.info("Deleting Delta related Records");
		    List<String> lDelQueries = readQueries(Constants.APP_DB_DEL_DL);
		    for (String lDelQuery : lDelQueries) {
			this.getJdbcTemplate().execute(lDelQuery);
		    }
		} else {
		    LOG.info("No Records deleted, we have both TP and DL");
		}
	    }
	} else {
	    LOG.info("Creating Meta Table");
	    this.getJdbcTemplate().execute("CREATE TABLE TABLE_META (ID SERIAL PRIMARY KEY, QUERY TEXT, EXECUTEDAT timestamp without time zone, EXECUTED BOOLEAN)");
	    checkUpdates();
	}
    }
}
