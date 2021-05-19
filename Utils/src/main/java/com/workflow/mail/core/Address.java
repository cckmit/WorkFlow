/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.workflow.mail.core;

/**
 *
 * @author USER
 */
public class Address implements CharSequence {

    private String Address;
    private String Name;
    private String display;

    public Address(String address, String name) {
	this.Address = address;
	this.Name = name;
	this.display = name + " <" + address + ">";
    }

    public String getAddress() {
	return Address;
    }

    public void setAddress(String address) {
	this.Address = address;
    }

    public String getName() {
	return Name;
    }

    public void setName(String name) {
	this.Name = name;
    }

    @Override
    public String toString() {
	return display;
    }

    @Override
    public int length() {
	return display.length();
    }

    @Override
    public char charAt(int index) {
	return display.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
	return display.subSequence(start, end);
    }

}
