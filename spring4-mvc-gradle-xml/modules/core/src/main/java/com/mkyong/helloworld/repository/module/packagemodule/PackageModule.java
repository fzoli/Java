package com.mkyong.helloworld.repository.module.packagemodule;

import com.mkyong.helloworld.common.module.DatabaseType;

public interface PackageModule {
    String getModuleName();
    DatabaseType getExpectedDatabaseType();
}
