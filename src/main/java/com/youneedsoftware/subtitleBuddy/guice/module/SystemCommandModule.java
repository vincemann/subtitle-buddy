package com.youneedsoftware.subtitleBuddy.guice.module;

import com.google.inject.AbstractModule;
import com.youneedsoftware.subtitleBuddy.runningExecutableFinder.RunningExecutableFinder;
import com.youneedsoftware.subtitleBuddy.runningExecutableFinder.RunningExecutableFinderProvider;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.SystemCommandExecutorProvider;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.SystemCommandExecutor;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.ynsSystemExecutor.cdCommandHandler.CdCommandHandler;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.ynsSystemExecutor.cdCommandHandler.CDCommandHandlerImpl;

public class SystemCommandModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(RunningExecutableFinder.class).toProvider(RunningExecutableFinderProvider.class);
        bind(CdCommandHandler.class).to(CDCommandHandlerImpl.class);
        bind(SystemCommandExecutor.class).toProvider(SystemCommandExecutorProvider.class);
    }
}
