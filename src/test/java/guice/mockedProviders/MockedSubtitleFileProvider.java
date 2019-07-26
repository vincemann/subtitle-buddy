package guice.mockedProviders;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.youneedsoftware.subtitleBuddy.srt.Subtitle;
import com.youneedsoftware.subtitleBuddy.srt.subtitleFile.SubtitleFile;
import com.youneedsoftware.subtitleBuddy.srt.subtitleFile.SubtitleFileImpl;
import com.youneedsoftware.subtitleBuddy.srt.subtitleTransformer.SrtFileTransformer;
import com.youneedsoftware.subtitleBuddy.srt.subtitleTransformer.SrtFileTransformerImpl;
import constants.TestConstants;

import java.io.File;
import java.util.List;

@Singleton
public class MockedSubtitleFileProvider implements Provider<SubtitleFile> {

    private File validFile;
    private SrtFileTransformer srtFileTransformer;

    public MockedSubtitleFileProvider() {
        this.validFile= new File(TestConstants.VALID_SRT_FILE_PATH);
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
