package com.mkyong;

import com.mkyong.helloworld.common.module.DatabaseType;
import com.mkyong.helloworld.repository.module.packagemodule.PackageModule;
import com.mkyong.helloworld.repository.module.packagemodule.PackageModuleDescriptor;

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
