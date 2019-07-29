package io.github.vincemann.subtitleBuddy.srt.subtitleFile;

import io.github.vincemann.subtitleBuddy.srt.Subtitle;
import io.github.vincemann.subtitleBuddy.srt.Timestamp;

import java.util.List;
import java.util.Optional;

public interface SubtitleFile {

    public Optional<Subtitle> getSubtitleAtTimeStamp(Timestamp timestamp) throws TimeStampOutOfBoundsException;

    public List<Subtitle> getSubtitles();

}
