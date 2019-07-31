package io.github.vincemann.subtitleBuddy.gui.stages.stageController.optionsStage;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertyFileKeys;
import io.github.vincemann.subtitleBuddy.config.uiStringsFile.UIStringsFileKeys;
import io.github.vincemann.subtitleBuddy.classpathFileFinder.ReadOnlyClassPathFileFinder;
import io.github.vincemann.subtitleBuddy.events.SrtFontColorChangeEvent;
import io.github.vincemann.subtitleBuddy.events.SrtFontChangeEvent;
import io.github.vincemann.subtitleBuddy.events.ToggleHotKeyEvent;
import io.github.vincemann.subtitleBuddy.srt.font.fontManager.SrtFontLoadingException;
import io.github.vincemann.subtitleBuddy.srt.font.fontsLocationManager.FontsLocationManager;
import io.github.vincemann.subtitleBuddy.gui.stages.stageController.AbstractStageController;
import io.github.vincemann.subtitleBuddy.gui.stages.StageState;
import io.github.vincemann.subtitleBuddy.inputListeners.keyListener.HotKey;
import io.github.vincemann.subtitleBuddy.srt.SrtFont;
import io.github.vincemann.subtitleBuddy.srt.font.fontManager.SrtFontManager;
import io.github.vincemann.subtitleBuddy.util.vec2d.Vector2D;
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
@io.github.vincemann.subtitleBuddy.gui.stages.OptionsStageController
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

    private ReadOnlyClassPathFileFinder readOnlyClassPathFileFinder;


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
                                  ReadOnlyClassPathFileFinder readOnlyClassPathFileFinder,
                                  FontsLocationManager fontsLocationManager)
            throws IOException {
        super(readOnlyClassPathFileFinder.findFileOnClassPath(OPTIONS_STAGE_FXML_FILE_PATH).getFile().toURI().toURL(), title, size);
        createStage(this);
        this.fontsLocationManager = fontsLocationManager;
        this.readOnlyClassPathFileFinder = readOnlyClassPathFileFinder;
        this.eventBus = eventBus;
        this.srtFontManager = srtFontManager;
        //geht nicht anders weil  apache constants nur List<Object> getList() supported..
        this.fontPathMap = new HashMap<>();
        this.userFontsPath = userFontsPath;
        updateCheckBoxes(nextClickCountsToggled, startStopHotKeyToggled);
        populateFontChoiceBox(srtFontManager.getUserFontSize(), userFontsPath);
    }

    private void updateCheckBoxes(boolean nextClickToggled, boolean startStopToggled) {
        Platform.runLater(() -> {
            this.nextClickCheckBox.setSelected(!nextClickToggled);
            this.startStopCheckBox.setSelected(!startStopToggled);
        });
    }

    @Override
    protected Table<Node, EventHandler, EventType> registerEventHandlers() {
        Table<Node, EventHandler, EventType> resultTable = HashBasedTable.create();
        EventHandler<ActionEvent> colorChooserEventHandler = event -> {
            //new color chosen
            log.info("User selected new Color -> colorchange event fired");
            eventBus.post(new SrtFontColorChangeEvent(colorChooser.getValue()));
            previewText.setFill(colorChooser.getValue());
        };
        colorChooser.setOnAction(colorChooserEventHandler);

        //no suitable eventType
        fontChoiceBoxChangeListener = (observable, oldValue, newValue) -> {
            log.info("User selected new Font: " + newValue.toString() + " -> fontchangeevent fired");
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
    public void openOptionsWindow(Vector2D position) {
        try {
            if (!getStageState().equals(StageState.UNINITIALIZED)) {
                showStage();
                getStage().setX(position.getX());
                getStage().setY(position.getY());
            } else {
                log.warn("cant open options stage, is not initialized yet");
            }
        } catch (IllegalStateException e) {
            log.debug("could not open options window, reason: " + e.getMessage() + ". Bringing stage to front");
            getStage().toFront();
        }
    }

    @Override
    protected void onStageCreate(Stage stage) {
        super.onStageCreate(stage);
        //options window is not bound to another stage
        stage.initModality(Modality.NONE);
        stage.setAlwaysOnTop(true);
    }


    private void populateFontChoiceBox(double userFontSize, String fontPath) throws IOException {
        log.trace("populating fontchoice box from (relative to executable) fontspath: " + fontPath);

        try {
            Path absFontPath = fontsLocationManager.findFontDirectory(Paths.get(fontPath));
            log.debug("using font path: " + absFontPath.toString());
            try (Stream<Path> paths = Files.walk(absFontPath)) {
                paths
                        .filter(Files::isRegularFile)
                        .forEach(path -> {
                            String fontFileAbsPathString = path.toAbsolutePath().toString();
                            //dont load italic fonts, font manger already loads regular and italic fonts, if u give him the regular font path
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
