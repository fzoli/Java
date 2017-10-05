package com.mkyong;

import com.mkyong.helloworld.repository.projectmodule.ProjectModule;
import com.mkyong.helloworld.repository.projectmodule.ProjectModuleDescriptor;

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
