package com.mkyong;

import com.mkyong.helloworld.common.DatabaseType;
import com.mkyong.helloworld.repository.packagemodule.PackageModule;
import com.mkyong.helloworld.repository.packagemodule.PackageModuleDescriptor;

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
