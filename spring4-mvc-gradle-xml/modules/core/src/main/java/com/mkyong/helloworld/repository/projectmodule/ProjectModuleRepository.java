package com.mkyong.helloworld.repository.projectmodule;

import com.google.common.collect.ImmutableList;
import com.mkyong.helloworld.repository.util.ModuleResolver;
import lombok.Synchronized;
import org.springframework.stereotype.Repository;

import java.util.Comparator;

@Repository
public class ProjectModuleRepository {

    private static final ModuleResolver<ProjectModuleDescriptor, ProjectModule> moduleResolver = new ModuleResolver<>(
            ProjectModuleDescriptor.class, ProjectModule.class);

    private ImmutableList<ProjectModule> projectModules;

    @Synchronized
    public ImmutableList<ProjectModule> getProjectModules() {
        if (projectModules == null) {
            projectModules = createProjectModules();
        }
        return projectModules;
    }

    private ImmutableList<ProjectModule> createProjectModules() {
        return moduleResolver.getModules().stream()
                .sorted(Comparator.comparingInt(o -> o.getDescriptor().priority()))
                .map(ModuleResolver.Wrapper::getModule)
                .collect(ImmutableList.toImmutableList());
    }

}