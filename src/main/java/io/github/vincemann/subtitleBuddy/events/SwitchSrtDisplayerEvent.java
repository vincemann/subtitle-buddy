package io.github.vincemann.subtitleBuddy.events;


import io.github.vincemann.subtitleBuddy.gui.srtDisplayer.SrtDisplayer;
import lombok.Getter;

import javax.validation.constraints.NotNull;


@Getter
public class SwitchSrtDisplayerEvent {

    @NotNull
    private Class<? extends SrtDisplayer> srtDisplayerMode;

    public SwitchSrtDisplayerEvent(Class<? extends SrtDisplayer> srtDisplayerMode) {
        this.srtDisplayerMode = srtDisplayerMode;
    }
}
