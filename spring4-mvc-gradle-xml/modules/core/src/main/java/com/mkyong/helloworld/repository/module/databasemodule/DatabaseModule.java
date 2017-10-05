package com.mkyong.helloworld.repository.module.databasemodule;

import com.mkyong.helloworld.common.module.DatabaseType;
import com.mkyong.helloworld.repository.module.projectmodule.ProjectModule;

public interface DatabaseModule {

    Class<? extends ProjectModule> getProjectModuleClass();

    DatabaseType getDatabaseType();

}
