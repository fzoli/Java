package com.farcsal.logic.repository.module.databasemodule;

import com.google.common.collect.ImmutableList;
import com.farcsal.logic.repository.module.ModuleResolver;
import lombok.Synchronized;
import org.springframework.stereotype.Repository;

@Repository
public class DatabaseModuleRepositoryImpl implements DatabaseModuleRepository {

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
