package com.youneedsoftware.subtitleBuddy.events;

import com.youneedsoftware.subtitleBuddy.inputListeners.keyListener.HotKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class HotKeyPressedEvent {
    private HotKey hotKey;


}
