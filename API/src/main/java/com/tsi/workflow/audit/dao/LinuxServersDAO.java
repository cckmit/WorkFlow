/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.dao;

import com.tsi.workflow.audit.base.AuditBaseDAO;
import com.tsi.workflow.audit.beans.dao.LinuxServers;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Radha.Adhimoolam
 */
@Repository
public class LinuxServersDAO extends AuditBaseDAO<LinuxServers> {

    public LinuxServers findByHostNameAndProfile(String hostName, String hostProfile) {
	String lQuery = "SELECT hs from LinuxServers hs where hs.dnsName = :HostName and hs.hostProfile = :HostProfile";
	return (LinuxServers) getCurrentSession().createQuery(lQuery).setParameter("HostName", hostName).setParameter("HostProfile", hostProfile).uniqueResult();

    }
}
