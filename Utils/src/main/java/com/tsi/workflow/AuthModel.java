/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import com.tsi.workflow.interfaces.ISystem;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class AuthModel {

    private User user;
    private List<AuthSystem> systems = new ArrayList<>();

    public AuthModel() {
    }

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public List<AuthSystem> getSystems() {
	return systems;
    }

    public void setSystems(List<AuthSystem> systems) {
	this.systems = systems;
    }

    public void addSystem(ISystem pSystem) {
	systems.add(new AuthSystem(pSystem.getIpaddress(), pSystem.getPortno(), pSystem.getName()));
    }

}
