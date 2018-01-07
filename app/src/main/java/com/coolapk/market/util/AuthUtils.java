package com.coolapk.market.util;

import android.content.Context;

public class AuthUtils {

    static {
        System.loadLibrary("a");
    }

    @SuppressWarnings("JniMissingFunction")
    public static native String getAS(Context paramContext, String paramString);

}
