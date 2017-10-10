package com.farcsal;

import com.farcsal.logic.common.module.DatabaseType;
import com.farcsal.logic.repository.module.packagemodule.PackageModule;
import com.farcsal.logic.repository.module.packagemodule.PackageModuleDescriptor;

@PackageModuleDescriptor
public class ProdAppEnginePackageModule implements PackageModule {

    @Override
    public String getModuleName() {
        return "prodAppEngine";
    }

    @Override
    public DatabaseType getExpectedDatabaseType() {
        return DatabaseType.MY_SQL;
    }

}
