package io.github.vincemann.subtitleBuddy.guice.mockedProviders;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertiesFile;
import io.github.vincemann.subtitleBuddy.filechooser.lastPathhandler.LastPathHandler;
import io.github.vincemann.subtitleBuddy.filechooser.lastPathhandler.LastPathHandlerImpl;


public class MockedLastPathHandlerProvider implements Provider<LastPathHandler> {

    @Inject
    private PropertiesFile propertiesManager;

    @Override
    public LastPathHandler get() {
        return new LastPathHandlerImpl(propertiesManager);
    }
}
