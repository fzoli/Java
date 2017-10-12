package com.farcsal.logic.repository.module.flavormodule;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

public class FlavorModuleRepositoryTestImpl implements FlavorModuleRepository {

    private final ImmutableList<FlavorModule> flavorModules;

    public FlavorModuleRepositoryTestImpl(@Nonnull ImmutableList<FlavorModule> flavorModules) {
        this.flavorModules = flavorModules;
    }

    @Override
    public ImmutableList<FlavorModule> getFlavorModules() {
        return flavorModules;
    }

}
