package com.farcsal.logic.service.module;

import com.farcsal.logic.common.module.DatabaseType;
import com.farcsal.logic.repository.module.databasemodule.DatabaseModule;
import com.farcsal.logic.repository.module.databasemodule.DatabaseModuleRepository;
import com.farcsal.logic.repository.module.databasemodule.DatabaseModuleRepositoryTestImpl;
import com.farcsal.logic.repository.module.flavormodule.FlavorModule;
import com.farcsal.logic.repository.module.flavormodule.FlavorModuleRepository;
import com.farcsal.logic.repository.module.flavormodule.FlavorModuleRepositoryTestImpl;
import com.farcsal.logic.repository.module.packagemodule.PackageModule;
import com.farcsal.logic.repository.module.packagemodule.PackageModuleRepository;
import com.farcsal.logic.repository.module.packagemodule.PackageModuleRepositoryTestImpl;
import com.farcsal.logic.repository.module.projectmodule.ProjectModule;
import com.farcsal.logic.repository.module.projectmodule.ProjectModuleRepository;
import com.farcsal.logic.repository.module.projectmodule.ProjectModuleRepositoryTestImpl;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Assert;
import org.junit.Test;

import javax.annotation.Nonnull;

public class PackageServiceTest {

    @Test
    public void positiveTest() {
        // Given
        final PackageModule prodMySql = new PackageModule() {
            @Nonnull
            @Override
            public String getModuleName() {
                return "prodMySql";
            }

            @Override
            public DatabaseType getExpectedDatabaseType() {
                return DatabaseType.MY_SQL;
            }

            @Nonnull
            @Override
            public ImmutableMap<String, String> getProperties() {
                return ImmutableMap.of("key1", "value1");
            }
        };
        final ProjectModule core = new ProjectModule() {
            @Override
            @Nonnull
            public String getModuleName() {
                return "core";
            }

            @Override
            public boolean hasDatabase() {
                return true;
            }

            @Override
            public int numberOfFlavors() {
                return 1;
            }
        };
        final FlavorModule coreLive = new FlavorModule() {
            @Nonnull
            @Override
            public String getModuleName() {
                return "coreLive";
            }

            @Nonnull
            @Override
            public Class<? extends ProjectModule> getProjectModuleClass() {
                return core.getClass();
            }
        };
        final DatabaseModule coreMySql = new DatabaseModule() {
            @Override
            @Nonnull
            public Class<? extends ProjectModule> getProjectModuleClass() {
                return core.getClass();
            }

            @Override
            @Nonnull
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
        FlavorModuleRepository flavorModuleRepository = new FlavorModuleRepositoryTestImpl(ImmutableList.<FlavorModule>builder()
                .add(coreLive)
                .build());
        DatabaseModuleRepository databaseModuleRepository = new DatabaseModuleRepositoryTestImpl(ImmutableList.<DatabaseModule>builder()
                .add(coreMySql)
                .build());
        PackageService packageService = new PackageService(packageModuleRepository, projectModuleRepository, databaseModuleRepository, flavorModuleRepository);

        // When
        Package pkg = packageService.getPackage();

        // Then
        Assert.assertEquals("prodMySql", pkg.getName());
        Assert.assertEquals("value1", pkg.getProperties().get("key1"));
        Assert.assertEquals(1, pkg.getProjectModules().size());
        Assert.assertEquals("core", pkg.getProjectModules().get(0).getName());
        Assert.assertNotNull(pkg.getProjectModules().get(0).getDatabaseModule());
        Assert.assertEquals(DatabaseType.MY_SQL, pkg.getProjectModules().get(0).getDatabaseModule().getDatabaseType());
        Assert.assertTrue(pkg.getDatabaseModule().isPresent());
        Assert.assertEquals(DatabaseType.MY_SQL, pkg.getDatabaseModule().get().getDatabaseType());
    }

}
