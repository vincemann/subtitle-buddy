package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractor;

public class ClassPathFileLocatorModule extends AbstractModule {

    private ClassPathFileExtractor classPathFileExtractor;

    public ClassPathFileLocatorModule(ClassPathFileExtractor classPathFileExtractor) {
        this.classPathFileExtractor = classPathFileExtractor;
    }

    @Override
    protected void configure() {
        bind(ClassPathFileExtractor.class).toInstance(classPathFileExtractor);
    }
}
