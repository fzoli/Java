package com.mkyong;

import com.mkyong.helloworld.util.ProjectModule;
import com.mkyong.helloworld.util.ProjectModuleDescriptor;

@ProjectModuleDescriptor
public class SampleProjectModule implements ProjectModule {

    @Override
    public String getModuleName() {
        return "sample";
    }

}
