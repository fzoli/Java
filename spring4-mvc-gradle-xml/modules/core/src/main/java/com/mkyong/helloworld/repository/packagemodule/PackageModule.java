package com.mkyong.helloworld.repository.packagemodule;

import com.mkyong.helloworld.common.DatabaseType;

public interface PackageModule {
    String getModuleName();
    DatabaseType getExpectedDatabaseType();
}
