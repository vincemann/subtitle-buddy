package io.github.vincemann.subtitleBuddy.guice.mockedProviders;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import io.github.vincemann.subtitleBuddy.TestFiles;
import io.github.vincemann.subtitleBuddy.srt.Subtitle;
import io.github.vincemann.subtitleBuddy.srt.subtitleFile.SubtitleFile;
import io.github.vincemann.subtitleBuddy.srt.subtitleFile.SubtitleFileImpl;
import io.github.vincemann.subtitleBuddy.srt.subtitleTransformer.SrtFileTransformer;
import io.github.vincemann.subtitleBuddy.srt.subtitleTransformer.SrtFileTransformerImpl;

import java.io.File;
import java.util.List;

@Singleton
public class MockedSubtitleFileProvider implements Provider<SubtitleFile> {

    private File validFile;
    private SrtFileTransformer srtFileTransformer;

    public MockedSubtitleFileProvider() {
        this.validFile= new File(TestFiles.VALID_SRT_FILE_PATH);
        this.srtFileTransformer= new SrtFileTransformerImpl();
    }

    @Override
    public SubtitleFile get() {
        try {
            List<Subtitle> subtitles = srtFileTransformer.transformFileToSubtitles(validFile);
            return new SubtitleFileImpl(subtitles);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }
}
