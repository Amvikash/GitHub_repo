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
import com.lge.iat.utils.MediaGraphData;
import com.lge.iat.perfindicatorapp.perfindicator.impl.mediaindicator.interfaces.IMediaNode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedAreaChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author jitendra.parkar
 */
public class GraphMediaNode implements IMediaNode{

    INodeChangeListener mNodeChangeListener = null;
    private int mCurrentSelIndex= -1;
    private ArrayList<CallSession> mCallSessionList = null;
    private Stage mStage = null;
    private IIMSAnalyzerDBHelper mDBHelper = null;
    public GraphMediaNode(INodeChangeListener nodeChangeListener, ArrayList<CallSession> callSessionList, int index, Stage stage,String dbPath) {
        mDBHelper = IMSAnalyzerDBManager.getImsAnalyzerDBHelper(dbPath);
        mNodeChangeListener = nodeChangeListener;
        mCurrentSelIndex = index;
        mCallSessionList = callSessionList;
        mStage = stage;
    }
    
    @Override
    public Node createNode() {
        
        GridPane gridPane = new GridPane();
        gridPane.autosize();
        gridPane.setHgap(20);
        gridPane.setVgap(7);
                
        GridPane G2 = new GridPane();
        G2.setHgap(20);
        G2.setVgap(10);
                       
        /*GridPane G3 = new GridPane();
        G3.setHgap(20);
        G3.setVgap(10);*/
                
        Label label  =   new Label("Media Graph");
        label.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        label.autosize();
        Label callDir  =   new Label("Media Graph");
        callDir.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        callDir.autosize();
        Label name  =   new Label();
        name.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        name.autosize();
        Label num  =   new Label();
        num.autosize();
        num.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        Label date  =   new Label();
        date.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        date.autosize();
        Label time  =   new Label();
        time.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        time.autosize();
        Label duration  =   new Label();
        duration.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
        duration.autosize();
        
        Button back =   new Button("Back");
        //System.out.println("You requested for "+l);

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
        
        gridPane.add(name,0,1);
        gridPane.add(num,0,2);
        gridPane.add(date,1,1);
        gridPane.add(time,2,1);
        gridPane.add(duration,3,1);
        gridPane.add(callDir,4,1);
        gridPane.add( back, 5, 1);
        gridPane.setStyle("-fx-background-color: #9BA1A2");

        
        ArrayList<MediaGraphData> mediaGrphDataList = createMediaGraphDataList();
        
        LineChart<String,Number> chart1 = createLostPacketGraph(mediaGrphDataList);
        LineChart<String,Number> chart2 = createJitterBuffGraph(mediaGrphDataList);
        StackedAreaChart<String, Number> sac = createAudioSilencePeriodGraph(mediaGrphDataList);

        ScrollPane sacPan = new ScrollPane(sac);
        ScrollPane chart1Pan = new ScrollPane(chart1);
        ScrollPane chart2Pan = new ScrollPane(chart2);
        G2.add(sacPan, 0, 0);
        G2.add(chart1Pan, 1, 0);
        G2.add(chart2Pan, 2, 0);
        G2.setAlignment(Pos.CENTER);
        G2.setPadding(new Insets(50, 10, 10, 10));
        G2.autosize();
        VBox layout = new VBox();
        layout.getChildren().addAll(gridPane,G2);
        layout.setPadding(new Insets(0, 20, 10, 20)); 
        layout.autosize();
        
       back.setOnAction((ActionEvent event) -> {
            mNodeChangeListener.changeNode(ImsAnalyzeToolConstants.RootNodeName.CallMedia,mCurrentSelIndex);
        });
        
        return layout;
    }
   private ArrayList<MediaGraphData> createMediaGraphDataList() {
       ArrayList<MediaGraphData> listmediaGraphData = new ArrayList<MediaGraphData>();
        CallSession callSession = mCallSessionList.get(mCurrentSelIndex);
        int callSessionId = callSession.getId();
        String whereRTPSession = "SELECT * FROM media_graph_data where call_id is "+ callSessionId;
        ResultSet cursor = mDBHelper.rawQuery(whereRTPSession);
        if(cursor == null) {
            return null;
        }
        try {
            while(cursor.next()) {
                MediaGraphData mediaGraphDataObj  = new MediaGraphData();
                mediaGraphDataObj.setCallID(cursor.getInt("call_id"));
                mediaGraphDataObj.setAudioSilencePeriod(cursor.getBoolean("audio_silence_period"));
                mediaGraphDataObj.setJitBuffer(cursor.getInt("jitter_buffer"));
                mediaGraphDataObj.setLostPacket(cursor.getInt("lost_packet"));
                mediaGraphDataObj.setTime(cursor.getString("time"));
                listmediaGraphData.add(mediaGraphDataObj);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return listmediaGraphData;
   } 
   
   
   private  LineChart<String,Number> createLostPacketGraph( ArrayList<MediaGraphData> mediaGrphDataList) {
        
       class compMaxYVal implements Comparator<MediaGraphData> {
            public int compare(MediaGraphData a, MediaGraphData b) {
                if (a.getLostPacket()< b.getLostPacket())
                    return -1; // highest value first
                if (a.getLostPacket() == b.getLostPacket())
                    return 0;
                return 1;
            }
        }

        MediaGraphData ZZ = Collections.max(mediaGrphDataList, new compMaxYVal());
        MediaGraphData YY = Collections.min(mediaGrphDataList, new compMaxYVal());
        final CategoryAxis x1 = new CategoryAxis();
        x1.setLabel("Time");
        final NumberAxis y1 = new NumberAxis(YY.getLostPacket(), ZZ.getLostPacket()+1, 1);
        y1.setLabel("Lost Packet");
        LineChart<String,Number> packetLostChart = new LineChart<>(x1,y1);

        XYChart.Series<String,Number> series1 = new XYChart.Series();
        for(MediaGraphData mGD: mediaGrphDataList) {
           String time =  mGD.getTime();
           int lostPacket = mGD.getLostPacket();
            series1.getData().add(new XYChart.Data(time, lostPacket));
        }
        packetLostChart.getData().add(series1);
        packetLostChart.setTitle("Loss");
        packetLostChart.autosize();
        return packetLostChart;
   }
   
   private  LineChart<String,Number> createJitterBuffGraph( ArrayList<MediaGraphData> mediaGrphDataList) {
         class compMaxYVal implements Comparator<MediaGraphData> {
            public int compare(MediaGraphData a, MediaGraphData b) {
                if (a.getJitBuffer()< b.getJitBuffer())
                    return -1; // highest value first
                if (a.getJitBuffer() == b.getJitBuffer())
                    return 0;
                return 1;
            }
         }

        MediaGraphData ZZ = Collections.max(mediaGrphDataList, new compMaxYVal());  
        MediaGraphData YY = Collections.min(mediaGrphDataList, new compMaxYVal()); 
        final CategoryAxis x1 = new CategoryAxis();
        x1.setLabel("Time");
        final NumberAxis y1 = new NumberAxis(YY.getJitBuffer(), ZZ.getJitBuffer()+1, 1);        
        y1.setLabel("Jitter Buffer");
        LineChart<String,Number> jitterBuffChart = new LineChart<>(x1,y1);
                
        XYChart.Series<String,Number> series1 = new XYChart.Series();
        for(MediaGraphData mGD: mediaGrphDataList) {
           String time =  mGD.getTime();
           int jitterBuff = mGD.getJitBuffer();
            series1.getData().add(new XYChart.Data(time, jitterBuff));
        }
        jitterBuffChart.getData().add(series1);
        jitterBuffChart.setTitle("Jitter buffer size");
        jitterBuffChart.autosize();
        return jitterBuffChart;
   }
   
   private StackedAreaChart<String, Number> createAudioSilencePeriodGraph(ArrayList<MediaGraphData> mediaGrphDataList) {         
        final CategoryAxis x3 = new CategoryAxis();
        x3.setLabel("Time");
        final NumberAxis y3 = new NumberAxis(0, 2, 1);
        y3.setLabel("Audio Silence(0) or no Silence(1) ");
        final StackedAreaChart<String, Number> sac =new StackedAreaChart<>(x3, y3);
 
        sac.setTitle("Audio Silence Period");
        XYChart.Series<String, Number> series3 =new XYChart.Series<>();
        for(MediaGraphData mGD: mediaGrphDataList) {
           String time =  mGD.getTime();
           boolean isAudioSilencePeriod = mGD.isAudioSilencePeriod();
            series3.getData().add(new XYChart.Data(time, isAudioSilencePeriod?1:0));
        }
        sac.getData().addAll(series3);
        return sac;
   }
}
