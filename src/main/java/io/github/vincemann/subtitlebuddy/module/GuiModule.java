package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.Singleton;
import com.google.inject.name.Names;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsKeys;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayerOptions;
import io.github.vincemann.subtitlebuddy.gui.WindowManager;
import io.github.vincemann.subtitlebuddy.gui.WindowManagerImpl;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieSrtDisplayer;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieStageController;
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


    public GuiModule(UIStringsFile stringConfiguration, PropertiesFile propertiesConfiguration) {
        super(stringConfiguration, propertiesConfiguration);
    }

    @Override
    protected void configurePropertyBindings() {
        bindConstant().annotatedWith(Names.named(UIStringsKeys.SELECT_FILE_DESC))
                .to(getStringsFile().getString(UIStringsKeys.SELECT_FILE_DESC));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.SELECT_FILE_WINDOW_TITLE))
                .to(getStringsFile().getString(UIStringsKeys.SELECT_FILE_WINDOW_TITLE));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.SETTINGS_STAGE_TITLE))
                .to(getStringsFile().getString(UIStringsKeys.SETTINGS_STAGE_TITLE));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.MOVIE_STAGE_TITLE))
                .to(getStringsFile().getString(UIStringsKeys.MOVIE_STAGE_TITLE));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.OPTIONS_WINDOW_TITLE))
                .to(getStringsFile().getString(UIStringsKeys.OPTIONS_WINDOW_TITLE));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.ASK_FOR_ROOT_PASSWORD_MESSAGE))
                .to(getStringsFile().getString(UIStringsKeys.ASK_FOR_ROOT_PASSWORD_MESSAGE));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.STOP_BUTTON_TEXT))
                .to(getStringsFile().getString(UIStringsKeys.STOP_BUTTON_TEXT));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.START_BUTTON_TEXT))
                .to(getStringsFile().getString(UIStringsKeys.START_BUTTON_TEXT));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.MOVIE_MODE_BUTTON_TEXT))
                .to(getStringsFile().getString(UIStringsKeys.MOVIE_MODE_BUTTON_TEXT));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.OPTIONS_BUTTON_TEXT))
                .to(getStringsFile().getString(UIStringsKeys.OPTIONS_BUTTON_TEXT));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.WRONG_TIMESTAMP_FORMAT_TEXT))
                .to(getStringsFile().getString(UIStringsKeys.WRONG_TIMESTAMP_FORMAT_TEXT));
        bindConstant().annotatedWith(Names.named(UIStringsKeys.TIMESTAMP_JUMP_HINT_TEXT))
                .to(getStringsFile().getString(UIStringsKeys.TIMESTAMP_JUMP_HINT_TEXT));

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
    }

}
