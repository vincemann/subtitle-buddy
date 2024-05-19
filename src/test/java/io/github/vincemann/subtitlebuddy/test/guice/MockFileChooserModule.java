package io.github.vincemann.subtitlebuddy.test.guice;

import io.github.vincemann.subtitlebuddy.options.PropertiesFile;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.gui.filechooser.FileChooser;
import io.github.vincemann.subtitlebuddy.gui.dialog.AlertDialog;
import io.github.vincemann.subtitlebuddy.gui.dialog.ContinueDialog;
import io.github.vincemann.subtitlebuddy.test.guice.providers.MockedFileChooserProvider;
import io.github.vincemann.subtitlebuddy.module.FileChooserModule;


public class MockFileChooserModule extends FileChooserModule {


    public MockFileChooserModule(UIStringsFile stringConfiguration, PropertiesFile propertiesConfiguration) {
        super(stringConfiguration, propertiesConfiguration);
    }

    @Override
    protected void configureClassBindings() {
        bind(FileChooser.class).toProvider(new MockedFileChooserProvider());
        bind(ContinueDialog.class).toInstance(message -> true);
        bind(AlertDialog.class).toInstance(message -> {
            //do nothing
        });
    }
}
