package com.mkyong;

import com.mkyong.helloworld.util.DatabaseModule;
import com.mkyong.helloworld.util.PackageModule;
import com.mkyong.helloworld.util.PackageModuleDescriptor;

@PackageModuleDescriptor
public class ProdOraclePackageModule implements PackageModule {

    @Override
    public String getModuleName() {
        return "prodOracle";
    }

    @Override
    public DatabaseModule.DatabaseType getExpectedDatabaseType() {
        return DatabaseModule.DatabaseType.ORACLE;
    }

}
