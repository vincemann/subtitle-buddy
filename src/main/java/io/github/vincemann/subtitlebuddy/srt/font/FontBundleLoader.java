package io.github.vincemann.subtitlebuddy.srt.font;

import io.github.vincemann.subtitlebuddy.srt.FontBundle;

import java.nio.file.Path;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Is able to load {@link io.github.vincemann.subtitlebuddy.srt.FontBundle} at given Path.
 */
public interface FontBundleLoader {


    FontBundle loadFontBundle(Path dir, String fontFilename) throws FontBundleLoadingException;
}
