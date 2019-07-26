package guice.mockedProviders;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertiesFile;
import com.youneedsoftware.subtitleBuddy.filechooser.lastPathhandler.LastPathHandler;
import com.youneedsoftware.subtitleBuddy.filechooser.lastPathhandler.LastPathHandlerImpl;


public class MockedLastPathHandlerProvider implements Provider<LastPathHandler> {

    @Inject
    private PropertiesFile propertiesManager;

    @Override
    public LastPathHandler get() {
        return new LastPathHandlerImpl(propertiesManager);
    }
}
