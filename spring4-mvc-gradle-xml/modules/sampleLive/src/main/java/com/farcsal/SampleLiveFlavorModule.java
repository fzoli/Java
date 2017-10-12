package com.farcsal;

import com.farcsal.logic.repository.module.flavormodule.FlavorModule;
import com.farcsal.logic.repository.module.flavormodule.FlavorModuleDescriptor;
import com.farcsal.logic.repository.module.projectmodule.ProjectModule;

import javax.annotation.Nonnull;

@FlavorModuleDescriptor
public class SampleLiveFlavorModule implements FlavorModule {

    @Nonnull
    @Override
    public String getModuleName() {
        return "sampleLive";
    }

    @Nonnull
    @Override
    public Class<? extends ProjectModule> getProjectModuleClass() {
        return SampleProjectModule.class;
    }

}
