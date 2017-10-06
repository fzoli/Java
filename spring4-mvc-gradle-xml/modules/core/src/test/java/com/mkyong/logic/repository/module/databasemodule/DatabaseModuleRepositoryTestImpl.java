package com.mkyong.logic.repository.module.databasemodule;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

public class DatabaseModuleRepositoryTestImpl implements DatabaseModuleRepository {

    private final ImmutableList<DatabaseModule> databaseModules;

    public DatabaseModuleRepositoryTestImpl(@Nonnull ImmutableList<DatabaseModule> databaseModules) {
        this.databaseModules = databaseModules;
    }

    @Override
    public ImmutableList<DatabaseModule> getDatabaseModules() {
        return databaseModules;
    }

}
