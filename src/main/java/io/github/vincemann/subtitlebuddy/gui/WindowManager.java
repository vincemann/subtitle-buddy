package io.github.vincemann.subtitlebuddy.gui;

import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class WindowManager {

    private List<Window> windows;

    private Window current;

    public WindowManager() {
        this.windows = new ArrayList<>();
    }

    public void registerWindow(Window window) {
        if (windows.contains(window)) {
            throw new IllegalStateException("window already registered");
        }
        this.windows.add(window);
    }

    private Window find(String name) {
        List<Window> windows = this.windows.stream().filter(w -> w.getName().equals(name))
                .collect(Collectors.toList());

        if (windows.size() > 1) {
            throw new IllegalStateException("multiple windows found for name: " + name);
        }

        if (windows.isEmpty()) {
            throw new IllegalStateException("no window found for name: " + name);
        }

        return windows.get(0);
    }

    public Window findWindow(String name){
        return find(name);
    }

    public Window showWindowAtPos(String name, Vector2D pos){
        Window window = showWindow(name);
        window.getStage().setX(pos.getX());
        window.getStage().setY(pos.getY());
        return window;
    }

    public Window showWindow(String name) {
        Window window = find(name);
        if (current != null) {
            if (current.equals(window)) {
                log.debug("already showing window: " + name);
                return current;
            }
            current.getStage().hide();
        }
        Stage stage = window.getStage();
        stage.show();
        stage.toFront();
        current = window;
        return current;
    }

    public Window getCurrent() {
        return current;
    }
}