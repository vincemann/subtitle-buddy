package io.github.vincemann.subtitlebuddy.gui.stages.controller;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitlebuddy.config.properties.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsKeys;
import io.github.vincemann.subtitlebuddy.cp.ClassPathFileLocator;
import io.github.vincemann.subtitlebuddy.events.SrtFontColorChangeEvent;
import io.github.vincemann.subtitlebuddy.events.SrtFontChangeEvent;
import io.github.vincemann.subtitlebuddy.events.ToggleHotKeyEvent;
import io.github.vincemann.subtitlebuddy.srt.font.SrtFontLoadingException;
import io.github.vincemann.subtitlebuddy.srt.font.FontsDirectory;
import io.github.vincemann.subtitlebuddy.gui.stages.StageState;
import io.github.vincemann.subtitlebuddy.listeners.key.HotKey;
import io.github.vincemann.subtitlebuddy.srt.SrtFonts;
import io.github.vincemann.subtitlebuddy.srt.font.SrtFontManager;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
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
@io.github.vincemann.subtitlebuddy.gui.stages.OptionsStageController
public class OptionsStageController extends AbstractStageController implements OptionsWindow {
    private static final String OPTIONS_STAGE_FXML_FILE_PATH = "/options-stage.fxml";

    @FXML
    private ColorPicker colorChooser;
    @FXML
    private ChoiceBox<SrtFonts> fontChoiceBox;
    @FXML
    private Text previewText;
    @FXML
    private CheckBox startStopCheckBox;
    @FXML
    private CheckBox nextClickCheckBox;

    private Map<SrtFonts, String> fontPathMap;

    private EventBus eventBus;

    private SrtFontManager srtFontManager;


    private FontsDirectory fontsLocator;

    private ChangeListener<SrtFonts> fontChoiceBoxChangeListener;
    private ChangeListener<Boolean> nextClickChangeListener;
    private ChangeListener<Boolean> startStopChangeListener;


    @Inject
    public OptionsStageController(EventBus eventBus,
                                  SrtFontManager srtFontManager,
                                  @Named(UIStringsKeys.OPTIONS_WINDOW_TITLE) String title,
                                  @Named(PropertyFileKeys.OPTIONS_WINDOW_SIZE) Vector2D size,
                                  @Named(PropertyFileKeys.NEXT_CLICK_HOT_KEY_TOGGLED) boolean nextClickCountsToggled,
                                  @Named(PropertyFileKeys.START_STOP_HOT_KEY_TOGGLED) boolean startStopHotKeyToggled,
                                  ClassPathFileLocator classPathFileLocator,
                                  FontsDirectory fontsLocator)
            throws IOException {
        super(classPathFileLocator.findOnClassPath(OPTIONS_STAGE_FXML_FILE_PATH).getFile().toURI().toURL(), title, size);
        createStage(this);
        this.fontsLocator = fontsLocator;
        this.eventBus = eventBus;
        this.srtFontManager = srtFontManager;
        // apache constants only supports List<Object> getList()
        this.fontPathMap = new HashMap<>();
        updateCheckBoxes(nextClickCountsToggled, startStopHotKeyToggled);
        populateFontChoiceBox(srtFontManager.getUserFontSize());
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


    private void populateFontChoiceBox(double userFontSize) throws IOException {
        log.trace("populating font choice box from fonts dir");

        try {
            Path fontsDirPath = fontsLocator.findOrCreate();
            log.debug("using font path: " + fontsDirPath.toString());
            try (Stream<Path> paths = Files.walk(fontsDirPath)) {
                paths
                        .filter(Files::isRegularFile)
                        .forEach(path -> {
                            // path is relative to fonts dir (config/fonts)
                            String fontFileRelPath = path.toAbsolutePath().toString();
                            //dont load italic fonts, font manger already loads regular and italic fonts, if u give him the regular font path
                            if (!Paths.get(fontFileRelPath).getFileName().toString().contains("italic")) {
                                try {
                                    SrtFonts srtFonts = srtFontManager.loadFont(fontFileRelPath, userFontSize);
                                    fontChoiceBox.getItems().add(srtFonts);
                                    fontPathMap.put(srtFonts, fontFileRelPath);
                                } catch (SrtFontLoadingException | MalformedURLException e) {
                                    log.error("could not load font (or respective italic font) with fontpath: " + fontFileRelPath + ", caused by: ", e);
                                }
                            }
                        });
            }
        } catch (Exception e) {
            log.warn("could not populate font choice box", e);
        }
    }

}
