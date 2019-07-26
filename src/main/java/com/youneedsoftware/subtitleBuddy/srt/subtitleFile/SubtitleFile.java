package com.youneedsoftware.subtitleBuddy.srt.subtitleFile;

import com.youneedsoftware.subtitleBuddy.srt.Subtitle;
import com.youneedsoftware.subtitleBuddy.srt.Timestamp;

import java.util.List;
import java.util.Optional;

public interface SubtitleFile {

    public Optional<Subtitle> getSubtitleAtTimeStamp(Timestamp timestamp) throws TimeStampOutOfBoundsException;

    public List<Subtitle> getSubtitles();

}
