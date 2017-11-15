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
public class MediaGraphData {
    private int id;
    private int lostPacket;
    private String time;
    private int jitBuffer;
    private boolean audioSilencePeriod;
    private int callID;

    public void setId(int id) {
        this.id = id;
    }

    public void setLostPacket(int lostPacket) {
        this.lostPacket = lostPacket;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setJitBuffer(int jitBuffer) {
        this.jitBuffer = jitBuffer;
    }

    public void setAudioSilencePeriod(boolean audioSilencePeriod) {
        this.audioSilencePeriod = audioSilencePeriod;
    }

    public void setCallID(int callID) {
        this.callID = callID;
    }

    public int getId() {
        return id;
    }

    public int getLostPacket() {
        return lostPacket;
    }

    public String getTime() {
        return time;
    }

    public int getJitBuffer() {
        return jitBuffer;
    }

    public boolean isAudioSilencePeriod() {
        return audioSilencePeriod;
    }

    public int getCallID() {
        return callID;
    }
}
