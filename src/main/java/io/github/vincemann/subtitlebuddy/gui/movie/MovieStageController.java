package io.github.vincemann.subtitlebuddy.gui.movie;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.*;
import io.github.vincemann.subtitlebuddy.font.FontManager;
import io.github.vincemann.subtitlebuddy.font.FontOptions;
import io.github.vincemann.subtitlebuddy.gui.EventHandlerRegistration;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayerOptions;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import io.github.vincemann.subtitlebuddy.srt.Subtitle;
import io.github.vincemann.subtitlebuddy.srt.SubtitleText;
import io.github.vincemann.subtitlebuddy.srt.SubtitleType;
import io.github.vincemann.subtitlebuddy.util.ScreenUtils;
import io.github.vincemann.subtitlebuddy.util.fx.DragResizeMod;
import io.github.vincemann.subtitlebuddy.util.fx.DrawingUtil;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static io.github.vincemann.subtitlebuddy.util.fx.ImageUtils.loadImageView;

/**
 * Controller for the movie stage.
 */
@Log4j2
@Singleton
public class MovieStageController implements MovieSrtDisplayer {

    private static final int MOVIE_CLICK_WARNING_SIZE = 60;
    //200 millis in nano
    private static final long UPDATE_SLEEP_DURATION = 200000000L;

    private static final String BLUE_HALF_TRANSPARENT_BACK_GROUND_STYLE = "-fx-background-color: rgba(0, 100, 100, 0.51); -fx-background-radius: 10;";
    private static final String TRANSPARENT_BACKGROUND_STYLE = "-fx-background-color: transparent;";
    public static final String OUTLINED_TEXT_STYLE = /*"color: #ff0;"+*/
//            "text-shadow: 3px 0 0 #000, 0 -3px 0 #000, 0 3px 0 #000, -3px 0 0 #000;";
//        "-fx-fill: lightseagreen;\n"+
            "-fx-stroke: black;\n" +
                    "-fx-stroke-width: 2px;\n";


    @FXML
    private VBox movieVBox;
    @FXML
    private TextFlow movieTextFlow;
    @FXML
    private AnchorPane movieAnchorPane;


    private ImageView clickWarning;


    private EventBus eventBus;

    private FontManager fontManager;

    private SrtDisplayerOptions options;



    @Getter
    private SubtitleText lastSubtitleText;

    private DragResizeMod dragResizeMod;

    private List<EventHandlerRegistration<?>> eventHandlerRegistrations = new ArrayList<>();

    private FontOptions fontOptions;

    private Stage stage;

    private StageResizer stageResizer;


    @Inject
    public MovieStageController(FontManager srtFontManager,
                                EventBus eventBus,
                                SrtDisplayerOptions options,
                                FontOptions fontOptions) {
        this.fontManager = srtFontManager;
        this.eventBus = eventBus;
        this.options = options;
        // make sure subtitles can be seen
        this.fontOptions = fontOptions;
        this.lastSubtitleText = SubtitleText.empty();
    }


    @Override
    public void displayNextClickCounts() {
        this.clickWarning.setVisible(true);
    }

    @Override
    public void hideNextClickCounts() {
        this.clickWarning.setVisible(false);
    }


    private void onVBoxClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                // double click
                log.debug("user double clicked movieText -> switching to settings srt displayer");
                eventBus.post(new SwitchSrtDisplayerEvent(SettingsSrtDisplayer.class));
            }
        }
    }


    @Override
    public void displaySubtitle(SubtitleText subtitleText) {
        log.debug("display new subtitle in movie mode: " + subtitleText);
        lastSubtitleText = subtitleText;

        if (true)
            return;

        Platform.runLater(() -> {
            Color fontColor = fontOptions.getFontColor();
            int fontSize = options.getMovieFontSize();
            FontBundle currentFont = fontManager.getCurrentFont().withSize(fontSize);

            if (log.isTraceEnabled()) {
                log.trace("using text size: " + fontSize);
                log.trace("setting fontcolor: " + fontColor);
                log.trace("using font: " + currentFont.getRegularFont().getName());

                log.trace("displaying new subtitle: " + subtitleText);
            }

            movieTextFlow.getChildren().clear();
            for (Subtitle subtitle : subtitleText.getSubtitles()) {
                Text text = new Text(subtitle.getText());

                if (subtitle.getType().equals(SubtitleType.ITALIC)) {
                    text.setFont(currentFont.getItalicFont());
                } else {
                    text.setFont(currentFont.getRegularFont());
                }

//                    FontUtils.adjustTextSize(text, fontSize);
                text.setFill(fontColor);
                text.setStyle(OUTLINED_TEXT_STYLE);

                if (log.isTraceEnabled())
                    log.trace("displaying text: " + text + " in movie mode");
                movieTextFlow.getChildren().add(text);
//                movieTextFlow.getChildren().add(new Text(System.lineSeparator()));
            }
        });
    }


    @FXML
    public void initialize() {
        // should always be absolute pos
//        Vector2D stagePos = VectorUtils.getVecWithinBounds(options.getSubtitlePosition(), getScreenBounds());
        Vector2D stageSize = evalStageSize();
        stageSize = new Vector2D(1000,300);

        Vector2D stagePos = options.getSubtitlePosition();
        stagePos = new Vector2D(ScreenUtils.getScreenBounds().getX()/2-stageSize.getX()/2,ScreenUtils.getScreenBounds().getY()/2-stageSize.getY()/2);

        // todo change back - visualize for debugging
        movieAnchorPane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        movieVBox.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
//        movieAnchorPane.setBackground(Background.EMPTY);
//        movieTextFlow.setBackground(Background.EMPTY);


        movieTextFlow.setPickOnBounds(true);



//        movieVBox.setLayoutX(stagePos.getX());
//        movieVBox.setLayoutY(stagePos.getY());

        registerEventHandlers();

        clickWarning = loadImageView(movieVBox,
                "images/finger.png",
                new Vector2D(MOVIE_CLICK_WARNING_SIZE, MOVIE_CLICK_WARNING_SIZE));
        clickWarning.setVisible(false);
        eventBus.post(new RequestSubtitleUpdateEvent());


        this.stageResizer = new StageResizer(stagePos, stageSize, movieVBox, movieAnchorPane);

        // Add size listeners to the VBox
//        movieVBox.widthProperty().addListener((obs, oldVal, newVal) -> adjustStageSize());
//        movieVBox.heightProperty().addListener((obs, oldVal, newVal) -> adjustStageSize());

//        adjustStageSizeAndPos();
    }

    private Vector2D evalStageSize() {
        int fontSize = options.getMovieFontSize();
        double y = fontSize*5;
        double x = fontSize*20;
        return new Vector2D(x,y);
    }

//    private void adjustStageSizeAndPos(){
//        adjustStageSize();
//        adjustStagePos();
//    }

//    private void adjustStagePos(){
//        Platform.runLater(() -> {
//            Point2D screenPos = movieVBox.localToScreen(0, 0);
//            if (screenPos != null && !Double.isNaN(screenPos.getX()) && !Double.isNaN(screenPos.getY())) {
////                stage.setX(screenPos.getX());
////                stage.setY(screenPos.getY());
//                stage.setX(ScreenUtils.getScreenBounds().getX()/2);
//                stage.setX(ScreenUtils.getScreenBounds().getY()/2);
//                log.info("Setting stage position to: (x/y) " + screenPos.getX() + "/" + screenPos.getY());
//                // only adjust size when pos works to avoid size is already adjusted so pos of vbox cant be determined properly
//            } else {
//                log.warn("Invalid screen coordinates: (x/y) " + screenPos.getX() + "/" + screenPos.getY());
//            }
//        });
//
//    }

    // stage should always just have the size of the movie box, bc mac does not support click through
//    private void adjustStageSize() {
//        if (stage != null) {
//            Platform.runLater(() -> {
//                double width = movieVBox.getWidth()*3;
//                double height = movieVBox.getHeight()*3;
//                if (width != 0 && height != 0){
//                    log.info("adjusting stage size to: w/h: " + width + "/" + height);
//                    stage.setWidth(width);
//                    stage.setHeight(height);
//                }
//                else {
//                    log.info("invalid size of movie box, ignoring");
//                }
//            });
//
//        }
//    }

    public void setStage(Stage stage) {
        this.stage = stage;
        this.stageResizer.setStage(stage);
        registerEventHandlingStageListener();
        stageResizer.adjust();
    }

    private void registerEventHandlingStageListener() {
        stage.showingProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                registerEventHandlers();
                stageResizer.adjust();
            } else {
                unregisterEventHandlers();
            }
        });
    }

    private void unregisterEventHandlers() {
        eventHandlerRegistrations.forEach(EventHandlerRegistration::unregister);
        dragResizeMod.unregisterListeners();
    }

    private void onDraggedInPosition(MouseEvent mouseEvent, double deltaX, double deltaY) {
        log.debug("old stage pos: " +stage.getX() +" " + stage.getY());
        // is called when user selected a new position for the movieVBox
        double newX = stage.getX() + deltaX;
        double newY = stage.getY() + deltaY;
        log.debug("new stage pos: " + newX + " " + newY);
//        Point2D absPos = movieAnchorPane.localToScreen(0, 0);
        Vector2D nodePos = new Vector2D(newX, newY);
        eventBus.post(new UpdateSubtitlePosEvent(nodePos));
        stageResizer.updatePos(nodePos);
    }

    private void onMovieBoxResize(Node node, double h, double w, double deltaH, double deltaW) {
//        checkArgument(node == movieVBox);
//        // todo change back
////        movieVBox.setStyle(BLUE_HALF_TRANSPARENT_BACK_GROUND_STYLE);
//        movieVBox.setPrefHeight(h);
//        movieVBox.setPrefWidth(w);
//        int fontSize = ((int) (h + w) / 2) / 9;
//        // dont write to disk too often, this method is called often in a short time
//        ExecutionLimiter.executeMaxEveryNMillis("fontResize", UPDATE_SLEEP_DURATION,
//                () -> eventBus.post(new UpdateMovieFontSizeEvent(fontSize)));
    }

    private void registerEventHandlers() {
        EventHandler<MouseEvent> movieBoxMouseEnteredHandler =
                event -> {
            // todo change back
//                    movieVBox.setStyle(BLUE_HALF_TRANSPARENT_BACK_GROUND_STYLE);
                };

        EventHandler<MouseEvent> movieBoxMouseExitedHandler =
                event -> {
            // todo change back
//            movieVBox.setStyle(TRANSPARENT_BACKGROUND_STYLE);
                };

        movieVBox.setOnMouseEntered(movieBoxMouseEnteredHandler);
        movieVBox.setOnMouseExited(movieBoxMouseExitedHandler);

        dragResizeMod = DragResizeMod.builder()
                .node(movieAnchorPane)
                .mouseReleasedFunction(this::onDraggedInPosition)
                .mouseClickedFunction(this::onVBoxClicked)
                .resizeFunction(this::onMovieBoxResize)
                .nodeHeight(movieAnchorPane.getHeight())
                .nodeWidth(movieAnchorPane.getWidth())
                .build();
        dragResizeMod.makeResizableAndDraggable();

        eventHandlerRegistrations.add(new EventHandlerRegistration<>(movieVBox, movieBoxMouseEnteredHandler, MouseEvent.MOUSE_ENTERED));
        eventHandlerRegistrations.add(new EventHandlerRegistration<>(movieVBox, movieBoxMouseExitedHandler, MouseEvent.MOUSE_EXITED));
    }

}
