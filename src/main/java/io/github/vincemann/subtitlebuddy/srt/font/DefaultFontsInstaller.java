package io.github.vincemann.subtitlebuddy.srt.font;

import java.io.IOException;
import java.nio.file.Path;

public interface DefaultFontsInstaller {

    void installIfNeeded(Path fontDir) throws IOException;
}
