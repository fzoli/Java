package com.farcsal;

import com.farcsal.logic.common.module.DatabaseType;
import com.farcsal.logic.repository.module.packagemodule.PackageModule;
import com.farcsal.logic.repository.module.packagemodule.PackageModuleDescriptor;

@PackageModuleDescriptor
public class ProdOraclePackageModule implements PackageModule {

    @Override
    public String getModuleName() {
        return "prodOracle";
    }

    @Override
    public DatabaseType getExpectedDatabaseType() {
        return DatabaseType.ORACLE;
    }

}
