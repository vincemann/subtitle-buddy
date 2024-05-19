package io.github.vincemann.subtitlebuddy.gui.movie;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitlebuddy.events.UpdateSubtitlePosEvent;
import io.github.vincemann.subtitlebuddy.events.SwitchSrtDisplayerEvent;
import io.github.vincemann.subtitlebuddy.gui.settings.SettingsSrtDisplayer;
import io.github.vincemann.subtitlebuddy.options.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import io.github.vincemann.subtitlebuddy.srt.SubtitleSegment;
import io.github.vincemann.subtitlebuddy.srt.SubtitleText;
import io.github.vincemann.subtitlebuddy.srt.SubtitleType;
import io.github.vincemann.subtitlebuddy.srt.font.SrtFontManager;
import io.github.vincemann.subtitlebuddy.util.ExecutionLimiter;
import io.github.vincemann.subtitlebuddy.util.fx.DragResizeMod;
import io.github.vincemann.subtitlebuddy.util.fx.FontUtils;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import io.github.vincemann.subtitlebuddy.util.vec.VectorDecodeException;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
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
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static io.github.vincemann.subtitlebuddy.util.ScreenUtils.getScreenBoundsVector;
import static io.github.vincemann.subtitlebuddy.util.fx.ImageUtils.loadImageView;

/**
 * Controller for the movie stage.
 */
@Log4j2
@Singleton
public class MovieStageController implements MovieSrtDisplayer {

    private static final int MOVIE_CLICK_WARNING_SIZE = 60;
    //200 millis in nano
    private static final long SUBTITLE_UPDATE_SLEEP_DURATION = 200000000L;

    private static final String BLUE_HALF_TRANSPARENT_BACK_GROUND_STYLE = "-fx-background-color: rgba(0, 100, 100, 0.51); -fx-background-radius: 10;";
    private static final String TRANSPARENT_BACKGROUND_STYLE = "-fx-background-color: transparent;";
    public static final String OUTLINED_TEXT_STYLE = /*"color: #ff0;"+*/
//            "text-shadow: 3px 0 0 #000, 0 -3px 0 #000, 0 3px 0 #000, -3px 0 0 #000;";
//        "-fx-fill: lightseagreen;\n"+
        "-fx-stroke: black;\n"+
        "-fx-stroke-width: 2px;\n";


    @FXML
    private VBox movieVBox;
    @FXML
    private TextFlow movieTextFlow;
    @FXML
    private AnchorPane movieAnchorPane;



    private ImageView clickWarning;

    @Getter
    @Setter
    private FontBundle currentFont;

    private EventBus eventBus;

    private SrtFontManager srtFontManager;

    private Vector2D movieVBoxPos;

    private int currentFontSize;

    private ExecutionLimiter updateSubtitleExecutionLimiter;

    @Getter
    private SubtitleText lastSubtitleText;

    private DragResizeMod dragResizeMod;

    private Table<Node, EventHandler, EventType> eventHandlers;


    @Inject
    public MovieStageController(SrtFontManager srtFontManager,
                                EventBus eventBus,
                                @Named(PropertyFileKeys.SUBTITLE_POS) String movieVBoxPosString)
    {
        this.srtFontManager = srtFontManager;
        this.eventBus= eventBus;
        this.movieVBoxPos = loadMovieVBoxStartPos(movieVBoxPosString,getScreenBoundsVector());
        this.updateSubtitleExecutionLimiter = new ExecutionLimiter(SUBTITLE_UPDATE_SLEEP_DURATION,this::updateSubtitle);
        this.currentFontSize = (int) srtFontManager.getUserFontSize();
        this.currentFont = srtFontManager.loadDefaultFont();
        this.lastSubtitleText = new SubtitleText(new ArrayList<>(Collections.emptyList()));
    }

    private Vector2D loadMovieVBoxStartPos(String savedPosition, Vector2D screenBounds){
        //todo test
        try {
            Vector2D screenPos = new Vector2D(savedPosition);
            if(!Vector2D.isVectorInBounds(screenBounds.getX(),screenBounds.getY(),0,0,screenPos)){
                throw new IllegalArgumentException("read movieVBoxPos: "+ screenPos + " is out of screenbounds: " + screenBounds);
            }
            return new Vector2D(savedPosition);
        }catch (IllegalArgumentException | VectorDecodeException e){
            log.warn("could not load user movie vbox startPos, using center of screen instead", e);
            return new Vector2D(screenBounds.getX()/2,screenBounds.getY()/2);
        }
    }

    @Override
    public void displayNextClickCounts() {
        this.clickWarning.setVisible(true);
    }

    @Override
    public void hideNextClickCounts() {
        this.clickWarning.setVisible(false);
    }


    private void handleVBoxDoubleClick(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
                // double click
                log.debug("user double clicked movieText -> switching to settings srt displayer");
                eventBus.post(new SwitchSrtDisplayerEvent(SettingsSrtDisplayer.class));
            }
        }
    }

    private void updateSubtitle(){
        displaySubtitle(lastSubtitleText);
    }


    @Override
    public void setFontColor(Color color) {
        srtFontManager.setFontColor(color);
    }

    @Override
    public Color getFontColor() {
        return srtFontManager.getFontColor();
    }

    @Override
    public void displaySubtitle(SubtitleText subtitleText) {
        log.debug("asking javafx to display new subtitle on MovieStageController : " + subtitleText);
        lastSubtitleText = subtitleText;


        Platform.runLater(() -> {
            log.trace("displaying new subtitle: " + subtitleText);
            movieTextFlow.getChildren().clear();
            for(List<SubtitleSegment> subtitleSegments: subtitleText.getSubtitleSegments()){
                for(SubtitleSegment subtitleSegment: subtitleSegments){
                    Text text = new Text(subtitleSegment.getText());

                    if(subtitleSegment.getType().equals(SubtitleType.ITALIC)){
                        text.setFont(currentFont.getItalicFont());
                    }else {
                        text.setFont(currentFont.getRegularFont());
                    }
                    FontUtils.adjustTextSize(text,currentFontSize);

                    log.trace("setting fontcolor: "+ srtFontManager.getFontColor());
                    text.setFill(srtFontManager.getFontColor());
                    text.setStyle(OUTLINED_TEXT_STYLE);
                    movieTextFlow.getChildren().add(text);
                    movieTextFlow.getChildren().add(new Text(System.lineSeparator()));
                }
            }
        });
    }


    @FXML
    protected void fxmlInit() {
        checkNotNull(movieVBox);
        checkNotNull(movieTextFlow);
        checkNotNull(movieAnchorPane);
        movieAnchorPane.setBackground(Background.EMPTY);
        movieTextFlow.setBackground(Background.EMPTY);
        movieTextFlow.setPickOnBounds(true);

        movieVBox.setLayoutX(movieVBoxPos.getX());
        movieVBox.setLayoutY(movieVBoxPos.getY());

        eventHandlers = registerEventHandlers();

        clickWarning = loadImageView(movieVBox,
                "/images/finger.png",
                new Vector2D(MOVIE_CLICK_WARNING_SIZE,MOVIE_CLICK_WARNING_SIZE));
        clickWarning.setVisible(false);
    }

    private void onDraggedInPosition(MouseEvent mouseEvent){
        // is called when user selected a new position for the movieVBox
        Vector2D nodePos = new Vector2D(movieVBox.getLayoutX(),movieVBox.getLayoutY());
        eventBus.post(new UpdateSubtitlePosEvent(nodePos));
    }

    private void onMovieBoxResize(Node node, double h, double w, double deltaH, double deltaW){
        checkArgument(node==movieVBox);
        movieVBox.setStyle(BLUE_HALF_TRANSPARENT_BACK_GROUND_STYLE);
        movieVBox.setPrefHeight(h);
        movieVBox.setPrefWidth(w);
        this.currentFontSize=  ((int)(h+w)/2)/9;
        updateSubtitleExecutionLimiter.tryExecuting();
    }

    protected Table<Node, EventHandler, EventType> registerEventHandlers() {
        EventHandler<MouseEvent> movieBoxMouseEnteredHandler =
                event -> movieVBox.setStyle(BLUE_HALF_TRANSPARENT_BACK_GROUND_STYLE);

        EventHandler<MouseEvent> movieBoxMouseExitedHandler =
                event -> movieVBox.setStyle(TRANSPARENT_BACKGROUND_STYLE);

        movieVBox.setOnMouseEntered(movieBoxMouseEnteredHandler);
        movieVBox.setOnMouseExited(movieBoxMouseExitedHandler);

        dragResizeMod = DragResizeMod.builder()
                .node(movieVBox)
                .mouseReleasedFunction(this::onDraggedInPosition)
                .mouseClickedFunction(this::handleVBoxDoubleClick)
                .resizeFunction(this::onMovieBoxResize)
                .nodeHeight(movieVBox.getHeight())
                .nodeWidth(movieVBox.getWidth())
                .build();
        dragResizeMod.makeResizableAndDraggable();

        Table<Node, EventHandler, EventType> resultTable =  HashBasedTable.create();
        resultTable.put(movieVBox,movieBoxMouseEnteredHandler,MouseEvent.MOUSE_ENTERED);
        resultTable.put(movieVBox,movieBoxMouseExitedHandler,MouseEvent.MOUSE_EXITED);
        return resultTable;
    }

//    @Override
//    public void onUnregisterEventHandlers() {
//        super.onUnregisterEventHandlers();
//        dragResizeMod.unregisterListeners();
//    }

}
