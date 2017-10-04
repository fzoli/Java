package com.mkyong.helloworld.service;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import com.mkyong.helloworld.repository.DatabaseModuleRepository;
import com.mkyong.helloworld.repository.PackageModuleRepository;
import com.mkyong.helloworld.repository.ProjectModuleRepository;
import com.mkyong.helloworld.util.DatabaseModule;
import com.mkyong.helloworld.util.PackageModule;
import com.mkyong.helloworld.util.ProjectModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ModuleService {

    @Autowired
    private PackageModuleRepository packageModuleRepository;

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
            PackageModule packageModule = getPackageModule();
            if (dbModule.get().getDatabaseType() != packageModule.getExpectedDatabaseType()) {
                throw new IllegalStateException(
                        "Wrong configuration. Database module differs."
                                + "Got: " + dbModule.get().getDatabaseType()
                                + "Expected: " + packageModule.getExpectedDatabaseType());
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
        Set<DatabaseModule.DatabaseType> types = new HashSet<>();
        for (DatabaseModule dbModule : databaseModuleRepository.getDatabaseModules()) {
            types.add(dbModule.getDatabaseType());
            if (projectModule.getClass() == dbModule.getProjectModuleClass()) {
                dbModules.add(dbModule);
            }
        }
        if (types.size() > 1) {
            throw new IllegalStateException("Wrong configuration. Multiple DatabaseType has found: " + types);
        }
        if (dbModules.isEmpty()) {
            return Optional.empty();
        }
        if (dbModules.size() == 1) {
            return Optional.of(dbModules.get(0));
        }
        throw new IllegalStateException("Wrong configuration. Multiple DatabaseModule has found");
    }

    public PackageModule getPackageModule() {
        List<PackageModule> l = packageModuleRepository.getPackageModules();
        if (l.isEmpty()) {
            throw new IllegalStateException("Wrong configuration. No PackageModule has found");
        }
        if (l.size() == 1) {
            return l.get(0);
        }
        throw new IllegalStateException("Wrong configuration. Multiple PackageModule has found");
    }

    /**
     * Creates the string representation of the project module.
     * @return the string representation, like prodMySql[core(MySql), sample(MySql)]
     */
    public String createProjectModuleStrings() {
        String name = getPackageModule().getModuleName();
        String modules = String.join(", ", projectModuleRepository.getProjectModules().stream()
                .map(this::createProjectModuleString)
                .collect(ImmutableList.toImmutableList()));
        return name + "[" + modules + "]";
    }

    private String createProjectModuleString(ProjectModule pm) {
        Optional<DatabaseModule> dbModule = getDatabaseModule(pm);
        return dbModule
                .map(databaseModule -> pm.getModuleName() + "(" + formatDatabaseType(databaseModule.getDatabaseType()) + ")")
                .orElseGet(pm::getModuleName);
    }

    private String formatDatabaseType(DatabaseModule.DatabaseType databaseType) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, databaseType.name());
    }

}
