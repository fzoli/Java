package com.mkyong.helloworld.util;

public interface DatabaseModule {

    enum DatabaseType {
        ORACLE,
        MYSQL,
    }

    Class<? extends ProjectModule> getProjectModuleClass();

    DatabaseType getDatabaseType();

}
