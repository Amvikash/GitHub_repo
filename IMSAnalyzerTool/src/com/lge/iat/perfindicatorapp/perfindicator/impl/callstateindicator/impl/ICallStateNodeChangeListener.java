/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.perfindicatorapp.perfindicator.impl.callstateindicator.impl;

import com.lge.iat.utils.ImsAnalyzeToolConstants;

/**
 *
 * @author jitendra.parkar
 */
public interface ICallStateNodeChangeListener {
    public void changeNode(ImsAnalyzeToolConstants.RootNodeName nodeName, ImsAnalyzeToolConstants.CallState callState);
}
