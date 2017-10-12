package com.farcsal.logic.service.module;

import com.farcsal.logic.common.module.DatabaseType;
import com.farcsal.logic.repository.module.databasemodule.DatabaseModule;
import com.farcsal.logic.repository.module.databasemodule.DatabaseModuleRepository;
import com.farcsal.logic.repository.module.flavormodule.FlavorModule;
import com.farcsal.logic.repository.module.flavormodule.FlavorModuleRepository;
import com.farcsal.logic.repository.module.packagemodule.PackageModule;
import com.farcsal.logic.repository.module.packagemodule.PackageModuleRepository;
import com.farcsal.logic.repository.module.projectmodule.ProjectModuleRepository;
import com.google.common.collect.ImmutableList;
import com.farcsal.logic.repository.module.projectmodule.ProjectModule;
import lombok.Builder;
import lombok.Synchronized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.*;

@Service
public class PackageService {

    private final PackageModuleRepository packageModuleRepository;
    private final ProjectModuleRepository projectModuleRepository;
    private final DatabaseModuleRepository databaseModuleRepository;
    private final FlavorModuleRepository flavorModuleRepository;

    private Package pkg;

    @Autowired
    @Builder
    public PackageService(PackageModuleRepository packageModuleRepository, ProjectModuleRepository projectModuleRepository, DatabaseModuleRepository databaseModuleRepository, FlavorModuleRepository flavorModuleRepository) {
        this.packageModuleRepository = packageModuleRepository;
        this.projectModuleRepository = projectModuleRepository;
        this.databaseModuleRepository = databaseModuleRepository;
        this.flavorModuleRepository = flavorModuleRepository;
    }

    @Synchronized
    public Package getPackage() {
        if (pkg == null) {
            pkg = createPackage();
        }
        return pkg;
    }

    private Package createPackage() {
        PackageModule packageModule = getPackageModule();
        ImmutableList.Builder<Package.ProjectModule> b = ImmutableList.builder();
        ImmutableList<ProjectModule> pms = projectModuleRepository.getProjectModules();
        if (pms.isEmpty()) {
            throw new WrongBuildConfigurationException("No project module has found.");
        }
        for (ProjectModule pm : pms) {
            Package.DatabaseModule dbm = getDatabaseModule(packageModule, pm)
                    .map(m -> Package.DatabaseModule.builder()
                            .databaseType(m.getDatabaseType())
                            .build())
                    .orElse(null);
            ImmutableList<FlavorModule> fms = getFlavorModules(pm);
            Package.ProjectModule m = Package.ProjectModule.builder()
                    .name(pm.getModuleName())
                    .databaseModule(dbm)
                    .flavorModules(mapFlavorModules(fms))
                    .build();
            b.add(m);
        }
        return Package.builder()
                .name(packageModule.getModuleName())
                .projectModules(b.build())
                .properties(packageModule.getProperties())
                .build();
    }

    private ImmutableList<FlavorModule> getFlavorModules(ProjectModule pm) {
        ImmutableList<FlavorModule> fms = findFlavorModules(pm);
        if (pm.numberOfFlavors() < 0) {
            throw new WrongBuildConfigurationException(String.format("Invalid number of flavors: %s", pm.numberOfFlavors()));
        }
        if (pm.numberOfFlavors() != fms.size()) {
            throw new WrongBuildConfigurationException(String.format("Number of flavors must be %s. Found: %s", pm.numberOfFlavors(), fms.size()));
        }
        return fms;
    }

    private ImmutableList<FlavorModule> findFlavorModules(ProjectModule pm) {
        ImmutableList<FlavorModule> fms = flavorModuleRepository.getFlavorModules();
        return fms.stream()
                .filter(fm -> fm.getProjectModuleClass() == pm.getClass())
                .collect(ImmutableList.toImmutableList());
    }

    @Nonnull
    private ImmutableList<Package.FlavorModule> mapFlavorModules(@Nonnull ImmutableList<FlavorModule> modules) {
        return modules.stream()
                .map(fm -> Package.FlavorModule.builder()
                        .name(fm.getModuleName())
                        .build())
                .collect(ImmutableList.toImmutableList());
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
