package io.github.vincemann.subtitlebuddy.srt.parser;

import io.github.vincemann.subtitlebuddy.srt.srtfile.TimeStampOutOfBoundsException;
import io.github.vincemann.subtitlebuddy.srt.SubtitleParagraph;
import io.github.vincemann.subtitlebuddy.srt.SubtitleText;
import io.github.vincemann.subtitlebuddy.srt.Timestamp;
import io.github.vincemann.subtitlebuddy.srt.stopwatch.RunningState;

import java.util.Optional;


public interface SrtParser {

    void start();

    void stop();

    void setTime(Timestamp timestamp);

    Timestamp getTime();

    SubtitleText getCurrentSubtitleText();

    Optional<SubtitleParagraph> getCurrentSubtitle();

    void updateCurrentSubtitle() throws TimeStampOutOfBoundsException;

    RunningState getCurrentState();

    void reset();

}