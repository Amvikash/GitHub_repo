/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.perfindicatorapp.perfindicator.impl;

import com.lge.iat.db.interfaces.IIMSAnalyzerDBHelper;
import com.lge.iat.db.interfaces.IMSAnalyzerDBManager;
import com.lge.iat.perfindicatorapp.perfindicator.impl.callstateindicator.impl.ICallStateNodeChangeListener;
import com.lge.iat.perfindicatorapp.perfindicator.impl.callstateindicator.interfaces.CallStateRootFactory;
import com.lge.iat.perfindicatorapp.perfindicator.impl.callstateindicator.interfaces.ICallStateNode;
import com.lge.iat.utils.IMSTab;
import com.lge.iat.utils.ImsAnalyzeToolConstants;
import com.lge.iat.perfindicatorapp.perfindicator.interfaces.IPerformanceIndicatorPane;
import com.lge.iat.utils.CallSession;
import com.lge.iat.utils.RTPSession;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.scene.Node;
import javafx.stage.Stage;

/**
 *
 * @author jitendra.parkar
 */
public class CallStatIndicatorPane implements IPerformanceIndicatorPane,ICallStateNodeChangeListener{
    private IIMSAnalyzerDBHelper mIMSAnalyzerDBHelper = null;
    private ITabSelectionListener mTabSelectionListener = null;
    private IMSTab mCallStateTab = null;
    private String mStartDate = null;
    private String mEndDate = null;
    private Stage mStage = null;
    private String mDBPath = null;
    private ArrayList<CallSession> callSessionObjList = new ArrayList<>();
    private ArrayList<RTPSession> rtpSessionObjList = new ArrayList<>();
    private ArrayList<Integer> mCallIdList = new ArrayList<>();
    
    public CallStatIndicatorPane(ITabSelectionListener tabSelectionListener,Stage prStage, String startDate, String endDate,String dbPath) {
         mIMSAnalyzerDBHelper = IMSAnalyzerDBManager.getImsAnalyzerDBHelper(dbPath);
         mTabSelectionListener = tabSelectionListener;
         mStartDate = startDate;
         mEndDate = endDate;
         mStage = prStage;
    }
    
    @Override
    public IMSTab createPane() {
        if(mCallStateTab != null) {
            return mCallStateTab;
        }
        callSessionObjList.clear();
        if(createRtpSessionList()== false) {
            return null;
        }
        mCallStateTab = new IMSTab(ImsAnalyzeToolConstants.TabName.CallStatIndicatorPane);
        mCallStateTab.setText("CallState");
        ICallStateNode callSuccessNode = CallStateRootFactory.createRootNode(ImsAnalyzeToolConstants.RootNodeName.CallSuccess, this, rtpSessionObjList,mStage,mDBPath);
        Node nodeRoot = callSuccessNode.createNode();
        mCallStateTab.setContent(nodeRoot);
        return mCallStateTab;
    }
    
    private boolean createCallSessionFrmDB(ImsAnalyzeToolConstants.CallState callState) {
        ArrayList<Integer> idList = new ArrayList<>();
        for(RTPSession rtpSess : rtpSessionObjList) {
           if(callState == ImsAnalyzeToolConstants.CallState.Failed && rtpSess.getPacketLoss() == -1) {
               idList.add(rtpSess.getCallId());
           } else if(callState == ImsAnalyzeToolConstants.CallState.LossGrtFiveLessTen && (rtpSess.getPacketLoss() >= 5 &&  rtpSess.getPacketLoss() < 10) ) {
               idList.add(rtpSess.getCallId());
           } else if(callState == ImsAnalyzeToolConstants.CallState.LossGrtTen && (rtpSess.getPacketLoss() >= 10) ) {
               idList.add(rtpSess.getCallId());
           } else if(callState == ImsAnalyzeToolConstants.CallState.LossLessFive &&(rtpSess.getPacketLoss() < 5 &&  rtpSess.getPacketLoss() >=0) ) {
               idList.add(rtpSess.getCallId());
           }
        }
        callSessionObjList.clear();
        StringBuilder wherePacketLossSB =  new StringBuilder("SELECT * FROM call_session");
            for(int i = 0 ;i<idList.size(); i++) {
                int callId = idList.get(i);
                if(callId <= 0) {
                    break;
                }
                if(i == 0){
                   wherePacketLossSB.append(" WHERE id IS "+callId) ;
                } else {
                   wherePacketLossSB.append(" OR id IS "+callId) ;
                }
            }
            if(!wherePacketLossSB.toString().contains("WHERE")) {
               return false; 
            }
        ResultSet cursor = mIMSAnalyzerDBHelper.rawQuery(wherePacketLossSB.toString());
        if(cursor == null) {
            return false;
        }
        System.out.println("query"+wherePacketLossSB.toString());
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

        private boolean createRtpSessionList() {
            String whereClauseCallId = "SELECT id FROM call_session WHERE time BETWEEN "+"\'"+mStartDate+ " 00:00"+"\'"+ " AND "+"\'"+mEndDate+" 59:59"+"\'";
            ResultSet cursorCallID = mIMSAnalyzerDBHelper.rawQuery(whereClauseCallId);
            if(cursorCallID == null) {
                return false;
            }
            try {
                while(cursorCallID.next()) {
                    int callId = cursorCallID.getInt("id");
                    mCallIdList.add(callId);
                }
            }catch(SQLException e) {

            }
            StringBuilder wherePacketLossSB =  new StringBuilder("SELECT * FROM rtp_session");
            for(int i = 0 ;i<mCallIdList.size(); i++) {
                int callId = mCallIdList.get(i);
                if(callId <= 0) {
                    break;
                }
                if(i == 0){
                   wherePacketLossSB.append(" WHERE call_id IS "+callId) ;
                } else {
                   wherePacketLossSB.append(" OR call_id IS "+callId) ;
                }
            }
            if(!wherePacketLossSB.toString().contains("WHERE")) {
               return false; 
            }
            ResultSet cursor = mIMSAnalyzerDBHelper.rawQuery(wherePacketLossSB.toString());
            if(cursor == null) {
                return false;
            }
            try {
                while(cursor.next()) {
                    RTPSession session = new RTPSession();
                    session.setPacketLoss(cursor.getInt("packet_loss"));
                    session.setCumJitter(cursor.getInt("cum_jitter"));
                    session.setCodecType(cursor.getInt("codec_type"));
                    session.setMaxJitter(cursor.getInt("max_jitter"));
                    session.setCallId(cursor.getInt("call_id"));
                    rtpSessionObjList.add(session);
                }
            }catch(SQLException e) {
                return false;
            }
            return true;
        }

    @Override
    public void changeNode(ImsAnalyzeToolConstants.RootNodeName nodeName, ImsAnalyzeToolConstants.CallState callState) {
        if(ImsAnalyzeToolConstants.RootNodeName.Media == nodeName) {
            createCallSessionFrmDB(callState);
            mTabSelectionListener.onTabSelected(ImsAnalyzeToolConstants.TabName.MediaIndicatorPane, mStartDate, mEndDate,callSessionObjList);
        }else {
            if(nodeName == ImsAnalyzeToolConstants.RootNodeName.PacketLostPerc) {
                mStage.getScene().getStylesheets().add("resources/chart.css");
            } else if(nodeName == ImsAnalyzeToolConstants.RootNodeName.CallSuccess) {
                mStage.getScene().getStylesheets().add("resources/chart_fail.css");
            }
            mCallStateTab.setContent(null);
            ICallStateNode node = CallStateRootFactory.createRootNode(nodeName, this, rtpSessionObjList,mStage,mDBPath);
            Node nodeRoot = node.createNode();
            mCallStateTab.setContent(nodeRoot);
        }
    }
}