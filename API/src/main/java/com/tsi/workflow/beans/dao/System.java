/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.dao;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.base.BaseBeans;
import com.tsi.workflow.interfaces.ISystem;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Filter;

/**
 *
 * @author USER
 */
@Entity
@Table(name = "system")
@NamedQueries({ @NamedQuery(name = "System.findAll", query = "SELECT s FROM System s") })
public class System extends BaseBeans implements Serializable, ISystem, Comparable<System> {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 10)
    @Column(name = "name")
    private String name;
    @Size(max = 2147483647)
    @Column(name = "ipaddress")
    private String ipaddress;
    @Column(name = "portno")
    private Integer portno;
    @Column(name = "defalut_put_level")
    private Integer defalutPutLevel;
    @Size(max = 50)
    @Column(name = "created_by")
    private String createdBy;
    @Column(name = "created_dt")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date createdDt;
    @Size(max = 50)
    @Column(name = "modified_by")
    private String modifiedBy;
    @Column(name = "modified_dt")
    @Temporal(TemporalType.TIMESTAMP)
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date modifiedDt;
    @Size(max = 1)
    @Column(name = "active")
    private String active;
    @Size(max = 1)
    @Column(name = "loadset_name_prefix")
    private String loadsetNamePrefix;
    @Column(name = "default_prod_cpu")
    private Integer defaultProdCpu;
    @Column(name = "default_native_cpu")
    private Integer defaultNativeCpu;
    @Column(name = "default_pre_prod_cpu")
    private Integer defaultPreProdCpu;
    @OneToMany(mappedBy = "systemId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<Dbcr> dbcrList;
    @OneToMany(mappedBy = "systemId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<SystemLoad> systemLoadList;
    @OneToMany(mappedBy = "systemId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<SystemLoadActions> systemLoadActionsList;
    @OneToMany(mappedBy = "systemId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<ProductionLoads> productionLoadsList;
    @OneToMany(mappedBy = "systemId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<SystemCpu> systemCpuList;
    @JoinColumn(name = "platform_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Platform platformId;
    @OneToMany(mappedBy = "systemId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<Build> buildList;
    @OneToMany(mappedBy = "systemId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<Vpars> vparsList;
    @OneToMany(mappedBy = "systemId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<PutLevel> putLevelList;
    @OneToMany(mappedBy = "systemId", fetch = FetchType.LAZY)
    @Filter(name = "activeFilter", condition = "active = 'Y'")
    private List<LoadCategories> loadCategoriesList;
    @Size(max = 3)
    @Column(name = "alias_name")
    private String aliasName;

    public System() {
    }

    public System(Integer id) {
	this.id = id;
    }

    public Integer getId() {
	return id;
    }

    public void setId(Integer id) {
	this.id = id;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getAliasName() {
	return aliasName;
    }

    public void setAliasName(String aliasName) {
	this.aliasName = aliasName;
    }

    public String getIpaddress() {
	return ipaddress;
    }

    public void setIpaddress(String ipaddress) {
	this.ipaddress = ipaddress;
    }

    public Integer getPortno() {
	return portno;
    }

    public void setPortno(Integer portno) {
	this.portno = portno;
    }

    public Integer getDefalutPutLevel() {
	return defalutPutLevel;
    }

    public void setDefalutPutLevel(Integer defalutPutLevel) {
	this.defalutPutLevel = defalutPutLevel;
    }

    @Override
    public String getCreatedBy() {
	return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
    }

    @Override
    public Date getCreatedDt() {
	return createdDt;
    }

    @Override
    public void setCreatedDt(Date createdDt) {
	this.createdDt = createdDt;
    }

    @Override
    public String getModifiedBy() {
	return modifiedBy;
    }

    @Override
    public void setModifiedBy(String modifiedBy) {
	this.modifiedBy = modifiedBy;
    }

    @Override
    public Date getModifiedDt() {
	return modifiedDt;
    }

    @Override
    public void setModifiedDt(Date modifiedDt) {
	this.modifiedDt = modifiedDt;
    }

    @Override
    public String getActive() {
	return active;
    }

    @Override
    public void setActive(String active) {
	this.active = active;
    }

    public String getLoadsetNamePrefix() {
	return loadsetNamePrefix;
    }

    public void setLoadsetNamePrefix(String loadsetNamePrefix) {
	this.loadsetNamePrefix = loadsetNamePrefix;
    }

    public Integer getDefaultPreProdCpu() {
	return defaultPreProdCpu;
    }

    public void setDefaultPreProdCpu(Integer defaultPreProdCpu) {
	this.defaultPreProdCpu = defaultPreProdCpu;
    }

    public Integer getDefaultProdCpu() {
	return defaultProdCpu;
    }

    public void setDefaultProdCpu(Integer defaultProdCpu) {
	this.defaultProdCpu = defaultProdCpu;
    }

    public Integer getDefaultNativeCpu() {
	return defaultNativeCpu;
    }

    public void setDefaultNativeCpu(Integer defaultNativeCpu) {
	this.defaultNativeCpu = defaultNativeCpu;
    }

    public List<Dbcr> getDbcrList() {
	return dbcrList;
    }

    public void setDbcrList(List<Dbcr> dbcrList) {
	this.dbcrList = dbcrList;
    }

    public List<SystemLoad> getSystemLoadList() {
	return systemLoadList;
    }

    public void setSystemLoadList(List<SystemLoad> systemLoadList) {
	this.systemLoadList = systemLoadList;
    }

    public List<SystemLoadActions> getSystemLoadActionsList() {
	return systemLoadActionsList;
    }

    public void setSystemLoadActionsList(List<SystemLoadActions> systemLoadActionsList) {
	this.systemLoadActionsList = systemLoadActionsList;
    }

    public List<ProductionLoads> getProductionLoadsList() {
	return productionLoadsList;
    }

    public void setProductionLoadsList(List<ProductionLoads> productionLoadsList) {
	this.productionLoadsList = productionLoadsList;
    }

    public List<SystemCpu> getSystemCpuList() {
	return systemCpuList;
    }

    public void setSystemCpuList(List<SystemCpu> systemCpuList) {
	this.systemCpuList = systemCpuList;
    }

    public Platform getPlatformId() {
	return platformId;
    }

    public void setPlatformId(Platform platformId) {
	this.platformId = platformId;
    }

    public List<Build> getBuildList() {
	return buildList;
    }

    public void setBuildList(List<Build> buildList) {
	this.buildList = buildList;
    }

    public List<Vpars> getVparsList() {
	return vparsList;
    }

    public void setVparsList(List<Vpars> vparsList) {
	this.vparsList = vparsList;
    }

    public List<PutLevel> getPutLevelList() {
	return putLevelList;
    }

    public void setPutLevelList(List<PutLevel> putLevelList) {
	this.putLevelList = putLevelList;
    }

    public List<LoadCategories> getLoadCategoriesList() {
	return loadCategoriesList;
    }

    public void setLoadCategoriesList(List<LoadCategories> loadCategoriesList) {
	this.loadCategoriesList = loadCategoriesList;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (id != null ? id.hashCode() : 0);
	return hash;
    }

    @Override
    public boolean equals(Object object) {
	// TODO: Warning - this method won't work in the case the id fields are not set
	if (!(object instanceof System)) {
	    return false;
	}
	System other = (System) object;
	if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
	    return false;
	}
	return true;
    }

    @Override
    public String toString() {
	return "com.tsi.workflow.beans.dao.System[ id=" + id + " ]";
    }

    @Override
    public int compareTo(System lValue) {
	return name.compareTo(lValue.getName());
    }

}
