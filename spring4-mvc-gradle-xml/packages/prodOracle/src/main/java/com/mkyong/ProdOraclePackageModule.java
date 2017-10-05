package com.mkyong;

import com.mkyong.helloworld.common.DatabaseType;
import com.mkyong.helloworld.repository.packagemodule.PackageModule;
import com.mkyong.helloworld.repository.packagemodule.PackageModuleDescriptor;

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
