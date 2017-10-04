package com.mkyong;

import com.mkyong.helloworld.util.DatabaseModule;
import com.mkyong.helloworld.util.DatabaseModuleDescriptor;
import com.mkyong.helloworld.util.ProjectModule;

@DatabaseModuleDescriptor
public class CoreMySqlDatabaseModule implements DatabaseModule {

    @Override
    public Class<? extends ProjectModule> getProjectModuleClass() {
        return CoreProjectModule.class;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.MY_SQL;
    }

}
