package com.mkyong;

import com.mkyong.helloworld.util.ProjectModule;
import com.mkyong.helloworld.util.ProjectModuleDescriptor;

@ProjectModuleDescriptor
public class ProdMySqlProjectModule implements ProjectModule {

    @Override
    public String getModuleName() {
        return "prodMySql";
    }

    @Override
    public boolean hasDatabase() {
        return false;
    }

}
