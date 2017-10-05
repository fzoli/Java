package com.mkyong;

import com.mkyong.helloworld.repository.databasemodule.DatabaseModule;
import com.mkyong.helloworld.repository.databasemodule.DatabaseModuleDescriptor;
import com.mkyong.helloworld.common.DatabaseType;
import com.mkyong.helloworld.repository.projectmodule.ProjectModule;

@DatabaseModuleDescriptor
public class SampleMySqlDatabaseModule implements DatabaseModule {

    @Override
    public Class<? extends ProjectModule> getProjectModuleClass() {
        return SampleProjectModule.class;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.MY_SQL;
    }

}
