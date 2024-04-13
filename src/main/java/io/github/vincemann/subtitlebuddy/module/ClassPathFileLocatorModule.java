package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileLocator;

public class ClassPathFileLocatorModule extends AbstractModule {

    private ClassPathFileLocator classPathFileLocator;

    public ClassPathFileLocatorModule(ClassPathFileLocator classPathFileLocator) {
        this.classPathFileLocator = classPathFileLocator;
    }

    @Override
    protected void configure() {
        bind(ClassPathFileLocator.class).toInstance(classPathFileLocator);
    }
}
