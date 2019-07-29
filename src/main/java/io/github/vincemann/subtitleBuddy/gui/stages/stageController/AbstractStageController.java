package io.github.vincemann.subtitleBuddy.gui.stages.stageController;

import com.google.common.collect.Table;
import io.github.vincemann.subtitleBuddy.gui.stages.StageState;
import io.github.vincemann.subtitleBuddy.util.vec2d.Vector2D;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

@Log4j
@NoArgsConstructor
public abstract class AbstractStageController {

    private StageState stageState = StageState.UNINITIALIZED;
    private Stage stage;
    private Table<Node,EventHandler,EventType> nodeEventHandlerEventTable;
    private String title;
    private Vector2D size;
    private URL fxmlUrl;

    /**
     * root of window must be a pane
     * @param fxmlUrl
     * @param title
     * @param size
     */
    public AbstractStageController(URL fxmlUrl, String title, Vector2D size) {
        //nötig weil: https://stackoverflow.com/questions/29302837/javafx-platform-runlater-never-running
        Platform.setImplicitExit(false);
        this.title=title;
        this.size=size;
        this.fxmlUrl=fxmlUrl;
    }

    protected ImageView createImageView(Pane parent, File imageFile, Vector2D size) throws FileNotFoundException {
        //klappt nur über diesen umweg, javafx zeigt die imageview nicht an wenn ich die im scenebuilder erstelle und dann über DI reinlinke..
        // deswegen über den Umweg mit dem parent
        Image clickWarningImage = new Image(new FileInputStream(imageFile));
        ImageView imageView = new javafx.scene.image.ImageView(clickWarningImage);
        imageView.setFitHeight(size.getX());
        imageView.setFitWidth(size.getY());
        imageView.setCache(true);
        parent.getChildren().add(imageView);
        return imageView;
    }

    protected final void createStage(@NonNull Object controllerInstance) throws IOException {
        if(!getStageState().equals(StageState.UNINITIALIZED)){
            throw new IllegalStateException("Stage is already initialized");
        }
        this.stage = createStage(title,size,fxmlUrl,controllerInstance);
        registerStageClosedListener(this.stage);
        onStageCreate(this.stage);
    }

    protected final void createStage(@NonNull Object controllerInstance,@NonNull Stage stage)
            throws IOException {
        if(!getStageState().equals(StageState.UNINITIALIZED)){
            throw new IllegalStateException("Stage is already initialized");
        }
        this.stage = createSceneOfStage(stage,title,size,fxmlUrl,controllerInstance);
        //todo root muss ein pane sein, ist immer so, aber ist irgendwie ungeil, sonst gibts nur nh unmodifyable children list
        //resizeNodesAccordingToDpi((Pane) stage.getScene().getRoot());
        registerStageClosedListener(this.stage);
        onStageCreate(this.stage);
    }


    protected synchronized void showStage(){
        log.debug("trying to show stage of controller " + getClass().getSimpleName());
        if(getStageState().equals(StageState.OPEN)){
            throw new IllegalStateException("Stage is already open");
        }
        if(getStageState().equals(StageState.UNINITIALIZED)){
            throw new IllegalStateException("Stage was not initialized yet");
        }
        checkState(stage!=null,"Stage was null");
        Platform.runLater(() -> {
            log.debug("Java FX Framework displays stage");
            this.stage.show();
            this.stage.toFront();
        });


        this.nodeEventHandlerEventTable = registerEventHandlers();
        setStageState(StageState.OPEN);
        onShowStage();
        log.debug("successfully opened stage of controller: " + getClass().getSimpleName());
    }

    @FXML
    public final void initialize(){
        //create Stage ist hier noch nicht komplett passiert,
        // initialize wird sofort nach fxmlLoader.load() aufgerufen!
        //-> stage is still null in this call
        checkState(getStageState().equals(StageState.UNINITIALIZED));
        onFXMLInitialize();

        log.trace("registering eventhandler of "+getClass().getSimpleName());
        this.nodeEventHandlerEventTable = checkNotNull(registerEventHandlers());
    }

    protected synchronized void closeStage(){
        log.debug("trying to close stage of controller " + getClass().getSimpleName());
        if(!getStageState().equals(StageState.OPEN)){
            throw new IllegalStateException("Stage wasnt open, state was: " + getStageState());
        }
        checkNotNull(stage);

        Platform.runLater(() -> this.stage.hide());  //ist das selbe wie close
        unregisterEventHandlers();
        setStageState(StageState.CLOSED);
    }


    private Stage createStage(String title, Vector2D size, URL fxmlUrl, Object controllerInstance)
            throws IOException {
        if(!stageState.equals(StageState.UNINITIALIZED)){
            throw new IllegalStateException("Stage is already initialized");
        }
        log.trace("creating Stage of "+getClass().getSimpleName());
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        fxmlLoader.setController(controllerInstance);
        Parent parent = fxmlLoader.load();
        Stage resultStage = new Stage();
        resultStage.setTitle(title);
        resultStage.setScene(new Scene(parent, size.getX(), size.getY()));
        setStageState(StageState.INITIALIZED);
        return resultStage;
    }

    /*private void resizeNodesAccordingToDpi(Pane parent) {
        for (Node component : parent.getChildren()) {

            double oldScaleX = component.getScaleX();
            double oldScaleY = component.getScaleY();
            double newScaleX = WindowUtils.scaleAccordingToDPI(oldScaleX);
            double newScaleY = WindowUtils.scaleAccordingToDPI(oldScaleY);
            double scaleXDelta = newScaleX-oldScaleX;
            double scaleYDelta = newScaleY-oldScaleY;
            component.setScaleX(newScaleX);
            component.setScaleY(newScaleY);
            component.setLayoutX(component.getLayoutX()*newScaleX);
            component.setLayoutY(component.getLayoutY()*newScaleY);

            //component.setLayoutY(WindowUtils.scaleAccordingToDPI(component.getLayoutY()));
            if(component instanceof Text){
                double oldTextSize = ((Text) component).getFont().getSize();
                double newTextSize = WindowUtils.scaleAccordingToDPI(oldTextSize);
                String oldStyle = ((Text) component).getStyle();
                String newStyle = FontUtils.resizeStyle(oldStyle,newTextSize);
                ((Text) component).setStyle(newStyle);
            }
        }
    }*/

    private Stage createSceneOfStage(Stage stage,String title, Vector2D size,
                                     URL fxmlUrl, Object controllerInstance) throws IOException{
        if(!stageState.equals(StageState.UNINITIALIZED)){
            throw new IllegalStateException("Stage is already initialized");
        }
        log.trace("creating Scene of "+getClass().getSimpleName());
        //todo change to original fxmlLoader
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlUrl);
        fxmlLoader.setController(controllerInstance);
        Parent parent = fxmlLoader.load();
        stage.setTitle(title);
        stage.setScene(new Scene(parent, size.getX(), size.getY()));
        setStageState(StageState.INITIALIZED);
        return stage;
    }

    protected abstract Table<Node,EventHandler,EventType> registerEventHandlers();

    private void unregisterEventHandlers(){
        log.trace("unregistering eventHandlers from " + getClass().getSimpleName());
        for(Table.Cell<Node,EventHandler,EventType> tableCell:  nodeEventHandlerEventTable.cellSet()){
            Node node = tableCell.getRowKey();
            //lässt sich leider nicht ändern mit dem raw type hier..
            node.removeEventHandler(tableCell.getValue(),tableCell.getColumnKey());
        }
        onUnregisterEventHandlers();
    }

    private void registerStageClosedListener(@NonNull Stage stage){
        log.trace("registering stage closed listener of "+ getClass().getSimpleName());
        stage.setOnCloseRequest(event -> {
            log.debug("OptionsWindow closed");
            closeStage();
            onStageClose();
        });
    }


    public void onUnregisterEventHandlers(){}

    private synchronized void setStageState(StageState stageState){
        this.stageState=stageState;
    }


    /**
     * nur benutzen wenn die gegebenen methoden nicht reichen
     * @return
     */
    public synchronized Stage getStage() {
        if(getStageState().equals(StageState.UNINITIALIZED)){
            throw new IllegalStateException("stage is not yet initialized");
        }
        return stage;
    }



    public synchronized StageState getStageState() {
        return stageState;
    }

    protected void onStageClose(){}

    /**
     * called when FXML initialize gets called, before this implementation of it -> bevor registereventHandlers
     */
    protected void onFXMLInitialize(){}

    /**
     * wird nach @FXML initialize aufgerufen, wenn die stage erstellt wurde und damit auch nach der registerEventhandlers
     * @param stage
     */
    protected void onStageCreate(Stage stage){ }

    protected void onShowStage(){}

    protected Vector2D getSize() {
        return size;
    }


}
