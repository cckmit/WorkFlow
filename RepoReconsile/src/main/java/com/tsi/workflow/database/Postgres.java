package com.tsi.workflow.database;

import com.tsi.workflow.config.AppConfig;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Database class for the MySQL database.
 * <p>
 * 
 * @author Prabhu.P
 *         </p>
 */
public class Postgres {

    private static final Logger LOG = Logger.getLogger(Postgres.class.getName());
    private static Postgres lPostgres;
    private BasicDataSource lDataSource;

    private Postgres() throws IOException, SQLException, PropertyVetoException, ConfigurationException {
	try {
	    Class.forName(AppConfig.getInstance().getDBDriver());
	} catch (ClassNotFoundException ex) {
	    LOG.info("Error : ", ex);
	}
	lDataSource = new BasicDataSource();
	LOG.info("DB URL : " + AppConfig.getInstance().getDBURL());
	lDataSource.setDriverClassName(AppConfig.getInstance().getDBDriver());
	lDataSource.setUsername(AppConfig.getInstance().getDBUserName());
	lDataSource.setPassword(AppConfig.getInstance().getDBPassword());
	lDataSource.setUrl(AppConfig.getInstance().getDBURL());
	lDataSource.setMinIdle(5);
	lDataSource.setMaxIdle(20);
	lDataSource.setMaxActive(20);
	lDataSource.setMaxOpenPreparedStatements(180);
    }

    public static Postgres getInstance() {
	synchronized (Postgres.class) {
	    if (lPostgres == null) {
		try {
		    lPostgres = new Postgres();
		} catch (Exception ex) {
		    Logger.getLogger(Postgres.class.getName()).log(Level.FATAL, null, ex);
		    lPostgres = null;
		}
	    }
	}
	return lPostgres;
    }

    public void log() {
	LOG.info("Max   : " + lDataSource.getMaxActive() + "; " + "Active: " + lDataSource.getNumActive() + "; " + "Idle  : " + lDataSource.getNumIdle());
    }

    public synchronized Connection getConnection() throws SQLException {
	return this.lDataSource.getConnection();
    }

}
