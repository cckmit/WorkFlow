/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.dao.external;

import com.tsi.workflow.beans.dao.Project;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author USER
 */
@Repository
public class CSRNumberDAO {

    private JdbcTemplate jdbcTemplate;

    private static final Logger LOG = Logger.getLogger(CSRNumberDAO.class.getName());

    public void setDataSource(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getJdbcTemplate() {
	return jdbcTemplate;
    }

    public List findFiltered(String filter) {
	String lQuery = "";
	List<Project> problemTicketList = new ArrayList<>();

	if (filter != null && !filter.isEmpty()) {
	    lQuery = "SELECT TOP 50" + " CONCAT(\"CSR Number\",' ',\"Product Number\") AS projectNumber," + " ProjectName AS projectName,"
	    // + " \"Project/Investment Unique ID\" AS sponsorId,"
		    + " ProjectOwnerName AS managerId," + " Portfolio_OBS AS lineOfBusiness" + " FROM pjrep.MSP_EpmProject_UserView" + " WHERE" + " \"CSR Number\" is not NULL" + " AND \"Project Status\" LIKE 'Active'" + " AND \"CSR Number\" LIKE '" + filter + "%'" + " ORDER BY \"CSR Number\"";
	    problemTicketList = this.getJdbcTemplate().query(lQuery, new BeanPropertyRowMapper(Project.class));
	} else {
	    lQuery = "SELECT TOP 50" + " CONCAT(\"CSR Number\",' ',\"Product Number\") AS projectNumber," + " ProjectName AS projectName,"
	    // + " \"Project/Investment Unique ID\" AS sponsorId,"
		    + " ProjectOwnerName AS managerId," + " Portfolio_OBS AS lineOfBusiness" + " FROM pjrep.MSP_EpmProject_UserView" + " WHERE" + " \"CSR Number\" is not NULL" + " AND \"Project Status\" LIKE 'Active'" + " ORDER BY \"CSR Number\"";
	    problemTicketList = this.getJdbcTemplate().query(lQuery, new BeanPropertyRowMapper(Project.class));
	}
	return problemTicketList;
    }

    public List findAll() {
	String lQuery = "SELECT" + " CONCAT(\"CSR Number\",' ',\"Product Number\") AS projectNumber," + " ProjectName AS projectName,"
	// + " \"Project/Investment Unique ID\" AS sponsorId,"
		+ " ProjectManagerName AS managerId," + " Portfolio_OBS AS lineOfBusiness" + " FROM pjrep.MSP_EpmProject_UserView" + " WHERE" + " \"CSR Number\" is not NULL" + " AND \"Project Status\" LIKE 'Active'" + " ORDER BY \"CSR Number\"";

	List<Project> problemTicketList = this.getJdbcTemplate().query(lQuery, new BeanPropertyRowMapper(Project.class));
	return problemTicketList;
    }

    // public void jdbcCallTest() {
    // SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
    // .withSchemaName("tpfdate")
    // .withCatalogName("dbo")
    // .withProcedureName("usp_tpf_linux_test_load");
    // jdbcCall.addDeclaredParameter(new SqlParameter("lab", 12, "varchar"));
    // jdbcCall.execute(callParams);
    // }
}
