/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.dao.external;

import com.tsi.workflow.User;
import com.tsi.workflow.beans.ui.YodaResult;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

/**
 *
 * @author USER
 */
@Repository
public class TestSystemLoadDAO {

    private JdbcTemplate jdbcTemplate;

    private static final Logger LOG = Logger.getLogger(TestSystemLoadDAO.class.getName());

    public void setDataSource(DataSource dataSource) {
	this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getJdbcTemplate() {
	return jdbcTemplate;
    }

    public List<YodaResult> getVparsIP(String VparName) {
	SimpleJdbcCall jdbcCall = new SimpleJdbcCall(getJdbcTemplate()).withProcedureName("usp_tpf_linux_test_getip").returningResultSet("#result-set-1", BeanPropertyRowMapper.newInstance(YodaResult.class));
	MapSqlParameterSource lParams = new MapSqlParameterSource();
	lParams.addValue("lab", VparName);
	Map<String, Object> lResult = jdbcCall.execute(lParams);
	List<YodaResult> list = (List<YodaResult>) lResult.get("#result-set-1");
	// if (list.size() > 0 && list.get(0).getRc() == 0) {
	// String ipAddress = list.get(0).getIp();
	// LOG.info("YODA Received IP Address: " + ipAddress);
	// return ipAddress.replaceAll("^0+", "").replaceAll("\\.0+", "\\.");
	// } else if (list.size() > 0) {
	// LOG.error("YODA Execution RC: " + list.get(0).getRc() + " Message : " +
	// list.get(0).getMessage());
	// }
	return list;
    }

    public List<YodaResult> loadAndActivate(User user, String VparName, String pLoadset) {
	if (pLoadset == null) {
	    return null;
	}
	SimpleJdbcCall jdbcCall = new SimpleJdbcCall(getJdbcTemplate()).withProcedureName("usp_tpf_linux_test_load").returningResultSet("#result-set-1", BeanPropertyRowMapper.newInstance(YodaResult.class));
	MapSqlParameterSource lParams = new MapSqlParameterSource();
	lParams.addValue("lab", VparName);
	lParams.addValue("lset", pLoadset);
	lParams.addValue("Linuxid", "YODALINUX");
	lParams.addValue("uname", "Linux Automation");
	Map<String, Object> lResult = jdbcCall.execute(lParams);
	List<YodaResult> list = (List<YodaResult>) lResult.get("#result-set-1");
	// if (list.size() > 0) {
	// LOG.info("YODA Execution Message : " + list.get(0).getMessage());
	// return list.get(0).getRc() == 0;
	// }
	return list;
    }

    public List<YodaResult> activate(User user, String VparName, String pLoadset) {
	if (pLoadset == null) {
	    return null;
	}
	SimpleJdbcCall jdbcCall = new SimpleJdbcCall(getJdbcTemplate()).withProcedureName("usp_tpf_linux_test_activate").returningResultSet("#result-set-1", BeanPropertyRowMapper.newInstance(YodaResult.class));
	MapSqlParameterSource lParams = new MapSqlParameterSource();
	lParams.addValue("lab", VparName);
	lParams.addValue("lset", pLoadset);
	lParams.addValue("Linuxid", "YODALINUX");
	lParams.addValue("uname", "Linux Automation");
	Map<String, Object> lResult = jdbcCall.execute(lParams);
	List<YodaResult> list = (List<YodaResult>) lResult.get("#result-set-1");
	// if (list.size() > 0) {
	// LOG.info("YODA Execution Message : " + list.get(0).getMessage());
	// return list.get(0).getRc() == 0;
	// }
	return list;
    }

    public List<YodaResult> deActivate(User user, String VparName, String pLoadset) {
	if (pLoadset == null) {
	    return null;
	}
	SimpleJdbcCall jdbcCall = new SimpleJdbcCall(getJdbcTemplate()).withProcedureName("usp_tpf_linux_test_deactivate").returningResultSet("#result-set-1", BeanPropertyRowMapper.newInstance(YodaResult.class));
	MapSqlParameterSource lParams = new MapSqlParameterSource();
	lParams.addValue("lab", VparName);
	lParams.addValue("lset", pLoadset);
	lParams.addValue("Linuxid", "YODALINUX");
	lParams.addValue("uname", "Linux Automation");
	Map<String, Object> lResult = jdbcCall.execute(lParams);
	List<YodaResult> list = (List<YodaResult>) lResult.get("#result-set-1");
	// if (list.size() > 0) {
	// LOG.info("YODA Execution Message : " + list.get(0).getMessage());
	// return list.get(0).getRc() == 0;
	// }
	return list;
    }

    public List<YodaResult> deleteAndDeActivate(User user, String VparName, String pLoadset) {
	if (pLoadset == null) {
	    return null;
	}
	SimpleJdbcCall jdbcCall = new SimpleJdbcCall(getJdbcTemplate()).withProcedureName("usp_tpf_linux_test_delete").returningResultSet("#result-set-1", BeanPropertyRowMapper.newInstance(YodaResult.class));
	MapSqlParameterSource lParams = new MapSqlParameterSource();
	lParams.addValue("lab", VparName);
	lParams.addValue("lset", pLoadset);
	lParams.addValue("Linuxid", "YODALINUX");
	lParams.addValue("uname", "Linux Automation");
	Map<String, Object> lResult = jdbcCall.execute(lParams);
	List<YodaResult> list = (List<YodaResult>) lResult.get("#result-set-1");
	// if (list.size() > 0) {
	// LOG.info("YODA Execution Message : " + list.get(0).getMessage());
	// return list.get(0).getRc() == 0;
	// }
	return list;
    }

}
