package io.github.vincemann.subtitlebuddy.srt;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
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
