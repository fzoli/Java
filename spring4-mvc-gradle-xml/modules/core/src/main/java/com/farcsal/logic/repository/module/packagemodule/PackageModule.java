package com.farcsal.logic.repository.module.packagemodule;

import com.farcsal.logic.common.module.DatabaseType;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface PackageModule {

    @Nonnull
    String getModuleName();

    @Nullable
    DatabaseType getExpectedDatabaseType();

    @Nonnull
    ImmutableMap<String, String> getProperties();

}
