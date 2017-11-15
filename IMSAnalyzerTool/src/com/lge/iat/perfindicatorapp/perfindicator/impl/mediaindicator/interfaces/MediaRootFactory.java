/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.perfindicatorapp.perfindicator.impl.mediaindicator.interfaces;

import com.lge.iat.perfindicatorapp.perfindicator.impl.mediaindicator.impl.CallMediaNode;
import com.lge.iat.utils.CallSession;
import com.lge.iat.perfindicatorapp.perfindicator.impl.mediaindicator.impl.GraphMediaNode;
import com.lge.iat.perfindicatorapp.perfindicator.impl.mediaindicator.impl.INodeChangeListener;
import com.lge.iat.utils.ImsAnalyzeToolConstants;
import com.lge.iat.perfindicatorapp.perfindicator.impl.mediaindicator.impl.MediaNode;
import java.util.ArrayList;
import javafx.stage.Stage;

/**
 *
 * @author jitendra.parkar
 */
public class MediaRootFactory {
    public static IMediaNode createRootNode(ImsAnalyzeToolConstants.RootNodeName nodeName, INodeChangeListener nodeChangeListener, ArrayList<CallSession> callSessionList, int index, Stage stage,String dbPath) {
        if(nodeName == ImsAnalyzeToolConstants.RootNodeName.Media) {
            return new MediaNode(nodeChangeListener,callSessionList,stage,dbPath);
        } else if(nodeName == ImsAnalyzeToolConstants.RootNodeName.CallMedia) {
            return new CallMediaNode(nodeChangeListener,callSessionList,index,stage,dbPath);
        } else if(nodeName == ImsAnalyzeToolConstants.RootNodeName.GraphMedia) {
            return new GraphMediaNode(nodeChangeListener,callSessionList,index,stage,dbPath);
        } else {
            return null;
        }
    }
}
