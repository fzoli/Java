package com.mkyong.helloworld.repository.databasemodule;

import com.mkyong.helloworld.common.DatabaseType;
import com.mkyong.helloworld.repository.projectmodule.ProjectModule;

public interface DatabaseModule {

    Class<? extends ProjectModule> getProjectModuleClass();

    DatabaseType getDatabaseType();

}
