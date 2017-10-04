package com.mkyong.helloworld.util;

public interface PackageModule {
    String getModuleName();
    DatabaseModule.DatabaseType getExpectedDatabaseType();
}
