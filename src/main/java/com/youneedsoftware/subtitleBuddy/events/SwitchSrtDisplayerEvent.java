package com.youneedsoftware.subtitleBuddy.events;


import com.youneedsoftware.subtitleBuddy.gui.srtDisplayer.SrtDisplayer;
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
