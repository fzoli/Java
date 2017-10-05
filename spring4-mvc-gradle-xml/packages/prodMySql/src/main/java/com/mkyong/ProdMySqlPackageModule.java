package com.mkyong;

import com.mkyong.logic.common.module.DatabaseType;
import com.mkyong.logic.repository.module.packagemodule.PackageModule;
import com.mkyong.logic.repository.module.packagemodule.PackageModuleDescriptor;

@PackageModuleDescriptor
public class ProdMySqlPackageModule implements PackageModule {

    @Override
    public String getModuleName() {
        return "prodMySql";
    }

    @Override
    public DatabaseType getExpectedDatabaseType() {
        return DatabaseType.MY_SQL;
    }

}
