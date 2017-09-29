package com.mkyong.helloworld.util;

import com.mkyong.BuildConfig;

public class AppVersion {

    private static final AppVersion instance = new AppVersion();

    public static AppVersion getInstance() {
        return instance;
    }

    private AppVersion() {
    }

    public String getShortVersion() {
        return BuildConfig.VERSION + "-" + BuildConfig.REVISION.substring(0, 6);
    }

}
