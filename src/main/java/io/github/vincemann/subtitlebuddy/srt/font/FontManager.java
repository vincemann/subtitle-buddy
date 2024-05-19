package io.github.vincemann.subtitlebuddy.srt.font;

import io.github.vincemann.subtitlebuddy.srt.FontBundle;

import java.util.List;

public interface FontManager {

    void loadFonts();

    FontBundle getSystemFont();

    List<FontBundle> getLoadedFonts();
    FontBundle getCurrentFont();
}
