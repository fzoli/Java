package com.farcsal;

import com.farcsal.logic.common.module.DatabaseType;
import com.farcsal.logic.repository.module.databasemodule.DatabaseModule;
import com.farcsal.logic.repository.module.databasemodule.DatabaseModuleDescriptor;
import com.farcsal.logic.repository.module.projectmodule.ProjectModule;

@DatabaseModuleDescriptor
public class SampleOracleDatabaseModule implements DatabaseModule {

    @Override
    public Class<? extends ProjectModule> getProjectModuleClass() {
        return SampleProjectModule.class;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.ORACLE;
    }

}
