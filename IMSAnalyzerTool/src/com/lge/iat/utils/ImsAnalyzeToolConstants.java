/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.utils;

/**
 *
 * @author jitendra.parkar
 */
public class ImsAnalyzeToolConstants {
   public enum TabName {
        SettingIndicatorTab,
        CallStatIndicatorPane,
        MediaIndicatorPane
    }
    
   /*public enum MediaRootNodeName {
        Media,
        CallMedia,
        GraphMedia
    }*/
   
   public enum RootNodeName {
       CallSuccess,
       PacketLostPerc,
       Media,
       CallMedia,
       GraphMedia,
   }
   
    public enum CallState {
       None,
       LossGrtTen,
       LossGrtFiveLessTen,
       LossLessFive,
       Failed
   }
}
