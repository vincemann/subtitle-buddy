package io.github.vincemann.subtitleBuddy.gui.stages.stageController.settingsStage;


import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitleBuddy.classpathFileFinder.ReadOnlyClassPathFileFinder;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertyFileKeys;
import io.github.vincemann.subtitleBuddy.config.uiStringsFile.UIStringsFileKeys;
import io.github.vincemann.subtitleBuddy.events.RequestSrtParserUpdateEvent;
import io.github.vincemann.subtitleBuddy.events.SwitchSrtDisplayerEvent;
import io.github.vincemann.subtitleBuddy.srt.*;
import io.github.vincemann.subtitleBuddy.srt.subtitleTransformer.InvalidTimestampFormatException;
import io.github.vincemann.subtitleBuddy.gui.srtDisplayer.MovieSrtDisplayer;
import io.github.vincemann.subtitleBuddy.gui.srtDisplayer.SettingsSrtDisplayer;
import io.github.vincemann.subtitleBuddy.gui.stages.StageState;
import io.github.vincemann.subtitleBuddy.gui.stages.stageController.AbstractStageController;
import io.github.vincemann.subtitleBuddy.gui.stages.stageController.optionsStage.OptionsWindow;
import io.github.vincemann.subtitleBuddy.srt.font.fontManager.SrtFontManager;
import io.github.vincemann.subtitleBuddy.srt.parser.SrtParser;
import io.github.vincemann.subtitleBuddy.srt.stopwatch.RunningState;
import io.github.vincemann.subtitleBuddy.util.vec2d.Vector2D;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

@Log4j
@NoArgsConstructor
@io.github.vincemann.subtitleBuddy.gui.stages.SettingsStageController
@Singleton
public class SettingsStageController extends AbstractStageController implements SettingsSrtDisplayer {
    private static final String SETTINGS_STAGE_FXML_FILE_PATH = "/settingsStage.fxml";
    private static final int SETTINGS_CLICK_WARNING_SIZE = 40;
    //millis
    private static final int MIN_TIME_STAMP_WARNING_DURATION = 1000;

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

    private ImageView settingsClickWarning;

    @Getter
    private SubtitleText lastSubtitleText;

    private SrtParser srtParser;

    @Getter
    private SrtFont currentFont;

    private Timestamp lastTimeStamp;

    private long timeStampWarningDuration;

    private SrtFontManager srtFontManager;

    private OptionsWindow optionsWindow;

    private EventBus eventBus;

    private int settingsFontSize;

    private int fastForwardDelta;

    private String startButtonText;
    private String stopButtonText;
    private String movieModeButtonText;
    private String optionsButtonText;
    private String wrongTimeStampFormatText;
    private String timestampJumpHintTextString;



    @Inject
    public SettingsStageController(Stage mainStage,
                                   SrtParser srtParser,
                                   @Named(UIStringsFileKeys.SETTINGS_STAGE_TITLE_KEY)
                                           String windowTitle,
                                   @Named(PropertyFileKeys.TIME_STAMP_WARNING_DURATION_KEY)
                                               long timeStampWarningDuration,
                                   SrtFontManager srtFontManager,
                                   OptionsWindow optionsWindow,
                                   @Named(PropertyFileKeys.SETTINGS_STAGE_SIZE_KEY) Vector2D minSize,
                                   @Named(PropertyFileKeys.SETTINGS_FONT_SIZE_KEY) int settingsFontSize,
                                   @Named(PropertyFileKeys.FAST_FORWARD_DELTA_KEY) int fastForwardDelta,
                                   EventBus eventBus,
                                   ReadOnlyClassPathFileFinder readOnlyClassPathFileFinder,
                                   @Named(PropertyFileKeys.CLICK_WARNING_IMAGE_PATH_KEY) String clickWarningImagePath,
                                   @Named(UIStringsFileKeys.START_BUTTON_TEXT_KEY) String startButtonText,
                                   @Named(UIStringsFileKeys.STOP_BUTTON_TEXT_KEY) String stopButtonText,
                                   @Named(UIStringsFileKeys.MOVIE_MODE_BUTTON_TEXT_KEY)String movieModeButtonText,
                                   @Named(UIStringsFileKeys.OPTIONS_BUTTON_TEXT_KEY) String optionsButtonText,
                                   @Named(UIStringsFileKeys.WRONG_TIMESTAMP_FORMAT_TEXT_KEY) String wrongTimeStampFormatText,
                                   @Named(UIStringsFileKeys.TIMESTAMP_JUMP_HINT_TEXT_KEY) String timestampJumpHintTextString
    )
            throws IOException {
        super(readOnlyClassPathFileFinder.findFileOnClassPath(SETTINGS_STAGE_FXML_FILE_PATH).getFile().toURI().toURL(),windowTitle,
                minSize);
        createStage(this,mainStage);
        this.settingsClickWarning = createImageView(imageHBox, readOnlyClassPathFileFinder.findFileOnClassPath(clickWarningImagePath).getFile(),new Vector2D(SETTINGS_CLICK_WARNING_SIZE,SETTINGS_CLICK_WARNING_SIZE));
        this.settingsFontSize=settingsFontSize;
        this.srtParser = srtParser;
        this.eventBus=eventBus;
        this.timeStampWarningDuration=timeStampWarningDuration;
        this.srtFontManager = srtFontManager;
        this.optionsWindow=optionsWindow;
        this.fastForwardDelta=fastForwardDelta;
        //ui strings
        this.startButtonText=startButtonText;
        this.stopButtonText=stopButtonText;
        this.optionsButtonText=optionsButtonText;
        this.movieModeButtonText=movieModeButtonText;
        this.wrongTimeStampFormatText=wrongTimeStampFormatText;
        this.timestampJumpHintTextString=timestampJumpHintTextString;
        constructorInit();
    }


    //todo evtl in interface ausbauen
    @Override
    public void setFontColor(Color color) {
        //nothing happens
    }

    @Override
    public Color getFontColor() {
        return SettingsSrtDisplayer.DEFAULT_FONT_COLOR;
    }

    private void constructorInit(){
        setUIStrings();
        lastTimeStamp = Timestamp.ZERO();
        lastSubtitleText = srtParser.getCurrentSubtitleText();
        this.currentFont= srtFontManager.loadDefaultFont(settingsFontSize);
        if(timeStampWarningDuration<MIN_TIME_STAMP_WARNING_DURATION){
            timeStampWarningDuration=MIN_TIME_STAMP_WARNING_DURATION;
        }
        this.settingsClickWarning.setVisible(false);
    }

    private void setUIStrings(){
        Platform.runLater(() -> {
            startButton.setText(startButtonText);
            stopButton.setText(stopButtonText);
            optionsButton.setText(optionsButtonText);
            movieModeButton.setText(movieModeButtonText);
            wrongFormatText.setText(wrongTimeStampFormatText);
            timestampJumpHintText.setText(timestampJumpHintTextString);
        });
    }


    @Override
    public void setCurrentFont(SrtFont font) {
        this.currentFont=font;
    }

    @Override
    protected Table<Node, EventHandler, EventType> registerEventHandlers() {

        //todo auslagern in austauschbare events + eventhandler -> business/io.github.vincemann.srtParser/logik von io.github.vincemann.gui trennen
        EventHandler<MouseEvent> startButtonPressedHandler = event -> {
            try {
                log.trace("startbutton pressed");
                srtParser.start();
            }catch (IllegalStateException e){
                log.debug(e.getMessage());
            }
        };

        EventHandler<MouseEvent> stopButtonPressedHandler = event -> {
            try {
                log.trace("stopbutton pressed");
                srtParser.stop();
            }catch (IllegalStateException e){
                log.debug(e.getMessage());
            }
        };

        EventHandler<MouseEvent> fastForwardButtonClickedHandler = event -> {
            try {
                log.trace("fastforward pressed");
                synchronized (srtParser) {
                    if (srtParser.getCurrentState().equals(RunningState.STATE_RUNNING)) {
                        srtParser.stop();
                    }
                    srtParser.setTime(new Timestamp(srtParser.getTime().toMilliSeconds() + fastForwardDelta));
                    srtParser.start();
                }
            }catch (IllegalStateException e){
                log.debug(e.getMessage());
            }
        };

        EventHandler<MouseEvent> fastBackwardButtonClickedHandler = event -> {
            try {
                log.trace("fastbackward pressed");
                synchronized (srtParser) {
                    if (srtParser.getCurrentState().equals(RunningState.STATE_RUNNING)) {
                        srtParser.stop();
                    }
                    srtParser.setTime(new Timestamp(srtParser.getTime().toMilliSeconds() - fastForwardDelta));
                    srtParser.start();
                }
            }catch (IllegalStateException e){
                log.debug(e.getMessage());
            }
        };
        //set Listener to wait until the scene&stage is initialized, then init the scene/stage specific stuff
        //-> remains stateless
        /*
        settingsPane.sceneProperty().addListener((observableScene, oldScene, newScene) -> {
            if (oldScene == null && newScene != null) {
                // scene is set for the first time. Now its the time to listen stage changes.
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
                    if (oldWindow == null && newWindow != null) {
                        // stage is set. now is the right time to do whatever we need to the stage in the controller.
                        initStage();
                    }
                });
            }
        });*/
        EventHandler<ActionEvent> timeFieldActionHandler = event -> {
            Timestamp timestamp;
            try {
                log.debug("timestamp string entered: " + timeField.getText());
                timestamp = new Timestamp(timeField.getText()+",000");
                log.debug("user set new timestamp: " + timestamp);
                synchronized (srtParser) {
                    if(srtParser.getCurrentState().equals(RunningState.STATE_RUNNING)) {
                        srtParser.stop();
                    }
                    srtParser.setTime(timestamp);
                    eventBus.post(new RequestSrtParserUpdateEvent());
                }
                setTime(timestamp);
            } catch (InvalidTimestampFormatException e) {
                log.info("Wrong timeStamp entered: ");
                displayWrongTimeStampWarning();
                timeField.clear();
                return;
            }
            timeField.clear();
        };

        EventHandler<MouseEvent> movieModeButtonPressedHandler = event -> eventBus.post(new SwitchSrtDisplayerEvent(MovieSrtDisplayer.class));

        EventHandler<MouseEvent> optionsButtonPressedHandler = event -> {
            //position options window right next settingsWindow, otherwise optionsWindow may be behind settingsWindow, bc they are both alwaysOnTop
            optionsWindow.openOptionsWindow(new Vector2D(getStage().getX()+getStage().getWidth(),getStage().getY()));
        };


        optionsButton.setOnMouseClicked(optionsButtonPressedHandler);
        stopButton.setOnMouseClicked(stopButtonPressedHandler);
        startButton.setOnMouseClicked(startButtonPressedHandler);
        timeField.setOnAction(timeFieldActionHandler);
        movieModeButton.setOnMouseClicked(movieModeButtonPressedHandler);
        fastBackwardButton.setOnMouseClicked(fastBackwardButtonClickedHandler);
        fastForwardButton.setOnMouseClicked(fastForwardButtonClickedHandler);

        Table<Node, EventHandler, EventType> resultTable = HashBasedTable.create();
        resultTable.put(optionsButton,optionsButtonPressedHandler,MouseEvent.MOUSE_CLICKED);
        resultTable.put(stopButton,stopButtonPressedHandler,MouseEvent.MOUSE_CLICKED);
        resultTable.put(startButton,startButtonPressedHandler,MouseEvent.MOUSE_CLICKED);
        resultTable.put(timeField,timeFieldActionHandler,ActionEvent.ACTION);
        resultTable.put(movieModeButton,movieModeButtonPressedHandler,MouseEvent.MOUSE_CLICKED);
        resultTable.put(fastBackwardButton,fastBackwardButtonClickedHandler,MouseEvent.MOUSE_CLICKED);
        resultTable.put(fastForwardButton,fastForwardButtonClickedHandler,MouseEvent.MOUSE_CLICKED);
        return resultTable;
    }

    @Override
    protected void onFXMLInitialize() {
        super.onFXMLInitialize();
        checkNotNull(optionsButton);
        checkNotNull(timeField);
        checkNotNull(startButton);
        checkNotNull(stopButton);
        checkNotNull(settingsTextFlow);
        checkNotNull(settingsTextFlow.getChildren());
        checkNotNull(currentTimeStampText);
        checkNotNull(wrongFormatText);
        checkNotNull(fastBackwardButton);
        checkNotNull(fastForwardButton);
    }

    @Override
    public void displayNextClickCounts() {
        Platform.runLater(() -> {
            log.debug("showing next click counts image now");
            settingsClickWarning.setVisible(true);
        });

    }

    @Override
    public void hideNextClickCounts() {

        Platform.runLater(() -> {
            log.debug("hiding next click counts image now");
            settingsClickWarning.setVisible(false);
        });
    }

    @Override
    public void onStageCreate(Stage stage){
        super.onStageCreate(stage);
        log.debug("stage initialized");
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
    }



    private void displayWrongTimeStampWarning(){
        new Thread(() -> {
            wrongFormatText.setVisible(true);
            try {
                Thread.sleep(timeStampWarningDuration);
            } catch (InterruptedException e) {
                log.error("display timestamp Warning Thread interrupted");
                timeField.setVisible(false);
            }
            wrongFormatText.setVisible(false);
        }).start();
    }


    @Override
    @FXML
    public void displaySubtitle(@NonNull SubtitleText subtitleText) {
        log.trace("asking javafx to display new subtitle in "+this.getClass().getSimpleName()+" : " + subtitleText);
        lastSubtitleText =subtitleText;

        Platform.runLater(() -> {
            log.trace("displaying new subtitle: " + subtitleText);
            settingsTextFlow.getChildren().clear();
            for(List<SubtitleSegment> subtitleSegments: subtitleText.getSubtitleSegments()){
                for(SubtitleSegment subtitleSegment: subtitleSegments){
                    Text text = new Text(subtitleSegment.getText());

                    if(subtitleSegment.getSubtitleType().equals(SubtitleType.ITALIC)){
                        text.setFont(currentFont.getItalicFont());
                    }else {
                        text.setFont(currentFont.getRegularFont());
                    }
                    log.trace("setting fontcolor: "+ srtFontManager.getFontColor());
                    text.setFill(SettingsSrtDisplayer.DEFAULT_FONT_COLOR);
                    adjustTextSize(text,settingsFontSize);

                    log.trace("displaying text: " + text + " in "+this.getClass().getSimpleName());
                    settingsTextFlow.getChildren().add(text);
                    settingsTextFlow.getChildren().add(new Text(System.lineSeparator()));
                }
            }
        });
    }


    @Override
    public void setTime(@NonNull Timestamp time) {
        Platform.runLater(() -> {
            if(!lastTimeStamp.equalBySeconds(time)) {
                currentTimeStampText.setText(time.toAlarmClockString());
                lastTimeStamp=new Timestamp(time);
            }
        });
    }

    @Override
    protected void onShowStage() {
        super.onShowStage();
    }

    @Override
    protected void onStageClose() {
        super.onStageClose();
        System.exit(0);
    }

    @Override
    public void close() {
        closeStage();
    }

    @Override
    public void open() {
        showStage();
    }

    @Override
    public boolean isDisplaying() {
        return getStageState().equals(StageState.OPEN);
    }
}
