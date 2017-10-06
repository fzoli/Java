package com.mkyong.logic.repository.module.packagemodule;

import com.google.common.collect.ImmutableList;
import com.mkyong.logic.repository.module.ModuleResolver;
import lombok.Synchronized;
import org.springframework.stereotype.Repository;

@Repository
public class PackageModuleRepositoryImpl implements PackageModuleRepository {

    private static final ModuleResolver<PackageModuleDescriptor, PackageModule> moduleResolver = new ModuleResolver<>(
            PackageModuleDescriptor.class, PackageModule.class);

    private ImmutableList<PackageModule> packageModules;

    @Synchronized
    public ImmutableList<PackageModule> getPackageModules() {
        if (packageModules == null) {
            packageModules = createPackageModules();
        }
        return packageModules;
    }

    private ImmutableList<PackageModule> createPackageModules() {
        return moduleResolver.getModules().stream()
                .map(ModuleResolver.Wrapper::getModule)
                .collect(ImmutableList.toImmutableList());
    }

}
