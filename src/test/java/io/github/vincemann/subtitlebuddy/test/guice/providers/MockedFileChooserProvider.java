package io.github.vincemann.subtitlebuddy.test.guice.providers;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.test.TestFiles;
import io.github.vincemann.subtitlebuddy.gui.filechooser.FileChooser;

import java.io.File;


@Singleton
public class MockedFileChooserProvider implements Provider<FileChooser> {

    private File validSrtFile;

    public MockedFileChooserProvider() {
        this.validSrtFile = new File(TestFiles.VALID_SRT_FILE_PATH);
    }

    @Override
    public FileChooser get() {
        return () -> validSrtFile;
    }
}
