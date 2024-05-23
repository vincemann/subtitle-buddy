package io.github.vincemann.subtitlebuddy.srt;

import lombok.*;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Subtitle {

    /**
     * The type of the subtitle, e.g. normal, italic, bold, etc.
     */
    private SubtitleType type;
    /**
     * The actual text of the subtitle, stripped of all metadata.
     */
    private String text;
}
