package com.farcsal.logic.service.module;

import com.farcsal.logic.repository.module.databasemodule.DatabaseModuleRepository;
import com.farcsal.logic.repository.module.databasemodule.DatabaseModuleRepositoryTestImpl;
import com.farcsal.logic.repository.module.packagemodule.PackageModule;
import com.farcsal.logic.repository.module.packagemodule.PackageModuleRepository;
import com.farcsal.logic.repository.module.projectmodule.ProjectModuleRepository;
import com.farcsal.logic.repository.module.projectmodule.ProjectModuleRepositoryTestImpl;
import com.google.common.collect.ImmutableList;
import com.farcsal.logic.common.module.DatabaseType;
import com.farcsal.logic.repository.module.databasemodule.DatabaseModule;
import com.farcsal.logic.repository.module.packagemodule.PackageModuleRepositoryTestImpl;
import com.farcsal.logic.repository.module.projectmodule.ProjectModule;

import org.junit.Assert;
import org.junit.Test;

public class PackageServiceTest {

    @Test
    public void positiveTest() {
        // Given
        final PackageModule prodMySql = new PackageModule() {
            @Override
            public String getModuleName() {
                return "prodMySql";
            }

            @Override
            public DatabaseType getExpectedDatabaseType() {
                return DatabaseType.MY_SQL;
            }
        };
        final ProjectModule core = new ProjectModule() {
            @Override
            public String getModuleName() {
                return "core";
            }

            @Override
            public boolean hasDatabase() {
                return true;
            }
        };
        final DatabaseModule coreMySql = new DatabaseModule() {
            @Override
            public Class<? extends ProjectModule> getProjectModuleClass() {
                return core.getClass();
            }

            @Override
            public DatabaseType getDatabaseType() {
                return DatabaseType.MY_SQL;
            }
        };
        PackageModuleRepository packageModuleRepository = new PackageModuleRepositoryTestImpl(ImmutableList.<PackageModule>builder()
                .add(prodMySql)
                .build());
        ProjectModuleRepository projectModuleRepository = new ProjectModuleRepositoryTestImpl(ImmutableList.<ProjectModule>builder()
                .add(core)
                .build());
        DatabaseModuleRepository databaseModuleRepository = new DatabaseModuleRepositoryTestImpl(ImmutableList.<DatabaseModule>builder()
                .add(coreMySql)
                .build());
        PackageService packageService = new PackageService(packageModuleRepository, projectModuleRepository, databaseModuleRepository);

        // When
        com.farcsal.logic.service.module.Package pkg = packageService.getPackage();

        // Then
        Assert.assertEquals("prodMySql", pkg.getName());
        Assert.assertEquals(1, pkg.getProjectModules().size());
        Assert.assertEquals("core", pkg.getProjectModules().get(0).getName());
        Assert.assertNotNull(pkg.getProjectModules().get(0).getDatabaseModule());
        Assert.assertEquals(DatabaseType.MY_SQL, pkg.getProjectModules().get(0).getDatabaseModule().getDatabaseType());
        Assert.assertTrue(pkg.getDatabaseModule().isPresent());
        Assert.assertEquals(DatabaseType.MY_SQL, pkg.getDatabaseModule().get().getDatabaseType());
    }

}
