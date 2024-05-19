package io.github.vincemann.subtitlebuddy.srt.parser;

import io.github.vincemann.subtitlebuddy.srt.srtfile.TimeStampOutOfBoundsException;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.RunningState;
import io.github.vincemann.subtitlebuddy.srt.SubtitleParagraph;
import io.github.vincemann.subtitlebuddy.srt.SubtitleText;
import io.github.vincemann.subtitlebuddy.srt.Timestamp;

import java.util.Optional;


public interface SrtParser {

    void start();

    void forward(long delta);

    void stop();

    void setTime(Timestamp timestamp);

    void jumpToTimestamp(Timestamp timestamp);

    Timestamp getTime();

    SubtitleText getCurrentSubtitleText();

    Optional<SubtitleParagraph> getCurrentSubtitle();

    void updateCurrentSubtitle() throws TimeStampOutOfBoundsException;

    RunningState getCurrentState();

    void reset();

}
