package io.github.vincemann.subtitlebuddy.events;

import io.github.vincemann.subtitlebuddy.listeners.key.HotKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HotKeyPressedEvent {
    private HotKey hotKey;
}
