/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.perfindicatorapp.perfindicator.impl;

import com.lge.iat.utils.CallSession;
import com.lge.iat.db.interfaces.IIMSAnalyzerDBHelper;
import com.lge.iat.db.interfaces.IMSAnalyzerDBManager;
import com.lge.iat.utils.IMSTab;
import com.lge.iat.perfindicatorapp.perfindicator.impl.mediaindicator.interfaces.IMediaNode;
import com.lge.iat.perfindicatorapp.perfindicator.impl.mediaindicator.impl.INodeChangeListener;
import com.lge.iat.utils.ImsAnalyzeToolConstants;
import com.lge.iat.perfindicatorapp.perfindicator.impl.mediaindicator.interfaces.MediaRootFactory;
import com.lge.iat.perfindicatorapp.perfindicator.interfaces.IPerformanceIndicatorPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 *
 * @author jitendra.parkar
 */
public class MediaIndicatorPane implements IPerformanceIndicatorPane, INodeChangeListener{
    private IIMSAnalyzerDBHelper mIMSAnalyzerDBHelper = null;
    private ITabSelectionListener mTabSelectionListener = null;
    private ArrayList<CallSession> mCallSessionList = null;
    private IMSTab nMedia = null;
    private String mStartDate = null;
    private String mEndDate = null;
    private Stage mStage = null;
    private String mDBPath = null;
    private ArrayList<CallSession> callSessionObjList = new ArrayList<>();
    public MediaIndicatorPane(ITabSelectionListener tabSelectionListener,Stage prStage, String startDate, String endDate,String dbPath, ArrayList<CallSession> callSessionList) {
         mIMSAnalyzerDBHelper = IMSAnalyzerDBManager.getImsAnalyzerDBHelper(dbPath);
         mTabSelectionListener = tabSelectionListener;
         mStartDate = startDate;
         mEndDate = endDate;
         mStage = prStage;
         mDBPath = dbPath;
         mCallSessionList = callSessionList;
    }
    
    @Override
    public IMSTab createPane() {
        if(nMedia != null) {
            return nMedia;
        }
        callSessionObjList.clear();
        if(createCallSessionFrmDB() == false) {
            return null;
        }
        nMedia = new IMSTab(ImsAnalyzeToolConstants.TabName.MediaIndicatorPane);
        nMedia.setText("Media");
        IMediaNode mediaNode = MediaRootFactory.createRootNode(ImsAnalyzeToolConstants.RootNodeName.Media, this, mCallSessionList, -1,mStage,mDBPath);
        Node nodeRoot = mediaNode.createNode();
        nMedia.setContent(nodeRoot);
        return nMedia;
    }

    @Override
    public void changeNode(ImsAnalyzeToolConstants.RootNodeName nodeName, int index) {
        nMedia.setContent(null);
        IMediaNode node = MediaRootFactory.createRootNode(nodeName, this, mCallSessionList, index,mStage,mDBPath);
        Node nodeRoot = node.createNode();
        nMedia.setContent(nodeRoot); 
    }
    
    private boolean createCallSessionFrmDB() {
        String whereClauseCallSession = "SELECT * FROM call_session WHERE time BETWEEN "+"\'"+mStartDate+ " 00:00"+"\'"+ " AND "+"\'"+mEndDate+" 59:59"+"\'";
        ResultSet cursor = mIMSAnalyzerDBHelper.rawQuery(whereClauseCallSession);
        if(cursor == null) {
            return false;
        }
        try {
            while (cursor.next()) {
                CallSession mCallSession = new CallSession();
                mCallSession.setId(cursor.getInt("id"));
                mCallSession.setCallId(cursor.getString("call_id"));
                mCallSession.setCalleeName(cursor.getString("callee_name"));
                mCallSession.setCallerName(cursor.getString("caller_name"));
                mCallSession.setType(cursor.getInt("type"));
                mCallSession.setDirection(cursor.getInt("direction"));
                mCallSession.setResponseCode(cursor.getInt("response_code"));
                mCallSession.setTime(cursor.getString("time"));
                mCallSession.setDuration(cursor.getInt("duration"));
                mCallSession.setOrgnNum(cursor.getString("originated_no"));
                mCallSession.setTermNum(cursor.getString("terminated_no"));
                mCallSession.setEndReason(cursor.getString("end_reason"));
                mCallSession.setCellId(cursor.getString("cell_id"));
                mCallSession.setSetTime(cursor.getInt("set_time"));
                callSessionObjList.add(mCallSession);
            }
        }
        catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
