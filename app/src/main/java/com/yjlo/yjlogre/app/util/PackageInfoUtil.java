package com.yjlo.yjlogre.app.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by siwei on 14/10/14.
 */
public class PackageInfoUtil {


    public static int getVersionCode(Context ctx) throws PackageManager.NameNotFoundException {
        PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
        return pinfo.versionCode;
    }

    public static String getVersionName(Context ctx) throws PackageManager.NameNotFoundException {
        PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0);
        return pinfo.versionName;
    }
}
