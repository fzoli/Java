package com.mkyong.helloworld.repository;

import com.google.common.collect.ImmutableList;
import com.mkyong.helloworld.util.DatabaseModule;
import com.mkyong.helloworld.util.DatabaseModuleDescriptor;
import lombok.Synchronized;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseModuleRepository {

    private static final ModuleResolver<DatabaseModuleDescriptor, DatabaseModule> moduleResolver = new ModuleResolver<>(
            DatabaseModuleDescriptor.class, DatabaseModule.class);

    private ImmutableList<DatabaseModule> databaseModules;

    @Synchronized
    public ImmutableList<DatabaseModule> getDatabaseModules() {
        if (databaseModules == null) {
            databaseModules = createDatabaseModules();
        }
        return databaseModules;
    }

    private ImmutableList<DatabaseModule> createDatabaseModules() {
        return moduleResolver.getModules().stream()
                .map(ModuleResolver.Wrapper::getModule)
                .collect(ImmutableList.toImmutableList());
    }

}
