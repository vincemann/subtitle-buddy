package com.youneedsoftware.subtitleBuddy.srt.parser;

import com.youneedsoftware.subtitleBuddy.srt.subtitleFile.TimeStampOutOfBoundsException;
import com.youneedsoftware.subtitleBuddy.srt.Subtitle;
import com.youneedsoftware.subtitleBuddy.srt.SubtitleText;
import com.youneedsoftware.subtitleBuddy.srt.Timestamp;
import com.youneedsoftware.subtitleBuddy.srt.stopWatch.RunningState;

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
