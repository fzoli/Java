package com.mkyong.logic.repository.module.databasemodule;

import com.mkyong.logic.common.module.DatabaseType;
import com.mkyong.logic.repository.module.projectmodule.ProjectModule;

public interface DatabaseModule {

    Class<? extends ProjectModule> getProjectModuleClass();

    DatabaseType getDatabaseType();

}
