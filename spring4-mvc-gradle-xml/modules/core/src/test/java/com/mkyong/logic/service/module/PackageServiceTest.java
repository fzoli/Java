package com.mkyong.logic.service.module;

import com.google.common.collect.ImmutableList;
import com.mkyong.logic.common.module.DatabaseType;
import com.mkyong.logic.repository.module.databasemodule.DatabaseModule;
import com.mkyong.logic.repository.module.databasemodule.DatabaseModuleRepository;
import com.mkyong.logic.repository.module.databasemodule.DatabaseModuleRepositoryTestImpl;
import com.mkyong.logic.repository.module.packagemodule.PackageModule;
import com.mkyong.logic.repository.module.packagemodule.PackageModuleRepository;
import com.mkyong.logic.repository.module.packagemodule.PackageModuleRepositoryTestImpl;
import com.mkyong.logic.repository.module.projectmodule.ProjectModule;
import com.mkyong.logic.repository.module.projectmodule.ProjectModuleRepository;
import com.mkyong.logic.repository.module.projectmodule.ProjectModuleRepositoryTestImpl;

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
        Package pkg = packageService.getPackage();

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
