package io.github.vincemann.subtitlebuddy.util.fx;

import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import lombok.Builder;
import lombok.Getter;


/**
 * {@link ResizeableNode} can be used to add mouse listeners to a {@link Region}
 * and make it resizable by the user by clicking and dragging the border in the
 * same way as a window.
 */
public class ResizeableNode {

    private static final int DEFAULT_RESIZE_MARGIN = 10;


    /**
     * The margin around the control that a user can click in to start resizing
     * the region.
     */
    private int resizeMargin = DEFAULT_RESIZE_MARGIN;

    @Getter
    private final Region region;

    private double y;

    private double x;

    private boolean initMinHeight;

    private boolean initMinWidth;

    private boolean draggableZoneX, draggableZoneY;

    private boolean dragging;

    @Getter
    private MouseEventFunction mouseDraggedFunction;

    @Getter
    private MouseEventFunction mousePressedFunction;

    @Getter
    private MouseEventFunction mouseReleasedFunction;

    @Getter
    private MouseEventFunction mouseMovedFunction;


    @Builder
    private ResizeableNode(Region region, int resizeMargin, MouseEventFunction mouseDraggedFunction, MouseEventFunction mousePressedFunction, MouseEventFunction mouseReleasedFunction, MouseEventFunction mouseMovedFunction) {
        this.region = region;
        this.resizeMargin = resizeMargin;
        this.mouseDraggedFunction = mouseDraggedFunction;
        this.mousePressedFunction = mousePressedFunction;
        this.mouseReleasedFunction = mouseReleasedFunction;
        this.mouseMovedFunction = mouseMovedFunction;
    }


    public void makeResizable() {
        region.setOnMousePressed(event -> {
            mousePressed(event);
            if (mousePressedFunction != null)
                mousePressedFunction.handleMouseEvent(event);
        });
        region.setOnMouseDragged(event -> mouseDragged(event));
        region.setOnMouseMoved(event -> {
            mouseOver(event);
            if (mouseMovedFunction != null)
                mouseMovedFunction.handleMouseEvent(event);
        });
        region.setOnMouseReleased(event -> {
            mouseReleased(event);
            if (mouseReleasedFunction != null)
                mouseReleasedFunction.handleMouseEvent(event);
        });
    }


    protected void mouseReleased(MouseEvent event) {
        dragging = false;
        region.setCursor(Cursor.DEFAULT);
    }

    protected void mouseOver(MouseEvent event) {
        if (isInDraggableZone(event) || dragging) {
            if (draggableZoneY) {
                region.setCursor(Cursor.S_RESIZE);
            }

            if (draggableZoneX) {
                region.setCursor(Cursor.E_RESIZE);
            }

        } else {
            region.setCursor(Cursor.DEFAULT);
        }
    }


    //had to use 2 variables for the controll, tried without, had unexpected behaviour (going big was ok, going small nope.)
    protected boolean isInDraggableZone(MouseEvent event) {
        draggableZoneY = (boolean) (event.getY() > (region.getHeight() - resizeMargin));
        draggableZoneX = (boolean) (event.getX() > (region.getWidth() - resizeMargin));
        return (draggableZoneY || draggableZoneX);
    }

    protected void mouseDragged(MouseEvent event) {
        if (!dragging) {
            if (mouseDraggedFunction != null)
                mouseDraggedFunction.handleMouseEvent(event);
            return;
        }

        if (draggableZoneY) {
            double mousey = event.getY();

            double newHeight = region.getMinHeight() + (mousey - y);

            region.setMinHeight(newHeight);

            y = mousey;
        }

        if (draggableZoneX) {
            double mousex = event.getX();

            double newWidth = region.getMinWidth() + (mousex - x);

            region.setMinWidth(newWidth);

            x = mousex;

        }

    }

    protected void mousePressed(MouseEvent event) {

        // ignore clicks outside of the draggable margin
        if (!isInDraggableZone(event)) {
            return;
        }

        dragging = true;

        // make sure that the minimum height is set to the current height once,
        // setting a min height that is smaller than the current height will
        // have no effect
        if (!initMinHeight) {
            region.setMinHeight(region.getHeight());
            initMinHeight = true;
        }

        y = event.getY();

        if (!initMinWidth) {
            region.setMinWidth(region.getWidth());
            initMinWidth = true;
        }

        x = event.getX();
    }
}