package com.yaerin.coolapk_token;

import android.annotation.SuppressLint;
import android.app.Application;

@SuppressLint("Registered")
public class ApplicationProxy extends Application {
    public ApplicationProxy() {
        super();
    }

    @Override
    public String getPackageName() {
        return "com.coolapk.market";
    }
}
