/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.perfindicatorapp.perfindicator.impl;

import com.lge.iat.db.interfaces.IIMSAnalyzerDBHelper;
import com.lge.iat.db.interfaces.IMSAnalyzerDBManager;
import com.lge.iat.utils.IMSTab;
import com.lge.iat.utils.ImsAnalyzeToolConstants;
import com.lge.iat.perfindicatorapp.perfindicator.interfaces.IPerformanceIndicatorPane;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author jitendra.parkar
 */
public class SettingIndicatorPane implements IPerformanceIndicatorPane{
    private IIMSAnalyzerDBHelper mIMSAnalyzerDBHelper = null;
    private ITabSelectionListener mTabSelectionListener = null;
    private Stage mStage = null;
     private String mMinStartDate = null;
    private String mMaxEndDate = null;
    private String mStartDate = null;
    private String mEndDate = null;
    private IMSTab mSettingTab = null;
    public SettingIndicatorPane(ITabSelectionListener tabSelectionListener,Stage prStage,String dbPath) {
        mIMSAnalyzerDBHelper = IMSAnalyzerDBManager.getImsAnalyzerDBHelper(dbPath);
        mTabSelectionListener = tabSelectionListener;
        mStage = prStage;
    }
    
    @Override
    public IMSTab createPane() {
        /*if(mSettingTab != null) {
            return mSettingTab;
        }*/
      String lable = createDescriptionLable();
      if(lable == null) {
          return null;
      }
      mSettingTab = new IMSTab(ImsAnalyzeToolConstants.TabName.SettingIndicatorTab);
      mSettingTab.setClosable(true);
      mSettingTab.setText("Setting");
      
      BorderPane rootPan =new BorderPane();
      Button submit  =   new Button("Done");
      submit.setDisable(true);
      submit.setPrefSize(100,45);
      submit.setOnAction((ActionEvent event) -> {
         
          mTabSelectionListener.onTabSelected(ImsAnalyzeToolConstants.TabName.CallStatIndicatorPane, mStartDate,mEndDate, null);
         // mTabSelectionListener.onTabSelected(ImsAnalyzeToolConstants.TabName.MediaIndicatorPane, mStartDate,mEndDate);
      });
      rootPan.setPrefSize(mStage.getWidth(), mStage.getHeight());
      rootPan.autosize();

      Label l1 = new Label(lable);
      l1.setFont(javafx.scene.text.Font.font("Verdana",18));
      l1.setWrapText(true);
      rootPan.setTop(l1);
      rootPan.setPadding(new Insets(10,0,0,10));
      GridPane gridPane = new GridPane();
      gridPane.autosize();
      gridPane.setAlignment(Pos.CENTER);
      Label startDateLabel = new Label("Start Date:");
      startDateLabel.setFont(javafx.scene.text.Font.font("Verdana",18));
      gridPane.add(startDateLabel, 1, 4);
      
      
      GridPane.setHalignment(startDateLabel, HPos.LEFT);
      DatePicker startDatePicker = new DatePicker();
      startDatePicker.autosize();
      startDatePicker.setPrefSize(200,35);
      startDatePicker.setOnAction(new EventHandler() {
        public void handle(Event t) {
            LocalDate date = startDatePicker.getValue();
            if(date != null) {
                mStartDate = date.format(DateTimeFormatter.ofPattern("dd:MM:yy"));
                if(disableDoneButton() == false) {
                    submit.setDisable(false);
                } else {
                    submit.setDisable(true);
                }
                System.err.println("Selected date: " + mStartDate);
            }
        }
       });
      gridPane.setHgap(20);
     gridPane.add(startDatePicker, 1, 5);
     Label endDateLabel = new Label("End Date:");
     gridPane.add(endDateLabel, 7, 4);
     endDateLabel.setFont(javafx.scene.text.Font.font("Verdana",18));
     GridPane.setHalignment(endDateLabel, HPos.RIGHT);
     gridPane.setHgap(20);
      //gridPane2.setVgap(10);
     DatePicker endDatePicker = new DatePicker();
     endDatePicker.autosize();
     endDatePicker.setPrefSize(200,35);
     endDatePicker.setOnAction(new EventHandler() {
        public void handle(Event t) {
            LocalDate date = endDatePicker.getValue();
            if(date != null) {
                mEndDate = date.format(DateTimeFormatter.ofPattern("dd:MM:yy"));
                if(disableDoneButton() == false) {
                    submit.setDisable(false);
                } else {
                    submit.setDisable(true);
                }
                System.err.println("Selected date: " + date);
            }
        }
       });
     gridPane.add(endDatePicker, 7, 5);
     rootPan.setCenter(gridPane);
     rootPan.setBottom(submit);
     BorderPane.setAlignment(submit, Pos.CENTER);
     mSettingTab.setContent(rootPan);
     return mSettingTab;
    }
    
        private String createDescriptionLable() {
            StringBuilder sb = new StringBuilder();
            sb.append("Call Data available from date ");
            String minTime = null ,maxTime = null;
            String query = "SELECT MIN(time),MAX(time) FROM service";
            try {
                ResultSet cursor = mIMSAnalyzerDBHelper.rawQuery(query);
                if(cursor != null) {
                    while(cursor.next()) {
                            minTime = cursor.getString("MIN(time)");
                            maxTime = cursor.getString("MAX(time)");
                    }
                    cursor.close();
                } else {
                    return null;
                }
            } catch(SQLException e) {
                
                e.printStackTrace();
                return null;
            }

            mMinStartDate = minTime;
            mMaxEndDate = maxTime;
            sb.append(minTime);
            sb.append(" to ");
            sb.append(maxTime);
            sb.append(". Please select start & end date to avail the call details for specific interval.");
            return sb.toString();
    }
        private boolean disableDoneButton() {
            SimpleDateFormat sdf = new SimpleDateFormat("dd:MM:yy");
            if(mEndDate != null && mStartDate != null) {
                if(mMaxEndDate != null && mMinStartDate != null) {
                    String [] dateAndTimeEnd = mMaxEndDate.split(" ");
                    String [] dateAndTimeStart = mMinStartDate.split(" ");
                    try {
                        Date selStartDate = sdf.parse(mStartDate);
                        Date selEndDate = sdf.parse(mEndDate);
                        Date minStartDate = sdf.parse(dateAndTimeStart[0]);
                        Date maxEndDate = sdf.parse(dateAndTimeEnd[0]);
                        if(maxEndDate.compareTo(selEndDate)>=0 && minStartDate.compareTo(selStartDate)<=0&& selEndDate.compareTo(selStartDate)>= 0) {
                            return false;
                        } else {
                            return true;
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(SettingIndicatorPane.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return true;
                }
                 return true;
            }
             return true;
        }
}
