package com.youneedsoftware.subtitleBuddy.guice.module;

import com.google.inject.AbstractModule;
import com.youneedsoftware.subtitleBuddy.classpathFileFinder.ClassPathFileFinder;

public class ClassPathFileFinderModule extends AbstractModule {

    private ClassPathFileFinder classPathFileFinder;

    public ClassPathFileFinderModule(ClassPathFileFinder classPathFileFinder) {
        this.classPathFileFinder = classPathFileFinder;
    }

    @Override
    protected void configure() {
        bind(ClassPathFileFinder.class).toInstance(classPathFileFinder);
    }
}
