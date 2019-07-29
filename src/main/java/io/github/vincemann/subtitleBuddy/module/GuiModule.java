package io.github.vincemann.subtitleBuddy.module;

import com.google.inject.name.Names;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertiesFile;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertyFileKeys;
import io.github.vincemann.subtitleBuddy.config.uiStringsFile.UIStringsFileKeys;
import io.github.vincemann.subtitleBuddy.config.uiStringsFile.UIStringsFile;
import io.github.vincemann.subtitleBuddy.gui.stages.MovieStageController;
import io.github.vincemann.subtitleBuddy.gui.stages.OptionsStageController;
import io.github.vincemann.subtitleBuddy.gui.stages.SettingsStageController;
import io.github.vincemann.subtitleBuddy.gui.stages.stageController.AbstractStageController;
import io.github.vincemann.subtitleBuddy.gui.stages.stageController.FXMLLoaderProvider;
import io.github.vincemann.subtitleBuddy.gui.stages.stageController.optionsStage.OptionsWindow;
import io.github.vincemann.subtitleBuddy.gui.srtDisplayer.*;
import io.github.vincemann.subtitleBuddy.util.vec2d.Vector2D;
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
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.BASIC_SELECT_FILE_DESC_KEY)).to(getStringsFile().getString(UIStringsFileKeys.BASIC_SELECT_FILE_DESC_KEY));
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.SELECT_FILE_WINDOW_TITLE_KEY)).to(getStringsFile().getString(UIStringsFileKeys.SELECT_FILE_WINDOW_TITLE_KEY));
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.SETTINGS_STAGE_TITLE_KEY)).to(getStringsFile().getString(UIStringsFileKeys.SETTINGS_STAGE_TITLE_KEY));
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.MOVIE_STAGE_TITLE_KEY)).to(getStringsFile().getString(UIStringsFileKeys.MOVIE_STAGE_TITLE_KEY));
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.OPTIONS_WINDOW_TITLE_KEY)).to(getStringsFile().getString(UIStringsFileKeys.OPTIONS_WINDOW_TITLE_KEY));
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.ASK_FOR_ROOT_PASSWORD_MESSAGE_KEY)).to(getStringsFile().getString(UIStringsFileKeys.ASK_FOR_ROOT_PASSWORD_MESSAGE_KEY));
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.STOP_BUTTON_TEXT_KEY)).to(getStringsFile().getString(UIStringsFileKeys.STOP_BUTTON_TEXT_KEY));
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.START_BUTTON_TEXT_KEY)).to(getStringsFile().getString(UIStringsFileKeys.START_BUTTON_TEXT_KEY));
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.MOVIE_MODE_BUTTON_TEXT_KEY)).to(getStringsFile().getString(UIStringsFileKeys.MOVIE_MODE_BUTTON_TEXT_KEY));
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.OPTIONS_BUTTON_TEXT_KEY)).to(getStringsFile().getString(UIStringsFileKeys.OPTIONS_BUTTON_TEXT_KEY));
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.WRONG_TIMESTAMP_FORMAT_TEXT_KEY)).to(getStringsFile().getString(UIStringsFileKeys.WRONG_TIMESTAMP_FORMAT_TEXT_KEY));
        bindConstant().annotatedWith(Names.named(UIStringsFileKeys.TIMESTAMP_JUMP_HINT_TEXT_KEY)).to(getStringsFile().getString(UIStringsFileKeys.TIMESTAMP_JUMP_HINT_TEXT_KEY));

        bindConstant().annotatedWith(Names.named(PropertyFileKeys.TIME_STAMP_WARNING_DURATION_KEY)).to(getPropertiesFile().getString(PropertyFileKeys.TIME_STAMP_WARNING_DURATION_KEY));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.USER_MOVIE_TEXT_POSITION_KEY)).to(getPropertiesFile().getString(PropertyFileKeys.USER_MOVIE_TEXT_POSITION_KEY));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.SETTINGS_FONT_SIZE_KEY)).to(getPropertiesFile().getInt(PropertyFileKeys.SETTINGS_FONT_SIZE_KEY));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.START_STOP_HOT_KEY_TOGGLED_KEY)).to(getPropertiesFile().getBoolean(PropertyFileKeys.START_STOP_HOT_KEY_TOGGLED_KEY));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.NEXT_CLICK_HOT_KEY_TOGGLED_KEY)).to(getPropertiesFile().getBoolean(PropertyFileKeys.NEXT_CLICK_HOT_KEY_TOGGLED_KEY));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.FAST_FORWARD_DELTA_KEY)).to(getPropertiesFile().getInt(PropertyFileKeys.FAST_FORWARD_DELTA_KEY));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.CLICK_WARNING_IMAGE_PATH_KEY)).to(getPropertiesFile().getString(PropertyFileKeys.CLICK_WARNING_IMAGE_PATH_KEY));
        bindConstant().annotatedWith(Names.named(PropertyFileKeys.FONTS_PATH_KEY)).to(getPropertiesFile().getString(PropertyFileKeys.FONTS_PATH_KEY));
        List<String> settingsWindowSizeList = (List<String>)(Object) getPropertiesFile().getList(PropertyFileKeys.SETTINGS_STAGE_SIZE_KEY);
        bind(Vector2D.class).annotatedWith(Names.named(PropertyFileKeys.SETTINGS_STAGE_SIZE_KEY)).toInstance(new Vector2D(Integer.valueOf(settingsWindowSizeList.get(0)),Integer.valueOf(settingsWindowSizeList.get(1))));
        List<String> optionsWindowSizeList = (List<String>)(Object) getPropertiesFile().getList(PropertyFileKeys.OPTIONS_WINDOW_SIZE_KEY);
        bind(Vector2D.class).annotatedWith(Names.named(PropertyFileKeys.OPTIONS_WINDOW_SIZE_KEY)).toInstance(new Vector2D(Integer.valueOf(optionsWindowSizeList.get(0)),Integer.valueOf(optionsWindowSizeList.get(1))));
    }

    @Override
    protected void initClassBindings() {
        bind(Stage.class).toInstance(primaryStage);
        bind(FXMLLoader.class).toProvider(FXMLLoaderProvider.class);
        bind(SettingsSrtDisplayer.class).to(io.github.vincemann.subtitleBuddy.gui.stages.stageController.settingsStage.SettingsStageController.class);
        bind(MovieSrtDisplayer.class).to(io.github.vincemann.subtitleBuddy.gui.stages.stageController.movieStage.MovieStageController.class);
        bind(SrtDisplayer.class).toProvider(SrtDisplayerProvider.class);
        bind(AbstractStageController.class).annotatedWith(MovieStageController.class).to(io.github.vincemann.subtitleBuddy.gui.stages.stageController.movieStage.MovieStageController.class);
        bind(AbstractStageController.class).annotatedWith(SettingsStageController.class).to(io.github.vincemann.subtitleBuddy.gui.stages.stageController.settingsStage.SettingsStageController.class);
        bind(AbstractStageController.class).annotatedWith(OptionsStageController.class).to(io.github.vincemann.subtitleBuddy.gui.stages.stageController.optionsStage.OptionsStageController.class);
        bind(OptionsWindow.class).to(io.github.vincemann.subtitleBuddy.gui.stages.stageController.optionsStage.OptionsStageController.class);
        bind(SrtDisplayerEventHandler.class).to(SrtDisplayerEventHandlerImpl.class);
    }
}
