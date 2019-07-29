package io.github.vincemann.subtitleBuddy.srt;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SubtitleSegment {

    private SubtitleType subtitleType;
    private String text;
}
