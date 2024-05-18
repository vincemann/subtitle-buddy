package io.github.vincemann.subtitlebuddy.gui;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import javafx.fxml.FXMLLoader;

@Singleton
public class FXMLLoaderProvider implements Provider<FXMLLoader> {

    @Inject
    private Injector injector;

    @Override
    public FXMLLoader get() {
        FXMLLoader loader = new FXMLLoader();
        loader.setControllerFactory(p -> {
            return injector.getInstance(p);
        });
        return loader;
    }

}