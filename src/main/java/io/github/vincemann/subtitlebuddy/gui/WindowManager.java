package io.github.vincemann.subtitlebuddy.gui;

import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;

import java.util.List;

public interface WindowManager {
    void registerWindow(Window window);

    Window find(String name);

    Window openAtPos(String name, Vector2D pos);

    Window open(String name);

    void closeAll();

    void close(String name);

    void close(Window window);

    List<Window> getOpened();
}
