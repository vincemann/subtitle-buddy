package io.github.vincemann.subtitlebuddy.srt;

import lombok.*;

import javax.print.DocFlavor;

@Data
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Subtitle {
    public static final Subtitle NEWLINE = new Subtitle(SubtitleType.NORMAL,"\n");

    /**
     * The type of the subtitle, e.g. normal, italic, bold, etc.
     */
    private SubtitleType type;
    /**
     * The actual text of the subtitle, stripped of all metadata.
     */
    private String text;

}
