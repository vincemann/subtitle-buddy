package io.github.vincemann.subtitlebuddy.util.fx;

import javafx.scene.Node;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import lombok.Builder;
import lombok.Getter;

public class DraggableNode {

    /**
            * The current x coordinate of the node.
	 */
    private double x = 0;

    /**
     * The current y coordinate of the node.
     */
    private double y = 0;

    /**
     * The current mouse x coordinate when dragging.
     */
    private double mouseX = 0;

    /**
     * The current mouse y coordinate when dragging.
     */
    private double mouseY = 0;

    /**
     * The node that will be draggable.
     */
    @Getter
    private Node node;

    @Getter
    private MouseEventFunction mouseClickedFunction;
    @Getter
    private MouseEventFunction mouseDraggedFunction;

    private MouseEventFunction mouseReleasedFunction;


    /**
     * JAVAFX:
     * creates a mouseclickedhandler and a mousedragged handler.
     * Dont add your own handlers of that type to the node! Instead pass the functions that contain your eventHandling Behavior as arguments!
     *
     * @param node   the node that should be dragged around
     * @param mouseClickedFunction      provide the additional function that should be called when the mousepressed handler is active   when null no additional func gets called
     * @param mouseDraggedFunction      provide the additional function that should be called when the mousedragged handler is active   when null no additional func gets called
     */
    @Builder
    private DraggableNode(Node node, MouseEventFunction mouseClickedFunction, MouseEventFunction mouseDraggedFunction, MouseEventFunction mouseReleasedFunction) {
        this.node = node;
        this.mouseClickedFunction = mouseClickedFunction;
        this.mouseDraggedFunction=mouseDraggedFunction;
        this.mouseReleasedFunction = mouseReleasedFunction;
    }

    /**
     * call this function to map the eventHandlers to your node thus making the node dragable
     */
    public void makeDraggable(){
        node.setOnMouseClicked(this::handleMouseClicked);
        node.setOnMouseDragged(this::handleMouseDrag);
        node.setOnMouseReleased(this::handleMouseReleased);
    }



    private void handleMouseClicked(MouseEvent event){
        if (event.getButton() == MouseButton.PRIMARY) {
            // get the current mouse coordinates according to the scene.
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();

            // get the current coordinates of the draggable node.
            x = node.getLayoutX()+(node.getBoundsInParent().getWidth()/2.0);
            y = node.getLayoutY()+(node.getBoundsInParent().getHeight()/2.0);
        }
        //call the function the user wants to be called when mouse is pressed
        if(mouseClickedFunction !=null) {
            mouseClickedFunction.handleMouseEvent(event);
        }
    }

    private void handleMouseReleased(MouseEvent event){
        if(mouseReleasedFunction !=null) {
            mouseReleasedFunction.handleMouseEvent(event);
        }
    }

    private void handleMouseDrag(MouseEvent event){
        if (event.getButton() == MouseButton.PRIMARY) {
            // find the delta coordinates by subtracting the new mouse
            // coordinates with the old.
            double deltaX = event.getSceneX() - mouseX;
            double deltaY = event.getSceneY() - mouseY;

            // add the delta coordinates to the node coordinates.
            x += deltaX;
            y += deltaY;

            // set the layout for the draggable node.
            node.setLayoutX(x -(node.getBoundsInParent().getWidth()/2.0));
            node.setLayoutY(y -(node.getBoundsInParent().getHeight()/2.0));

            // get the latest mouse coordinate.
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();

        }
        //call the function the user wants to be called when mouse is dragged
        if(mouseDraggedFunction!=null){
            mouseDraggedFunction.handleMouseEvent(event);
        }
    }

}
