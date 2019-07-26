package guice.mockedProviders;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.youneedsoftware.subtitleBuddy.filechooser.FileChooser;
import constants.TestConstants;

import java.io.File;


@Singleton
public class MockedFileChooserProvider implements Provider<FileChooser> {

    private File validSrtFile;

    public MockedFileChooserProvider() {
        this.validSrtFile = new File(TestConstants.VALID_SRT_FILE_PATH);
    }

    @Override
    public FileChooser get() {
        return new FileChooser() {
            @Override
            public File letUserChooseFile() {
                return validSrtFile;
            }
        };
    }
}
