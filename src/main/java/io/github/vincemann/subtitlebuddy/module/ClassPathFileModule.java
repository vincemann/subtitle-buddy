package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractor;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileExtractorImpl;

public class ClassPathFileModule extends AbstractModule {


    @Override
    protected void configure() {
        bind(ClassPathFileExtractor.class).to(ClassPathFileExtractorImpl.class);
    }
}
