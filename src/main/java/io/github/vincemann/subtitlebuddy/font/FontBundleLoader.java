package io.github.vincemann.subtitlebuddy.font;

import io.github.vincemann.subtitlebuddy.srt.FontBundle;

import java.nio.file.Path;

/**
 * Is able to load {@link io.github.vincemann.subtitlebuddy.srt.FontBundle} at given fonts dir.
 */
public interface FontBundleLoader {


    FontBundle loadFontBundle(Path dir, String fontFilename) throws FontBundleLoadingException;
}
