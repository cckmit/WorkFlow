/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.base;

import javax.persistence.MappedSuperclass;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;

/**
 *
 * @author USER
 */
@FilterDefs({ @FilterDef(name = "activeFilter") })
@Filter(name = "activeFilter", condition = "active = 'Y'")
@MappedSuperclass
public abstract class BaseBeans implements IBeans {

}
