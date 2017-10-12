package com.farcsal;

import com.farcsal.logic.repository.module.projectmodule.ProjectModuleDescriptor;
import com.farcsal.logic.repository.module.projectmodule.ProjectModule;

import javax.annotation.Nonnull;

@ProjectModuleDescriptor
public class SampleProjectModule implements ProjectModule {

    @Override
    @Nonnull
    public String getModuleName() {
        return "sample";
    }

    @Override
    public boolean hasDatabase() {
        return true;
    }

    @Override
    public int numberOfFlavors() {
        return 1;
    }

}
