package io.github.vincemann.subtitleBuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitleBuddy.classpathFileFinder.ReadOnlyClassPathFileFinder;

public class ClassPathFileFinderModule extends AbstractModule {

    private ReadOnlyClassPathFileFinder readOnlyClassPathFileFinder;

    public ClassPathFileFinderModule(ReadOnlyClassPathFileFinder readOnlyClassPathFileFinder) {
        this.readOnlyClassPathFileFinder = readOnlyClassPathFileFinder;
    }

    @Override
    protected void configure() {
        bind(ReadOnlyClassPathFileFinder.class).toInstance(readOnlyClassPathFileFinder);
    }
}
