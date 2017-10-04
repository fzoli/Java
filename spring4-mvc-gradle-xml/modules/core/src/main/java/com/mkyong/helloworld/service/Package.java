package com.mkyong.helloworld.service;

import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import com.mkyong.helloworld.util.DatabaseType;
import lombok.Builder;
import lombok.Getter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.util.Optional;

@Immutable
@Builder
@Getter
public final class Package {

    @Nonnull
    private final String name;

    @Nonnull
    private final ImmutableList<Package.ProjectModule> projectModules;

    @Immutable
    @Builder
    @Getter
    public static final class ProjectModule {

        @Nonnull
        private final String name;

        @Nullable
        private final Package.DatabaseModule databaseModule;

    }

    @Immutable
    @Builder
    @Getter
    public static final class DatabaseModule {

        @Nonnull
        private final DatabaseType databaseType;

    }

    /**
     * Creates the string representation of the project module.
     * @return the string representation, like prodMySql[core(MySql), sample(MySql)]
     */
    @Override
    public String toString() {
        String modules = String.join(", ", projectModules.stream()
                .map(this::createProjectModuleString)
                .collect(ImmutableList.toImmutableList()));
        return name + "[" + modules + "]";
    }

    private String createProjectModuleString(ProjectModule pm) {
        Optional<Package.DatabaseModule> dbModule = Optional.ofNullable(pm.getDatabaseModule());
        return dbModule
                .map(databaseModule -> pm.getName() + "(" + formatDatabaseType(databaseModule.getDatabaseType()) + ")")
                .orElseGet(pm::getName);
    }

    private String formatDatabaseType(DatabaseType databaseType) {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, databaseType.name());
    }

}
