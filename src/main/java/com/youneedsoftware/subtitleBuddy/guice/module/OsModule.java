package com.youneedsoftware.subtitleBuddy.guice.module;

import com.google.inject.AbstractModule;
import com.youneedsoftware.subtitleBuddy.os.osDetector.ApacheOperatingSystemDetector;
import com.youneedsoftware.subtitleBuddy.os.osDetector.OperatingSystemDetector;

public class OsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(OperatingSystemDetector.class).to(ApacheOperatingSystemDetector.class);
    }
}
