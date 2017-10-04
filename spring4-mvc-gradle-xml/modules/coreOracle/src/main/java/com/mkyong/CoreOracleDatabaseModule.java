package com.mkyong;

import com.mkyong.helloworld.util.DatabaseModule;
import com.mkyong.helloworld.util.DatabaseModuleDescriptor;
import com.mkyong.helloworld.util.DatabaseType;
import com.mkyong.helloworld.util.ProjectModule;

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
