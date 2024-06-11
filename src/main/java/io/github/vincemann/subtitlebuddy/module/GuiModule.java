package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.Singleton;
import com.google.inject.name.Names;
import io.github.vincemann.subtitlebuddy.config.strings.MessageSource;
import io.github.vincemann.subtitlebuddy.config.strings.MessageKeys;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayerOptions;
import io.github.vincemann.subtitlebuddy.gui.WindowManager;
import io.github.vincemann.subtitlebuddy.gui.WindowManagerImpl;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieSrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieStageController;
import io.github.vincemann.subtitlebuddy.gui.options.OptionsDisplayer;
import io.github.vincemann.subtitlebuddy.gui.options.OptionsStageController;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsStageController;
import io.github.vincemann.subtitlebuddy.gui.event.*;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsStageFactory;
import io.github.vincemann.subtitlebuddy.options.PropertiesFile;
import io.github.vincemann.subtitlebuddy.options.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.application.Platform;

import java.util.List;


public class GuiModule extends PropertyFilesModule {


    public GuiModule(MessageSource stringConfiguration, PropertiesFile propertiesConfiguration) {
        super(stringConfiguration, propertiesConfiguration);
    }

    @Override
    protected void configurePropertyBindings() {
        bindConstant().annotatedWith(Names.named(MessageKeys.SELECT_FILE_DESC))
                .to(getStringsFile().getString(MessageKeys.SELECT_FILE_DESC));
        bindConstant().annotatedWith(Names.named(MessageKeys.SELECT_FILE_WINDOW_TITLE))
                .to(getStringsFile().getString(MessageKeys.SELECT_FILE_WINDOW_TITLE));
        bindConstant().annotatedWith(Names.named(MessageKeys.SETTINGS_STAGE_TITLE))
                .to(getStringsFile().getString(MessageKeys.SETTINGS_STAGE_TITLE));
        bindConstant().annotatedWith(Names.named(MessageKeys.MOVIE_STAGE_TITLE))
                .to(getStringsFile().getString(MessageKeys.MOVIE_STAGE_TITLE));
        bindConstant().annotatedWith(Names.named(MessageKeys.OPTIONS_WINDOW_TITLE))
                .to(getStringsFile().getString(MessageKeys.OPTIONS_WINDOW_TITLE));
        bindConstant().annotatedWith(Names.named(MessageKeys.ASK_FOR_ROOT_PASSWORD_MESSAGE))
                .to(getStringsFile().getString(MessageKeys.ASK_FOR_ROOT_PASSWORD_MESSAGE));
        bindConstant().annotatedWith(Names.named(MessageKeys.STOP_BUTTON_TEXT))
                .to(getStringsFile().getString(MessageKeys.STOP_BUTTON_TEXT));
        bindConstant().annotatedWith(Names.named(MessageKeys.START_BUTTON_TEXT))
                .to(getStringsFile().getString(MessageKeys.START_BUTTON_TEXT));
        bindConstant().annotatedWith(Names.named(MessageKeys.MOVIE_MODE_BUTTON_TEXT))
                .to(getStringsFile().getString(MessageKeys.MOVIE_MODE_BUTTON_TEXT));
        bindConstant().annotatedWith(Names.named(MessageKeys.OPTIONS_BUTTON_TEXT))
                .to(getStringsFile().getString(MessageKeys.OPTIONS_BUTTON_TEXT));
        bindConstant().annotatedWith(Names.named(MessageKeys.WRONG_TIMESTAMP_FORMAT_TEXT))
                .to(getStringsFile().getString(MessageKeys.WRONG_TIMESTAMP_FORMAT_TEXT));
        bindConstant().annotatedWith(Names.named(MessageKeys.TIMESTAMP_JUMP_HINT_TEXT))
                .to(getStringsFile().getString(MessageKeys.TIMESTAMP_JUMP_HINT_TEXT));

        List<String> settingsWindowSizeList = (List<String>)(Object) getProperties().getList(PropertyFileKeys.SETTINGS_WINDOW_SIZE);
        bind(Vector2D.class).annotatedWith(Names.named(PropertyFileKeys.SETTINGS_WINDOW_SIZE))
                .toInstance(new Vector2D(Integer.valueOf(settingsWindowSizeList.get(0)),Integer.valueOf(settingsWindowSizeList.get(1))));
        List<String> optionsWindowSizeList = (List<String>)(Object) getProperties().getList(PropertyFileKeys.OPTIONS_WINDOW_SIZE);
        bind(Vector2D.class).annotatedWith(Names.named(PropertyFileKeys.OPTIONS_WINDOW_SIZE))
                .toInstance(new Vector2D(Integer.valueOf(optionsWindowSizeList.get(0)),Integer.valueOf(optionsWindowSizeList.get(1))));
    }

    @Override
    protected void configureClassBindings() {
        Platform.setImplicitExit(false); // https://stackoverflow.com/questions/29302837/javafx-platform-runlater-never-running
        bind(WindowManager.class).to(WindowManagerImpl.class);
        bind(SettingsStageFactory.class).in(Singleton.class);
        bind(SrtDisplayerOptions.class).in(Singleton.class);
        bind(SettingsSrtDisplayer.class).to(SettingsStageController.class);
        bind(MovieSrtDisplayer.class).to(MovieStageController.class);
        bind(SrtDisplayer.class).toProvider(SrtDisplayerProvider.class);
        bind(SrtDisplayerEventHandler.class).in(Singleton.class);
        bind(OptionsDisplayer.class).to(OptionsStageController.class);
    }

}
