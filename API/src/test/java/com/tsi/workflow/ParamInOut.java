/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USER
 */
public class ParamInOut {

    List<Object> in = new ArrayList<>();
    Object out;

    public ParamInOut() {
    }

    public List<Object> getIn() {
	return in;
    }

    public void setIn(List<Object> in) {
	this.in = in;
    }

    public void addIn(Object in) {
	this.in.add(in);
    }

    public Object getOut() {
	return out;
    }

    public void setOut(Object out) {
	this.out = out;
    }
}
