package io.github.vincemann.subtitlebuddy.test.guice.providers;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.test.TestFiles;
import io.github.vincemann.subtitlebuddy.srt.SubtitleParagraph;
import io.github.vincemann.subtitlebuddy.srt.srtfile.SubtitleFile;
import io.github.vincemann.subtitlebuddy.srt.srtfile.SubtitleFileImpl;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtFileParser;
import io.github.vincemann.subtitlebuddy.srt.parser.SrtFileParserImpl;

import java.io.File;
import java.util.List;

@Singleton
public class MockedSubtitleFileProvider implements Provider<SubtitleFile> {

    private File validFile;
    private SrtFileParser srtFileParser;

    public MockedSubtitleFileProvider() {
        this.validFile= new File(TestFiles.VALID_SRT_FILE_PATH);
        this.srtFileParser = new SrtFileParserImpl();
    }

    @Override
    public SubtitleFile get() {
        try {
            List<SubtitleParagraph> subtitles = srtFileParser.parseFile(validFile);
            return new SubtitleFileImpl(subtitles);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}
