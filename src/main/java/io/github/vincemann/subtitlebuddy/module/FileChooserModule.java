package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import io.github.vincemann.subtitlebuddy.config.properties.PropertiesFile;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsKeys;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.gui.filechooser.FileChooser;
import io.github.vincemann.subtitlebuddy.gui.filechooser.SwingFileChooser;
import io.github.vincemann.subtitlebuddy.gui.dialog.AlertDialog;
import io.github.vincemann.subtitlebuddy.gui.dialog.SwingAlertDialog;
import io.github.vincemann.subtitlebuddy.gui.dialog.ContinueDialog;
import io.github.vincemann.subtitlebuddy.gui.dialog.SwingContinueDialog;

public class FileChooserModule extends PropertyFilesModule {

    public FileChooserModule(UIStringsFile stringConfiguration, PropertiesFile propertiesConfiguration) {
        super(stringConfiguration, propertiesConfiguration);
    }

    @Override
    protected void initPropertyBindings() {
        bindConstant().annotatedWith(Names.named(UIStringsKeys.FILE_NOT_FOUND_MESSAGE))
                .to(getStringsFile().getString(UIStringsKeys.FILE_NOT_FOUND_MESSAGE));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.INVALID_FILE_FORMAT_MESSAGE))
                .to(getStringsFile().getString(UIStringsKeys.INVALID_FILE_FORMAT_MESSAGE));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.EMPTY_FILE_MESSAGE))
                .to(getStringsFile().getString(UIStringsKeys.EMPTY_FILE_MESSAGE));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.CORRUPTED_FILE_MESSAGE))
                .to(getStringsFile().getString(UIStringsKeys.CORRUPTED_FILE_MESSAGE));
        bind(new TypeLiteral<String[]>(){})
                .annotatedWith(Names.named(UIStringsKeys.SRT_FILE_TYPES))
                .toInstance(new String[]{getStringsFile().getString(UIStringsKeys.SRT_FILE_TYPES)});
    }

    @Override
    protected void initClassBindings() {
        bind(FileChooser.class).to(SwingFileChooser.class);
        bind(ContinueDialog.class).to(SwingContinueDialog.class);
        bind(AlertDialog.class).to(SwingAlertDialog.class);
    }
}