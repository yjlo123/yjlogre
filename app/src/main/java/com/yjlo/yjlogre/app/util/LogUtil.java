package com.yjlo.yjlogre.app.util;

import android.util.Log;

import com.yjlo.yjlogre.app.BuildConfig;

/**
 * Created by siwei on 14/10/14.
 */
public class LogUtil {

    public static void Log(String tag, String msg){
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

}
