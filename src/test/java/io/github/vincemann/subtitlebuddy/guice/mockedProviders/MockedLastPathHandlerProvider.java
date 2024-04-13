package io.github.vincemann.subtitlebuddy.guice.mockedProviders;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.github.vincemann.subtitlebuddy.config.properties.PropertiesFile;
import io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath.LastPathRegistry;
import io.github.vincemann.subtitlebuddy.gui.filechooser.lathpath.PropertiesFileLastPathRegistry;


public class MockedLastPathHandlerProvider implements Provider<LastPathRegistry> {

    @Inject
    private PropertiesFile propertiesManager;

    @Override
    public LastPathRegistry get() {
        return new PropertiesFileLastPathRegistry(propertiesManager);
    }
}
