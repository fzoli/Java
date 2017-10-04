package com.mkyong;

import com.mkyong.helloworld.util.DatabaseType;
import com.mkyong.helloworld.util.PackageModule;
import com.mkyong.helloworld.util.PackageModuleDescriptor;

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
