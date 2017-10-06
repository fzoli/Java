package com.mkyong.logic.repository.module.projectmodule;

import com.google.common.collect.ImmutableList;

import javax.annotation.Nonnull;

public class ProjectModuleRepositoryTestImpl implements ProjectModuleRepository {

    private final ImmutableList<ProjectModule> projectModules;

    public ProjectModuleRepositoryTestImpl(@Nonnull ImmutableList<ProjectModule> projectModules) {
        this.projectModules = projectModules;
    }

    @Override
    public ImmutableList<ProjectModule> getProjectModules() {
        return projectModules;
    }

}
