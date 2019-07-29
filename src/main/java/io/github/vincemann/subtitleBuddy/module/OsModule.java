package io.github.vincemann.subtitleBuddy.module;

import com.google.inject.AbstractModule;
import io.github.vincemann.subtitleBuddy.os.osDetector.ApacheOperatingSystemDetector;
import io.github.vincemann.subtitleBuddy.os.osDetector.OperatingSystemDetector;

public class OsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(OperatingSystemDetector.class).to(ApacheOperatingSystemDetector.class);
    }
}
