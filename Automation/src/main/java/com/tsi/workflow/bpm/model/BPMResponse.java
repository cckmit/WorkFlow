/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.bpm.model;

import java.util.List;

/**
 *
 * @author User
 */
public class BPMResponse {

    private List<BPMProcess> data;
    private Integer total;
    private Integer start;
    private String sort;
    private String order;
    private Integer size;

    public List<BPMProcess> getData() {
	return data;
    }

    public void setData(List<BPMProcess> data) {
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
