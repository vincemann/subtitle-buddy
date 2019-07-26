package com.youneedsoftware.subtitleBuddy.events;

import com.youneedsoftware.subtitleBuddy.inputListeners.keyListener.HotKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public class ToggleHotKeyEvent {
    private HotKey hotKey;
    private boolean toggled;
}
