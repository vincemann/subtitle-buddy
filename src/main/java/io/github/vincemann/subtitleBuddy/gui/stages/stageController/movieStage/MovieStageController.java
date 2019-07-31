package io.github.vincemann.subtitleBuddy.gui.stages.stageController.movieStage;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitleBuddy.config.propertiesFile.PropertyFileKeys;
import io.github.vincemann.subtitleBuddy.config.uiStringsFile.UIStringsFileKeys;
import io.github.vincemann.subtitleBuddy.classpathFileFinder.ReadOnlyClassPathFileFinder;
import io.github.vincemann.subtitleBuddy.events.MovieTextPositionChangedEvent;
import io.github.vincemann.subtitleBuddy.events.SwitchSrtDisplayerEvent;
import io.github.vincemann.subtitleBuddy.gui.srtDisplayer.MovieSrtDisplayer;
import io.github.vincemann.subtitleBuddy.gui.srtDisplayer.SettingsSrtDisplayer;
import io.github.vincemann.subtitleBuddy.gui.stages.stageController.AbstractStageController;
import io.github.vincemann.subtitleBuddy.srt.SrtFont;
import io.github.vincemann.subtitleBuddy.srt.SubtitleSegment;
import io.github.vincemann.subtitleBuddy.srt.SubtitleText;
import io.github.vincemann.subtitleBuddy.srt.SubtitleType;
import io.github.vincemann.subtitleBuddy.srt.font.fontManager.SrtFontManager;
import io.github.vincemann.subtitleBuddy.util.vec2d.VectorDecodeException;
import io.github.vincemann.subtitleBuddy.gui.stages.StageState;
import io.github.vincemann.subtitleBuddy.util.ExecutionLimiter;
import io.github.vincemann.subtitleBuddy.util.FxUtils.DragResizeMod;
import io.github.vincemann.subtitleBuddy.util.vec2d.Vector2D;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
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
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Log4j
@Singleton
@io.github.vincemann.subtitleBuddy.gui.stages.MovieStageController
public class MovieStageController extends AbstractStageController implements MovieSrtDisplayer {

    private static final String MOVIE_STAGE_FXML_FILE_PATH = "/movieStage.fxml";
    private static final int MOVIE_CLICK_WARNING_SIZE = 60;
    //200 millis in nan0
    private static final long SUBTITLE_UPDATE_SLEEP_DURATION = 200000000L;


    private static final String BLUE_HALF_TRANSPARENT_BACK_GROUND_STYLE = "-fx-background-color: rgba(0, 100, 100, 0.51); -fx-background-radius: 10;";
    private static final String TRANSPARENT_BACKGROUND_STYLE = "-fx-background-color: transparent;";

    @FXML
    private VBox movieVBox;
    @FXML
    private TextFlow movieTextFlow;
    @FXML
    private AnchorPane movieAnchorPane;



    private ImageView clickWarning;



    @Getter
    @Setter
    private SrtFont currentFont;

    private EventBus eventBus;

    private SrtFontManager srtFontManager;

    private Vector2D movieVBoxPos;

    private int currentFontSize;

    private ExecutionLimiter updateSubtitleExecutionLimiter;

    @Getter
    private SubtitleText lastSubtitleText;

    private DragResizeMod dragResizeMod;


    @Inject
    public MovieStageController(@Named(UIStringsFileKeys.MOVIE_STAGE_TITLE_KEY) String title,
                                SrtFontManager srtFontManager, EventBus eventBus,
                                @Named(PropertyFileKeys.USER_MOVIE_TEXT_POSITION_KEY) String movieVBoxPosString,
                                ReadOnlyClassPathFileFinder readOnlyClassPathFileFinder,
                                @Named(PropertyFileKeys.CLICK_WARNING_IMAGE_PATH_KEY) String clickWarningImagePath)
            throws IOException {
        super(readOnlyClassPathFileFinder.findFileOnClassPath(MOVIE_STAGE_FXML_FILE_PATH).getFile().toURI().toURL(), title, getScreenBoundsVector());
        this.srtFontManager = srtFontManager;
        this.eventBus= eventBus;
        this.movieVBoxPos = loadMovieVBoxStartPos(movieVBoxPosString,getSize());
        this.updateSubtitleExecutionLimiter = new ExecutionLimiter(SUBTITLE_UPDATE_SLEEP_DURATION,this::updateSubtitle);
        createStage(this);
        this.clickWarning = createImageView(movieVBox, readOnlyClassPathFileFinder.findFileOnClassPath(clickWarningImagePath).getFile(),new Vector2D(MOVIE_CLICK_WARNING_SIZE,MOVIE_CLICK_WARNING_SIZE));
        constructorInit();
    }

    private Vector2D loadMovieVBoxStartPos(String s, Vector2D screenBounds){
        //todo test
        try {
            Vector2D screenPos = new Vector2D(s);
            if(!Vector2D.isVectorInBounds(screenBounds.getX(),screenBounds.getY(),0,0,screenPos)){
                throw new IllegalArgumentException("read movieVBoxPos: "+ screenPos + " is out of screenbounds: " + screenBounds);
            }
            return new Vector2D(s);
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

    private void constructorInit(){
        currentFontSize = (int) srtFontManager.getUserFontSize();
        currentFont = srtFontManager.loadDefaultFont();
        lastSubtitleText = new SubtitleText(new ArrayList<>(Collections.emptyList()));
        movieVBox.setLayoutX(movieVBoxPos.getX());
        movieVBox.setLayoutY(movieVBoxPos.getY());
        clickWarning.setVisible(false);
    }

    private static Vector2D getScreenBoundsVector(){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        return new Vector2D(screenBounds.getWidth()-1,screenBounds.getHeight()-1);
    }

    private void handleVBoxDoubleClick(MouseEvent event){
        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
                //doppelklick
                log.debug("user doubleclicked movieText -> switching to SettingsSrtDisplayer");
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
        lastSubtitleText =subtitleText;


        Platform.runLater(() -> {
            log.trace("displaying new subtitle: " + subtitleText);
            movieTextFlow.getChildren().clear();
            for(List<SubtitleSegment> subtitleSegments: subtitleText.getSubtitleSegments()){
                for(SubtitleSegment subtitleSegment: subtitleSegments){
                    Text text = new Text(subtitleSegment.getText());

                    if(subtitleSegment.getSubtitleType().equals(SubtitleType.ITALIC)){
                        text.setFont(currentFont.getItalicFont());
                    }else {
                        text.setFont(currentFont.getRegularFont());
                    }
                    adjustTextSize(text,currentFontSize);

                    log.trace("setting fontcolor: "+ srtFontManager.getFontColor());
                    text.setFill(srtFontManager.getFontColor());
                    movieTextFlow.getChildren().add(text);
                    movieTextFlow.getChildren().add(new Text(System.lineSeparator()));
                }
            }
        });
    }


    @Override
    protected void onFXMLInitialize() {
        super.onFXMLInitialize();
        checkNotNull(movieVBox);
        checkNotNull(movieTextFlow);
        checkNotNull(movieAnchorPane);
        movieAnchorPane.setBackground(Background.EMPTY);
        movieTextFlow.setBackground(Background.EMPTY);
        movieTextFlow.setPickOnBounds(true);

        movieVBox.setLayoutX(movieVBoxPos.getX());
        movieVBox.setLayoutY(movieVBoxPos.getY());
    }

    @Override
    protected void onStageCreate(Stage stage) {
        super.onStageCreate(stage);
        stage.getScene().setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setAlwaysOnTop(true);
    }

    @Override
    public void close() {
        closeStage();
    }

    private void onDraggedInPosition(MouseEvent mouseEvent){
        //wird gecallt wenn der user sich seine position ausgesucht hat
        Vector2D nodePos = new Vector2D(movieVBox.getLayoutX(),movieVBox.getLayoutY());
        eventBus.post(new MovieTextPositionChangedEvent(nodePos));
    }

    private void onMovieBoxResize(Node node, double h, double w, double deltaH, double deltaW){
        checkArgument(node==movieVBox);
        movieVBox.setStyle(BLUE_HALF_TRANSPARENT_BACK_GROUND_STYLE);
        movieVBox.setPrefHeight(h);
        movieVBox.setPrefWidth(w);
        this.currentFontSize=  ((int)(h+w)/2)/9;
        updateSubtitleExecutionLimiter.tryExecuting();
    }




    @Override
    public void open() {
        showStage();
    }

    @Override
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

    @Override
    public void onUnregisterEventHandlers() {
        super.onUnregisterEventHandlers();
        dragResizeMod.unregisterListeners();
    }

    @Override
    public boolean isDisplaying() {
        return getStageState().equals(StageState.OPEN);
    }
}
