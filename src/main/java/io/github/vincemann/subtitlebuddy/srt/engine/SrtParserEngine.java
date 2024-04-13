package io.github.vincemann.subtitlebuddy.srt.engine;

import lombok.Getter;

@Getter
public abstract class SrtParserEngine {
    private final long updateDelay;

    SrtParserEngine(long updateDelay) {
        this.updateDelay = updateDelay;
    }

    public abstract void start();

    public abstract void stop();

    public abstract void update();


}
