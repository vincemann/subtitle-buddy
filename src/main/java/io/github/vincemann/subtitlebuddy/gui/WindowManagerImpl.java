package io.github.vincemann.subtitlebuddy.gui;

import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Singleton
public class WindowManagerImpl implements WindowManager {

    private List<Window> windows;

    private List<Window> opened;

    public WindowManagerImpl() {
        this.windows = new ArrayList<>();
        this.opened = new ArrayList<>();
    }

    @Override
    public void registerWindow(Window window) {
        if (windows.contains(window)) {
            throw new IllegalStateException("window already registered");
        }
        this.windows.add(window);
    }

    private Window findWindow(String name) {
        List<Window> windows = this.windows.stream()
                .filter(w -> w.getName().equals(name))
                .toList();

        if (windows.size() > 1) {
            throw new IllegalStateException("multiple windows found for name: " + name);
        }

        if (windows.isEmpty()) {
            throw new IllegalStateException("no window found for name: " + name);
        }

        return windows.get(0);
    }

    @Override
    public void close(String name) {
        close(find(name));
    }

    @Override
    public Window find(String name) {
        return findWindow(name);
    }

    @Override
    public Window openAtPos(String name, Vector2D pos) {
        Window window = open(name);
        window.getStage().setX(pos.getX());
        window.getStage().setY(pos.getY());
        return window;
    }

    @Override
    public Window open(String name) {
        Window window = findWindow(name);
        if (!opened.isEmpty()) {
            // if already showing this window, put to the front
            if (opened.contains(window)) {
                log.debug("already showing window: " + name + " - focusing");
                window.getStage().toFront();
            }
        }

        open(window);
        return window;
    }

    private void open(Window window) {
        Stage stage = window.getStage();
        stage.show();
        stage.toFront();
        opened.add(window);
    }

    @Override
    public void closeAll() {
        // avoid comod exception
        List<Window> copiedWindows = new ArrayList<>(opened);
        copiedWindows.forEach(this::close);
        assert opened.size() == 0;
    }


    @Override
    public void close(Window window) {
        window.getStage().hide();
        opened.remove(window);
    }

    @Override
    public List<Window> getOpened() {
        return opened;
    }
}
