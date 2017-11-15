/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lge.iat.db.impl;

import com.lge.iat.utils.ContentValues;
import com.lge.iat.utils.IDTLog;
import com.lge.iat.utils.StringUtils;
import com.lge.iat.db.interfaces.IIMSAnalyzerDBHelper;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.regex.Pattern;
import java.sql.Types;
/**
 *
 * @author jitendra.parkar
 */
public class IMSAnalyzerDBHelper implements IIMSAnalyzerDBHelper{

        private static final Pattern sLimitPattern =
            Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");
        private Connection mDBConnection = null;

    /**
     * When a constraint violation occurs, an immediate ROLLBACK occurs,
     * thus ending the current transaction, and the command aborts with a
     * return code of SQLITE_CONSTRAINT. If no transaction is active
     * (other than the implied transaction that is created on every command)
     * then this algorithm works the same as ABORT.
     */
    public static final int CONFLICT_ROLLBACK = 1;

    /**
     * When a constraint violation occurs,no ROLLBACK is executed
     * so changes from prior commands within the same transaction
     * are preserved. This is the default behavior.
     */
    public static final int CONFLICT_ABORT = 2;

    /**
     * When a constraint violation occurs, the command aborts with a return
     * code SQLITE_CONSTRAINT. But any changes to the database that
     * the command made prior to encountering the constraint violation
     * are preserved and are not backed out.
     */
    public static final int CONFLICT_FAIL = 3;

    /**
     * When a constraint violation occurs, the one row that contains
     * the constraint violation is not inserted or changed.
     * But the command continues executing normally. Other rows before and
     * after the row that contained the constraint violation continue to be
     * inserted or updated normally. No error is returned.
     */
    public static final int CONFLICT_IGNORE = 4;

    /**
     * When a UNIQUE constraint violation occurs, the pre-existing rows that
     * are causing the constraint violation are removed prior to inserting
     * or updating the current row. Thus the insert or update always occurs.
     * The command continues executing normally. No error is returned.
     * If a NOT NULL constraint violation occurs, the NULL value is replaced
     * by the default value for that column. If the column has no default
     * value, then the ABORT algorithm is used. If a CHECK constraint
     * violation occurs then the IGNORE algorithm is used. When this conflict
     * resolution strategy deletes rows in order to satisfy a constraint,
     * it does not invoke delete triggers on those rows.
     * This behavior might change in a future release.
     */
    public static final int CONFLICT_REPLACE = 5;

    /**
     * Use the following when no conflict action is specified.
     */
    public static final int CONFLICT_NONE = 0;

    private static final String[] CONFLICT_VALUES = new String[]{"", " OR ROLLBACK ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR REPLACE "};
    private static final int DATABASE_VERSION = 1;
    private String m_objDbName;

//    public static final String ID = "id";
//    public static final String S_LOG = "s_log";
//    public static final String E_LOG = "e_log";
//    public static final String MSG = "msg";
//    public static final String RC = "rc";
//    public static final String E_ID = "e_id";
//    public static final String SE_ID = "se_id";
//    public static final String PE_CHILD = "pe_child";
//    public static final String LEVEL = "level";
//    public static final String MODE = "mode";
//    public static final String TO_BE = "to_be";
    public IMSAnalyzerDBHelper(String db_Path) {
        //super(context, db_name, null, DATABASE_VERSION);
        mDBConnection = createDBConnection(db_Path);
        m_objDbName = db_Path;
        IDTLog.d("DBHelper, ");
    }

    String getDatabaseName() {
        return m_objDbName;
    }
    
    void deleteDatabase(String dbName) {
        
    }
    
    private Connection createDBConnection(String db_Path) {
        File file = new File(db_Path);
        if(file.exists()) {
            String url = "jdbc:sqlite:" + db_Path;
            try{
                Connection conn = DriverManager.getConnection(url);
                if (conn != null) {
                    return conn;
                }    
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /* public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (String table : DATABASE_TABLES) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
        onCreate(db);
    }*/
    public long insert(String table, ContentValues contentValues) {
            return insertWithOnConflict(table, null, contentValues, CONFLICT_NONE);
    }

    public long insertWithOnConflict(String table, String nullColumnHack,
            ContentValues initialValues, int conflictAlgorithm) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT");
            sql.append(CONFLICT_VALUES[conflictAlgorithm]);
            sql.append(" INTO ");
            sql.append(table);
            sql.append('(');

            Object[] bindArgs = null;
            int size = (initialValues != null && initialValues.size() > 0)
                    ? initialValues.size() : 0;
            if (size > 0) {
                bindArgs = new Object[size];
                int i = 0;
                for (String colName : initialValues.keySet()) {
                    sql.append((i > 0) ? "," : "");
                    sql.append(colName);
                    bindArgs[i++] = initialValues.get(colName);
                }
                sql.append(')');
                sql.append(" VALUES (");
                for (i = 0; i < size; i++) {
                    String argsStr = null;
                    if(bindArgs[i] instanceof String) {
                        argsStr = "\'" + bindArgs[i]+"\'";
                    } else if(bindArgs[i] instanceof Boolean) {
                        argsStr = "" + ((Boolean)bindArgs[i]?1:0);
                    }else {
                        argsStr = ""+bindArgs[i];
                    }
                    sql.append(((i > 0) ? ", ": "")+argsStr);
                }
            } else {
                sql.append(nullColumnHack + ") VALUES (NULL");
            }
            sql.append(')');
            // SQLiteStatement statement = new SQLiteStatement(this, sql.toString(), bindArgs);
            IDTLog.d("SQL :"+sql.toString());
            Connection conn = mDBConnection;
            try (Statement pstmt = conn.createStatement() //prepareStatement(sql.toString());
            ) {
                    int ret =  pstmt.executeUpdate(sql.toString());
                    
                    if(ret == 1) {
                        
                    }
                    return ret;
            } catch (SQLException e){
                e.printStackTrace();
            }
        }finally {
        }
        return -1;
    }
    
 public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return updateWithOnConflict(table, values, whereClause, whereArgs, CONFLICT_NONE);
    }
 public int updateWithOnConflict(String table, ContentValues values,
            String whereClause, String[] whereArgs, int conflictAlgorithm) {
        if (values == null || values.size() == 0) {
            throw new IllegalArgumentException("Empty values");
        }
        try {
            StringBuilder sql = new StringBuilder(120);
            sql.append("UPDATE ");
            sql.append(CONFLICT_VALUES[conflictAlgorithm]);
            sql.append(table);
            sql.append(" SET ");

            // move all bind args to one array
            int setValuesSize = values.size();
            int bindArgsSize = (whereArgs == null) ? setValuesSize : (setValuesSize + whereArgs.length);
            Object[] bindArgs = new Object[bindArgsSize];
            int i = 0;
            for (String colName : values.keySet()) {
                sql.append((i > 0) ? "," : "");
                sql.append(colName);
                bindArgs[i++] = values.get(colName);
                sql.append("=?");
            }
            if (whereArgs != null) {
                for (i = setValuesSize; i < bindArgsSize; i++) {
                    bindArgs[i] = whereArgs[i - setValuesSize];
                }
            }
            if (!StringUtils.isEmpty(whereClause)) {
                sql.append(" WHERE ");
                sql.append(whereClause);
            }

           Connection conn = mDBConnection;
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            try {
               if (pstmt.execute() == true) {
                    return pstmt.getUpdateCount();
                } else {
                     pstmt.close();
                    return -1;
                }
            } catch(SQLException e) {
            }finally {
                pstmt.close();
            }
        }  catch(SQLException e) {
            }finally {
           
        }
        return -1;
    }
 
    public ResultSet query(String table, String[] columns, String selection, String[] selectionArgs) {
        ResultSet rs = null;
        try{
            Connection conn = mDBConnection;
            PreparedStatement stmt  = conn.prepareStatement(buildQueryString(false, table, columns, selection, selectionArgs, null,
                null, null, null));
            /*int i=1;
            for(String args:selectionArgs) {
                stmt.setObject(i, args);
                i++;
            }*/
            rs = stmt.executeQuery();
        } catch (SQLException e) {
            IDTLog.d(e.getMessage());
        }
        return rs;
    }

     public  String buildQueryString(
            boolean distinct, String tables, String[] columns, String where,String whereArgs[],
            String groupBy, String having, String orderBy, String limit) {
        if (groupBy != null &&  !groupBy.equalsIgnoreCase("") && having != null &&  !having.equalsIgnoreCase("")) {
            throw new IllegalArgumentException(
                    "HAVING clauses are only permitted when using a groupBy clause");
        }
        if (!StringUtils.isEmpty(limit) && !sLimitPattern.matcher(limit).matches()) {
            throw new IllegalArgumentException("invalid LIMIT clauses:" + limit);
        }

        StringBuilder query = new StringBuilder(120);

        query.append("SELECT ");
        if (distinct) {
            query.append("DISTINCT ");
        }
        if (columns != null && columns.length != 0) {
            appendColumns(query, columns);
        } else {
            query.append("* ");
        }
        
        
        String replaceStr = where;
        if(where != null && whereArgs != null) {
            StringBuilder whereClauseBuilder = new StringBuilder(where);
            int lastIndexOfQue = 0;
            for(String str :whereArgs) {
                int indexOfEqu = whereClauseBuilder.indexOf("=", lastIndexOfQue);
                
                String nameOfField = whereClauseBuilder.substring(lastIndexOfQue, indexOfEqu).trim();
                int lastIndexOfSpace = nameOfField.lastIndexOf(" ");
                if( lastIndexOfSpace > -1) {
                     nameOfField = nameOfField.substring(lastIndexOfSpace,nameOfField.length());
                }
                try {
                Connection conn = mDBConnection;
                PreparedStatement stm = conn.prepareStatement("SELECT "+ nameOfField+ " FROM "+ tables);
                ResultSet cursor = stm.executeQuery();
                ResultSetMetaData rsm = cursor.getMetaData();
                Class<?> type = SQLTypeMap.toClass(rsm.getColumnType(1));
                cursor.close();
                String typeName = type.getName();
                lastIndexOfQue = whereClauseBuilder.indexOf("?", lastIndexOfQue);
                if(typeName.contains("String")) {
                    whereClauseBuilder.replace(lastIndexOfQue, lastIndexOfQue+1, "'"+str+"'");
                    lastIndexOfQue+=(str.length()-1+2);
                } else if(typeName.equals("Boolean")) {
                    whereClauseBuilder.replace(lastIndexOfQue, lastIndexOfQue+1, str.equals("true")?"1":"0");
                    lastIndexOfQue+=1;
                } else {
                    whereClauseBuilder.replace(lastIndexOfQue, lastIndexOfQue+1, str);
                    lastIndexOfQue+=(str.length()-1);
                }
                } catch (SQLException e) {
                }
                
            }
            replaceStr = whereClauseBuilder.toString();
        }
        query.append("FROM ");
        query.append(tables);
        appendClause(query, " WHERE ", replaceStr);
        appendClause(query, " GROUP BY ", groupBy);
        appendClause(query, " HAVING ", having);
        appendClause(query, " ORDER BY ", orderBy);
        appendClause(query, " LIMIT ", limit);

        return query.toString();
    }
    
    private static void appendClause(StringBuilder s, String name, String clause) {
        if (!StringUtils.isEmpty(clause)) {
            s.append(name);
            s.append(clause);
        }
    }
    
     /**
     * Add the names that are non-null in columns to s, separating
     * them with commas.
     */
    public static void appendColumns(StringBuilder s, String[] columns) {
        int n = columns.length;

        for (int i = 0; i < n; i++) {
            String column = columns[i];

            if (column != null) {
                if (i > 0) {
                    s.append(", ");
                }
                s.append(column);
            }
        }
        s.append(' ');
    }
    
    
    public ResultSet rawQuery(String where) {
        ResultSet rs = null;
        try {
             Statement stmt  = mDBConnection.createStatement();
             rs  = stmt.executeQuery(where);
        } catch (SQLException e) {
            e.printStackTrace();
        }           
        return rs;
    }
    
    /*public ResultSet rawQuery(String where) {
        ResultSet rs = null;
        try {
        Connection conn = connect();
             PreparedStatement stmt  = conn.prepareStatement(where, ResultSet.TYPE_SCROLL_SENSITIVE, 
                        ResultSet.CONCUR_UPDATABLE);
             rs  = stmt.executeQuery();
        } catch (SQLException e) {
            
        }           
        return rs;
    }*/

}

/**
 * Converts database types to Java class types.
 */
class SQLTypeMap {
    /**
     * Translates a data type from an integer (java.sql.Types value) to a string
     * that represents the corresponding class.
     * 
     * @param type
     *            The java.sql.Types value to convert to its corresponding class.
     * @return The class that corresponds to the given java.sql.Types
     *         value, or Object.class if the type has no known mapping.
     */
    public static Class<?> toClass(int type) {
        Class<?> result = Object.class;

        switch (type) {
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
                result = String.class;
                break;

            case Types.NUMERIC:
            case Types.DECIMAL:
                result = java.math.BigDecimal.class;
                break;

            case Types.BIT:
                result = Boolean.class;
                break;

            case Types.TINYINT:
                result = Byte.class;
                break;

            case Types.SMALLINT:
                result = Short.class;
                break;

            case Types.INTEGER:
                result = Integer.class;
                break;

            case Types.BIGINT:
                result = Long.class;
                break;

            case Types.REAL:
            case Types.FLOAT:
                result = Float.class;
                break;

            case Types.DOUBLE:
                result = Double.class;
                break;

            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
                result = Byte[].class;
                break;

            case Types.DATE:
                result = java.sql.Date.class;
                break;

            case Types.TIME:
                result = java.sql.Time.class;
                break;

            case Types.TIMESTAMP:
                result = java.sql.Timestamp.class;
                break;
        }

        return result;
    }
}
