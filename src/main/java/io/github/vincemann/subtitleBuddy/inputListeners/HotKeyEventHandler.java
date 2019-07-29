package io.github.vincemann.subtitleBuddy.inputListeners;

import io.github.vincemann.subtitleBuddy.events.HotKeyPressedEvent;
import io.github.vincemann.subtitleBuddy.events.ToggleHotKeyEvent;

public interface HotKeyEventHandler {

    void handleHotKeyPressedEvent(HotKeyPressedEvent e);
    void handleToggleHotKeyEvent(ToggleHotKeyEvent e);
}
