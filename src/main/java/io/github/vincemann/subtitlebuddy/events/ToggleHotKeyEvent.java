package io.github.vincemann.subtitlebuddy.events;

import io.github.vincemann.subtitlebuddy.listeners.key.HotKey;
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
