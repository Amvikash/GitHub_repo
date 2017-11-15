/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.perfindicatorapp.perfindicator.impl;
import com.lge.iat.utils.CallSession;
import com.lge.iat.utils.ImsAnalyzeToolConstants.TabName;
import java.util.ArrayList;
/**
 *
 * @author jitendra.parkar
 */
public interface ITabSelectionListener {
    public void onTabSelected(TabName tabName, String startDate, String endDate, ArrayList<CallSession> callSessionList);
}
