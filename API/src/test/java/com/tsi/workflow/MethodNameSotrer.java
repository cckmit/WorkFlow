/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow;

import java.lang.reflect.Method;
import java.util.Comparator;

/**
 *
 * @author USER
 */
class MethodNameSotrer implements Comparator<Method> {

    public MethodNameSotrer() {
    }

    @Override
    public int compare(Method o1, Method o2) {
	return o1.getName().compareTo(o2.getName());
    }

}
