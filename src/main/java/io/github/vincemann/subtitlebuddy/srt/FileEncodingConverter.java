package io.github.vincemann.subtitlebuddy.srt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Load a file and convert to target encoding
 */
public interface FileEncodingConverter {
    FileInputStream loadWithEncoding(Charset target, File file) throws IOException;
}
