package com.mkyong.helloworld.repository;

import com.google.common.collect.ImmutableList;
import com.mkyong.helloworld.util.ProjectModule;
import com.mkyong.helloworld.util.ProjectModuleDescriptor;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Constructor;
import java.util.Comparator;

@Repository
public class ProjectModuleRepository {

    private static final String SCAN_PACKAGE = "com";

    private ImmutableList<ProjectModule> projectModules;

    @Synchronized
    public ImmutableList<ProjectModule> getProjectModules() {
        if (projectModules == null) {
            projectModules = createProjectModules();
        }
        return projectModules;
    }

    private ImmutableList<ProjectModule> createProjectModules() {
        ImmutableList.Builder<Wrapper> builder = ImmutableList.builder();
        ClassPathScanningCandidateComponentProvider provider = createProjectModuleScanner();
        for (BeanDefinition beanDef : provider.findCandidateComponents(SCAN_PACKAGE)) {
            builder.add(createProjectModule(beanDef));
        }
        return builder.build().stream()
                .sorted(Comparator.comparingInt(o -> o.getDescriptor().priority()))
                .map(Wrapper::getModule)
                .collect(ImmutableList.toImmutableList());
    }

    private ClassPathScanningCandidateComponentProvider createProjectModuleScanner() {
        // Don't pull default filters (@Component, etc.):
        ClassPathScanningCandidateComponentProvider provider
                = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(ProjectModuleDescriptor.class));
        return provider;
    }

    @SneakyThrows
    private Wrapper createProjectModule(BeanDefinition beanDef) {
            Class<?> cl = Class.forName(beanDef.getBeanClassName());
            ProjectModuleDescriptor descriptor = cl.getAnnotation(ProjectModuleDescriptor.class);
            Constructor<?> constructor = cl.getConstructor();
            ProjectModule module = (ProjectModule) constructor.newInstance();
            return Wrapper.builder()
                    .module(module)
                    .descriptor(descriptor)
                    .build();
    }

    @Builder
    @Getter
    private static final class Wrapper {
        private final ProjectModule module;
        private final ProjectModuleDescriptor descriptor;
    }

}
