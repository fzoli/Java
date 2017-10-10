package com.farcsal.logic.repository.module.databasemodule;

import com.farcsal.logic.common.module.DatabaseType;
import com.farcsal.logic.repository.module.projectmodule.ProjectModule;

public interface DatabaseModule {

    Class<? extends ProjectModule> getProjectModuleClass();

    DatabaseType getDatabaseType();

}
