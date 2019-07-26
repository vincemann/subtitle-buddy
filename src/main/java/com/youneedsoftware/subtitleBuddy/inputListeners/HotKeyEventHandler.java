package com.youneedsoftware.subtitleBuddy.inputListeners;

import com.youneedsoftware.subtitleBuddy.events.HotKeyPressedEvent;
import com.youneedsoftware.subtitleBuddy.events.ToggleHotKeyEvent;

public interface HotKeyEventHandler {

    void handleHotKeyPressedEvent(HotKeyPressedEvent e);
    void handleToggleHotKeyEvent(ToggleHotKeyEvent e);
}
