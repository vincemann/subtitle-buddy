package io.github.vincemann.subtitlebuddy.srt.parser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.srt.*;
import io.github.vincemann.subtitlebuddy.srt.srtfile.SubtitleFile;
import io.github.vincemann.subtitlebuddy.srt.srtfile.TimeStampOutOfBoundsException;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.RunningState;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.StopWatch;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.Optional;

@Log4j2
@Singleton
public class SrtPlayerImpl implements SrtPlayer {
    private static final long NANO_2_MILLIS = 1000000L;

    private SubtitleText currentSubtitleText;
    private SubtitleFile subtitleFile;
    private StopWatch stopWatch;
    private final SubtitleText defaultSubtitleText;
    @Getter
    private Optional<SubtitleParagraph> currentSubtitle = Optional.empty();

    private SrtOptions options;


    @Inject
    public SrtPlayerImpl(SubtitleFile subtitleFile, StopWatch stopWatch, SrtOptions options) {
        this.subtitleFile = subtitleFile;
        this.stopWatch = stopWatch;
        this.defaultSubtitleText = new SubtitleText(
                Collections.singletonList(
                        new Subtitle(SubtitleType.NORMAL, options.getDefaultSubtitle())
                )

        );
        this.options = options;
        updateCurrentSubtitleText(getDefaultSubtitleText());
    }

    @Override
    public synchronized void jumpToTimestamp(Timestamp timestamp) {
        if (getCurrentState().equals(RunningState.STATE_RUNNING)) {
            stop();
        }
        setTime(timestamp);
    }

    @Override
    public synchronized void start() throws IllegalStateException {
        log.debug("srt player start called");
        if (stopWatch.getCurrentState() == RunningState.STATE_UNSTARTED) {
            stopWatch.start();
        } else if (stopWatch.getCurrentState() == RunningState.STATE_SUSPENDED) {
            stopWatch.resume();
        } else {
            log.warn("Parser is already running");
            throw new IllegalStateException("Parser is already running");
        }
    }

    @Override
    public synchronized void forward(long delta) throws TimeStampOutOfBoundsException {
        if (getCurrentState().equals(RunningState.STATE_RUNNING)) {
            stop();
        }
        setTime(new Timestamp(getTime().toMilliSeconds() + delta));
        start();
    }

    @Override
    public synchronized void stop() throws IllegalStateException {
        log.debug("srt parser stop called");
        if (stopWatch.getCurrentState() == RunningState.STATE_RUNNING) {
            stopWatch.suspend();
        } else {
            log.warn("Parser is already stopped");
            throw new IllegalStateException("Parser is already stopped");
        }
    }

    @Override
    public synchronized void setTime(Timestamp timestamp) throws IllegalStateException {
        log.debug("srt parser set time called");
        if (stopWatch.getCurrentState() == RunningState.STATE_UNSTARTED || stopWatch.getCurrentState() == RunningState.STATE_SUSPENDED) {
            //valid
            log.debug("setting Parsers time manually to timestamp : " + timestamp);
            stopWatch.reset();
            stopWatch.start(timestamp.toMilliSeconds() * NANO_2_MILLIS);
            stopWatch.suspend();
        } else {
            //invalid
            log.warn("Invalid Access. Time can only be set when srtParser is either unstarted or suspended");
            throw new IllegalStateException("Invalid Access. Time can only be set when srtParser is either unstarted or suspended");
        }
        updateCurrentSubtitle();
    }

    @Override
    public synchronized SubtitleText getCurrentSubtitleText() {
        return currentSubtitleText;
    }

    @Override
    public synchronized void updateCurrentSubtitle() throws TimeStampOutOfBoundsException {
        Timestamp timestamp = new Timestamp(stopWatch.getTime() / NANO_2_MILLIS);
        //log.trace("updating Parser to timestamp: " + timestamp);
        currentSubtitle = subtitleFile.getSubtitleAtTimeStamp(timestamp);
        if (currentSubtitle.isPresent()) {
            updateCurrentSubtitleText(currentSubtitle.get().getText());
        } else {
            updateCurrentSubtitleText(getDefaultSubtitleText());
        }
    }

    @Override
    public synchronized RunningState getCurrentState() {
        return stopWatch.getCurrentState();
    }

    @Override
    public synchronized void reset() {
        log.debug("srtParser reseted");
        stopWatch.reset();
        updateCurrentSubtitleText(getDefaultSubtitleText());
    }

    private void updateCurrentSubtitleText(SubtitleText text) {
        if (log.isTraceEnabled())
            log.trace("changing current subtitle text to: " + text);
        this.currentSubtitleText = text;
    }

    private SubtitleText getDefaultSubtitleText() {
        if (options.isDefaultSubtitleVisible()) {
            return defaultSubtitleText;
        } else {
            return SubtitleText.empty();
        }
    }

    @Override
    public synchronized Timestamp getTime() {
        return new Timestamp(stopWatch.getTime() / NANO_2_MILLIS);
    }
}
