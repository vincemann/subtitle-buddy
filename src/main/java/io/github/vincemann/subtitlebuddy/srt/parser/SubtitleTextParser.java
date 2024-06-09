package io.github.vincemann.subtitlebuddy.srt.parser;

import io.github.vincemann.subtitlebuddy.srt.SubtitleText;

public interface SubtitleTextParser {

    String ITALIC_START_DELIMITER = "<i>";
    String ITALIC_END_DELIMITER = "</i>";
    String NEW_LINE_DELIMITER = "<n>";

    /**
     * Receives formatted string of subtitle payload.
     * Example:
     * "A world without <i>rules</i> and controls,
     * without borders or boundaries."
     *
     * should be formatted to:
     * "A world without <i>rules</i> and controls,<n>without borders or boundaries."
     *
     * And translates into:
     * Subtitle(NORMAL,"A world without"),Subtitle(ITALIC,"rules),Subtitle(NORMAL,"and controls,"), Subtitle.NEWLINE, Subtitle(NORMAL,"without borders and boundaries")
     *
     * Supports <i>italic</i> and <n> = newline.
     * @throws InvalidDelimiterException if any unknown delimiter is found <?>
     *
     **/
    SubtitleText parse(String subtitleString) throws InvalidDelimiterException;
}
