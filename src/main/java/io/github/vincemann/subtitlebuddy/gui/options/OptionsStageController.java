package io.github.vincemann.subtitlebuddy.gui.options;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.ToggleHotKeyEvent;
import io.github.vincemann.subtitlebuddy.events.UpdateCurrentFontEvent;
import io.github.vincemann.subtitlebuddy.events.UpdateFontColorEvent;
import io.github.vincemann.subtitlebuddy.font.FontManager;
import io.github.vincemann.subtitlebuddy.gui.EventHandlerRegistration;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayerOptions;
import io.github.vincemann.subtitlebuddy.listeners.key.HotKey;
import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

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

    private EventBus eventBus;

    private ChangeListener<FontBundle> fontChoiceBoxChangeListener;
    private ChangeListener<Boolean> nextClickChangeListener;
    private ChangeListener<Boolean> startStopChangeListener;

    private List<EventHandlerRegistration<?>> eventHandlerRegistrations = new ArrayList<>();

    private FontManager fontManager;
    private SrtDisplayerOptions options;

    private Stage stage;


    @Inject
    public OptionsStageController(EventBus eventBus,
                                  FontManager fontManager,
                                  SrtDisplayerOptions options) {
        this.eventBus = eventBus;
        this.fontManager = fontManager;
        this.options = options;
    }


    private void updateCheckBoxes(boolean nextClickHotkeyEnabled, boolean spaceHotkeyEnabled) {
        Platform.runLater(() -> {
            this.nextClickCheckBox.setSelected(nextClickHotkeyEnabled);
            this.startStopCheckBox.setSelected(spaceHotkeyEnabled);
        });
    }

    protected void registerEventHandlers() {
        EventHandler<ActionEvent> colorChooserEventHandler = event -> {
            //new color chosen
            log.info("User selected new Color -> color change event fired");
            eventBus.post(new UpdateFontColorEvent(colorChooser.getValue()));
            previewText.setFill(colorChooser.getValue());
        };
        colorChooser.setOnAction(colorChooserEventHandler);

        //no suitable eventType
        fontChoiceBoxChangeListener = (observable, oldValue, newValue) -> {
            log.info("User selected new Font: " + newValue.toString() + " -> update current font event fired");
            eventBus.post(new UpdateCurrentFontEvent(newValue.getRegularFileName()));
            previewText.setFont(newValue.getRegularFont());
        };

        fontChoiceBox.getSelectionModel().selectedItemProperty().addListener(fontChoiceBoxChangeListener);

        nextClickChangeListener = (observable, oldValue, newValue) -> eventBus.post(new ToggleHotKeyEvent(HotKey.NEXT_CLICK, !newValue));
        startStopChangeListener = (observable, oldValue, newValue) -> eventBus.post(new ToggleHotKeyEvent(HotKey.START_STOP, !newValue));

        nextClickCheckBox.selectedProperty().addListener(nextClickChangeListener);
        startStopCheckBox.selectedProperty().addListener(startStopChangeListener);

        eventHandlerRegistrations.add(
                new EventHandlerRegistration<>(colorChooser, colorChooserEventHandler, ActionEvent.ACTION)
        );
    }

    @FXML
    public void initialize() {
        checkNotNull(previewText);
        checkNotNull(colorChooser);
        checkNotNull(colorChooser.getValue());
        checkNotNull(fontChoiceBox);
        checkNotNull(fontChoiceBox.getSelectionModel());
        checkNotNull(fontChoiceBox.getItems());
        checkNotNull(startStopCheckBox);
        checkNotNull(nextClickCheckBox);

        registerEventHandlers();

        updateCheckBoxes(options.getNextClickHotkeyEnabled(), options.getSpaceHotkeyEnabled());
        populateFontChoiceBox();
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        registerEventHandlingStageListener();
    }

    private void registerEventHandlingStageListener() {
        stage.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                registerEventHandlers();
            } else {
                unregisterEventHandlers();
            }
        });
    }

    private void unregisterEventHandlers() {
        eventHandlerRegistrations.forEach(EventHandlerRegistration::unregister);
        fontChoiceBox.getSelectionModel().selectedItemProperty().removeListener(fontChoiceBoxChangeListener);
        startStopCheckBox.selectedProperty().removeListener(startStopChangeListener);
        nextClickCheckBox.selectedProperty().removeListener(nextClickChangeListener);
    }


    private void populateFontChoiceBox() {
        log.trace("populating font choice box from fonts dir");

        List<FontBundle> fonts = fontManager.getLoadedFonts();
        for (FontBundle fontBundle : fonts) {
            fontChoiceBox.getItems().add(fontBundle);
        }
    }

}
