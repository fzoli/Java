package com.farcsal.logic.repository.module.packagemodule;

import com.farcsal.logic.common.module.DatabaseType;

public interface PackageModule {
    String getModuleName();
    DatabaseType getExpectedDatabaseType();
}
