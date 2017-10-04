package com.mkyong.helloworld.util;

public interface DatabaseModule {

    enum DatabaseType {
        ORACLE,
        MY_SQL,
    }

    Class<? extends ProjectModule> getProjectModuleClass();

    DatabaseType getDatabaseType();

}
