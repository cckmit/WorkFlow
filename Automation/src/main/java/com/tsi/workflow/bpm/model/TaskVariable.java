/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.bpm.model;

/**
 *
 * @author User
 */
public class TaskVariable {

    private String name;
    private String scope;
    private Object type;
    private Object value;

    public TaskVariable(String name, String value) {
	this.name = name;
	this.scope = "local";
	this.type = "string";
	this.value = value;
    }

    public TaskVariable(String name, String scope, String type, String value) {
	this.name = name;
	this.scope = scope;
	this.type = type;
	this.value = value;
    }

    public TaskVariable(String name, String scope, String type, boolean value) {
	this.name = name;
	this.scope = scope;
	this.type = type;
	this.value = value;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public String getScope() {
	return scope;
    }

    public void setScope(String scope) {
	this.scope = scope;
    }

    public Object getType() {
	return type;
    }

    public void setType(Object type) {
	this.type = type;
    }

    public Object getValue() {
	return value;
    }

    public void setValue(Object value) {
	this.value = value;
    }

}
