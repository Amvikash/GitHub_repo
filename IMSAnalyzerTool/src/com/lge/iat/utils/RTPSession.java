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
public class RTPSession {
    private int callId ;   
    private int codecType;
    private int cumJitter;
    private int maxJitter;
    private int packetLoss;
    
     public void setCallId(int callId) {
        this.callId = callId;
    }

    public int getCallId() {
        return callId;
    }
    
    public void setCodecType(int codecType) {
        this.codecType = codecType;
    }
    
    public void setCumJitter(int cumJitter) {
        this.cumJitter = cumJitter;
    }
    
    public void setMaxJitter(int maxJitter) {
        this.maxJitter = maxJitter;
    }
    
    public void setPacketLoss(int packetLoss) {
        this.packetLoss = packetLoss;
    }
    
    public int getCodecType() {
        return this.codecType;
    }
    
    public int getCumJitter() {
        return this.cumJitter;
    }
    
    public int getMaxJitter() {
        return this.maxJitter;
    }
    
    public int getPacketLoss() {
        return this.packetLoss;
    }
}
