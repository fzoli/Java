package com.mkyong.helloworld.repository.util;

import com.google.common.collect.ImmutableList;
import lombok.Builder;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.Synchronized;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

public class ModuleResolver<DescriptorT extends Annotation, ModuleT> {

    private static final String SCAN_PACKAGE = "com";

    private final Class<DescriptorT> descriptorClass;
    private final Class<ModuleT> moduleClass;

    private ImmutableList<Wrapper<DescriptorT, ModuleT>> modules;

    public ModuleResolver(Class<DescriptorT> descriptorClass, Class<ModuleT> moduleClass) {
        this.descriptorClass = descriptorClass;
        this.moduleClass = moduleClass;
    }

    @Synchronized
    public ImmutableList<Wrapper<DescriptorT, ModuleT>> getModules() {
        if (modules == null) {
            modules = createModules();
        }
        return modules;
    }

    private ImmutableList<Wrapper<DescriptorT, ModuleT>> createModules() {
        ImmutableList.Builder<Wrapper<DescriptorT, ModuleT>> builder = ImmutableList.builder();
        ClassPathScanningCandidateComponentProvider provider = createModuleScanner();
        for (BeanDefinition beanDef : provider.findCandidateComponents(SCAN_PACKAGE)) {
            builder.add(createModule(beanDef));
        }
        return builder.build();
    }

    private ClassPathScanningCandidateComponentProvider createModuleScanner() {
        // Don't pull default filters (@Component, etc.):
        ClassPathScanningCandidateComponentProvider provider
                = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(descriptorClass));
        return provider;
    }

    @SneakyThrows
    private Wrapper<DescriptorT, ModuleT> createModule(BeanDefinition beanDef) {
            Class<?> cl = Class.forName(beanDef.getBeanClassName());
            DescriptorT descriptor = cl.getAnnotation(descriptorClass);
            Constructor<?> constructor = cl.getConstructor();
            Object moduleObject = constructor.newInstance();
            return Wrapper.<DescriptorT, ModuleT>builder()
                .module((ModuleT) moduleObject)
                .descriptor(descriptor)
                .build();
    }

    @Builder
    @Getter
    public static final class Wrapper<DescriptorT extends Annotation, ModuleT> {
        private final ModuleT module;
        private final DescriptorT descriptor;
    }

}
