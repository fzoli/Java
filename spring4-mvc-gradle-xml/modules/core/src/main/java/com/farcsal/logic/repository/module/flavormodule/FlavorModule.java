package com.farcsal.logic.repository.module.flavormodule;

import com.farcsal.logic.repository.module.projectmodule.ProjectModule;

import javax.annotation.Nonnull;

public interface FlavorModule {

    @Nonnull
    String getModuleName();

    @Nonnull
    Class<? extends ProjectModule> getProjectModuleClass();

}
