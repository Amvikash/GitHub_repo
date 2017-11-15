/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.perfindicatorapp.perfindicator.impl.callstateindicator.interfaces;

import com.lge.iat.perfindicatorapp.perfindicator.impl.callstateindicator.impl.CallStatePacketPercLossNode;
import com.lge.iat.perfindicatorapp.perfindicator.impl.callstateindicator.impl.CallStateSuccessFailNode;
import com.lge.iat.perfindicatorapp.perfindicator.impl.callstateindicator.impl.ICallStateNodeChangeListener;
import com.lge.iat.utils.ImsAnalyzeToolConstants;
import com.lge.iat.utils.RTPSession;
import java.util.ArrayList;
import javafx.stage.Stage;

/**
 *
 * @author jitendra.parkar
 */
public class CallStateRootFactory {
    public static ICallStateNode createRootNode(ImsAnalyzeToolConstants.RootNodeName nodeName, ICallStateNodeChangeListener nodeChangeListener, ArrayList<RTPSession> rtpSessionList, Stage stage,String dbPath) {
        if(nodeName == ImsAnalyzeToolConstants.RootNodeName.CallSuccess) {
            return new CallStateSuccessFailNode(nodeChangeListener,rtpSessionList,stage,dbPath);
        } else if(nodeName == ImsAnalyzeToolConstants.RootNodeName.PacketLostPerc) {
            return new CallStatePacketPercLossNode(nodeChangeListener,rtpSessionList,stage,dbPath);
        } else {
            return null;
        }
    }
}
