/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.configgenerator;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

/**
 *
 * @author USER
 */
public class SortedProperties extends Properties {

    /**
     * Overrides, called by the store method.
     */
    @SuppressWarnings("unchecked")
    public synchronized Enumeration keys() {
	Enumeration keysEnum = super.keys();
	Vector keyList = new Vector();
	while (keysEnum.hasMoreElements()) {
	    keyList.add(keysEnum.nextElement());
	}
	Collections.sort(keyList);
	return keyList.elements();
    }

    @Override
    public Set<Object> keySet() {
	return (SortedSet) new TreeSet(super.keySet());
    }

}
