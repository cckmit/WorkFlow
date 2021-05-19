/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.tsi.workflow.interfaces.ISystem;

/**
 *
 * @author USER
 */
public class AuthSystem implements ISystem {

    String ipaddress;
    Integer portno;
    String name;

    public AuthSystem() {
    }

    public AuthSystem(String ipaddress, Integer portno, String name) {
	this.ipaddress = ipaddress;
	this.portno = portno;
	this.name = name;
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

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

}
