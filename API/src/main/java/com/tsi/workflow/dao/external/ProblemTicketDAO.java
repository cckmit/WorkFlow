package com.tsi.workflow.dao.external;

import com.tsi.workflow.beans.external.ProblemTicket;
import java.util.List;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 *
 * @author User
 */
@Repository
public class ProblemTicketDAO {

    private JdbcTemplate jdbcTemplate;

    private static final Logger LOG = Logger.getLogger(ProblemTicketDAO.class.getName());

    public void setDataSource(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getJdbcTemplate() {
	return jdbcTemplate;
    }

    public List<ProblemTicket> getProblemTicket(String ticketNumber) {
	String lQuery = "SELECT * FROM dbo.z_pr_validation WHERE ref_num = ?";
	List<ProblemTicket> problemTicketList = this.getJdbcTemplate().query(lQuery, new Object[] { ticketNumber }, new BeanPropertyRowMapper(ProblemTicket.class));
	return problemTicketList;
    }
}
