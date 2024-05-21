package io.github.vincemann.subtitlebuddy.gui;

import io.github.vincemann.subtitlebuddy.util.vec.Vector2D;

import java.util.List;

public interface WindowManager {
    void registerWindow(Window window);

    Window findWindow(String name);

    Window showWindowAtPos(String name, Vector2D pos, boolean hideOther);

    Window showWindow(String name);

    Window showWindow(String name, boolean hideOther);

    void closeAll();

    void close(Window window);

    List<Window> getOpened();
}
