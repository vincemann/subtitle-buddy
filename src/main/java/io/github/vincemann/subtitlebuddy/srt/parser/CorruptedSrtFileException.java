package io.github.vincemann.subtitlebuddy.srt.parser;

import io.github.vincemann.subtitlebuddy.srt.SubtitleParagraph;
import lombok.Data;

import java.util.List;

@Data
public class CorruptedSrtFileException extends Exception{
    private int linesRead;
    private List<SubtitleParagraph> readSubtitles;

    public CorruptedSrtFileException(String message, int linesRead, List<SubtitleParagraph> readSubtitles) {
        super(message);
        this.linesRead = linesRead;
        this.readSubtitles = readSubtitles;
    }

    public CorruptedSrtFileException(int linesRead, List<SubtitleParagraph> readSubtitles, Throwable throwable) {
        super(throwable);
        this.linesRead = linesRead;
        this.readSubtitles = readSubtitles;
    }
}
