package com.mkyong.helloworld.util;

public interface DatabaseModule {

    Class<? extends ProjectModule> getProjectModuleClass();

    DatabaseType getDatabaseType();

}
