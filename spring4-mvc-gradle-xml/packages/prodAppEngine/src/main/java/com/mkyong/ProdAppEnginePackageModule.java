package com.mkyong;

import com.mkyong.logic.common.module.DatabaseType;
import com.mkyong.logic.repository.module.packagemodule.PackageModule;
import com.mkyong.logic.repository.module.packagemodule.PackageModuleDescriptor;

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
