package com.mkyong;

import com.mkyong.logic.common.module.DatabaseType;
import com.mkyong.logic.repository.module.packagemodule.PackageModule;
import com.mkyong.logic.repository.module.packagemodule.PackageModuleDescriptor;

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
