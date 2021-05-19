/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tsi.workflow.websocket;

import com.tsi.workflow.utils.Constants;
import java.io.Serializable;

/**
 *
 * @author prabhu.prabhakaran
 */
public class WSSMessage implements Serializable {

    private static final long serialVersionUID = -3753701701137388623L;

    private Constants.Channels channel;
    private String userId;
    private String planId;
    private Object message;
    private boolean finalMessage;
    private String userIdAndChannel;
    private String broadCastPath;

    public WSSMessage(Constants.Channels channel, String userId, String planId, Object message, boolean finalMessage) {
	this.channel = channel;
	this.userId = userId;
	this.planId = planId;
	this.message = message;
	this.finalMessage = finalMessage;
	this.broadCastPath = "/" + channel + "/" + userId + "/" + planId;
	this.userIdAndChannel = userId + "-" + channel;
    }

    public WSSMessage(Constants.Channels channel, String userId, String planId, Object message) {
	this.channel = channel;
	this.userId = userId;
	this.planId = planId;
	this.message = message;
	this.broadCastPath = "/" + channel + "/" + userId + "/" + planId;
	this.userIdAndChannel = userId + "-" + channel;
    }

    public void switchChannel() {
	this.userIdAndChannel = this.channel.name();
    }

    public Constants.Channels getChannel() {
	return channel;
    }

    public void setChannel(Constants.Channels channel) {
	this.channel = channel;
    }

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public String getPlanId() {
	return planId;
    }

    public void setPlanId(String planId) {
	this.planId = planId;
    }

    public Object getMessage() {
	return message;
    }

    public void setMessage(Object message) {
	this.message = message;
    }

    public boolean isFinalMessage() {
	return finalMessage;
    }

    public void setFinalMessage(boolean finalMessage) {
	this.finalMessage = finalMessage;
    }

    public String getUserIdAndChannel() {
	return userIdAndChannel;
    }

    public void setUserIdAndChannel(String userIdAndChannel) {
	this.userIdAndChannel = userIdAndChannel;
    }

    public String getBroadCastPath() {
	return broadCastPath;
    }

    public void setBroadCastPath(String broadCastPath) {
	this.broadCastPath = broadCastPath;
    }

}
