package com.mkyong.logic.repository.module.databasemodule;

import com.google.common.collect.ImmutableList;

public interface DatabaseModuleRepository {
    ImmutableList<DatabaseModule> getDatabaseModules();
}
