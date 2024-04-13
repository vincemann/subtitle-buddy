package io.github.vincemann.subtitlebuddy.util.fx;

import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import lombok.Builder;
import lombok.NonNull;

import java.util.*;

/**
 *  ************* How to use ************************
 *
 * Rectangle rectangle = new Rectangle(50, 50);
 * rectangle.setFill(Color.BLACK);
 * DragResizeMod.makeResizableAndDraggable(rectangle, null);
 *
 * Pane root = new Pane();
 * root.getChildren().add(rectangle);
 *
 * primaryStage.setScene(new Scene(root, 300, 275));
 * primaryStage.show();
 *
 * ************* OnDragResizeEventListener **********
 *
 * You need to override OnDragResizeEventListener and
 * 1) preform out of main field bounds check
 * 2) make changes to the node
 * (this class will not change anything in node coordinates)
 *
 * There is defaultListener and it works only with Canvas nad Rectangle
 *
 * original author : Peter Varren
 * modified by io.github.vincemann
 */

public class DragResizeMod {
    public interface OnDragResizeEventListener {
        void onDrag(Node node, double x, double y, double h, double w);

        void onResize(Node node, double x, double y, double h, double w, double deltaH, double deltaW);
    }

    private final OnDragResizeEventListener defaultListener = new OnDragResizeEventListener() {
        @Override
        public void onDrag(Node node, double x, double y, double h, double w) {
            /*
            // TODO find generic way to get parent width and height of any node
            // can perform out of bounds check here if you know your parent size
            if (x > width - w ) x = width - w;
            if (y > height - h) y = height - h;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
            */
            setNodeSize(node, x, y, h, w,0,0);
        }

        @Override
        public void onResize(Node node, double x, double y, double h, double w, double deltaH, double deltaW) {
            /*
            // TODO find generic way to get parent width and height of any node
            // can perform out of bounds check here if you know your parent size
            if (w > width - x) w = width - x;
            if (h > height - y) h = height - y;
            if (x < 0) x = 0;
            if (y < 0) y = 0;
            */
            setNodeSize(node,x,y,h,w,deltaH,deltaW);
        }

        private void setNodeSize(Node node, double x, double y, double h, double w, double deltaH, double deltaW) {
            node.setLayoutX(x);
            node.setLayoutY(y);
            // TODO find generic way to set width and height of any node
            // here we cant set height and width to node directly.
            resizeFunction.onResize(node,h,w, deltaH, deltaW);
        }
    };

    public static enum S {
        DEFAULT,
        DRAG,
        NW_RESIZE,
        SW_RESIZE,
        NE_RESIZE,
        SE_RESIZE,
        E_RESIZE,
        W_RESIZE,
        N_RESIZE,
        S_RESIZE;
    }


    private double clickX, clickY, nodeX, nodeY;

    private S state = S.DEFAULT;

    private Node node;
    private OnDragResizeEventListener listener = defaultListener;
    private MouseEventFunction mouseClickedFunction;
    private MouseEventFunction mouseDraggedFunction;
    private MouseEventFunction mouseMovedFunction;
    private MouseEventFunction mouseReleasedFunction;
    private ResizeFunction resizeFunction;

    private Map<EventHandler<MouseEvent>,EventType<MouseEvent>> registeredEventHandlers;

    private static final int MARGIN = 8;
    private static final double MIN_W = 30;
    private static final double MIN_H = 20;
    private double nodeW;
    private double nodeH;

    @Builder
    public DragResizeMod(@NonNull Node node, OnDragResizeEventListener listener, MouseEventFunction mouseClickedFunction, MouseEventFunction mouseDraggedFunction, MouseEventFunction mouseMovedFunction, MouseEventFunction mouseReleasedFunction, ResizeFunction resizeFunction, @NonNull Double nodeWidth,@NonNull Double nodeHeight) {
        this.node=node;
        this.registeredEventHandlers = new HashMap<>();
        if(listener!=null)
            this.listener = listener;
        this.mouseClickedFunction = mouseClickedFunction;
        this.mouseDraggedFunction = mouseDraggedFunction;
        this.mouseMovedFunction = mouseMovedFunction;
        this.mouseReleasedFunction = mouseReleasedFunction;
        this.resizeFunction=resizeFunction;
    }


    public void unregisterListeners(){
        for(Map.Entry<EventHandler<MouseEvent>,EventType<MouseEvent>> entry : registeredEventHandlers.entrySet()){
            node.removeEventHandler(entry.getValue(),entry.getKey());
        }
    }


    /**
     * registers the listeners
     */
    public void makeResizableAndDraggable() {

        EventHandler<MouseEvent> mousePressedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mousePressed(event);
                if(mouseClickedFunction!=null)
                    mouseClickedFunction.handleMouseEvent(event);
            }
        };

        EventHandler<MouseEvent> mouseDraggedHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseDragged(event);
                if(mouseDraggedFunction!=null)
                    mouseDraggedFunction.handleMouseEvent(event);
            }
        };

        EventHandler<MouseEvent> mouseMovedHandler  = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseOver(event);
                if(mouseMovedFunction!=null)
                    mouseMovedFunction.handleMouseEvent(event);
            }
        };

        EventHandler<MouseEvent> mouseReleasedHandler  = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mouseReleased(event);
                if(mouseReleasedFunction!=null)
                    mouseReleasedFunction.handleMouseEvent(event);
            }
        };

        node.setOnMousePressed(mousePressedHandler);
        node.setOnMouseDragged(mouseDraggedHandler);
        node.setOnMouseMoved(mouseMovedHandler);
        node.setOnMouseReleased(mouseReleasedHandler);

        this.registeredEventHandlers.put(mousePressedHandler,MouseEvent.MOUSE_CLICKED);
        this.registeredEventHandlers.put(mouseDraggedHandler,MouseEvent.MOUSE_DRAGGED);
        this.registeredEventHandlers.put(mouseMovedHandler,MouseEvent.MOUSE_MOVED);
        this.registeredEventHandlers.put(mouseReleasedHandler,MouseEvent.MOUSE_RELEASED);
    }

    protected void mouseReleased(MouseEvent event) {
        node.setCursor(Cursor.DEFAULT);
        state = S.DEFAULT;
    }

    protected void mouseOver(MouseEvent event) {
        S state = currentMouseState(event);
        Cursor cursor = getCursorForState(state);
        node.setCursor(cursor);
    }

    private S currentMouseState(MouseEvent event) {
        S state = S.DEFAULT;
        boolean left = isLeftResizeZone(event);
        boolean right = isRightResizeZone(event);
        boolean top = isTopResizeZone(event);
        boolean bottom = isBottomResizeZone(event);

        if (left && top) state = S.NW_RESIZE;
        else if (left && bottom) state = S.SW_RESIZE;
        else if (right && top) state = S.NE_RESIZE;
        else if (right && bottom) state = S.SE_RESIZE;
        else if (right) state = S.E_RESIZE;
        else if (left) state = S.W_RESIZE;
        else if (top) state = S.N_RESIZE;
        else if (bottom) state = S.S_RESIZE;
        else if (isInDragZone(event)) state = S.DRAG;

        return state;
    }

    private static Cursor getCursorForState(S state) {
        switch (state) {
            case NW_RESIZE:
                return Cursor.NW_RESIZE;
            case SW_RESIZE:
                return Cursor.SW_RESIZE;
            case NE_RESIZE:
                return Cursor.NE_RESIZE;
            case SE_RESIZE:
                return Cursor.SE_RESIZE;
            case E_RESIZE:
                return Cursor.E_RESIZE;
            case W_RESIZE:
                return Cursor.W_RESIZE;
            case N_RESIZE:
                return Cursor.N_RESIZE;
            case S_RESIZE:
                return Cursor.S_RESIZE;
            default:
                return Cursor.DEFAULT;
        }
    }


    protected void mouseDragged(MouseEvent event) {

        if (listener != null) {
            double mouseX = parentX(event.getX());
            double mouseY = parentY(event.getY());
            if (state == S.DRAG) {
                listener.onDrag(node, mouseX - clickX, mouseY - clickY, nodeH, nodeW);
            } else if (state != S.DEFAULT) {
                //resizing
                double newX = nodeX;
                double newY = nodeY;
                double newH = nodeH;
                double newW = nodeW;
                double deltaW = 0;
                double deltaH = 0;

                // Right Resize
                if (state == S.E_RESIZE || state == S.NE_RESIZE || state == S.SE_RESIZE) {
                    newW = mouseX - nodeX;
                }
                // Left Resize
                if (state == S.W_RESIZE || state == S.NW_RESIZE || state == S.SW_RESIZE) {
                    newX = mouseX;
                    newW = nodeW + nodeX - newX;
                }

                // Bottom Resize
                if (state == S.S_RESIZE || state == S.SE_RESIZE || state == S.SW_RESIZE) {
                    newH = mouseY - nodeY;
                }
                // Top Resize
                if (state == S.N_RESIZE || state == S.NW_RESIZE || state == S.NE_RESIZE) {
                    newY = mouseY;
                    newH = nodeH + nodeY - newY;
                }

                //min valid rect Size Check
                if (newW < MIN_W) {
                    if (state == S.W_RESIZE || state == S.NW_RESIZE || state == S.SW_RESIZE)
                        newX = newX - MIN_W + newW;
                    newW = MIN_W;
                }

                if (newH < MIN_H) {
                    if (state == S.N_RESIZE || state == S.NW_RESIZE || state == S.NE_RESIZE)
                        newY = newY + newH - MIN_H;
                    newH = MIN_H;
                }
                deltaW = nodeW-newW;
                deltaH = nodeH - newH;

                listener.onResize(node, newX, newY, newH, newW,deltaH,deltaW);
            }
        }
    }

    protected void mousePressed(MouseEvent event) {

        if (isInResizeZone(event)) {
            setNewInitialEventCoordinates(event);
            state = currentMouseState(event);
        } else if (isInDragZone(event)) {
            setNewInitialEventCoordinates(event);
            state = S.DRAG;
        } else {
            state = S.DEFAULT;
        }
    }

    private void setNewInitialEventCoordinates(MouseEvent event) {
        nodeX = nodeX();
        nodeY = nodeY();
        nodeH = nodeH();
        nodeW = nodeW();
        clickX = event.getX();
        clickY = event.getY();
    }

    private boolean isInResizeZone(MouseEvent event) {
        return isLeftResizeZone(event) || isRightResizeZone(event)
                || isBottomResizeZone(event) || isTopResizeZone(event);
    }

    private boolean isInDragZone(MouseEvent event) {
        double xPos = parentX(event.getX());
        double yPos = parentY(event.getY());
        double nodeX = nodeX() + MARGIN;
        double nodeY = nodeY() + MARGIN;
        double nodeX0 = nodeX() + nodeW() - MARGIN;
        double nodeY0 = nodeY() + nodeH() - MARGIN;

        return (xPos > nodeX && xPos < nodeX0) && (yPos > nodeY && yPos < nodeY0);
    }

    private boolean isLeftResizeZone(MouseEvent event) {
        return intersect(0, event.getX());
    }

    private boolean isRightResizeZone(MouseEvent event) {
        return intersect(nodeW(), event.getX());
    }

    private boolean isTopResizeZone(MouseEvent event) {
        return intersect(0, event.getY());
    }

    private boolean isBottomResizeZone(MouseEvent event) {
        return intersect(nodeH(), event.getY());
    }

    private boolean intersect(double side, double point) {
        return side + MARGIN > point && side - MARGIN < point;
    }

    private double parentX(double localX) {
        return nodeX() + localX;
    }

    private double parentY(double localY) {
        return nodeY() + localY;
    }

    private double nodeX() {
        return node.getBoundsInParent().getMinX();
    }

    private double nodeY() {
        return node.getBoundsInParent().getMinY();
    }

    private double nodeW() {
        return node.getBoundsInParent().getWidth();
    }

    private double nodeH() {
        return node.getBoundsInParent().getHeight();
    }
}
