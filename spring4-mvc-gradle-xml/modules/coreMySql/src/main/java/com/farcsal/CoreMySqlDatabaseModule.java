package com.farcsal;

import com.farcsal.logic.common.module.DatabaseType;
import com.farcsal.logic.repository.module.databasemodule.DatabaseModule;
import com.farcsal.logic.repository.module.databasemodule.DatabaseModuleDescriptor;
import com.farcsal.logic.repository.module.projectmodule.ProjectModule;

import javax.annotation.Nonnull;

@DatabaseModuleDescriptor
public class CoreMySqlDatabaseModule implements DatabaseModule {

    @Override
    @Nonnull
    public Class<? extends ProjectModule> getProjectModuleClass() {
        return CoreProjectModule.class;
    }

    @Override
    @Nonnull
    public DatabaseType getDatabaseType() {
        return DatabaseType.MY_SQL;
    }

}
