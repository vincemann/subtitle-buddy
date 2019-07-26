package com.youneedsoftware.subtitleBuddy.srt.subtitleTransformer;

import com.youneedsoftware.subtitleBuddy.srt.Subtitle;
import lombok.Data;

import java.util.List;

@Data
public class CorruptedSrtFileException extends Exception{
    private int linesRead;
    private List<Subtitle> readSubtitles;

    public CorruptedSrtFileException(String message, int linesRead, List<Subtitle> readSubtitles) {
        super(message);
        this.linesRead = linesRead;
        this.readSubtitles = readSubtitles;
    }

    public CorruptedSrtFileException(int linesRead, List<Subtitle> readSubtitles, Throwable throwable) {
        super(throwable);
        this.linesRead = linesRead;
        this.readSubtitles = readSubtitles;
    }
}
