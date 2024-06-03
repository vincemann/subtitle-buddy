package io.github.vincemann.subtitlebuddy.gui.settings;


import io.github.vincemann.subtitlebuddy.events.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitlebuddy.config.strings.UIStringsKeys;
import io.github.vincemann.subtitlebuddy.events.RequestSubtitleUpdateEvent;
import io.github.vincemann.subtitlebuddy.events.SwitchSrtDisplayerEvent;
import io.github.vincemann.subtitlebuddy.font.FontManager;
import io.github.vincemann.subtitlebuddy.gui.*;
import io.github.vincemann.subtitlebuddy.gui.movie.MovieSrtDisplayer;
import io.github.vincemann.subtitlebuddy.srt.*;
import io.github.vincemann.subtitlebuddy.srt.parser.InvalidTimestampFormatException;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtPlayer;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static io.github.vincemann.subtitlebuddy.util.fx.ImageUtils.loadImageView;

@Log4j2
@NoArgsConstructor
@Singleton
public class SettingsStageController implements SettingsSrtDisplayer {
    private static final int SETTINGS_CLICK_WARNING_SIZE = 40;
    //millis
    private static final int TIME_STAMP_WARNING_DURATION = 1000;

    private static final int FORWARD_DELTA = 500;

    @FXML
    private Button startButton;
    @FXML
    private Button stopButton;
    @FXML
    private TextFlow settingsTextFlow;
    @FXML
    private TextField timeField;
    @FXML
    private Text currentTimeStampText;
    @FXML
    private Text timestampJumpHintText;
    @FXML
    private Text wrongFormatText;
    @FXML
    private Button optionsButton;
    @FXML
    private Button movieModeButton;
    @FXML
    private Button fastForwardButton;
    @FXML
    private Button fastBackwardButton;
    @FXML
    private HBox imageHBox;

    private ImageView clickWarning;


    private SrtPlayer srtPlayer;


    private FontManager fontManager;

    private WindowManager windowManager;

    private SrtDisplayerOptions options;

    private EventBus eventBus;

    private String startButtonText;
    private String stopButtonText;
    private String movieModeButtonText;
    private String optionsButtonText;
    private String wrongTimeStampFormatText;
    private String timestampJumpHintTextString;

    private Stage stage;

    private List<EventHandlerRegistration<?>> eventHandlerRegistrations = new ArrayList<>();


    @Inject
    public SettingsStageController(SrtPlayer srtPlayer,
                                   FontManager fontManager,
                                   WindowManager windowManager,
                                   SrtDisplayerOptions options,
                                   EventBus eventBus,
                                   @Named(UIStringsKeys.START_BUTTON_TEXT) String startButtonText,
                                   @Named(UIStringsKeys.STOP_BUTTON_TEXT) String stopButtonText,
                                   @Named(UIStringsKeys.MOVIE_MODE_BUTTON_TEXT) String movieModeButtonText,
                                   @Named(UIStringsKeys.OPTIONS_BUTTON_TEXT) String optionsButtonText,
                                   @Named(UIStringsKeys.WRONG_TIMESTAMP_FORMAT_TEXT) String wrongTimeStampFormatText,
                                   @Named(UIStringsKeys.TIMESTAMP_JUMP_HINT_TEXT) String timestampJumpHintTextString
    ) {
        this.srtPlayer = srtPlayer;
        this.options = options;
        this.eventBus = eventBus;
        this.fontManager = fontManager;
        this.windowManager = windowManager;
        this.startButtonText = startButtonText;
        this.stopButtonText = stopButtonText;
        this.optionsButtonText = optionsButtonText;
        this.movieModeButtonText = movieModeButtonText;
        this.wrongTimeStampFormatText = wrongTimeStampFormatText;
        this.timestampJumpHintTextString = timestampJumpHintTextString;
    }

    @FXML
    public void initialize() {
        registerEventHandlers();
        loadStrings();
        clickWarning = loadImageView(imageHBox,
                "images/finger.png",
                new Vector2D(SETTINGS_CLICK_WARNING_SIZE, SETTINGS_CLICK_WARNING_SIZE));
        clickWarning.setVisible(false);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        addShowStageListener();
    }

    private void addShowStageListener() {
        stage.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                onShowStage();
            } else {
                onHideStage();
            }
        });
    }

    private void onShowStage(){
        registerEventHandlers();
        eventBus.post(new RequestSubtitleUpdateEvent());
    }

    private void onHideStage(){
        unregisterEventHandlers();
    }

    private void unregisterEventHandlers() {
        eventHandlerRegistrations.forEach(EventHandlerRegistration::unregister);
    }

    private void loadStrings() {
        Platform.runLater(() -> {
            startButton.setText(startButtonText);
            stopButton.setText(stopButtonText);
            optionsButton.setText(optionsButtonText);
            movieModeButton.setText(movieModeButtonText);
            wrongFormatText.setText(wrongTimeStampFormatText);
            timestampJumpHintText.setText(timestampJumpHintTextString);
        });
    }


    private void registerEventHandlers() {
        EventHandler<MouseEvent> startButtonPressedHandler = event -> {
            try {
                log.info("start button pressed");
                srtPlayer.start();
                eventBus.post(new RequestSubtitleUpdateEvent());
            } catch (IllegalStateException e) {
                log.error(e.getMessage());
            }
        };

        EventHandler<MouseEvent> stopButtonPressedHandler = event -> {
            try {
                log.info("stop button pressed");
                srtPlayer.stop();
            } catch (IllegalStateException e) {
                log.error(e.getMessage());
            }
        };

        EventHandler<MouseEvent> fastForwardButtonClickedHandler = event -> {
            try {
                log.info("fast forward pressed");
                srtPlayer.forward(FORWARD_DELTA);
                eventBus.post(new RequestSubtitleUpdateEvent());
            } catch (IllegalStateException e) {
                log.error(e.getMessage());
            }
        };

        EventHandler<MouseEvent> fastBackwardButtonClickedHandler = event -> {
            try {
                log.info("fast backward pressed");
                srtPlayer.forward(-FORWARD_DELTA);
                eventBus.post(new RequestSubtitleUpdateEvent());
            } catch (IllegalStateException e) {
                log.error(e.getMessage());
            }
        };
        EventHandler<ActionEvent> timeFieldActionHandler = event -> jumpToTimestamp();

        EventHandler<MouseEvent> movieModeButtonPressedHandler = event -> eventBus.post(new SwitchSrtDisplayerEvent(MovieSrtDisplayer.class));

        EventHandler<MouseEvent> optionsButtonPressedHandler = event -> Platform.runLater(this::openOptionsWindow);


        optionsButton.setOnMouseClicked(optionsButtonPressedHandler);
        stopButton.setOnMouseClicked(stopButtonPressedHandler);
        startButton.setOnMouseClicked(startButtonPressedHandler);
        timeField.setOnAction(timeFieldActionHandler);
        movieModeButton.setOnMouseClicked(movieModeButtonPressedHandler);
        fastBackwardButton.setOnMouseClicked(fastBackwardButtonClickedHandler);
        fastForwardButton.setOnMouseClicked(fastForwardButtonClickedHandler);

        eventHandlerRegistrations.add(new EventHandlerRegistration<>(optionsButton, optionsButtonPressedHandler, MouseEvent.MOUSE_CLICKED));
        eventHandlerRegistrations.add(new EventHandlerRegistration<>(stopButton, stopButtonPressedHandler, MouseEvent.MOUSE_CLICKED));
        eventHandlerRegistrations.add(new EventHandlerRegistration<>(startButton, startButtonPressedHandler, MouseEvent.MOUSE_CLICKED));
        eventHandlerRegistrations.add(new EventHandlerRegistration<>(timeField, timeFieldActionHandler, ActionEvent.ACTION));
        eventHandlerRegistrations.add(new EventHandlerRegistration<>(movieModeButton, movieModeButtonPressedHandler, MouseEvent.MOUSE_CLICKED));
        eventHandlerRegistrations.add(new EventHandlerRegistration<>(fastBackwardButton, fastBackwardButtonClickedHandler, MouseEvent.MOUSE_CLICKED));
        eventHandlerRegistrations.add(new EventHandlerRegistration<>(fastForwardButton, fastForwardButtonClickedHandler, MouseEvent.MOUSE_CLICKED));
    }

    private void jumpToTimestamp() {
        try {
            // put this into method of parser or own component
            log.debug("timestamp string entered: " + timeField.getText());
            Timestamp timestamp = new Timestamp(timeField.getText() + ",000");
            log.debug("user set new timestamp: " + timestamp);
            srtPlayer.jumpToTimestamp(timestamp);
            displayTime(timestamp);
            eventBus.post(new RequestSubtitleUpdateEvent());
        } catch (InvalidTimestampFormatException e) {
            log.error("Wrong timeStamp entered: " + e.getMessage());
            displayWrongTimeStampWarning();
            timeField.clear();
            return;
        }
        timeField.clear();
    }

    private void openOptionsWindow() {
        //position options window right next settingsWindow, otherwise optionsWindow may be behind settingsWindow, bc they are both alwaysOnTop
        Window settingsWindow = windowManager.getOpened().get(0);
        Vector2D nextToSettingsWindow = new Vector2D(settingsWindow.getStage().getX() + settingsWindow.getStage().getWidth(), settingsWindow.getStage().getY());
        windowManager.openAtPos(Windows.OPTIONS, nextToSettingsWindow);
    }


    @Override
    public void displayNextClickCounts() {
        Platform.runLater(() -> {
            log.debug("showing next click counts image now");
            clickWarning.setVisible(true);
        });

    }

    @Override
    public void hideNextClickCounts() {
        Platform.runLater(() -> {
            log.debug("hiding next click counts image now");
            clickWarning.setVisible(false);
        });
    }

    private void displayWrongTimeStampWarning() {
        new Thread(() -> {
            wrongFormatText.setVisible(true);
            try {
                Thread.sleep(TIME_STAMP_WARNING_DURATION);
            } catch (InterruptedException e) {
                log.error("display timestamp Warning Thread interrupted");
            }
            wrongFormatText.setVisible(false);
        }).start();
    }


    @Override
    public void displaySubtitle(@NonNull SubtitleText subtitleText) {
        Platform.runLater(() -> {
            int fontSize = options.getSettingsFontSize();
            FontBundle currentFont = fontManager.getCurrentFont().withSize(fontSize);
            // hard code color to black for visibility in settings mode
//            Color fontColor = fontOptions.getFontColor();
            Color fontColor = Color.BLACK;

            if (log.isTraceEnabled()) {
                log.trace("setting fontcolor: " + fontColor);
                log.trace("setting font size: " + fontSize);
                log.trace("using font: " + currentFont.getRegularFont().getName());

                log.trace("displaying new subtitle: " + subtitleText);
            }

            settingsTextFlow.getChildren().clear();
            for (Subtitle subtitle : subtitleText.getSubtitles()) {
                Text text = new Text(subtitle.getText());

                if (subtitle.getType().equals(SubtitleType.ITALIC)) {
                    text.setFont(currentFont.getItalicFont());
                } else {
                    text.setFont(currentFont.getRegularFont());
                }

                text.setFill(fontColor);

                if (log.isTraceEnabled())
                    log.trace("displaying text: " + text + " in settings mode");
                settingsTextFlow.getChildren().add(text);
            }
        });
    }


    @Override
    public void displayTime(@NonNull Timestamp time) {
        Platform.runLater(() -> {
            currentTimeStampText.setText(time.toAlarmClockString());
        });
    }
}
