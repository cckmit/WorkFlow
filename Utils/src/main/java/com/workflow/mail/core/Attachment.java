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
public class Attachment {

    private String FileName;
    private String ContentType;
    private String ContentUrl;
    private String Content;

    public String getFileName() {
	return FileName;
    }

    public void setFileName(String fileName) {
	this.FileName = fileName;
    }

    public String getContentType() {
	return ContentType;
    }

    public void setContentType(String contentType) {
	this.ContentType = contentType;
    }

    public String getContent() {
	return Content;
    }

    public void setContent(String content) {
	this.Content = content;
    }

    public String getContentUrl() {
	return ContentUrl;
    }

    public void setContentUrl(String ContentUrl) {
	this.ContentUrl = ContentUrl;
    }

}
