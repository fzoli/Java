package com.farcsal.logic.repository.module.projectmodule;

import javax.annotation.Nonnull;

public interface ProjectModule {

    @Nonnull
    String getModuleName();

    boolean hasDatabase();

}
