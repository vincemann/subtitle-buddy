package io.github.vincemann.subtitleBuddy.srt.parser;

import io.github.vincemann.subtitleBuddy.srt.subtitleFile.TimeStampOutOfBoundsException;
import io.github.vincemann.subtitleBuddy.srt.Subtitle;
import io.github.vincemann.subtitleBuddy.srt.SubtitleText;
import io.github.vincemann.subtitleBuddy.srt.Timestamp;
import io.github.vincemann.subtitleBuddy.srt.stopwatch.RunningState;

import java.util.Optional;


public interface SrtParser {

    public void start();

    public void stop();

    public void setTime(Timestamp timestamp);

    public Timestamp getTime();

    public SubtitleText getCurrentSubtitleText();

    Optional<Subtitle> getCurrentSubtitle();

    public void updateCurrentSubtitle() throws TimeStampOutOfBoundsException;

    public RunningState getCurrentState();

    public void reset();

}
