package com.mkyong;

import com.mkyong.logic.repository.module.databasemodule.DatabaseModule;
import com.mkyong.logic.repository.module.databasemodule.DatabaseModuleDescriptor;
import com.mkyong.logic.common.module.DatabaseType;
import com.mkyong.logic.repository.module.projectmodule.ProjectModule;

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
