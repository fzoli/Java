package com.farcsal;

import com.farcsal.logic.common.module.DatabaseType;
import com.farcsal.logic.repository.module.packagemodule.PackageModule;
import com.farcsal.logic.repository.module.packagemodule.PackageModuleDescriptor;
import com.google.common.collect.ImmutableMap;

import javax.annotation.Nonnull;

@PackageModuleDescriptor
public class ProdOraclePackageModule implements PackageModule {

    @Override
    @Nonnull
    public String getModuleName() {
        return "prodOracle";
    }

    @Override
    public DatabaseType getExpectedDatabaseType() {
        return DatabaseType.ORACLE;
    }

    @Nonnull
    @Override
    public ImmutableMap<String, String> getProperties() {
        return ImmutableMap.of();
    }

}
