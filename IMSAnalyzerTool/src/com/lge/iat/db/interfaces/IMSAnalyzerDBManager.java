/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.db.interfaces;

import com.lge.iat.db.impl.IMSAnalyzerDBHelper;
import java.util.HashMap;

/**
 *
 * @author jitendra.parkar
 */
public class IMSAnalyzerDBManager {
    
    private static HashMap<String,IIMSAnalyzerDBHelper> pathDBHelperMap = new HashMap<String,IIMSAnalyzerDBHelper>();
    public static void CreateDBHelper(String dbPath)
    {
        //if(mDBHelperInterface == null)
        if(pathDBHelperMap.get(dbPath) == null) {
            IIMSAnalyzerDBHelper mDBHelperInterface  = new IMSAnalyzerDBHelper(dbPath);
            pathDBHelperMap.put(dbPath, mDBHelperInterface);
        }
    }
    
    public static IIMSAnalyzerDBHelper getImsAnalyzerDBHelper(String dbPath)
    {
        return pathDBHelperMap.get(dbPath);
    }
    
     public static void DeleteDBHelper(String dbPath)
    {
        if(pathDBHelperMap != null)
            pathDBHelperMap.remove(dbPath);
    }
    
}
