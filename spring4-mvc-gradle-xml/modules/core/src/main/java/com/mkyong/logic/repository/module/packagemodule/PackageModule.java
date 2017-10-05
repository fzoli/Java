package com.mkyong.logic.repository.module.packagemodule;

import com.mkyong.logic.common.module.DatabaseType;

public interface PackageModule {
    String getModuleName();
    DatabaseType getExpectedDatabaseType();
}
