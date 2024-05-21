package io.github.vincemann.subtitlebuddy.gui;

import io.github.vincemann.subtitlebuddy.srt.SubtitleText;

public interface SrtDisplayer {


    void displayNextClickCounts();

    void hideNextClickCounts();

    void displaySubtitle(SubtitleText subtitleText);


}
