package io.github.vincemann.subtitlebuddy.events;

import io.github.vincemann.subtitlebuddy.listeners.mouse.MouseKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MouseClickedEvent {
    private MouseKey mouseKey;
}
