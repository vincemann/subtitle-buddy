package io.github.vincemann.subtitleBuddy.events;

import io.github.vincemann.subtitleBuddy.inputListeners.keyListener.HotKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HotKeyPressedEvent {
    private HotKey hotKey;


}
