package com.youneedsoftware.subtitleBuddy.srt.subtitleTransformer;

import com.youneedsoftware.subtitleBuddy.srt.Subtitle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public interface SrtFileTransformer {

    public List<Subtitle> transformFileToSubtitles(File srtFile) throws FileNotFoundException, CorruptedSrtFileException;

}
