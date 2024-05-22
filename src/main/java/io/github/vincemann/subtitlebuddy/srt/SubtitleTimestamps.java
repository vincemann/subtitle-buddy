package io.github.vincemann.subtitlebuddy.srt;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SubtitleTimestamps {
    private Timestamp start;
    private Timestamp end;

    @Override
    public String toString() {
        return "SubtitleTimestamps{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
