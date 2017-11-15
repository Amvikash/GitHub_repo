/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.perfindicatorapp.perfindicator.impl.callstateindicator.impl;

import com.lge.iat.db.interfaces.IIMSAnalyzerDBHelper;
import com.lge.iat.db.interfaces.IMSAnalyzerDBManager;
import com.lge.iat.perfindicatorapp.perfindicator.impl.callstateindicator.interfaces.ICallStateNode;
import com.lge.iat.utils.ImsAnalyzeToolConstants;
import com.lge.iat.utils.RTPSession;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 *
 * @author jitendra.parkar
 */
public class CallStateSuccessFailNode implements ICallStateNode{

    private IIMSAnalyzerDBHelper mIMSAnalyzerDBHelper = null;
    private ICallStateNodeChangeListener mNodeChangeListener = null;
    ArrayList<Integer> mPacketLostList = new ArrayList<>();
    ArrayList<Integer> mCallIdList = new ArrayList<>();
    ArrayList<RTPSession> mRTPSessionsList = null;
    private int countFailed=0,countSuccess=0, totalCount =0;
                
    private Stage mStage = null;
                
    public CallStateSuccessFailNode(ICallStateNodeChangeListener nodeChangeListener,ArrayList<RTPSession> rtpSessionsList,Stage prStage,String dbPath) {
         mIMSAnalyzerDBHelper = IMSAnalyzerDBManager.getImsAnalyzerDBHelper(dbPath);
         mStage = prStage;
         mStage.getScene().getStylesheets().add("resources/chart_fail.css");
         mNodeChangeListener= nodeChangeListener;
         mRTPSessionsList= rtpSessionsList;
    }
    @Override
    public Node createNode() {
        double sceneWidth = mStage.getScene().getWidth();
        double sceneHeight = mStage.getScene().getHeight();
        StackPane pane1 = new StackPane();        
        pane1.setPrefSize( sceneWidth * .65, sceneHeight);
        pane1.autosize();
        GridPane.setHalignment(pane1, HPos.CENTER);
        PieChart chart = createPieChart();
        chart.setLabelLineLength(12);
        chart.setPrefSize(sceneWidth * .8, sceneHeight * .8);
        StackPane.setAlignment(chart,Pos.CENTER_LEFT);
        chart.autosize();       
         /*Platform.runLater(new Runnable() {
            @Override
            public void run() {
               Node node = chart.lookup(".default-color0.chart-pie");
               node.setStyle("-fx-pie-color: #1B8E5C;");
               node = chart.lookup(".default-color1.chart-pie");
               node.setStyle("-fx-pie-color: #FC230D;");
              }
          });*/
        pane1.getChildren().addAll(chart);
        
        StackPane pane2 = new StackPane(); 
        pane2.setPrefSize(sceneWidth * .35, sceneHeight);
        pane2.autosize();
        GridPane.setHalignment(pane2, HPos.CENTER);
        
         GridPane gridPane = new GridPane();
         GridPane gp = new GridPane();
         gp.setHgap(10);
         gp.setVgap(10);
          Label t1=new Label("Total Call\t: "+totalCount);
          t1.setStyle("-fx-background-color: #16A2CF");
          t1.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
          Label t2=new Label("Call Success\t: "+countSuccess);
          t2.setStyle("-fx-background-color: #A6ACAF");
          t2.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
          Label t3=new Label("Call Failed\t: "+countFailed);
          t3.setStyle("-fx-background-color: #E7F1F7");
          t3.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
          
          t1.setMaxWidth(Double.MAX_VALUE);
          t2.setMaxWidth(Double.MAX_VALUE);
          t3.setMaxWidth(Double.MAX_VALUE);
          
          t1.setPadding(new Insets(5, 10, 5, 10));
          t2.setPadding(new Insets(5, 10, 5, 10));
          t3.setPadding(new Insets(5, 10, 5, 10));
          
          Pane pane3  = new Pane();
          pane3.autosize();
          GridPane.setHalignment(pane1, HPos.CENTER);
          GridPane detail = new GridPane();
          detail.setAlignment(Pos.CENTER);
          detail.add(t1, 0, 0);
          detail.add(t2, 0, 1);
          detail.add(t3, 0, 2);
          pane3.getChildren().add(detail);
          gp.add(pane3,2,16);
          gp.autosize();
          StackPane.setAlignment(gp, Pos.CENTER_RIGHT);
          pane2.getChildren().addAll(gp);
          pane1.setStyle("-fx-background-color: #fff");
          gridPane.add(pane1, 0, 0);
          gridPane.add(pane2, 1, 0);
          gridPane.setAlignment(Pos.CENTER);
          gridPane.autosize();
          
          VBox callStatics_vBox = new VBox();
          callStatics_vBox.setAlignment(Pos.CENTER);
          //callStatics_vBox.setPrefSize(mStage.getWidth(), mStage.getHeight());
          callStatics_vBox.getChildren().addAll(gridPane);
         // VBox.setVgrow(gridPane, Priority.ALWAYS);
        //  mCallStaticsSuccessNode.setContent(callStatics_vBox);
        Scene scene =  mStage.getScene();
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override 
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                    System.out.println("Width: " + newSceneWidth);
                    if(oldSceneWidth.doubleValue() >= 0.0 ) {
                        //scene.getRoot().setScaleX(newSceneWidth.doubleValue()/oldSceneWidth.doubleValue());
                        pane1.setPrefWidth(newSceneWidth.doubleValue()*.65);
                        pane2.setPrefWidth(newSceneWidth.doubleValue() * .35);      
                        chart.setPrefWidth(newSceneWidth.doubleValue()* .8);
                    }
                }
            });
            scene.heightProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                    System.out.println("Height: " + newSceneHeight);
                    if(oldSceneHeight.doubleValue() >= 0.0 ) {
                     //scene.getRoot().setScaleY(newSceneHeight.doubleValue()/oldSceneHeight.doubleValue());
                     pane1.setPrefHeight(newSceneHeight.doubleValue());
                     pane2.setPrefHeight(newSceneHeight.doubleValue());       
                     chart.setPrefHeight(newSceneHeight.doubleValue()* .8);
                }
            }
        });
          return callStatics_vBox;
    }
    private PieChart createPieChart() {

        
        for(RTPSession rtpSession:mRTPSessionsList ) {
            int packetLost = rtpSession.getPacketLoss();
            if(packetLost < 0) {
                countFailed++;
            } else {
                countSuccess++;
            }
        }
        mPacketLostList.clear();
        totalCount = countSuccess+countFailed;
        double percSuccess = 100 *countSuccess/totalCount;

        double percFailed = 100 *countFailed/totalCount;
        PieChart.Data dataSuccess = new PieChart.Data("Call Success", percSuccess);
        PieChart.Data dataFailed = new PieChart.Data("Call Failed", percFailed);
        ObservableList<PieChart.Data> pieChartData = 
              FXCollections.observableArrayList(
                      dataSuccess,
                      dataFailed
              );
        PieChart chart = new PieChart(pieChartData);
          dataSuccess.getNode().setStyle("-fx-pie-color: " + "#1B8E5C" + ";");
            dataSuccess.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        mNodeChangeListener.changeNode(ImsAnalyzeToolConstants.RootNodeName.PacketLostPerc, ImsAnalyzeToolConstants.CallState.None);
                     }
                });
          dataFailed.getNode().setStyle("-fx-pie-color: " + "#FC230D" + ";");
          dataFailed.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        mNodeChangeListener.changeNode(ImsAnalyzeToolConstants.RootNodeName.Media, ImsAnalyzeToolConstants.CallState.Failed);
                     }
                });
          return chart;
    }
}
