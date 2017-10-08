package com.mkyong.logic.service.module;

import com.google.common.collect.ImmutableList;
import com.mkyong.logic.repository.module.databasemodule.DatabaseModuleRepository;
import com.mkyong.logic.repository.module.packagemodule.PackageModuleRepository;
import com.mkyong.logic.repository.module.projectmodule.ProjectModuleRepository;
import com.mkyong.logic.repository.module.databasemodule.DatabaseModule;
import com.mkyong.logic.common.module.DatabaseType;
import com.mkyong.logic.repository.module.packagemodule.PackageModule;
import com.mkyong.logic.repository.module.projectmodule.ProjectModule;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PackageService {

    private final PackageModuleRepository packageModuleRepository;
    private final ProjectModuleRepository projectModuleRepository;
    private final DatabaseModuleRepository databaseModuleRepository;

    @Autowired
    @Builder
    public PackageService(PackageModuleRepository packageModuleRepository, ProjectModuleRepository projectModuleRepository, DatabaseModuleRepository databaseModuleRepository) {
        this.packageModuleRepository = packageModuleRepository;
        this.projectModuleRepository = projectModuleRepository;
        this.databaseModuleRepository = databaseModuleRepository;
    }

    public Package getPackage() {
        PackageModule packageModule = getPackageModule();
        ImmutableList.Builder<Package.ProjectModule> b = ImmutableList.builder();
        ImmutableList<ProjectModule> pms = projectModuleRepository.getProjectModules();
        if (pms.isEmpty()) {
            throw new WrongBuildConfigurationException("No project module has found.");
        }
        for (ProjectModule pm : pms) {
            Optional<DatabaseModule> a = getDatabaseModule(packageModule, pm);
            Package.DatabaseModule dbm;
            if (a.isPresent()) {
                dbm = Package.DatabaseModule.builder()
                        .databaseType(a.get().getDatabaseType())
                        .build();
            }
            else {
                dbm = null;
            }
            Package.ProjectModule m = Package.ProjectModule.builder()
                    .name(pm.getModuleName())
                    .databaseModule(dbm)
                    .build();
            b.add(m);
        }
        return Package.builder()
                .name(packageModule.getModuleName())
                .projectModules(b.build())
                .build();
    }

    /**
     * Find, validate and return the database module of the requested project module.
     * @param projectModule the requested project module
     * @return empty optional if the project module has no database; otherwise the database module
     * @throws WrongBuildConfigurationException if the configuration is wrong
     */
    private Optional<DatabaseModule> getDatabaseModule(PackageModule packageModule, ProjectModule projectModule) {
        Optional<DatabaseModule> dbModule = findDatabaseModule(projectModule);
        if (projectModule.hasDatabase()) {
            if (!dbModule.isPresent()) {
                throw new WrongBuildConfigurationException("Database module does NOT present.");
            }
            if (dbModule.get().getDatabaseType() != packageModule.getExpectedDatabaseType()) {
                throw new WrongBuildConfigurationException(
                        "Database module differs."
                                + "Got: " + dbModule.get().getDatabaseType()
                                + "Expected: " + packageModule.getExpectedDatabaseType());
            }
            return dbModule;
        }
        else {
            if (dbModule.isPresent()) {
                throw new WrongBuildConfigurationException("Database module IS present.");
            }
            return Optional.empty();
        }
    }

    private Optional<DatabaseModule> findDatabaseModule(ProjectModule projectModule) {
        List<DatabaseModule> dbModules = new ArrayList<>();
        Set<DatabaseType> types = new HashSet<>();
        for (DatabaseModule dbModule : databaseModuleRepository.getDatabaseModules()) {
            types.add(dbModule.getDatabaseType());
            if (projectModule.getClass() == dbModule.getProjectModuleClass()) {
                dbModules.add(dbModule);
            }
        }
        if (types.size() > 1) {
            throw new WrongBuildConfigurationException("Multiple DatabaseType has found: " + types);
        }
        if (dbModules.isEmpty()) {
            return Optional.empty();
        }
        if (dbModules.size() == 1) {
            return Optional.of(dbModules.get(0));
        }
        throw new WrongBuildConfigurationException("Multiple DatabaseModule has found");
    }

    private PackageModule getPackageModule() {
        List<PackageModule> l = packageModuleRepository.getPackageModules();
        if (l.isEmpty()) {
            throw new WrongBuildConfigurationException("No PackageModule has found");
        }
        if (l.size() == 1) {
            return l.get(0);
        }
        throw new WrongBuildConfigurationException("Multiple PackageModule has found");
    }

}
