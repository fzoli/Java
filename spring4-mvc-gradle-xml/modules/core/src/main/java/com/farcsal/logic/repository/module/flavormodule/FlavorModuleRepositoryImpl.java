package com.farcsal.logic.repository.module.flavormodule;

import com.farcsal.logic.repository.module.ModuleResolver;
import com.google.common.collect.ImmutableList;
import lombok.Synchronized;
import org.springframework.stereotype.Repository;

@Repository
public class FlavorModuleRepositoryImpl implements FlavorModuleRepository {

    private static final ModuleResolver<FlavorModuleDescriptor, FlavorModule> moduleResolver = new ModuleResolver<>(
            FlavorModuleDescriptor.class, FlavorModule.class);

    private ImmutableList<FlavorModule> flavorModules;

    @Synchronized
    public ImmutableList<FlavorModule> getFlavorModules() {
        if (flavorModules == null) {
            flavorModules = createFlavorModules();
        }
        return flavorModules;
    }

    private ImmutableList<FlavorModule> createFlavorModules() {
        return moduleResolver.getModules().stream()
                .map(ModuleResolver.Wrapper::getModule)
                .collect(ImmutableList.toImmutableList());
    }

}
