package com.mkyong;

import com.mkyong.helloworld.repository.projectmodule.ProjectModule;
import com.mkyong.helloworld.repository.projectmodule.ProjectModuleDescriptor;

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
