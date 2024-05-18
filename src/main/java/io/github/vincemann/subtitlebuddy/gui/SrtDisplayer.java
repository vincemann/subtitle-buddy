package io.github.vincemann.subtitlebuddy.gui;

import io.github.vincemann.subtitlebuddy.srt.FontBundle;
import io.github.vincemann.subtitlebuddy.srt.SubtitleText;
import javafx.scene.paint.Color;

public interface SrtDisplayer {


    void displayNextClickCounts();

    void hideNextClickCounts();

    void displaySubtitle(SubtitleText subtitleText);

    FontBundle getCurrentFont();

    void setCurrentFont(FontBundle font);

    Color getFontColor();

    void setFontColor(Color color);

    SubtitleText getLastSubtitleText();

}
