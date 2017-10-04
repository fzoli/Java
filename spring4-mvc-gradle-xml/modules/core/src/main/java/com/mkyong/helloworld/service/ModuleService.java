package com.mkyong.helloworld.service;

import com.google.common.collect.ImmutableList;
import com.mkyong.helloworld.repository.DatabaseModuleRepository;
import com.mkyong.helloworld.repository.ProjectModuleRepository;
import com.mkyong.helloworld.util.DatabaseModule;
import com.mkyong.helloworld.util.ProjectModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ModuleService {

    @Autowired
    private ProjectModuleRepository projectModuleRepository;

    @Autowired
    private DatabaseModuleRepository databaseModuleRepository;

    /**
     * Find, validate and return the database module of the requested project module.
     * @param projectModule the requested project module
     * @return empty optional if the project module has no database; otherwise the database module
     * @throws IllegalStateException if the configuration is wrong
     */
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
        List<DatabaseModule> dbModules = new ArrayList<>();
        for (DatabaseModule dbModule : databaseModuleRepository.getDatabaseModules()) {
            if (projectModule.getClass() == dbModule.getProjectModuleClass()) {
                dbModules.add(dbModule);
            }
        }
        if (dbModules.isEmpty()) {
            return Optional.empty();
        }
        if (dbModules.size() == 1) {
            return Optional.of(dbModules.get(0));
        }
        throw new IllegalStateException("Multiple DatabaseModule has found");
    }

    /**
     * Creates the string representation of the project module.
     * @return the string representation, like core(MY_SQL)
     */
    public String createProjectModuleStrings() {
        return String.join(", ", projectModuleRepository.getProjectModules().stream()
                .map(this::createProjectModuleString)
                .collect(ImmutableList.toImmutableList()));
    }

    private String createProjectModuleString(ProjectModule pm) {
        Optional<DatabaseModule> dbModule = getDatabaseModule(pm);
        return dbModule
                .map(databaseModule -> pm.getModuleName() + "(" + databaseModule.getDatabaseType().name() + ")")
                .orElseGet(pm::getModuleName);
    }

}
