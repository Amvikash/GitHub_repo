/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.perfindicatorapp;

import com.lge.iat.db.interfaces.IMSAnalyzerDBManager;
import com.lge.iat.utils.IMSTab;
import com.lge.iat.perfindicatorapp.perfindicator.interfaces.IPerformanceIndicatorPane;
import com.lge.iat.perfindicatorapp.perfindicator.impl.ITabSelectionListener;
import com.lge.iat.utils.ImsAnalyzeToolConstants;
import com.lge.iat.perfindicatorapp.perfindicator.interfaces.PerformanceIndicatorFactory;
import com.lge.iat.utils.CallSession;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.control.Tab;
import javafx.scene.paint.Color;

/**
 *
 * @author jitendra.parkar
 */
public class PerformanceIndicatorFrame extends Application implements ITabSelectionListener{

    TabPane mTabPane = null;
    BorderPane mainPane = null;
    Group root = null;
    Scene scene = null;
    Stage mPrimaryStage = null;
    String mDBPath = null;
    private String mPrevStartDate = null, mPrevEndDate = null;
    public PerformanceIndicatorFrame(String dbPath) {
        mDBPath = dbPath;
        mTabPane = new TabPane (); 
        mainPane = new BorderPane();
        root = new Group();
        scene = new Scene(root,800,600, Color.LIGHTGREY);
        scene.getStylesheets().add("resources/table.css");
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override 
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                    System.out.println("Width: " + newSceneWidth);
                    if(oldSceneWidth.doubleValue() >= 0.0 ) {
                        //scene.getRoot().setScaleX(newSceneWidth.doubleValue()/oldSceneWidth.doubleValue());
                        mTabPane.setPrefWidth(newSceneWidth.doubleValue());
                    }
                }
            });
            scene.heightProperty().addListener(new ChangeListener<Number>() {
                @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                    System.out.println("Height: " + newSceneHeight);
                    if(oldSceneHeight.doubleValue() >= 0.0 ) {
                     //scene.getRoot().setScaleY(newSceneHeight.doubleValue()/oldSceneHeight.doubleValue());
                     mTabPane.setPrefHeight(newSceneHeight.doubleValue());
                }
            }
        });
        
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("IMS Analyzer : Performance");
        mPrimaryStage = primaryStage;
        IPerformanceIndicatorPane settingPanel = PerformanceIndicatorFactory.createPerformanceIndicatorPanel(ImsAnalyzeToolConstants.TabName.SettingIndicatorTab, this,mPrimaryStage, null,null,mDBPath,null);
        IMSTab settingTab = settingPanel.createPane();
        if(settingTab == null) {
            IMSAnalyzerDBManager.DeleteDBHelper(mDBPath);
            return;
        }
        createAndShowTab(settingTab);
    }
     
    private void createAndShowTab(IMSTab tab) {
        mainPane.setCenter(mTabPane);
        //if(!mTabPane.getTabs().contains(tab)) {
            mTabPane.getTabs().add(tab);
       // }
        root.getChildren().add(mainPane);
        mTabPane.setMinWidth(800);
        mTabPane.setMinHeight(600);
        mPrimaryStage.setScene(scene);
        mPrimaryStage.setMinHeight(600);
        mPrimaryStage.setMinWidth(800);
        mPrimaryStage.setResizable(true);
        mPrimaryStage.show();
    }

    int count = 0;
    @Override
    public void onTabSelected(ImsAnalyzeToolConstants.TabName tabName,String startDate ,String endDate, ArrayList<CallSession> callSessionList) {
        ObservableList<Tab> tabList = mTabPane.getTabs();
        for (int i = 0; i < tabList.size(); i++) {
            if (tabName == ((IMSTab)tabList.get(i)).getTabName()) {
                if((mPrevStartDate!= null && !mPrevStartDate.equals(startDate)) || (mPrevEndDate!= null && !mPrevEndDate.equals(endDate))) {
                   if (tabList.size() == 3) {
                        mTabPane.getTabs().remove(1);
                        mTabPane.getTabs().remove(1);
                   } else if(tabList.size() == 2) {
                       mTabPane.getTabs().remove(1);
                   }
                 //   count++;
                    break;
                } else if(mTabPane.getSelectionModel().getSelectedIndex() == 0){
                    if(tabList.size() == 3) {
                        if(tabList.get(2).getText().equals("CallState"))   {
                            mTabPane.getSelectionModel().select(tabList.get(2));
                            return;
                        } else if(tabList.get(1).getText().equals("CallState")) {
                            mTabPane.getSelectionModel().select(tabList.get(1));
                            return;
                        }
                    
                    } else if(tabList.size() == 2) {
                        if(tabList.get(1).getText().equals("CallState")) {
                            mTabPane.getSelectionModel().select(tabList.get(1));
                            return;
                        }
                    }
                }
            }
        }
                
        IPerformanceIndicatorPane panel = PerformanceIndicatorFactory.createPerformanceIndicatorPanel(tabName,this,mPrimaryStage,startDate,endDate,mDBPath,callSessionList);
        Tab tab = panel.createPane();
      
        if(tab != null) {
            //if(tabName == ImsAnalyzeToolConstants.TabName.MediaIndicatorPane) {
                if(tabList.size() == 3) {
                    if(tabList.get(2).getText().equals("Media"))   {
                         tabList.remove(2);
                    } else if(tabList.get(1).getText().equals("Media")) {
                        tabList.remove(1);
                    } else if(tabList.get(0).getText().equals("Media")) {
                        tabList.remove(0);
                    }
                } else if(tabList.size() == 2) {
                    if(tabList.get(1).getText().equals("Media")) {
                        tabList.remove(1);
                    } else if(tabList.get(0).getText().equals("Media")) {
                        tabList.remove(0);
                    }
                } else if(tabList.size() == 1) {
                        if(tabList.get(0).getText().equals("Media")) {
                          tabList.remove(0);
                    }
                } 
            //}
            mTabPane.getTabs().add(tab);
            mTabPane.getSelectionModel().select(tab);
           // if(count%2 == 0) {
                mPrevStartDate = startDate;
                mPrevEndDate  = endDate;
          //  }
        }
    }
}
