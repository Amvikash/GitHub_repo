/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.perfindicatorapp.perfindicator.impl.mediaindicator.impl;

import com.lge.iat.utils.CallSession;
import com.lge.iat.db.interfaces.IIMSAnalyzerDBHelper;
import com.lge.iat.db.interfaces.IMSAnalyzerDBManager;
import com.lge.iat.utils.ImsAnalyzeToolConstants;
import com.lge.iat.utils.RTPSession;
import com.lge.iat.perfindicatorapp.perfindicator.impl.mediaindicator.interfaces.IMediaNode;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author jitendra.parkar
 */
public class CallMediaNode implements IMediaNode{

    TableView tableCallSession;
    TableView table1RTPSession;
    INodeChangeListener mNodeChangeListener = null;
    private int mCurrentSelIndex = -1;
    private ArrayList<CallSession> mCallSessionList = null;
    private IIMSAnalyzerDBHelper mDBHelper = null;
    public CallMediaNode(INodeChangeListener nodeChangeListener, ArrayList<CallSession> callSessionList, int index, Stage stage,String dbPath) {
        mDBHelper = IMSAnalyzerDBManager.getImsAnalyzerDBHelper(dbPath);
        mNodeChangeListener = nodeChangeListener;
        mCurrentSelIndex = index;
        mCallSessionList = callSessionList;
    }
    @Override
    public Node createNode() {
         //media.setContent(null);

        GridPane g1 = new GridPane();
        g1.autosize();
        g1.setHgap(20);
        g1.setVgap(7);
        
        GridPane g2 = new GridPane();
        g2.setHgap(20);
        g2.setVgap(10);
        g2.autosize();
        
        GridPane g3 = new GridPane();
        g3.setHgap(20);
        g3.setVgap(10);
        g3.autosize();
        
        Button back =   new Button("Back");
        Button graphB =   new Button("Media Graph");
        
        Label callDir  =   new Label("Media Graph");
        callDir.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Label name  =   new Label();
        name.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        name.autosize();
        Label num  =   new Label();
        num.autosize();
        num.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Label date  =   new Label();
        date.autosize();
        date.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Label time  =   new Label();
        time.autosize();
        time.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Label duration  =   new Label();
        duration.autosize();
        duration.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        
        CallSession callSession = mCallSessionList.get(mCurrentSelIndex);
        int dir = callSession.getDirection();
        if(dir == 1){
            name.setText(callSession.getCallerName());
            num.setText(callSession.getOrgnNum());
            callDir.setText("Incoming Call");
        } else {
            name.setText(callSession.getCalleeName());
            num.setText(callSession.getTermNum());
            callDir.setText("Outgoing Call");
        }
        String dateAndTime = callSession.getTime();
        String []   dateTimeArr = dateAndTime.split(" ");
        date.setText(dateTimeArr[0]);
        time.setText(dateTimeArr[1]);
        duration.setText("Duration: "+callSession.getDuration());
        //System.out.println("You requested for "+l);
        
        g1.add(name,0,1);
        g1.add(num,0,2);
        g1.add(date,1,1);
        g1.add(time,2,1);
        g1.add(duration,3,1);
        g1.add(callDir,4,1);
        g1.add( back, 5, 1);
        g1.add(graphB,6,1);
        g1.setStyle("-fx-background-color: #9BA1A2");
        
        Label t1 = new Label("Call Session Info");
        t1.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        g2.add(t1, 6, 0);
        createTableViewCallSession();
        g2.add(tableCallSession, 6, 1);
        Label t2 = new Label("RTP Session Info");
        g2.add(t2, 6, 2);
        t2.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        createTableViewRTPSession();
        g2.add(table1RTPSession, 6, 3);
        VBox layout = new VBox();
        layout.setSpacing(10);
        layout.setPadding(new Insets(0, 20, 10, 20)); 
        layout.getChildren().addAll(g1,g2);
        
        graphB.setOnAction((ActionEvent event) -> {
            mNodeChangeListener.changeNode(ImsAnalyzeToolConstants.RootNodeName.GraphMedia,mCurrentSelIndex);
        });
        
        back.setOnAction((ActionEvent event) -> {
            mNodeChangeListener.changeNode(ImsAnalyzeToolConstants.RootNodeName.Media, -1);
        });
       return layout;
    }
    
    private void createTableViewCallSession() {
        tableCallSession = new TableView();
        tableCallSession.getItems().add(1);
        CallSession callSession = mCallSessionList.get(mCurrentSelIndex);
        Field []fields = callSession.getClass().getDeclaredFields();
        Object[] objArr = new Object[fields.length];
        for(int k = 0; k< fields.length; k++) {
            try {
                fields[k].setAccessible(true);
                objArr[k] = fields[k].get(callSession);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(CallMediaNode.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(CallMediaNode.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         String[] stringArray = new String[objArr.length];
         int j = 0;
        for(Object obj:objArr) {
            stringArray[j] = ""+obj;
            j++;
        }
        List<String> nameList = Arrays.asList(stringArray);
        for (int i = 0; i<fields.length;i++) {
            String stringVal =nameList.get(i);
            TableColumn<Integer, String> nam = new TableColumn<>(fields[i].getName().toUpperCase());
           //TableColumn<Integer, String> nam = new TableColumn<>("Name");
            nam.setCellValueFactory(cellData -> {
            //Integer rowIndex = i;
            return new ReadOnlyStringWrapper(stringVal);
        });
            tableCallSession.getColumns().add(nam);
            
        }
        tableCallSession.autosize();
    }
    
    private void createTableViewRTPSession() {
        table1RTPSession = new TableView();
        table1RTPSession.getItems().add(1);
        CallSession callSession = mCallSessionList.get(mCurrentSelIndex);
        int callSessionId = callSession.getId();
        String whereRTPSession = "SELECT * FROM rtp_session where call_id is "+ callSessionId;
        RTPSession rtpSessionObj  = new RTPSession();

        ResultSet cursor = mDBHelper.rawQuery(whereRTPSession);
        if(cursor == null) {
            return;
        }
        try {
            while(cursor.next()) {
                rtpSessionObj.setCallId(cursor.getInt("call_id"));
                rtpSessionObj.setCodecType(cursor.getInt("codec_type"));
                rtpSessionObj.setCumJitter(cursor.getInt("cum_jitter"));
                rtpSessionObj.setMaxJitter(cursor.getInt("max_jitter"));
                rtpSessionObj.setPacketLoss(cursor.getInt("packet_loss"));
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        if(rtpSessionObj== null) {
            return;
        }
        Field []fields = rtpSessionObj.getClass().getDeclaredFields();
        Object[] objArr = new Object[fields.length];
        for(int k = 0; k< fields.length; k++) {
            try {
                fields[k].setAccessible(true);
                objArr[k] = fields[k].get(rtpSessionObj);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(CallMediaNode.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(CallMediaNode.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
         String[] stringArray = new String[objArr.length];
         int j = 0;
        for(Object obj:objArr) {
            stringArray[j] = ""+obj;
            j++;
        }
        List<String> nameList = Arrays.asList(stringArray);
        for (int i = 0; i<fields.length;i++) {
            String stringVal =nameList.get(i);
            TableColumn<Integer, String> nam = new TableColumn<>(fields[i].getName().toUpperCase());
           //TableColumn<Integer, String> nam = new TableColumn<>("Name");
            nam.setCellValueFactory(cellData -> {
            //Integer rowIndex = i;
            return new ReadOnlyStringWrapper(stringVal);
        });
            table1RTPSession.getColumns().add(nam);
            
        }
        
        table1RTPSession.autosize();
    }
    
    private void createBarChart() {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String, Number> bc =
                new BarChart<String, Number>(xAxis, yAxis);
        bc.setTitle("RTP Session Graph");
        xAxis.setLabel("RTP behaviore");
        xAxis.setTickLabelRotation(90);
        yAxis.setLabel("Time");
 
        XYChart.Series series1 = new XYChart.Series();
         ArrayList<RTPSession> list = createRTPSessionList();
        for(RTPSession session : list) {
             series1.getData().add(new XYChart.Data("", 25601.34));
        }
    }
    
    
       private ArrayList<RTPSession> createRTPSessionList() {
       ArrayList<RTPSession> listmediaGraphData = new ArrayList<RTPSession>();
        CallSession callSession = mCallSessionList.get(mCurrentSelIndex);
        int callSessionId = callSession.getId();
        String whereRTPSession = "SELECT * FROM rtp_session where call_id is "+ callSessionId;

        ResultSet cursor = mDBHelper.rawQuery(whereRTPSession);
        if(cursor == null) {
            return null;
        }
        try {
            while(cursor.next()) {
                RTPSession rtpSessionObj  = new RTPSession();
                rtpSessionObj.setCallId(cursor.getInt("call_id"));
                rtpSessionObj.setCodecType(cursor.getInt("codec_type"));
                rtpSessionObj.setCumJitter(cursor.getInt("cum_jitter"));
                rtpSessionObj.setMaxJitter(cursor.getInt("max_jitter"));
                rtpSessionObj.setPacketLoss(cursor.getInt("packet_loss"));
                listmediaGraphData.add(rtpSessionObj);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return listmediaGraphData;
   } 
}
