/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.beans.dao;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tsi.workflow.json.JsonDateDeSerializer;
import com.tsi.workflow.json.JsonDateSerializer;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author yeshwanth.shenoy
 */
public class LoadFreezeGrouped {

    private String ids;
    private String name;
    private Integer systemid;
    private String load_categories;
    private String reason;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date from_date;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeSerializer.class)
    private Date to_date;
    private String loadcategoryid;

    public Integer getSystemId() {
	return systemid;
    }

    public void setSystemId(Integer systemId) {
	this.systemid = systemId;
    }

    public List<String> getLoadCategoryIdList() {
	return Arrays.asList(this.loadcategoryid.split(","));
    }

    public void setLoadCategoryIdList(String loadCategoryIdList) {
	this.loadcategoryid = String.join(",", loadCategoryIdList);
    }

    public String getIds() {
	return ids;
    }

    public void setIds(String ids) {
	this.ids = ids;
    }

    public List<String> getListIds() {
	return Arrays.asList(this.ids.split(","));
    }

    public void setListIds(List<String> ids) {
	this.ids = String.join(",", ids);
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getLoad_categories() {
	return load_categories;
    }

    public void setLoad_categories(String load_categories) {
	this.load_categories = load_categories;
    }

    public String getReason() {
	return reason;
    }

    public void setReason(String reason) {
	this.reason = reason;
    }

    public Date getFrom_date() {
	return from_date;
    }

    public void setFrom_date(Date from_date) {
	this.from_date = from_date;
    }

    public Date getTo_date() {
	return to_date;
    }

    public void setTo_date(Date to_date) {
	this.to_date = to_date;
    }

    @Override
    public boolean equals(Object object) {
	if (!(object instanceof LoadFreezeGrouped)) {
	    return false;
	}
	LoadFreezeGrouped other = (LoadFreezeGrouped) object;
	if ((this.ids == null && other.ids != null) || (this.ids != null && !this.ids.equals(other.ids))) {
	    return false;
	}
	return true;
    }

    @Override
    public int hashCode() {
	int hash = 0;
	hash += (ids != null ? ids.hashCode() : 0);
	return hash;
    }

}
