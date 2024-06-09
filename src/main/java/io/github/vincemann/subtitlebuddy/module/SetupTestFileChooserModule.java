package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import io.github.vincemann.subtitlebuddy.config.strings.MessageSource;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsKeys;
import io.github.vincemann.subtitlebuddy.gui.dialog.AlertDialog;
import io.github.vincemann.subtitlebuddy.gui.dialog.ContinueDialog;
import io.github.vincemann.subtitlebuddy.gui.filechooser.FileChooser;
import io.github.vincemann.subtitlebuddy.options.PropertiesFile;

import java.io.File;

// file is given via string arg for program - for setup testing purposes
public class SetupTestFileChooserModule extends PropertyFilesModule {

    private String path;

    public SetupTestFileChooserModule(MessageSource stringsFile, PropertiesFile properties, String path) {
        super(stringsFile, properties);
        this.path = path;
    }

    @Override
    protected void configurePropertyBindings() {
        bindConstant().annotatedWith(Names.named(UIStringsKeys.FILE_NOT_FOUND_MESSAGE))
                .to(getStringsFile().getString(UIStringsKeys.FILE_NOT_FOUND_MESSAGE));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.INVALID_FILE_FORMAT_MESSAGE))
                .to(getStringsFile().getString(UIStringsKeys.INVALID_FILE_FORMAT_MESSAGE));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.EMPTY_FILE_MESSAGE))
                .to(getStringsFile().getString(UIStringsKeys.EMPTY_FILE_MESSAGE));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.CORRUPTED_FILE_MESSAGE))
                .to(getStringsFile().getString(UIStringsKeys.CORRUPTED_FILE_MESSAGE));
    }

    @Override
    protected void configureClassBindings() {
        bind(FileChooser.class).toInstance(() -> new File(path));
        bind(ContinueDialog.class).toInstance(message -> true);
        bind(AlertDialog.class).toInstance(message -> {

        });
    }

}
