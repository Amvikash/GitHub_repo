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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
public class CallStatePacketPercLossNode implements ICallStateNode{
    private IIMSAnalyzerDBHelper mIMSAnalyzerDBHelper = null;
    private ICallStateNodeChangeListener mNodeChangeListener = null;
    //private String mStartDate = null;
   // private String mEndDate = null;
    //private IMSTab mCallStaticsSuccessNode = null;
    ArrayList<Integer> mPacketLostList = new ArrayList<>();
    ArrayList<Integer> mCallIdList = new ArrayList<>();
    ArrayList<RTPSession> mRTPSessionsList = null;
    private int countPacketLostless5=0,countPacketLostmore5=0,countPacketLostMore10=0, totalCount =0;
                
    private Stage mStage = null;
    public CallStatePacketPercLossNode(ICallStateNodeChangeListener nodeChangeListener,ArrayList<RTPSession> rtpSessionsList,Stage prStage,String dbPath) {
         mIMSAnalyzerDBHelper = IMSAnalyzerDBManager.getImsAnalyzerDBHelper(dbPath);
         mStage = prStage;
         mNodeChangeListener= nodeChangeListener;
         mRTPSessionsList= rtpSessionsList;
    }
     @Override
    public Node createNode() {
        double sceneWidth = mStage.getScene().getWidth();
        double sceneHeight = mStage.getScene().getHeight();
        mStage.getScene().getStylesheets().add("resources/chart.css");
        StackPane pane1 = new StackPane();
        pane1.setPrefSize(sceneWidth * .65, sceneHeight);
        pane1.autosize();
        PieChart chart = createPieChart();
        chart.setLabelLineLength(12);
        chart.setPrefSize(sceneWidth * .8, sceneHeight * .8);
        chart.autosize();
        StackPane.setAlignment(chart,Pos.CENTER_LEFT);
        pane1.getChildren().addAll(chart);
        pane1.setStyle("-fx-background-color: #fff");
        GridPane.setHalignment(pane1, HPos.CENTER);
                  
        StackPane pane2 = new StackPane();
        pane2.setPrefSize(sceneWidth * .35,sceneHeight);
        pane2.autosize();
        GridPane.setHalignment(pane2, HPos.CENTER);
        GridPane gp = new GridPane();
        gp.setHgap(10);
        gp.setVgap(10);
            Pane pane3  = new Pane();
            pane3.setPrefSize(mStage.getWidth(), mStage.getHeight());
            pane3.autosize();
            GridPane.setHalignment(pane3, HPos.CENTER);
                       
                GridPane detail = new GridPane();
                detail.setAlignment(Pos.BOTTOM_RIGHT);
                detail.autosize();
                Label t1=new Label("Total Call\t: "+totalCount);
                t1.setStyle("-fx-background-color: #16A2CF");
                t1.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
                Label t2=new Label("Packet Dropped \n< 5%\t: "+countPacketLostless5);
                t2.setStyle("-fx-background-color: #A6ACAF");
                t2.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
                Label t3=new Label("Packet Dropped\n5 – 10%\t: "+countPacketLostmore5);
                t3.setStyle("-fx-background-color: #E7F1F7");
                t3.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));
                Label t4=new Label("Packet Dropped  \n> 10%\t:  "+countPacketLostMore10);
                t4.setStyle("-fx-background-color: #A6ACAF");
                t4.setFont(Font.font("Verdana", FontWeight.NORMAL, 14));

                t1.setMaxWidth(Double.MAX_VALUE);
                t2.setMaxWidth(Double.MAX_VALUE);
                t3.setMaxWidth(Double.MAX_VALUE);
                t4.setMaxWidth(Double.MAX_VALUE);

                t1.setPadding(new Insets(5, 10, 5, 10));
                t2.setPadding(new Insets(5, 10, 5, 10));
                t3.setPadding(new Insets(5, 10, 5, 10));
                t4.setPadding(new Insets(5, 10, 5, 10));

                detail.add(t1, 0, 0);
                detail.add(t2, 0, 1);
                detail.add(t3, 0, 2);
                detail.add(t4, 0, 3);
            
            pane3.getChildren().add(detail);
        gp.add(pane3,2,16);
            Pane pane4  = new Pane();
            pane4.setPrefSize(mStage.getWidth(), mStage.getHeight());
            pane4.autosize();
            GridPane.setHalignment(pane4, HPos.CENTER);
            Image im = new Image(getClass().getResourceAsStream("/resources/backbutton.png"));
           // Button backButton = new Button("Back");
           ImageView iv = new ImageView(im);
           iv.setFitHeight(50);
           iv.setFitWidth(100);
           pane4.getChildren().add(iv);
           iv.setOnMouseClicked((MouseEvent e) -> {
                 mNodeChangeListener.changeNode(ImsAnalyzeToolConstants.RootNodeName.CallSuccess, ImsAnalyzeToolConstants.CallState.None);
          });
//           iv.onMouseReleasedProperty(ActionEvent event) -> {
//                  mNodeChangeListener.changeNode(ImsAnalyzeToolConstants.RootNodeName.CallSuccess, ImsAnalyzeToolConstants.CallState.None);
//            });
            
           
//            Button backButton = new Button();
//            backButton.setPrefSize(100,40);
//            backButton.setGraphic(new ImageView(im));
//            pane4.getChildren().add(backButton);
//            backButton.setOnAction((ActionEvent event) -> {
//                  mNodeChangeListener.changeNode(ImsAnalyzeToolConstants.RootNodeName.CallSuccess, ImsAnalyzeToolConstants.CallState.None);
//            });
        gp.add(pane4,3,10);
        gp.autosize();
        pane2.getChildren().addAll(gp);
        
        GridPane gridPane = new GridPane();
        gridPane.setPrefSize(mStage.getWidth(), mStage.getHeight());
        gridPane.add(pane1, 0, 0);
        gridPane.add(pane2, 1, 0);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.autosize();
        VBox callStatics_vBox = new VBox();
        callStatics_vBox.setAlignment(Pos.CENTER);
        //  callStatics_vBox.setPrefSize(mStage.getWidth(), mStage.getHeight());
        callStatics_vBox.getChildren().addAll(gridPane);
        //  VBox.setVgrow(gridPane, Priority.ALWAYS);
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
            if(packetLost >= 0 && packetLost < 5) {
                countPacketLostless5++;
            } else if(packetLost >= 5 && packetLost < 10) {
                countPacketLostmore5++;
            } else if(packetLost >= 10) {
                countPacketLostMore10++;
            }
        }
        mPacketLostList.clear();
        totalCount = countPacketLostless5+countPacketLostmore5+countPacketLostMore10;
        double percPacketLostless5 = 100 *countPacketLostless5/totalCount;
        double percPacketLostmore5 = 100 *countPacketLostmore5/totalCount;
        double percPacketLostMore10 = 100 *countPacketLostMore10/totalCount;
        PieChart.Data dataPcktLostless5 = new PieChart.Data("Packet Dropped < 5%", percPacketLostless5);
        PieChart.Data dataPcktLostmore5 = new PieChart.Data("Packet Dropped  5 – 10%", percPacketLostmore5);
        
        PieChart.Data dataPckttLostMore10 = new PieChart.Data("Packet Dropped > 10%", percPacketLostMore10);
        ObservableList<PieChart.Data> pieChartData = 
              FXCollections.observableArrayList(
                      dataPcktLostless5,
                      dataPcktLostmore5,
                      dataPckttLostMore10
              );
        PieChart chart = new PieChart(pieChartData);
          chart.setPrefSize(mStage.getWidth()*.8, mStage.getHeight()*.8);
          dataPcktLostless5.getNode().setStyle("-fx-pie-color: " + "#1B8E5C" + ";");
            dataPcktLostless5.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        mNodeChangeListener.changeNode(ImsAnalyzeToolConstants.RootNodeName.Media, ImsAnalyzeToolConstants.CallState.LossLessFive);
                     }
                });
            
          dataPcktLostmore5.getNode().setStyle("-fx-pie-color: " + "#11B027" + ";");
            dataPcktLostmore5.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        mNodeChangeListener.changeNode(ImsAnalyzeToolConstants.RootNodeName.Media, ImsAnalyzeToolConstants.CallState.LossGrtFiveLessTen);
                     }
                });
            
          dataPckttLostMore10.getNode().setStyle("-fx-pie-color: " + "#FCFC0D" + ";");
            dataPckttLostMore10.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        mNodeChangeListener.changeNode(ImsAnalyzeToolConstants.RootNodeName.Media, ImsAnalyzeToolConstants.CallState.LossGrtTen);
                     }
                });
            
          return chart;
    }
}
