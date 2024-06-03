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
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

import static io.github.vincemann.subtitlebuddy.util.ScreenUtils.getScreenBounds;
import static io.github.vincemann.subtitlebuddy.util.fx.ImageUtils.loadImageView;

/**
 * Controller for the movie stage.
 */
@Log4j2
@Singleton
public class MovieStageController implements MovieSrtDisplayer {

    private static final int MIN_FONT_SIZE = 20;
    private static final int MAX_FONT_SIZE = 90;
    private static final int MOVIE_CLICK_WARNING_SIZE = 60;

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

    private MovieStageMod movieStageMod;

    private double currentFontSize;


    @Inject
    public MovieStageController(FontManager srtFontManager,
                                EventBus eventBus,
                                SrtDisplayerOptions options,
                                FontOptions fontOptions) {
        this.fontManager = srtFontManager;
        this.eventBus = eventBus;
        this.options = options;
        this.fontOptions = fontOptions;
        this.lastSubtitleText = SubtitleText.empty();
        this.currentFontSize = options.getMovieFontSize();
    }


    @Override
    public void displayNextClickCounts() {
        this.clickWarning.setVisible(true);
    }

    @Override
    public void hideNextClickCounts() {
        this.clickWarning.setVisible(false);
    }



    @Override
    public void displaySubtitle(SubtitleText subtitleText) {
        log.debug("display new subtitle in movie mode: " + subtitleText);
        lastSubtitleText = subtitleText;

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

                text.setFill(fontColor);
                text.setStyle(OUTLINED_TEXT_STYLE);

                if (log.isTraceEnabled())
                    log.trace("displaying text: " + text + " in movie mode");
                movieTextFlow.getChildren().add(text);
            }
            adjustStageSizeToText();
        });
    }

    // make sure stage is only as big as needed
    private void adjustStageSizeToText(){
        Vector2D minStageSize = evalMinStageSize(movieTextFlow);
        movieStageMod.updateMinimumSize(minStageSize);
    }

    /**
     * Find out minimum stage size required to display subtitles.
     */
    private Vector2D evalMinStageSize(TextFlow textFlow) {
        double maxWidth = textFlow.getPrefWidth();
        double totalHeight = 0;

        for (javafx.scene.Node node : textFlow.getChildren()) {
            if (node instanceof Text) {
                Text text = (Text) node;
                text.applyCss();
                double textHeight = text.getBoundsInLocal().getHeight();
                totalHeight += textHeight * Math.ceil(text.getBoundsInLocal().getWidth() / maxWidth);
            }
        }

        // add some padding
        double padding = 20;
        double stageWidth = maxWidth + padding * 2;
        double stageHeight = totalHeight + padding * 2;

        return new Vector2D(stageWidth, stageHeight);
    }


    @FXML
    public void initialize() {
        movieAnchorPane.setBackground(Background.EMPTY);
        movieTextFlow.setBackground(Background.EMPTY);


        movieTextFlow.setPickOnBounds(true);
        // needs a fixed width so the subtitles are not displayed awkwardly word under word
        // user can still adjust the box manually by dragging the edges
        movieTextFlow.setPrefWidth(ScreenUtils.getScreenBounds().getX()/2.5);

        registerEventHandlers();
        initClickWarning();
    }

    private void initClickWarning(){
        clickWarning = loadImageView(movieAnchorPane,
                "images/finger.png",
                new Vector2D(MOVIE_CLICK_WARNING_SIZE, MOVIE_CLICK_WARNING_SIZE));
        clickWarning.setVisible(false);

        // Anchor the ImageView to the top and right of the AnchorPane
        AnchorPane.setTopAnchor(clickWarning, 10.0);
        AnchorPane.setRightAnchor(clickWarning, 10.0);
    }


    public void setStage(Stage stage) {
        this.stage = stage;
        // should always be absolute pos
        this.movieStageMod = new MovieStageMod(stage, movieVBox, movieAnchorPane, movieTextFlow);
        addShowStageListener();
    }

    /**
     * Init stage pos and size.
     * Take values from config (pos and font size).
     * If subtitles are not visible on screen, display in center.
     */
    private void initStage(){
        Vector2D stageSize = evalMinStageSize(movieTextFlow);
        Vector2D subtitlePos = options.getSubtitlePosition(); // top left of subtitle vbox
        // stage is as big as subtitle box, so its ok to work with stage size for calculating center
        Vector2D centerOfSubs = new Vector2D(subtitlePos.getX()+stageSize.getX()/2,subtitlePos.getY()+stageSize.getY()/2);
        boolean onScreen = VectorUtils.isVecWithinBounds(centerOfSubs, getScreenBounds());
        movieStageMod.updatePos(onScreen ? subtitlePos : VectorUtils.getCenterPos(ScreenUtils.getScreenBounds(),stageSize));
        movieStageMod.updateSize(stageSize);
        movieStageMod.initUserDefinedBounds();
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
        initStage(); // needs to be called when stage is shown, for proper init values
        eventBus.post(new RequestSubtitleUpdateEvent());
    }

    private void onHideStage(){
        unregisterEventHandlers();
    }

    private void unregisterEventHandlers() {
        eventHandlerRegistrations.forEach(EventHandlerRegistration::unregister);
        dragResizeMod.unregisterListeners();
    }

    /**
     * Update font size of currently displayed subs.
     */
    private void updateTextsFontSize(double fontSize) {
        for (javafx.scene.Node node : movieTextFlow.getChildren()) {
            if (node instanceof Text) {
                Text text = (Text) node;
                Font currentFont = text.getFont();
                text.setFont(Font.font(currentFont.getFamily(), fontSize));
            }
        }
    }

    private double calculateFontSize(double deltaW, double deltaH) {
        // Simple heuristic for font size adjustment
        // Adjust the font size proportionally to the change in width and height
        double adjustmentFactor = (-deltaW + -deltaH) / 100;
        double selectedFontSize = currentFontSize + adjustmentFactor;
        // Ensure bounds for font size
        currentFontSize = Math.min(MAX_FONT_SIZE,Math.max(MIN_FONT_SIZE,selectedFontSize));
        return currentFontSize;
    }

    private void registerEventHandlers() {
        EventHandler<MouseEvent> movieBoxMouseEnteredHandler =
                event -> movieVBox.setStyle(BLUE_HALF_TRANSPARENT_BACK_GROUND_STYLE);

        EventHandler<MouseEvent> movieBoxMouseExitedHandler =
                event -> movieVBox.setStyle(TRANSPARENT_BACKGROUND_STYLE);

        movieVBox.setOnMouseEntered(movieBoxMouseEnteredHandler);
        movieVBox.setOnMouseExited(movieBoxMouseExitedHandler);

        dragResizeMod = DragResizeMod.builder()
                .node(movieAnchorPane)
                .mouseReleasedFunction(this::onAnchorPaneDraggedInPosition)
                .mouseClickedFunction(this::onAnchorPaneClicked)
                .resizeFunction(this::onAnchorPaneResize)
                .build();
        dragResizeMod.makeResizableAndDraggable();

        eventHandlerRegistrations.add(new EventHandlerRegistration<>(movieVBox, movieBoxMouseEnteredHandler, MouseEvent.MOUSE_ENTERED));
        eventHandlerRegistrations.add(new EventHandlerRegistration<>(movieVBox, movieBoxMouseExitedHandler, MouseEvent.MOUSE_EXITED));
    }

    private void onAnchorPaneDraggedInPosition(MouseEvent mouseEvent, double deltaX, double deltaY, boolean resize) {
        if (resize)
            return;
        // is called when user selected a new position for the anchor pane
        // need to use the delta values bc the mouse x,y are relative or something
        double newX = stage.getX() + deltaX;
        double newY = stage.getY() + deltaY;
        Vector2D nodePos = new Vector2D(newX, newY);
        movieStageMod.updatePos(nodePos);
        eventBus.post(new UpdateSubtitlePosEvent(nodePos));
    }

    private void onAnchorPaneResize(Node node, double h, double w, double deltaH, double deltaW) {
        movieVBox.setStyle(BLUE_HALF_TRANSPARENT_BACK_GROUND_STYLE);

        // if resizing via corner only scale font size
        // if resizing via edge only scale box
        if (deltaW != 0 && deltaH != 0) {
            // Adjust font size based on new width and height
            double fontSize = calculateFontSize(deltaW, deltaH);
            updateTextsFontSize(fontSize); // update ui as often as possible to create fluent animation
            // dont write to disk too often, this method is called often in a short time
            if (fontSize == options.getMovieFontSize()){
                return;
            }
            eventBus.post(new UpdateMovieFontSizeEvent((int) fontSize));
        } else {
            // Resizing in one direction only
            if (deltaW != 0) {
                movieStageMod.userUpdateWidth(w);
            }
            if (deltaH != 0) {
                movieStageMod.userUpdateHeight(h);
            }
        }
    }

    private void onAnchorPaneClicked(MouseEvent event) {
        if (event.getButton().equals(MouseButton.PRIMARY)) {
            if (event.getClickCount() == 2) {
                // double click
                log.debug("user double clicked movieText -> switching to settings srt displayer");
                eventBus.post(new SwitchSrtDisplayerEvent(SettingsSrtDisplayer.class));
            }
        }
    }
}
