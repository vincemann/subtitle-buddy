package io.github.vincemann.subtitlebuddy.gui.options;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.ToggleHotKeyEvent;
import io.github.vincemann.subtitlebuddy.events.UpdateBackViaEscapeEvent;
import io.github.vincemann.subtitlebuddy.events.UpdateCurrentFontEvent;
import io.github.vincemann.subtitlebuddy.events.UpdateFontColorEvent;
import io.github.vincemann.subtitlebuddy.font.FontManager;
import io.github.vincemann.subtitlebuddy.font.FontOptions;
import io.github.vincemann.subtitlebuddy.gui.EventHandlerRegistration;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayerOptions;
import io.github.vincemann.subtitlebuddy.listeners.key.HotKey;
import io.github.vincemann.subtitlebuddy.options.OptionsUpdatedEvent;
import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

import static io.github.vincemann.subtitlebuddy.gui.movie.MovieStageController.OUTLINED_TEXT_STYLE;

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

    @FXML
    private CheckBox backViaEscCheckBox;

    private EventBus eventBus;

    private ChangeListener<FontBundle> fontChoiceBoxChangeListener;
    private ChangeListener<Boolean> nextClickChangeListener;
    private ChangeListener<Boolean> startStopChangeListener;
    private ChangeListener<Boolean> backViaEscapeChangeListener;

    private List<EventHandlerRegistration<?>> eventHandlerRegistrations = new ArrayList<>();

    private FontManager fontManager;
    private SrtDisplayerOptions options;

    private FontOptions fontOptions;

    private Stage stage;


    @Inject
    public OptionsStageController(EventBus eventBus,
                                  FontManager fontManager,
                                  SrtDisplayerOptions options, FontOptions fontOptions) {
        this.eventBus = eventBus;
        this.fontManager = fontManager;
        this.options = options;
        this.fontOptions = fontOptions;
    }


    private void updateCheckBoxes() {
        Platform.runLater(() -> {
            this.nextClickCheckBox.setSelected(options.getNextClickHotkeyEnabled());
            this.startStopCheckBox.setSelected(options.getSpaceHotkeyEnabled());
            this.backViaEscCheckBox.setSelected(options.getBackViaEsc());
        });
    }

    protected void registerEventHandlers() {
        EventHandler<ActionEvent> colorChooserEventHandler = event -> {
            //new color chosen
            log.info("User selected new Color -> color change event fired");
            eventBus.post(new UpdateFontColorEvent(colorChooser.getValue()));
        };
        colorChooser.setOnAction(colorChooserEventHandler);

        //no suitable eventType
        fontChoiceBoxChangeListener = (observable, oldValue, newValue) -> {
            log.info("User selected new Font: " + newValue.toString() + " -> update current font event fired");
            eventBus.post(new UpdateCurrentFontEvent(newValue.getRegularFileName()));
        };

        fontChoiceBox.getSelectionModel().selectedItemProperty().addListener(fontChoiceBoxChangeListener);

        nextClickChangeListener = (observable, oldValue, newValue) -> eventBus.post(new ToggleHotKeyEvent(HotKey.NEXT_CLICK, newValue));
        startStopChangeListener = (observable, oldValue, newValue) -> eventBus.post(new ToggleHotKeyEvent(HotKey.START_STOP, newValue));
        backViaEscapeChangeListener = (observable, oldValue, newValue) -> eventBus.post(new UpdateBackViaEscapeEvent(newValue));

        nextClickCheckBox.selectedProperty().addListener(nextClickChangeListener);
        startStopCheckBox.selectedProperty().addListener(startStopChangeListener);
        backViaEscCheckBox.selectedProperty().addListener(backViaEscapeChangeListener);

        eventHandlerRegistrations.add(
                new EventHandlerRegistration<>(colorChooser, colorChooserEventHandler, ActionEvent.ACTION)
        );
    }

    @Subscribe
    public void handleOptionsUpdatedEvent(OptionsUpdatedEvent event){
        updatePreviewText();
    }

    @FXML
    public void initialize() {
        registerEventHandlers();
        updateCheckBoxes();
        populateFontChoiceBox();
        updatePreviewText();
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

    private void updatePreviewText(){
        Font font = fontManager.getCurrentFont().withSize(options.getMovieFontSize()).getRegularFont();
        previewText.setFill(fontOptions.getFontColor());
        previewText.setFont(font);
        previewText.setStyle(OUTLINED_TEXT_STYLE);
    }

    private void unregisterEventHandlers() {
        eventHandlerRegistrations.forEach(EventHandlerRegistration::unregister);
        fontChoiceBox.getSelectionModel().selectedItemProperty().removeListener(fontChoiceBoxChangeListener);
        startStopCheckBox.selectedProperty().removeListener(startStopChangeListener);
        nextClickCheckBox.selectedProperty().removeListener(nextClickChangeListener);
        backViaEscCheckBox.selectedProperty().removeListener(backViaEscapeChangeListener);
    }


    private void populateFontChoiceBox() {
        log.trace("populating font choice box from fonts dir");

        List<FontBundle> fonts = fontManager.getLoadedFonts();
        for (FontBundle fontBundle : fonts) {
            fontChoiceBox.getItems().add(fontBundle);
        }
    }

}
