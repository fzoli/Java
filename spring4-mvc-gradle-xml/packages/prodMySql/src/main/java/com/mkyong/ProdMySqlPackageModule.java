package com.mkyong;

import com.mkyong.helloworld.common.module.DatabaseType;
import com.mkyong.helloworld.repository.module.packagemodule.PackageModule;
import com.mkyong.helloworld.repository.module.packagemodule.PackageModuleDescriptor;

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
