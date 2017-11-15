/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.utils;

import javafx.scene.control.Tab;

/**
 *
 * @author jitendra.parkar
 */
public class IMSTab extends Tab {
    private ImsAnalyzeToolConstants.TabName tabName = null;
    
    public IMSTab(ImsAnalyzeToolConstants.TabName name) {
        super();
        tabName = name;
    }

    public ImsAnalyzeToolConstants.TabName getTabName() {
        return tabName;
    }
}
