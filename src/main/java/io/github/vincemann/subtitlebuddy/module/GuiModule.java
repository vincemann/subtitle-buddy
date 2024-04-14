package io.github.vincemann.subtitlebuddy.module;

import com.google.inject.name.Names;
import io.github.vincemann.subtitlebuddy.config.properties.PropertiesFile;
import io.github.vincemann.subtitlebuddy.config.properties.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsKeys;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsFile;
import io.github.vincemann.subtitlebuddy.gui.stages.MovieStageController;
import io.github.vincemann.subtitlebuddy.gui.stages.OptionsStageController;
import io.github.vincemann.subtitlebuddy.gui.stages.SettingsStageController;
import io.github.vincemann.subtitlebuddy.gui.stages.controller.AbstractStageController;
import io.github.vincemann.subtitlebuddy.gui.stages.controller.FXMLLoaderProvider;
import io.github.vincemann.subtitlebuddy.gui.stages.controller.OptionsWindow;
import io.github.vincemann.subtitlebuddy.gui.srtdisplayer.*;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.util.List;


public class GuiModule extends PropertyFilesModule {

    private Stage primaryStage;

    public GuiModule(UIStringsFile stringConfiguration, PropertiesFile propertiesConfiguration, Stage primaryStage) {
        super(stringConfiguration, propertiesConfiguration);
        this.primaryStage = primaryStage;
    }

    @Override
    protected void initPropertyBindings() {
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

        bindConstant().annotatedWith(Names.named(PropertyFileKeys.TIME_STAMP_WARNING_DURATION))
                .to(getPropertiesFile().getString(PropertyFileKeys.TIME_STAMP_WARNING_DURATION));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.USER_MOVIE_TEXT_POSITION))
                .to(getPropertiesFile().getString(PropertyFileKeys.USER_MOVIE_TEXT_POSITION));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.SETTINGS_FONT_SIZE))
                .to(getPropertiesFile().getInt(PropertyFileKeys.SETTINGS_FONT_SIZE));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.START_STOP_HOT_KEY_TOGGLED))
                .to(getPropertiesFile().getBoolean(PropertyFileKeys.START_STOP_HOT_KEY_TOGGLED));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.NEXT_CLICK_HOT_KEY_TOGGLED))
                .to(getPropertiesFile().getBoolean(PropertyFileKeys.NEXT_CLICK_HOT_KEY_TOGGLED));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.FAST_FORWARD_DELTA))
                .to(getPropertiesFile().getInt(PropertyFileKeys.FAST_FORWARD_DELTA));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.CLICK_WARNING_IMAGE_PATH))
                .to(getPropertiesFile().getString(PropertyFileKeys.CLICK_WARNING_IMAGE_PATH));
        List<String> settingsWindowSizeList = (List<String>)(Object) getPropertiesFile().getList(PropertyFileKeys.SETTINGS_WINDOW_SIZE);
        bind(Vector2D.class).annotatedWith(Names.named(PropertyFileKeys.SETTINGS_WINDOW_SIZE))
                .toInstance(new Vector2D(Integer.valueOf(settingsWindowSizeList.get(0)),Integer.valueOf(settingsWindowSizeList.get(1))));
        List<String> optionsWindowSizeList = (List<String>)(Object) getPropertiesFile().getList(PropertyFileKeys.OPTIONS_WINDOW_SIZE);
        bind(Vector2D.class).annotatedWith(Names.named(PropertyFileKeys.OPTIONS_WINDOW_SIZE))
                .toInstance(new Vector2D(Integer.valueOf(optionsWindowSizeList.get(0)),Integer.valueOf(optionsWindowSizeList.get(1))));
    }

    @Override
    protected void initClassBindings() {
        bind(Stage.class).toInstance(primaryStage);
        bind(FXMLLoader.class).toProvider(FXMLLoaderProvider.class);
        bind(SettingsSrtDisplayer.class).to(io.github.vincemann.subtitlebuddy.gui.stages.controller.SettingsStageController.class);
        bind(MovieSrtDisplayer.class).to(io.github.vincemann.subtitlebuddy.gui.stages.controller.MovieStageController.class);
        bind(SrtDisplayer.class).toProvider(SrtDisplayerProvider.class);
        bind(AbstractStageController.class).annotatedWith(MovieStageController.class).to(io.github.vincemann.subtitlebuddy.gui.stages.controller.MovieStageController.class);
        bind(AbstractStageController.class).annotatedWith(SettingsStageController.class).to(io.github.vincemann.subtitlebuddy.gui.stages.controller.SettingsStageController.class);
        bind(AbstractStageController.class).annotatedWith(OptionsStageController.class).to(io.github.vincemann.subtitlebuddy.gui.stages.controller.OptionsStageController.class);
        bind(OptionsWindow.class).to(io.github.vincemann.subtitlebuddy.gui.stages.controller.OptionsStageController.class);
        bind(SrtDisplayerEventHandler.class).to(SrtDisplayerEventHandlerImpl.class);
    }
}
