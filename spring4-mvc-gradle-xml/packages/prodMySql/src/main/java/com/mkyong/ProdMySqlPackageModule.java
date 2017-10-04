package com.mkyong;

import com.mkyong.helloworld.util.DatabaseModule;
import com.mkyong.helloworld.util.PackageModule;
import com.mkyong.helloworld.util.PackageModuleDescriptor;

@PackageModuleDescriptor
public class ProdMySqlPackageModule implements PackageModule {

    @Override
    public String getModuleName() {
        return "prodMySql";
    }

    @Override
    public DatabaseModule.DatabaseType getExpectedDatabaseType() {
        return DatabaseModule.DatabaseType.MY_SQL;
    }

}
