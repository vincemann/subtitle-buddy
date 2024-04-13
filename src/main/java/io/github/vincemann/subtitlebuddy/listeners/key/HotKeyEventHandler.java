package io.github.vincemann.subtitlebuddy.listeners.key;

import io.github.vincemann.subtitlebuddy.events.HotKeyPressedEvent;
import io.github.vincemann.subtitlebuddy.events.ToggleHotKeyEvent;

public interface HotKeyEventHandler {

    void handleHotKeyPressedEvent(HotKeyPressedEvent e);
    void handleToggleHotKeyEvent(ToggleHotKeyEvent e);
}
