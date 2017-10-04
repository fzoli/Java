package com.mkyong;

import com.mkyong.helloworld.util.ProjectModule;
import com.mkyong.helloworld.util.ProjectModuleDescriptor;

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
