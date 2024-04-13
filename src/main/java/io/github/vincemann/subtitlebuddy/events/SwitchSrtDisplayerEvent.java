package io.github.vincemann.subtitlebuddy.events;


import io.github.vincemann.subtitlebuddy.gui.srtdisplayer.SrtDisplayer;
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
