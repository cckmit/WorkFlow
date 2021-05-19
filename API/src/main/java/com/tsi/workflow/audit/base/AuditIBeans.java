/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.audit.base;

import java.util.Date;

/**
 *
 * @author Radha.Adhimoolam
 */
public interface AuditIBeans {

    public Date getCreatedDt();

    public void setCreatedDt(Date createdDt);

    public Date getModifiedDt();

    public void setModifiedDt(Date modifiedDt);

}
