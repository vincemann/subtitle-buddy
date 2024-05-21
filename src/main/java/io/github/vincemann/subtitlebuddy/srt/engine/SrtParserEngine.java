package io.github.vincemann.subtitlebuddy.srt.engine;

import lombok.Getter;

/**
 * Controller for {@link io.github.vincemann.subtitlebuddy.srt.parser.SrtParser} that runs all the time
 * as soon as start was called in given interval.
 * Also offers update and stop methods.
 */
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
