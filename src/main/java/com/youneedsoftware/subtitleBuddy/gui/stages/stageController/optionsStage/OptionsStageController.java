package com.youneedsoftware.subtitleBuddy.gui.stages.stageController.optionsStage;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.youneedsoftware.subtitleBuddy.config.propertyFile.PropertyFileKeys;
import com.youneedsoftware.subtitleBuddy.config.uiStringsFile.UIStringsFileKeys;
import com.youneedsoftware.subtitleBuddy.classpathFileFinder.ClassPathFileFinder;
import com.youneedsoftware.subtitleBuddy.events.SrtFontColorChangeEvent;
import com.youneedsoftware.subtitleBuddy.events.SrtFontChangeEvent;
import com.youneedsoftware.subtitleBuddy.events.ToggleHotKeyEvent;
import com.youneedsoftware.subtitleBuddy.srt.font.fontManager.SrtFontLoadingException;
import com.youneedsoftware.subtitleBuddy.srt.font.fontsLocationManager.FontsLocationManager;
import com.youneedsoftware.subtitleBuddy.gui.stages.OptionsStage;
import com.youneedsoftware.subtitleBuddy.gui.stages.stageController.AbstractStageController;
import com.youneedsoftware.subtitleBuddy.gui.stages.StageState;
import com.youneedsoftware.subtitleBuddy.inputListeners.keyListener.HotKey;
import com.youneedsoftware.subtitleBuddy.srt.SrtFont;
import com.youneedsoftware.subtitleBuddy.srt.font.fontManager.SrtFontManager;
import com.youneedsoftware.subtitleBuddy.util.vec2d.Vector2D;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

@Log4j
@NoArgsConstructor
@Singleton
@OptionsStage
public class OptionsStageController extends AbstractStageController implements OptionsWindow {
    private static final String OPTIONS_STAGE_FXML_FILE_PATH = "/optionsStage.fxml";

    @FXML
    private ColorPicker colorChooser;
    @FXML
    private ChoiceBox<SrtFont> fontChoiceBox;
    @FXML
    private Text previewText;
    @FXML
    private CheckBox startStopCheckBox;
    @FXML
    private CheckBox nextClickCheckBox;

    private Map<SrtFont, String> fontPathMap;

    private EventBus eventBus;

    private SrtFontManager srtFontManager;

    private ClassPathFileFinder classPathFileFinder;


    private String userFontsPath;
    private FontsLocationManager fontsLocationManager;

    private ChangeListener<SrtFont> fontChoiceBoxChangeListener;
    private ChangeListener<Boolean> nextClickChangeListener;
    private ChangeListener<Boolean> startStopChangeListener;


    @Inject
    public OptionsStageController(EventBus eventBus, @Named(PropertyFileKeys.FONTS_PATH_KEY) String userFontsPath,
                                  SrtFontManager srtFontManager,
                                  @Named(UIStringsFileKeys.OPTIONS_WINDOW_TITLE_KEY) String title,
                                  @Named(PropertyFileKeys.OPTIONS_WINDOW_SIZE_KEY) Vector2D size,
                                  @Named(PropertyFileKeys.NEXT_CLICK_HOT_KEY_TOGGLED_KEY) boolean nextClickCountsToggled,
                                  @Named(PropertyFileKeys.START_STOP_HOT_KEY_TOGGLED_KEY) boolean startStopHotKeyToggled,
                                  ClassPathFileFinder classPathFileFinder,
                                  FontsLocationManager fontsLocationManager)
            throws IOException {
        super(classPathFileFinder.findFileOnClassPath(OPTIONS_STAGE_FXML_FILE_PATH).getFile().toURI().toURL(), title, size);
        createStage(this);
        this.fontsLocationManager = fontsLocationManager;
        this.classPathFileFinder = classPathFileFinder;
        this.eventBus = eventBus;
        this.srtFontManager = srtFontManager;
        //geht nicht anders weil  apache constants nur List<Object> getList() supported..
        this.fontPathMap = new HashMap<>();
        this.userFontsPath = userFontsPath;
        updateCheckBoxes(nextClickCountsToggled, startStopHotKeyToggled);
        constructorInit();
    }

    private void updateCheckBoxes(boolean nextClickToggled, boolean startStopToggled) {
        Platform.runLater(() -> {
            this.nextClickCheckBox.setSelected(!nextClickToggled);
            this.startStopCheckBox.setSelected(!startStopToggled);
        });
    }


    private void constructorInit() throws IOException {
        populateFontChoiceBox(srtFontManager.getUserFontSize(), userFontsPath);
    }


    @Override
    protected Table<Node, EventHandler, EventType> registerEventHandlers() {
        Table<Node, EventHandler, EventType> resultTable = HashBasedTable.create();
        EventHandler<ActionEvent> colorChooserEventHandler = event -> {
            //neue farbe ausgewählt
            log.info("User selected new Color -> colorchange event fired");
            eventBus.post(new SrtFontColorChangeEvent(colorChooser.getValue()));
            previewText.setFill(colorChooser.getValue());
        };
        colorChooser.setOnAction(colorChooserEventHandler);

        //kein passender eventType..
        fontChoiceBoxChangeListener = (observable, oldValue, newValue) -> {
            log.info("User selected new Font: " + newValue.toString() + " -> fontchangeevent fired");
            //todo gets called 2 times, I dont know why
            eventBus.post(new SrtFontChangeEvent(newValue, fontPathMap.get(newValue)));
            previewText.setFont(newValue.getRegularFont());
        };

        fontChoiceBox.getSelectionModel().selectedItemProperty().addListener(fontChoiceBoxChangeListener);

        nextClickChangeListener = (observable, oldValue, newValue) -> eventBus.post(new ToggleHotKeyEvent(HotKey.NEXT_CLICK, !newValue));
        startStopChangeListener = (observable, oldValue, newValue) -> eventBus.post(new ToggleHotKeyEvent(HotKey.START_STOP, !newValue));

        nextClickCheckBox.selectedProperty().addListener(nextClickChangeListener);
        startStopCheckBox.selectedProperty().addListener(startStopChangeListener);

        resultTable.put(colorChooser, colorChooserEventHandler, ActionEvent.ACTION);
        return resultTable;
    }

    @Override
    public void onUnregisterEventHandlers() {
        super.onUnregisterEventHandlers();
        fontChoiceBox.getSelectionModel().selectedItemProperty().removeListener(fontChoiceBoxChangeListener);
        startStopCheckBox.selectedProperty().removeListener(startStopChangeListener);
        nextClickCheckBox.selectedProperty().removeListener(nextClickChangeListener);
    }

    @Override
    protected void onFXMLInitialize() {
        super.onFXMLInitialize();
        log.trace("fxml initialize of : " + this.getClass().getSimpleName());
        checkNotNull(previewText);
        checkNotNull(colorChooser);
        checkNotNull(colorChooser.getValue());
        checkNotNull(fontChoiceBox);
        checkNotNull(fontChoiceBox.getSelectionModel());
        checkNotNull(fontChoiceBox.getItems());
        checkNotNull(startStopCheckBox);
        checkNotNull(nextClickCheckBox);
    }

    @Override
    public void openOptionsWindow() {
        try {
            if (!getStageState().equals(StageState.UNINITIALIZED)) {
                showStage();
            } else {
                log.warn("cant open options stage, is not initialized yet");
            }
        } catch (IllegalStateException e) {
            log.debug("could not open options window, reason: " + e.getMessage() + " bringing stage to front");
            getStage().toFront();
        }
    }

    @Override
    protected void onStageCreate(Stage stage) {
        super.onStageCreate(stage);
        //options window ist an keine andere stage gebunden
        stage.initModality(Modality.NONE);
        stage.setAlwaysOnTop(true);
    }


    private void populateFontChoiceBox(double userFontSize, String fontPath) throws IOException {
        log.trace("populating fontchoice box from (relative to executable)fontspath: " + fontPath);

        try {
            Path absFontPath = fontsLocationManager.findFontDirectory(Paths.get(fontPath));
            log.debug("using font path: " + absFontPath.toString());
            try (Stream<Path> paths = Files.walk(absFontPath)) {
                paths
                        .filter(Files::isRegularFile)
                        .forEach(path -> {
                            String fontFileAbsPathString = path.toAbsolutePath().toString();
                            //dont load italic fonts, font manger already loads regular and italic if u give him the regular font path
                            if (!Paths.get(fontFileAbsPathString).getFileName().toString().contains("italic")) {
                                try {
                                    SrtFont srtFont = srtFontManager.loadFont(fontFileAbsPathString, userFontSize);
                                    fontChoiceBox.getItems().add(srtFont);
                                    fontPathMap.put(srtFont, fontFileAbsPathString);
                                } catch (SrtFontLoadingException | MalformedURLException e) {
                                    log.error("could not load font (or respective italic font) with fontpath: " + fontFileAbsPathString + ", caused by: ", e);
                                }
                            }
                        });
            }
        } catch (Exception e) {
            log.warn("could not populate font choice box", e);
        }
    }

}
