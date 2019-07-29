package io.github.vincemann.subtitleBuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitleBuddy.classpathFileFinder.ClassPathFileFinder;

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
