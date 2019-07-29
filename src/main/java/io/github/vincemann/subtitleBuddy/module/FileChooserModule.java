package io.github.vincemann.subtitleBuddy.module;

import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertiesFile;
import io.github.vincemann.subtitleBuddy.config.uiStringsFile.UIStringsFileKeys;
import io.github.vincemann.subtitleBuddy.config.uiStringsFile.UIStringsFile;
import io.github.vincemann.subtitleBuddy.filechooser.FileChooser;
import io.github.vincemann.subtitleBuddy.filechooser.SwingFileChooser;
import io.github.vincemann.subtitleBuddy.gui.dialog.alertDialog.AlertDialog;
import io.github.vincemann.subtitleBuddy.gui.dialog.alertDialog.SwingAlertDialog;
import io.github.vincemann.subtitleBuddy.gui.dialog.continueDialog.ContinueDialog;
import io.github.vincemann.subtitleBuddy.gui.dialog.continueDialog.SwingContinueDialog;

public class FileChooserModule extends PropertyFilesModule {

    public FileChooserModule(UIStringsFile stringConfiguration, PropertiesFile propertiesConfiguration) {
        super(stringConfiguration, propertiesConfiguration);
    }

    @Override
    protected void initPropertyBindings() {
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.FILE_NOT_FOUND_MESSAGE_KEY)).to(getStringsFile().getString(UIStringsFileKeys.FILE_NOT_FOUND_MESSAGE_KEY));
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.INVALID_FILE_FORMAT_MESSAGE_KEY)).to(getStringsFile().getString(UIStringsFileKeys.INVALID_FILE_FORMAT_MESSAGE_KEY ));
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.EMPTY_FILE_MESSAGE_KEY)).to(getStringsFile().getString(UIStringsFileKeys.EMPTY_FILE_MESSAGE_KEY));
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.CORRUPTED_FILE_MESSAGE_KEY)).to(getStringsFile().getString(UIStringsFileKeys.CORRUPTED_FILE_MESSAGE_KEY));
        bind(new TypeLiteral<String[]>(){})
                .annotatedWith(Names.named(UIStringsFileKeys.SRT_FILE_TYPE_KEY))
                .toInstance(new String[]{getStringsFile().getString(UIStringsFileKeys.SRT_FILE_TYPE_KEY)});
    }

    @Override
    protected void initClassBindings() {
        bind(FileChooser.class).to(SwingFileChooser.class);
        bind(ContinueDialog.class).to(SwingContinueDialog.class);
        bind(AlertDialog.class).to(SwingAlertDialog.class);
    }
}
