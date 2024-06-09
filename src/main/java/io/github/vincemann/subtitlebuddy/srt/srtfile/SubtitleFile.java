package io.github.vincemann.subtitlebuddy.srt.srtfile;

import io.github.vincemann.subtitlebuddy.srt.SubtitleParagraph;
import io.github.vincemann.subtitlebuddy.srt.Timestamp;

import java.util.List;
import java.util.Optional;

/**
 * Accessor for subtitles from one srt file.
 */
public interface SubtitleFile {

    Optional<SubtitleParagraph> getSubtitleAtTimeStamp(Timestamp timestamp) throws TimeStampOutOfBoundsException;

    List<SubtitleParagraph> getSubtitles();

}
