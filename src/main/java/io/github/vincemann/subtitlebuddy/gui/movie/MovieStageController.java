package io.github.vincemann.subtitlebuddy.gui.movie;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.events.RequestSubtitleUpdateEvent;
import io.github.vincemann.subtitlebuddy.events.SwitchSrtDisplayerEvent;
import io.github.vincemann.subtitlebuddy.events.UpdateMovieFontSizeEvent;
import io.github.vincemann.subtitlebuddy.events.UpdateSubtitlePosEvent;
import io.github.vincemann.subtitlebuddy.font.FontManager;
import io.github.vincemann.subtitlebuddy.font.FontOptions;
import io.github.vincemann.subtitlebuddy.gui.EventHandlerRegistration;
import io.github.vincemann.subtitlebuddy.gui.SrtDisplayerOptions;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import io.github.vincemann.subtitlebuddy.srt.Subtitle;
import io.github.vincemann.subtitlebuddy.srt.SubtitleText;
import io.github.vincemann.subtitlebuddy.srt.SubtitleType;
import io.github.vincemann.subtitlebuddy.util.ExecutionLimiter;
import io.github.vincemann.subtitlebuddy.util.fx.DragResizeMod;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import io.github.vincemann.subtitlebuddy.util.vec.VectorUtils;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.github.vincemann.subtitlebuddy.util.ScreenUtils.getScreenBounds;
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

    private Vector2D movieVBoxPos;


    @Getter
    private SubtitleText lastSubtitleText;

    private DragResizeMod dragResizeMod;

    private List<EventHandlerRegistration<?>> eventHandlerRegistrations = new ArrayList<>();

    private FontOptions fontOptions;

    private Stage stage;


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
        this.lastSubtitleText = new SubtitleText(new ArrayList<>(Collections.emptyList()));
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
        log.debug("asking javafx to display new subtitle on MovieStageController : " + subtitleText);
        lastSubtitleText = subtitleText;


        Platform.runLater(() -> {
            Color fontColor = fontOptions.getFontColor();
            int fontSize = options.getMovieFontSize();
            FontBundle currentFont = fontManager.getCurrentFont().withSize(fontSize);

            if (log.isDebugEnabled()) {
                log.debug("using text size: " + fontSize);
                log.debug("setting fontcolor: " + fontColor);
                log.debug("using font: " + currentFont.getRegularFont().getName());

                log.debug("displaying new subtitle: " + subtitleText);
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

                movieTextFlow.getChildren().add(text);
                movieTextFlow.getChildren().add(new Text(System.lineSeparator()));
            }
        });
    }


    @FXML
    public void initialize() {
        movieVBoxPos = VectorUtils.getVecWithinBounds(options.getSubtitlePosition(), getScreenBounds());
        checkNotNull(movieVBox);
        checkNotNull(movieTextFlow);
        checkNotNull(movieAnchorPane);
        movieAnchorPane.setBackground(Background.EMPTY);
        movieTextFlow.setBackground(Background.EMPTY);
        movieTextFlow.setPickOnBounds(true);

        movieVBox.setLayoutX(movieVBoxPos.getX());
        movieVBox.setLayoutY(movieVBoxPos.getY());

        registerEventHandlers();

        clickWarning = loadImageView(movieVBox,
                "/images/finger.png",
                new Vector2D(MOVIE_CLICK_WARNING_SIZE, MOVIE_CLICK_WARNING_SIZE));
        clickWarning.setVisible(false);
        eventBus.post(new RequestSubtitleUpdateEvent());
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
        dragResizeMod.unregisterListeners();
    }

    private void onDraggedInPosition(MouseEvent mouseEvent) {
        // is called when user selected a new position for the movieVBox
        Vector2D nodePos = new Vector2D(movieVBox.getLayoutX(), movieVBox.getLayoutY());
        eventBus.post(new UpdateSubtitlePosEvent(nodePos));
    }

    private void onMovieBoxResize(Node node, double h, double w, double deltaH, double deltaW) {
        checkArgument(node == movieVBox);
        movieVBox.setStyle(BLUE_HALF_TRANSPARENT_BACK_GROUND_STYLE);
        movieVBox.setPrefHeight(h);
        movieVBox.setPrefWidth(w);
        int fontSize = ((int) (h + w) / 2) / 9;
        log.debug("font size update by movie box size adjustments  : " + fontSize);
        // dont write to disk too often, this method is called often in a short time
        ExecutionLimiter.executeMaxEveryNMillis("fontResize", UPDATE_SLEEP_DURATION,
                () -> eventBus.post(new UpdateMovieFontSizeEvent(fontSize)));
    }

    private void registerEventHandlers() {
        EventHandler<MouseEvent> movieBoxMouseEnteredHandler =
                event -> movieVBox.setStyle(BLUE_HALF_TRANSPARENT_BACK_GROUND_STYLE);

        EventHandler<MouseEvent> movieBoxMouseExitedHandler =
                event -> movieVBox.setStyle(TRANSPARENT_BACKGROUND_STYLE);

        movieVBox.setOnMouseEntered(movieBoxMouseEnteredHandler);
        movieVBox.setOnMouseExited(movieBoxMouseExitedHandler);

        dragResizeMod = DragResizeMod.builder()
                .node(movieVBox)
                .mouseReleasedFunction(this::onDraggedInPosition)
                .mouseClickedFunction(this::onVBoxClicked)
                .resizeFunction(this::onMovieBoxResize)
                .nodeHeight(movieVBox.getHeight())
                .nodeWidth(movieVBox.getWidth())
                .build();
        dragResizeMod.makeResizableAndDraggable();

        eventHandlerRegistrations.add(new EventHandlerRegistration<>(movieVBox, movieBoxMouseEnteredHandler, MouseEvent.MOUSE_ENTERED));
        eventHandlerRegistrations.add(new EventHandlerRegistration<>(movieVBox, movieBoxMouseExitedHandler, MouseEvent.MOUSE_EXITED));
    }

}
