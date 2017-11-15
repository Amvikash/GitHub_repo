/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.perfindicatorapp.perfindicator.impl.mediaindicator.impl;

import com.lge.iat.utils.ImsAnalyzeToolConstants;

/**
 *
 * @author jitendra.parkar
 */
public interface INodeChangeListener {
    public void changeNode(ImsAnalyzeToolConstants.RootNodeName nodeName, int index);
}
