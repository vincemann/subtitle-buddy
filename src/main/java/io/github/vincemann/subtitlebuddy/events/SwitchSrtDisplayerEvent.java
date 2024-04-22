package io.github.vincemann.subtitlebuddy.events;


import io.github.vincemann.subtitlebuddy.gui.srtdisplayer.SrtDisplayer;
import lombok.Getter;




@Getter
public class SwitchSrtDisplayerEvent {

    
    private Class<? extends SrtDisplayer> srtDisplayerMode;

    public SwitchSrtDisplayerEvent(Class<? extends SrtDisplayer> srtDisplayerMode) {
        this.srtDisplayerMode = srtDisplayerMode;
    }
}
