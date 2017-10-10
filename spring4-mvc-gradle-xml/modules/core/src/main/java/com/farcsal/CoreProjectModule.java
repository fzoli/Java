package com.farcsal;

import com.farcsal.logic.repository.module.projectmodule.ProjectModuleDescriptor;
import com.farcsal.logic.repository.module.projectmodule.ProjectModule;

@ProjectModuleDescriptor(priority = -1)
public class CoreProjectModule implements ProjectModule {

    @Override
    public String getModuleName() {
        return "core";
    }

    @Override
    public boolean hasDatabase() {
        return true;
    }

}
