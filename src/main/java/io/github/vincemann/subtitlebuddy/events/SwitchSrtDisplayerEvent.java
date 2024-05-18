package io.github.vincemann.subtitlebuddy.events;

import io.github.vincemann.subtitlebuddy.gui.SrtDisplayer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SwitchSrtDisplayerEvent {
    private Class<? extends SrtDisplayer> target;
}
