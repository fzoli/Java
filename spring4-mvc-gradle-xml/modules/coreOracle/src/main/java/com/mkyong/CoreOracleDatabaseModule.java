package com.mkyong;

import com.mkyong.helloworld.repository.module.databasemodule.DatabaseModule;
import com.mkyong.helloworld.repository.module.databasemodule.DatabaseModuleDescriptor;
import com.mkyong.helloworld.common.module.DatabaseType;
import com.mkyong.helloworld.repository.module.projectmodule.ProjectModule;

@DatabaseModuleDescriptor
public class CoreOracleDatabaseModule implements DatabaseModule {

    @Override
    public Class<? extends ProjectModule> getProjectModuleClass() {
        return CoreProjectModule.class;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.ORACLE;
    }

}
