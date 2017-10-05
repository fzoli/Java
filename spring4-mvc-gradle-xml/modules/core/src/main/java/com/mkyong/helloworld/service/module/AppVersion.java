package com.mkyong.helloworld.service.module;

import com.mkyong.BuildConfig;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.time.ZoneId;

public class AppVersion {

    @Getter
    private static final AppVersion instance = new AppVersion();

    private AppVersion() {
    }

    public String getShortVersion() {
        return BuildConfig.VERSION + "-" + BuildConfig.REVISION.substring(0, 6) + (BuildConfig.DIRTY ? "-dirty" : "");
    }

    public String getRevision() {
        return BuildConfig.REVISION;
    }

    public OffsetDateTime getCommitTime() {
        return OffsetDateTime.ofInstant(BuildConfig.COMMIT_INSTANT, ZoneId.systemDefault());
    }

    public OffsetDateTime getBuildTime() {
        return OffsetDateTime.ofInstant(BuildConfig.BUILD_INSTANT, ZoneId.systemDefault());
    }

}
