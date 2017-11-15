package com.lge.iat.utils;

import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by raghavendra.niranjan on 06-02-2017.
 */

public class IDTLog {
   
    private static final boolean DEBUG = false;
    private static final String TAG = "IDTLog";

    public static final void v(String log) {
        if (DEBUG) {
            Logger loggs = Logger.getLogger("my.logger");
            loggs.log(Level.ALL, getMessage(log));
            System.out.println(log);
        }
    }

    public static final void d(String log) {
        if (DEBUG) {
            Logger loggs = Logger.getLogger("my.logger");
            loggs.log(Level.FINE, getMessage(log));
            System.out.println(log);
        }
    }

    public static final void i(String log) {
        if (DEBUG) {
            Logger loggs = Logger.getLogger("my.logger");
            loggs.log(Level.INFO, getMessage(log));
            System.out.println(log);
        }
    }

    public static final void w(String log) {
        if (DEBUG) {
            Logger loggs = Logger.getLogger("my.logger");
            loggs.log(Level.WARNING, getMessage(log));
            System.out.println(log);
        }
    }

    public static final void e(String log) {
        if (DEBUG) {
            Logger loggs = Logger.getLogger("my.logger");
            loggs.log(Level.SEVERE, getMessage(log));
        }
    }

    public static String lastSubString(String s, String delimiter) {
        if (s == null) {
            return "(null)";
        } else if (s.isEmpty()) {
            return "(empty)";
        } else {
            int index = (delimiter == null) ? (-1) : s.lastIndexOf(delimiter);
            return (index < 0) ? s : s.substring(index + 1, s.length());
        }
    }

    public static String lastSubString(String s, int count) {
        if (s == null) {
            return "(null)";
        } else if (s.isEmpty()) {
            return "(empty)";
        } else {
            if (s.length() < count) {
                return s;
            }

            return subString(s, s.length() - count, s.length());
        }
    }

    public static String subString(String s, int start, int end) {
        if (s == null) {
            return "(null)";
        } else if (s.isEmpty()) {
            return "(empty)";
        } else {
            start = ((start < 0) || (start > s.length())) ? 0 : start;
            end = ((end < 0) || (end > s.length())) ? s.length() : end;

            if (start > end) {
                return "(empty)";
            }

            return s.substring(start, end);
        }
    }

    private static String getMessage(String log) {
        StackTraceElement[] elements = (new Throwable()).getStackTrace();
        StackTraceElement ste = elements[2];

        return ("[" + lastSubString(ste.getClassName(), ".")
                + "::" + ste.getMethodName() + "] "
                + log
                + " --- " + ste.getFileName()
                + ":" + ste.getLineNumber());
    }
}
