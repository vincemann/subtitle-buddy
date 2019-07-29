package io.github.vincemann.subtitleBuddy.events;

import io.github.vincemann.subtitleBuddy.inputListeners.mouseListener.MouseKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MouseClickedEvent {
    private MouseKey mouseKey;
}
