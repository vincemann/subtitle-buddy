package io.github.vincemann.subtitlebuddy.gui.options;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitlebuddy.options.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.events.UpdateFontColorEvent;
import io.github.vincemann.subtitlebuddy.events.UpdateCurrentFontEvent;
import io.github.vincemann.subtitlebuddy.events.ToggleHotKeyEvent;
import io.github.vincemann.subtitlebuddy.srt.font.SrtFontLoadingException;
import io.github.vincemann.subtitlebuddy.srt.font.FontsDirectory;
import io.github.vincemann.subtitlebuddy.listeners.key.HotKey;
import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import io.github.vincemann.subtitlebuddy.srt.font.SrtFontManager;
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
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.checkNotNull;

@Log4j2
@NoArgsConstructor
@Singleton
public class OptionsStageController {

    @FXML
    private ColorPicker colorChooser;
    @FXML
    private ChoiceBox<FontBundle> fontChoiceBox;
    @FXML
    private Text previewText;
    @FXML
    private CheckBox startStopCheckBox;
    @FXML
    private CheckBox nextClickCheckBox;

    private Map<FontBundle, String> fontPathMap;

    private EventBus eventBus;

    private SrtFontManager srtFontManager;


    private FontsDirectory fontsLocator;

    private ChangeListener<FontBundle> fontChoiceBoxChangeListener;
    private ChangeListener<Boolean> nextClickChangeListener;
    private ChangeListener<Boolean> startStopChangeListener;

    private Table<Node, EventHandler, EventType> eventHandlers;
    private boolean nextClickCountsToggled;
    private boolean startStopHotKeyToggled;


    @Inject
    public OptionsStageController(EventBus eventBus,
                                  SrtFontManager srtFontManager,
                                  @Named(PropertyFileKeys.NEXT_CLICK_HOT_KEY_TOGGLED) boolean nextClickCountsToggled,
                                  @Named(PropertyFileKeys.START_STOP_HOT_KEY_TOGGLED) boolean startStopHotKeyToggled,
                                  FontsDirectory fontsLocator)
            throws IOException {
        this.fontsLocator = fontsLocator;
        this.eventBus = eventBus;
        this.srtFontManager = srtFontManager;
        // apache constants only supports List<Object> getList()
        this.fontPathMap = new HashMap<>();
        this.nextClickCountsToggled = nextClickCountsToggled;
        this.startStopHotKeyToggled = startStopHotKeyToggled;
    }


    private void updateCheckBoxes(boolean nextClickToggled, boolean startStopToggled) {
        Platform.runLater(() -> {
            this.nextClickCheckBox.setSelected(!nextClickToggled);
            this.startStopCheckBox.setSelected(!startStopToggled);
        });
    }

    protected Table<Node, EventHandler, EventType> registerEventHandlers() {
        Table<Node, EventHandler, EventType> resultTable = HashBasedTable.create();
        EventHandler<ActionEvent> colorChooserEventHandler = event -> {
            //new color chosen
            log.info("User selected new Color -> colorchange event fired");
            eventBus.post(new UpdateFontColorEvent(colorChooser.getValue()));
            previewText.setFill(colorChooser.getValue());
        };
        colorChooser.setOnAction(colorChooserEventHandler);

        //no suitable eventType
        fontChoiceBoxChangeListener = (observable, oldValue, newValue) -> {
            log.info("User selected new Font: " + newValue.toString() + " -> fontchangeevent fired");
            eventBus.post(new UpdateCurrentFontEvent(newValue));
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

//    @Override
//    public void onUnregisterEventHandlers() {
//        super.onUnregisterEventHandlers();
//        fontChoiceBox.getSelectionModel().selectedItemProperty().removeListener(fontChoiceBoxChangeListener);
//        startStopCheckBox.selectedProperty().removeListener(startStopChangeListener);
//        nextClickCheckBox.selectedProperty().removeListener(nextClickChangeListener);
//    }

    @FXML
    protected void fxmlInit() {
        checkNotNull(previewText);
        checkNotNull(colorChooser);
        checkNotNull(colorChooser.getValue());
        checkNotNull(fontChoiceBox);
        checkNotNull(fontChoiceBox.getSelectionModel());
        checkNotNull(fontChoiceBox.getItems());
        checkNotNull(startStopCheckBox);
        checkNotNull(nextClickCheckBox);
        eventHandlers = registerEventHandlers();

        updateCheckBoxes(nextClickCountsToggled, startStopHotKeyToggled);
        populateFontChoiceBox(srtFontManager.getUserFontSize());
    }

//    @Override
//    public void openOptionsWindow(Vector2D position) {
//        try {
//            if (!getStageState().equals(StageState.UNINITIALIZED)) {
//                showStage();
//                getStage().setX(position.getX());
//                getStage().setY(position.getY());
//            } else {
//                log.warn("cant open options stage, is not initialized yet");
//            }
//        } catch (IllegalStateException e) {
//            log.debug("could not open options window, reason: " + e.getMessage() + ". Bringing stage to front");
//            getStage().toFront();
//        }
//    }

//    @Override
//    protected void onStageCreate(Stage stage) {
//        super.onStageCreate(stage);
//        //options window is not bound to another stage
//        stage.initModality(Modality.NONE);
//        stage.setAlwaysOnTop(true);
//    }


    private void populateFontChoiceBox(double userFontSize) {
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
                                    FontBundle fontBundle = srtFontManager.loadFont(fontFileRelPath, userFontSize);
                                    fontChoiceBox.getItems().add(fontBundle);
                                    fontPathMap.put(fontBundle, fontFileRelPath);
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