/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.db.interfaces;

import com.lge.iat.utils.ContentValues;
import java.sql.ResultSet;

/**
 *
 * @author jitendra.parkar
 */
public interface IIMSAnalyzerDBHelper {
    public long insert(String table, ContentValues contentValues);
     public long insertWithOnConflict(String table, String nullColumnHack,
            ContentValues initialValues, int conflictAlgorithm);
     public int update(String table, ContentValues values, String whereClause, String[] whereArgs);
     public int updateWithOnConflict(String table, ContentValues values,
            String whereClause, String[] whereArgs, int conflictAlgorithm);
     public ResultSet query(String table, String[] columns, String selection, String[] selectionArgs);
     public ResultSet rawQuery(String where);
}
