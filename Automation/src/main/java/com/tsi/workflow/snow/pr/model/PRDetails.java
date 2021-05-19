/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.snow.pr.model;

/**
 *
 * @author Radha.Adhimoolam
 */
public class PRDetails {
    private String sys_row_error;
    private String u_load_status;
    private String u_effective_date;
    private String u_problem_number;
    private String number;
    private String state;

    public String getNumber() {
	return number;
    }

    public void setNumber(String number) {
	this.number = number;
    }

    public String getState() {
	return state;
    }

    public void setState(String state) {
	this.state = state;
    }

    public String getSys_row_error() {
	return sys_row_error;
    }

    public void setSys_row_error(String sys_row_error) {
	this.sys_row_error = sys_row_error;
    }

    public String getU_load_status() {
	return u_load_status;
    }

    public void setU_load_status(String u_load_status) {
	this.u_load_status = u_load_status;
    }

    public String getU_effective_date() {
	return u_effective_date;
    }

    public void setU_effective_date(String u_effective_date) {
	this.u_effective_date = u_effective_date;
    }

    public String getU_problem_number() {
	return u_problem_number;
    }

    public void setU_problem_number(String u_problem_number) {
	this.u_problem_number = u_problem_number;
    }

}
