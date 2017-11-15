/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.perfindicatorapp.perfindicator.interfaces;

import com.lge.iat.perfindicatorapp.perfindicator.impl.CallStatIndicatorPane;
import com.lge.iat.perfindicatorapp.perfindicator.impl.ITabSelectionListener;
import com.lge.iat.utils.ImsAnalyzeToolConstants.TabName;
import com.lge.iat.perfindicatorapp.perfindicator.impl.MediaIndicatorPane;
import com.lge.iat.perfindicatorapp.perfindicator.impl.SettingIndicatorPane;
import com.lge.iat.utils.CallSession;
import java.util.ArrayList;
import javafx.stage.Stage;
/**
 *
 * @author jitendra.parkar
 */
public class PerformanceIndicatorFactory {
   /* private static SettingIndicatorPane mSettingIndicatorPane = null;
     private static CallStatIndicatorPane mCallStatIndicatorPane = null;
      private static MediaIndicatorPane mMediaIndicatorPane = null;*/
    public static IPerformanceIndicatorPane createPerformanceIndicatorPanel(TabName panelName, ITabSelectionListener tabSelectionListener,Stage prStage,String startDate, String endDate,String dbPath, ArrayList<CallSession> callSessionList) {
        if(panelName == TabName.SettingIndicatorTab) {
           /*if(mSettingIndicatorPane ==  null) {
                mSettingIndicatorPane = new SettingIndicatorPane(tabSelectionListener,prStage);
           }*/
            return  new SettingIndicatorPane(tabSelectionListener,prStage,dbPath);
        } else if(panelName == TabName.CallStatIndicatorPane) {
            /*if(mCallStatIndicatorPane == null) {
                mCallStatIndicatorPane = new CallStatIndicatorPane(tabSelectionListener,prStage,startDate,endDate);
            }*/
            return new CallStatIndicatorPane(tabSelectionListener,prStage,startDate,endDate,dbPath);
        } else if(panelName == TabName.MediaIndicatorPane) {
          /*  if(mMediaIndicatorPane == null) {
                mMediaIndicatorPane = new MediaIndicatorPane(tabSelectionListener,prStage,startDate,endDate);
            }*/
            return new MediaIndicatorPane(tabSelectionListener,prStage,startDate,endDate,dbPath,callSessionList);
        } else {
            return null;
        }
    }
    
}
