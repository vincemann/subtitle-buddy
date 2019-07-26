package com.youneedsoftware.subtitleBuddy.srt.updater;

import lombok.Getter;

@Getter
public abstract class SrtParserUpdater {
    private final long updateDelay;

    SrtParserUpdater(long updateDelay) {
        this.updateDelay = updateDelay;
    }

    public abstract void start();

    public abstract void stop();

    public abstract void update();


}
