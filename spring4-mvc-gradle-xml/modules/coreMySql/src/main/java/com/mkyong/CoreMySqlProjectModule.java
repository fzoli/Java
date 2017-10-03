package com.mkyong;

import com.mkyong.helloworld.util.ProjectModule;
import com.mkyong.helloworld.util.ProjectModuleDescriptor;

@ProjectModuleDescriptor
public class CoreMySqlProjectModule implements ProjectModule {

    @Override
    public String getModuleName() {
        return "coreMySql";
    }

}
