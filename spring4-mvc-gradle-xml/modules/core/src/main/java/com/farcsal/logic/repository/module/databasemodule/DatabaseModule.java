package com.farcsal.logic.repository.module.databasemodule;

import com.farcsal.logic.common.module.DatabaseType;
import com.farcsal.logic.repository.module.projectmodule.ProjectModule;

import javax.annotation.Nonnull;

public interface DatabaseModule {

    @Nonnull
    Class<? extends ProjectModule> getProjectModuleClass();

    @Nonnull
    DatabaseType getDatabaseType();

}
