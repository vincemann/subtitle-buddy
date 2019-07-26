package com.youneedsoftware.subtitleBuddy.guice.module;

import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertiesFile;
import com.youneedsoftware.subtitleBuddy.config.uiStringsFile.UIStringsFileKeys;
import com.youneedsoftware.subtitleBuddy.config.uiStringsFile.UIStringsFile;
import com.youneedsoftware.subtitleBuddy.filechooser.FileChooser;
import com.youneedsoftware.subtitleBuddy.filechooser.SwingFileChooser;
import com.youneedsoftware.subtitleBuddy.gui.guiDialogs.alertDialog.AlertDialog;
import com.youneedsoftware.subtitleBuddy.gui.guiDialogs.alertDialog.SwingAlertDialog;
import com.youneedsoftware.subtitleBuddy.gui.guiDialogs.continueDialog.ContinueDialog;
import com.youneedsoftware.subtitleBuddy.gui.guiDialogs.continueDialog.SwingContinueDialog;

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
