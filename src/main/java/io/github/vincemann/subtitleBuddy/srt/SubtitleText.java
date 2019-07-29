package io.github.vincemann.subtitleBuddy.srt;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class SubtitleText {

    @NonNull
    private final List<List<SubtitleSegment>> subtitleSegments;
}
