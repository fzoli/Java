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

import org.junit.Test;

public class PackageServiceTest {

    @Test
    public void positiveTest() {
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
        PackageModuleRepository packageModuleRepository = new PackageModuleRepositoryTestImpl(ImmutableList.<PackageModule>builder()
                .add(prodMySql)
                .build());
        ProjectModuleRepository projectModuleRepository = new ProjectModuleRepositoryTestImpl(ImmutableList.<ProjectModule>builder()
                // TODO
                .build());
        DatabaseModuleRepository databaseModuleRepository = new DatabaseModuleRepositoryTestImpl(ImmutableList.<DatabaseModule>builder()
                // TODO
                .build());
        PackageService packageService = new PackageService(packageModuleRepository, projectModuleRepository, databaseModuleRepository);

        Package pkg = packageService.getPackage();
        // TODO
    }

}
