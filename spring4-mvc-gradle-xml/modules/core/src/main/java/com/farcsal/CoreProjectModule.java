package com.farcsal;

import com.farcsal.logic.repository.module.projectmodule.ProjectModuleDescriptor;
import com.farcsal.logic.repository.module.projectmodule.ProjectModule;

import javax.annotation.Nonnull;

@ProjectModuleDescriptor(priority = -1)
public class CoreProjectModule implements ProjectModule {

    @Override
    @Nonnull
    public String getModuleName() {
        return "core";
    }

    @Override
    public boolean hasDatabase() {
        return true;
    }

    @Override
    public int numberOfFlavors() {
        return 0;
    }

}
