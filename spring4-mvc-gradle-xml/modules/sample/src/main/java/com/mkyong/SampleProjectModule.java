package com.mkyong;

import com.mkyong.logic.repository.module.projectmodule.ProjectModule;
import com.mkyong.logic.repository.module.projectmodule.ProjectModuleDescriptor;

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
