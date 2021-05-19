/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.base;

import java.util.Date;

/**
 *
 * @author USER
 */
public interface IBeans {

    public String getActive();

    public void setActive(String active);

    public String getCreatedBy();

    public void setCreatedBy(String createdBy);

    public Date getCreatedDt();

    public void setCreatedDt(Date createdDt);

    public String getModifiedBy();

    public void setModifiedBy(String modifiedBy);

    public Date getModifiedDt();

    public void setModifiedDt(Date modifiedDt);

}
