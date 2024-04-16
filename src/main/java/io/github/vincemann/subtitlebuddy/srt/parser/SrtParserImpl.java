package io.github.vincemann.subtitlebuddy.srt.parser;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import io.github.vincemann.subtitlebuddy.config.properties.PropertyFileKeys;
import io.github.vincemann.subtitlebuddy.srt.srtfile.TimeStampOutOfBoundsException;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.RunningState;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.StopWatch;
import io.github.vincemann.subtitlebuddy.srt.srtfile.SubtitleFile;
import io.github.vincemann.subtitlebuddy.srt.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Singleton
public class SrtParserImpl implements SrtParser {
    private static final long NANO_2_MILLIS = 1000000L;

    private SubtitleText currentSubtitleText;
    private SubtitleFile subtitleFile;
    private StopWatch stopWatch;
    private final SubtitleText defaultSubtitleText;
    @Getter
    private Optional<SubtitleParagraph> currentSubtitle= Optional.empty();

    @Inject
    public SrtParserImpl(@NotNull SubtitleFile subtitleFile, @NotNull StopWatch stopWatch, final @Named(PropertyFileKeys.DEFAULT_SUBTITLE) @NonNull String defaultSubtitleText) {
        this.subtitleFile = subtitleFile;
        this.stopWatch = stopWatch;
        checkArgument(!defaultSubtitleText.isEmpty());
        this.defaultSubtitleText = new SubtitleText(Collections.singletonList(Collections.singletonList(new SubtitleSegment(SubtitleType.NORMAL,defaultSubtitleText))));
        this.currentSubtitleText=this.defaultSubtitleText;
    }


    @Override
    public synchronized void start() throws IllegalStateException{
        log.debug("srt parser start called");
        if(stopWatch.getCurrentState()== RunningState.STATE_UNSTARTED){
            stopWatch.start();
        }else if(stopWatch.getCurrentState()==RunningState.STATE_SUSPENDED){
            stopWatch.resume();
        }else {
            log.warn("Parser is already running");
            throw new IllegalStateException("Parser is already running");
        }
    }

    @Override
    public synchronized void stop() throws IllegalStateException{
        log.debug("srt parser stop called");
        if(stopWatch.getCurrentState()==RunningState.STATE_RUNNING){
            stopWatch.suspend();
        }else {
            log.warn("Parser is already stopped");
            throw new IllegalStateException("Parser is already stopped");
        }

    }

    @Override
    public synchronized void setTime(Timestamp timestamp) throws IllegalStateException{
        log.debug("srt parser set time called");
        if(stopWatch.getCurrentState()==RunningState.STATE_UNSTARTED || stopWatch.getCurrentState() == RunningState.STATE_SUSPENDED){
            //valid
            log.debug("setting Parsers time manually to timestamp : " + timestamp);
            stopWatch.reset();
            stopWatch.start(timestamp.toMilliSeconds()*NANO_2_MILLIS);
            stopWatch.suspend();
        }
        else {
            //invalid
            log.warn("Invalid Access. Time can only be set when io.github.vincemann.srtParser is either unstarted or suspended");
            throw new IllegalStateException("Invalid Access. Time can only be set when io.github.vincemann.srtParser is either unstarted or suspended");
        }
    }

    @Override
    public synchronized SubtitleText getCurrentSubtitleText() {
        return currentSubtitleText;
    }

    @Override
    public synchronized void updateCurrentSubtitle() throws TimeStampOutOfBoundsException {
        Timestamp timestamp = new Timestamp(stopWatch.getTime()/NANO_2_MILLIS);
        //log.trace("updating Parser to timestamp: " + timestamp);
        currentSubtitle = subtitleFile.getSubtitleAtTimeStamp(timestamp);
        if(currentSubtitle.isPresent()){
            this.currentSubtitleText=currentSubtitle.get().getText();
        }else {
            this.currentSubtitleText = defaultSubtitleText;
        }
    }

    @Override
    public synchronized RunningState getCurrentState() {
        return stopWatch.getCurrentState();
    }

    @Override
    public synchronized void reset() {
        log.debug("io.github.vincemann.srtParser gets resetted");
        stopWatch.reset();
        currentSubtitleText =defaultSubtitleText;
    }

    @Override
    public synchronized Timestamp getTime() {
        return new Timestamp(stopWatch.getTime()/NANO_2_MILLIS);
    }
}
