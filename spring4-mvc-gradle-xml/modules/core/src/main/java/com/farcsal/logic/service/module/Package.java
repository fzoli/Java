package com.farcsal.logic.service.module;

import com.farcsal.logic.common.module.DatabaseType;
import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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

    @Nonnull
    private final ImmutableMap<String, String> properties;

    @Immutable
    @Builder
    @Getter
    public static final class ProjectModule {

        @Nonnull
        private final String name;

        @Nullable
        private final Package.DatabaseModule databaseModule;

        @Nonnull
        private final ImmutableList<Package.FlavorModule> flavorModules;

    }

    @Immutable
    @Builder
    @Getter
    public static final class DatabaseModule {

        @Nonnull
        private final DatabaseType databaseType;

        public String getUpperCamelDatabaseTypeName() {
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, databaseType.name());
        }

        public String getLowerCamelDatabaseTypeName() {
            return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, databaseType.name());
        }

    }

    @Immutable
    @Builder
    @Getter
    public static final class FlavorModule {

        @Nonnull
        private final String name;

    }

    /**
     * Returns the available flavor modules.
     */
    public ImmutableList<FlavorModule> getFlavorModules() {
        ImmutableList.Builder<FlavorModule> b = ImmutableList.builder();
        for (ProjectModule pm : projectModules) {
            b = b.addAll(pm.getFlavorModules());
        }
        return b.build();
    }

    /**
     * Returns the first database module defined in the project modules.
     * @return empty if no database module is found; otherwise the database module
     */
    public Optional<DatabaseModule> getDatabaseModule() {
        for (ProjectModule pm : projectModules) {
            if (pm.getDatabaseModule() != null) {
                return Optional.of(pm.getDatabaseModule());
            }
        }
        return Optional.empty();
    }

    /**
     * Creates the string representation of the project module.
     * @return the string representation, like prodMySql[core(MySql), sample(MySql)]
     */
    @Override
    public String toString() {
        String joinedProjectModuleStrings = String.join(", ", createProjectModuleStrings());
        return String.format("%s[%s]", name, joinedProjectModuleStrings);
    }

    private ImmutableList<String> createProjectModuleStrings() {
        return projectModules.stream()
                .map(this::createProjectModuleString)
                .collect(ImmutableList.toImmutableList());
    }

    private String createProjectModuleString(ProjectModule pm) {
        Optional<Package.DatabaseModule> dbModule = Optional.ofNullable(pm.getDatabaseModule());
        String dbModuleName = dbModule
                .map(DatabaseModule::getLowerCamelDatabaseTypeName)
                .orElse("");
        ImmutableList<String> flavorModuleNames = pm.getFlavorModules().stream()
                .map(FlavorModule::getName)
                .collect(ImmutableList.toImmutableList());
        ImmutableList<String> names = ImmutableList.<String>builder()
                .add(dbModuleName)
                .addAll(flavorModuleNames)
                .build();
        String joinedNames = String.join(", ", names);
        return String.format("%s(%s)", pm.getName(), joinedNames);
    }

}
