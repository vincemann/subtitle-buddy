package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.name.Names;
import io.github.vincemann.subtitlebuddy.config.strings.MessageSource;
import io.github.vincemann.subtitlebuddy.config.strings.MessageKeys;
import io.github.vincemann.subtitlebuddy.gui.dialog.AlertDialog;
import io.github.vincemann.subtitlebuddy.gui.dialog.ContinueDialog;
import io.github.vincemann.subtitlebuddy.gui.dialog.JavaFxAlertDialog;
import io.github.vincemann.subtitlebuddy.gui.dialog.JavaFxContinueDialog;
import io.github.vincemann.subtitlebuddy.gui.filechooser.FileChooser;
import io.github.vincemann.subtitlebuddy.gui.filechooser.JavaFxFileChooser;
import io.github.vincemann.subtitlebuddy.options.PropertiesFile;

public class FileChooserModule extends PropertyFilesModule {

    public FileChooserModule(MessageSource stringConfiguration, PropertiesFile propertiesConfiguration) {
        super(stringConfiguration, propertiesConfiguration);
    }

    @Override
    protected void configurePropertyBindings() {
        bindConstant().annotatedWith(Names.named(MessageKeys.FILE_NOT_FOUND_MESSAGE))
                .to(getStringsFile().getString(MessageKeys.FILE_NOT_FOUND_MESSAGE));
        bindConstant().annotatedWith(Names.named(MessageKeys.INVALID_FILE_FORMAT_MESSAGE))
                .to(getStringsFile().getString(MessageKeys.INVALID_FILE_FORMAT_MESSAGE));
        bindConstant().annotatedWith(Names.named(MessageKeys.EMPTY_FILE_MESSAGE))
                .to(getStringsFile().getString(MessageKeys.EMPTY_FILE_MESSAGE));
        bindConstant().annotatedWith(Names.named(MessageKeys.CORRUPTED_FILE_MESSAGE))
                .to(getStringsFile().getString(MessageKeys.CORRUPTED_FILE_MESSAGE));
    }

    @Override
    protected void configureClassBindings() {
        bind(FileChooser.class).to(JavaFxFileChooser.class);
        bind(ContinueDialog.class).to(JavaFxContinueDialog.class);
        bind(AlertDialog.class).to(JavaFxAlertDialog.class);
    }
}
