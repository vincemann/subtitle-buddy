package io.github.vincemann.subtitlebuddy.srt;

import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class SubtitleText {

    public static SubtitleText empty(){
        return new SubtitleText(new ArrayList<>(Collections.emptyList()));
    }

    private final List<Subtitle> subtitles;
}
