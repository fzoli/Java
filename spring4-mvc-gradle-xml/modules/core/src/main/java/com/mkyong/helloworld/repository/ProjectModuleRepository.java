package com.mkyong.helloworld.repository;

import com.google.common.collect.ImmutableList;
import com.mkyong.helloworld.util.DatabaseModule;
import com.mkyong.helloworld.util.ProjectModule;
import com.mkyong.helloworld.util.ProjectModuleDescriptor;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.Optional;

@Repository
public class ProjectModuleRepository {

    private static final ModuleResolver<ProjectModuleDescriptor, ProjectModule> moduleResolver = new ModuleResolver<>(
            ProjectModuleDescriptor.class, ProjectModule.class);

    private ImmutableList<ProjectModule> projectModules;

    @Autowired
    private DatabaseModuleRepository databaseModuleRepository;

    @Synchronized
    public ImmutableList<ProjectModule> getProjectModules() {
        if (projectModules == null) {
            projectModules = createProjectModules();
        }
        return projectModules;
    }

    public Optional<DatabaseModule> getDatabaseModule(ProjectModule projectModule) {
        Optional<DatabaseModule> dbModule = findDatabaseModule(projectModule);
        if (projectModule.hasDatabase()) {
            if (!dbModule.isPresent()) {
                throw new IllegalStateException("Wrong configuration. Database module is NOT present.");
            }
            return dbModule;
        }
        else {
            if (dbModule.isPresent()) {
                throw new IllegalStateException("Wrong configuration. Database module IS present.");
            }
            return Optional.empty();
        }
    }

    private Optional<DatabaseModule> findDatabaseModule(ProjectModule projectModule) {
        for (DatabaseModule dbModule : databaseModuleRepository.getDatabaseModules()) {
            if (projectModule.getClass() == dbModule.getProjectModuleClass()) {
                return Optional.of(dbModule);
            }
        }
        return Optional.empty();
    }

    private ImmutableList<ProjectModule> createProjectModules() {
        return moduleResolver.getModules().stream()
                .sorted(Comparator.comparingInt(o -> o.getDescriptor().priority()))
                .map(ModuleResolver.Wrapper::getModule)
                .collect(ImmutableList.toImmutableList());
    }

}
