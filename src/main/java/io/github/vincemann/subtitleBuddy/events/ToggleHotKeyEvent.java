package io.github.vincemann.subtitleBuddy.events;

import io.github.vincemann.subtitleBuddy.inputListeners.keyListener.HotKey;
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
