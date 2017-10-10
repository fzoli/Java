package com.farcsal;

import com.farcsal.logic.repository.module.projectmodule.ProjectModuleDescriptor;
import com.farcsal.logic.repository.module.projectmodule.ProjectModule;

@ProjectModuleDescriptor
public class SampleProjectModule implements ProjectModule {

    @Override
    public String getModuleName() {
        return "sample";
    }

    @Override
    public boolean hasDatabase() {
        return true;
    }

}
