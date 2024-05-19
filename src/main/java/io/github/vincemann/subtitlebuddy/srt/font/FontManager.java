package io.github.vincemann.subtitlebuddy.srt.font;

import io.github.vincemann.subtitlebuddy.srt.FontBundle;

import java.util.List;

public interface FontManager {

    void loadFonts();

    /**
     * Usually just call load fonts once and then call this#getCurrentFont.
     * This is for reloading if current font changed.
     * You could also call loadFonts again to reload everything.
     */
    void reloadCurrentFont();

    FontBundle getSystemFont();

    List<FontBundle> getLoadedFonts();
    FontBundle getCurrentFont();
}
