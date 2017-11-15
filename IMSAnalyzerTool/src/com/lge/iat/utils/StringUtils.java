package com.lge.iat.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raghavendra.niranjan on 01-02-2017.
 */

public class StringUtils {
    //private static final String TAG = "StringUtils";
    private static StringUtils mInstance;

    private StringUtils(){}

    public static StringUtils getInstance()
    {
        if(mInstance == null)
        {
            mInstance = new StringUtils();
        }
        return mInstance;
    }
    //@org.jetbrains.annotations.Contract("null, _ -> null")
    public static String[] splitString(String str, char delimiter) {

        if (str == null) {
            return null;
        }

        //return str.split(String.valueOf(delimiter));
       if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return new String[0];
        }
        final List<String> list = new ArrayList<String>();
        int i = 0, start = 0;
        boolean match = false;
        boolean lastMatch = false;
        for (i = 0;i<len;i++) {
            if (str.charAt(i) == delimiter) {
                if (match ) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = i+1;
                continue;
            }
            lastMatch = false;
            match = true;
        }
        if (match || lastMatch) {
            list.add(str.substring(start, i));
        }
        return list.toArray(new String[list.size()]);
    }

    public boolean findMultiplePatten(String[] patterns, String targetString)
    {
        if(patterns == null || targetString == null || patterns.length == 0 )
            return false;
        for(String pattern : patterns)
        {
            if(!findPatten(pattern,targetString))
            {
                return false;
            }
        }
        return true;
    }

    public boolean findPatten(String pattern, String targetString)
    {
        if(targetString == null) {
            return false;
        }
        //return targetString.contains(pattern);
        int index = KMPSearch(pattern,targetString);
        if(index == -1)
        {
            return false;
        }
        return true;
    }
    public int findPattenwithindex(String pattern, String targetString)
    {
        if(targetString == null)
            return 0;
        //return targetString.indexOf(pattern);
       return KMPSearch(pattern,targetString);
    }
    int KMPSearch(String pat, String targetString)
    {
        //IDTLog.i("KMPSearch, pat =  "+ pat+", targetString = "+targetString);
        if(pat == null || targetString == null)
        {
            return -1;
        }

        int M = pat.length();
        int N = targetString.length();
        if(M == 0 || N == 0)
        {
            return -1;
        }
        int index = -1;
        //boolean retVal = false;
        // create lps[] that will hold the longest
        // prefix suffix values for pattern
        int lps[] = new int[M];
        int j = 0;  // index for pat[]

        // Preprocess the pattern (calculate lps[]
        // array)
        computeLPSArray(pat,M,lps);

        int i = 0;  // index for txt[]
        while (i < N)
        {
            if (pat.charAt(j) == targetString.charAt(i))
            {
                j++;
                i++;
            }
            if (j == M)
            {
//                IDTLog.i("Found pattern "+
//                        "at index " + (i-j));
                //retVal = true;
                index = i - j;
                j = lps[j-1];
            }

            // mismatch after j matches
            else if (i < N && pat.charAt(j) != targetString.charAt(i))
            {
                // Do not match lps[0..lps[j-1]] characters,
                // they will match anyway
                if (j != 0)
                    j = lps[j-1];
                else
                    i = i+1;
            }
        }
        return index;
    }

    void computeLPSArray(String pat, int M, int lps[])
    {
        // length of the previous longest prefix suffix
        int len = 0;
        int i = 1;
        lps[0] = 0;  // lps[0] is always 0

        // the loop calculates lps[i] for i = 1 to M-1
        while (i < M)
        {
            if (pat.charAt(i) == pat.charAt(len))
            {
                len++;
                lps[i] = len;
                i++;
            }
            else  // (pat[i] != pat[len])
            {
                // This is tricky. Consider the example.
                // AAACAAAA and i = 7. The idea is similar
                // to search step.
                if (len != 0)
                {
                    len = lps[len-1];

                    // Also, note that we do not increment
                    // i here
                }
                else  // if (len == 0)
                {
                    lps[i] = len;
                    i++;
                }
            }
        }
    }
    
    public static boolean isEmpty(String str) {
        if(str == null) {
            return true;
        }
        if(str.equalsIgnoreCase("")) {
            return true;
        }
        return false;
    }
}
