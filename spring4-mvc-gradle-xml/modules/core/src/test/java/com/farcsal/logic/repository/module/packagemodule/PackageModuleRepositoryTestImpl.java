package com.farcsal.logic.repository.module.packagemodule;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

public class PackageModuleRepositoryTestImpl implements PackageModuleRepository {

    private final ImmutableList<PackageModule> packageModules;

    public PackageModuleRepositoryTestImpl(@Nonnull ImmutableList<PackageModule> packageModules) {
        this.packageModules = packageModules;
    }

    @Override
    public ImmutableList<PackageModule> getPackageModules() {
        return packageModules;
    }

}
