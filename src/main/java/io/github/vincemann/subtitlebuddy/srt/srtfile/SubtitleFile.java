package io.github.vincemann.subtitlebuddy.srt.srtfile;

import io.github.vincemann.subtitlebuddy.srt.SubtitleParagraph;
import io.github.vincemann.subtitlebuddy.srt.Timestamp;

import java.util.List;
import java.util.Optional;

public interface SubtitleFile {

    Optional<SubtitleParagraph> getSubtitleAtTimeStamp(Timestamp timestamp) throws TimeStampOutOfBoundsException;

    List<SubtitleParagraph> getSubtitles();

}
