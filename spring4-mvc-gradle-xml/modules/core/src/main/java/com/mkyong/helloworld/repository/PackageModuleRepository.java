package com.mkyong.helloworld.repository;

import com.google.common.collect.ImmutableList;
import com.mkyong.helloworld.util.ModuleResolver;
import com.mkyong.helloworld.util.PackageModule;
import com.mkyong.helloworld.util.PackageModuleDescriptor;
import lombok.Synchronized;
import org.springframework.stereotype.Repository;

@Repository
public class PackageModuleRepository {

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
