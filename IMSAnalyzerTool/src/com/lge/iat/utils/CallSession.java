/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.utils;

/**
 *
 * @author jitendra.parkar
 */
public class CallSession {
    private int id;
    private int type;
    private int direction;
    private int responseCode;
    private int setTime;
    private int duration;
    private String time;
    private String callId;
    private String orgnNum;
    private String termNum;
    private String endReason;
    private String cellId;
    private String callerName;
    private String calleeName;

    
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    public void setType(int type) {
        this.type = type;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public void setSetTime(int setTime) {
        this.setTime = setTime;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public void setOrgnNum(String orgnNum) {
        this.orgnNum = orgnNum;
    }

    public void setTermNum(String termNum) {
        this.termNum = termNum;
    }

    public void setEndReason(String endReason) {
        this.endReason = endReason;
    }

    public void setCellId(String cellId) {
        this.cellId = cellId;
    }

    public void setCallerName(String callerName) {
        this.callerName = callerName;
    }

    public void setCalleeName(String calleeName) {
        this.calleeName = calleeName;
    }

    public int getType() {
        return type;
    }

    public int getDirection() {
        return direction;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public int getSetTime() {
        return setTime;
    }

    public int getDuration() {
        return duration;
    }

    public String getTime() {
        return time;
    }

    public String getCallId() {
        return callId;
    }

    public String getOrgnNum() {
        return orgnNum;
    }

    public String getTermNum() {
        return termNum;
    }

    public String getEndReason() {
        return endReason;
    }

    public String getCellId() {
        return cellId;
    }

    public String getCallerName() {
        return callerName;
    }

    public String getCalleeName() {
        return calleeName;
    }
    
}
