package com.youneedsoftware.subtitleBuddy.events;

import com.youneedsoftware.subtitleBuddy.inputListeners.mouseListener.MouseKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MouseClickedEvent {
    private MouseKey mouseKey;
}
