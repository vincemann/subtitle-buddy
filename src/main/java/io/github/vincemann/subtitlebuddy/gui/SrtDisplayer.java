package io.github.vincemann.subtitlebuddy.gui;

import io.github.vincemann.subtitlebuddy.srt.SrtFonts;
import io.github.vincemann.subtitlebuddy.srt.SubtitleText;
import javafx.scene.paint.Color;

public interface SrtDisplayer {


    void displayNextClickCounts();

    void hideNextClickCounts();

    void displaySubtitle(SubtitleText subtitleText);

    SrtFonts getCurrentFont();

    void setCurrentFont(SrtFonts font);

    Color getFontColor();

    void setFontColor(Color color);

    SubtitleText getLastSubtitleText();

}
