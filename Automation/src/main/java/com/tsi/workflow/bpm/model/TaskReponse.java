/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.bpm.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author User
 */
public class TaskReponse {

    public List<TaskRepresentation> data = new ArrayList<>();
    public Integer total;
    public Integer start;
    public String sort;
    public String order;
    public Integer size;

    public List<TaskRepresentation> getData() {
	return data;
    }

    public void setData(List<TaskRepresentation> data) {
	this.data = data;
    }

    public Integer getTotal() {
	return total;
    }

    public void setTotal(Integer total) {
	this.total = total;
    }

    public Integer getStart() {
	return start;
    }

    public void setStart(Integer start) {
	this.start = start;
    }

    public String getSort() {
	return sort;
    }

    public void setSort(String sort) {
	this.sort = sort;
    }

    public String getOrder() {
	return order;
    }

    public void setOrder(String order) {
	this.order = order;
    }

    public Integer getSize() {
	return size;
    }

    public void setSize(Integer size) {
	this.size = size;
    }

}
