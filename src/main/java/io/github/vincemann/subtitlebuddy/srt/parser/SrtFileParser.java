package io.github.vincemann.subtitlebuddy.srt.parser;

import io.github.vincemann.subtitlebuddy.srt.SubtitleParagraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * Parses a srt file into a list of subtitles.
 */
public interface SrtFileParser {

    List<SubtitleParagraph> transformFileToSubtitles(File srtFile) throws FileNotFoundException, CorruptedSrtFileException;

}
