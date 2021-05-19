/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

/**
 *
 * @author USER
 */
public class ExecModel {

    private User user;
    private AuthSystem system;
    private String command;

    public ExecModel(User user, AuthSystem system, String command) {
	this.user = user;
	this.system = system;
	this.command = command;
    }

    public ExecModel() {
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public AuthSystem getSystem() {
	return system;
    }

    public void setSystem(AuthSystem system) {
	this.system = system;
    }

    public String getCommand() {
	return command;
    }

    public void setCommand(String command) {
	this.command = command;
    }

}
