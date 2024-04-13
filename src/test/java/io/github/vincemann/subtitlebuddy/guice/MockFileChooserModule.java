package io.github.vincemann.subtitlebuddy.guice;

import io.github.vincemann.subtitlebuddy.config.properties.PropertiesFile;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.gui.filechooser.FileChooser;
import io.github.vincemann.subtitlebuddy.gui.dialog.AlertDialog;
import io.github.vincemann.subtitlebuddy.gui.dialog.ContinueDialog;
import io.github.vincemann.subtitlebuddy.guice.providers.MockedFileChooserProvider;
import io.github.vincemann.subtitlebuddy.module.FileChooserModule;


public class MockFileChooserModule extends FileChooserModule {


    public MockFileChooserModule(UIStringsFile stringConfiguration, PropertiesFile propertiesConfiguration) {
        super(stringConfiguration, propertiesConfiguration);
    }

    @Override
    protected void initClassBindings() {
        bind(FileChooser.class).toProvider(new MockedFileChooserProvider());
        bind(ContinueDialog.class).toInstance(new ContinueDialog() {
            @Override
            public boolean askUserToContinue(String message) {
                return true;
            }
        });
        bind(AlertDialog.class).toInstance(new AlertDialog() {
            @Override
            public void tellUser(String message) {
                //do nothing
            }
        });
    }
}
