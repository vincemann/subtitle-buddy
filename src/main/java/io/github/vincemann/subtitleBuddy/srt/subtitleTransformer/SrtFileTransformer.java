package io.github.vincemann.subtitleBuddy.srt.subtitleTransformer;

import io.github.vincemann.subtitleBuddy.srt.Subtitle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface SrtFileTransformer {

    public List<Subtitle> transformFileToSubtitles(File srtFile) throws FileNotFoundException, CorruptedSrtFileException;

}
