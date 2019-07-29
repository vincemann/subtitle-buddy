package io.github.vincemann.subtitleBuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitleBuddy.runningExecutableFinder.RunningExecutableFinder;
import io.github.vincemann.subtitleBuddy.runningExecutableFinder.RunningExecutableFinderProvider;

public class SystemCommandModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RunningExecutableFinder.class).toProvider(RunningExecutableFinderProvider.class);
    }
}
