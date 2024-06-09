package io.github.vincemann.subtitlebuddy.test.guice;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import io.github.vincemann.subtitlebuddy.test.SrtFiles;
import io.github.vincemann.subtitlebuddy.gui.filechooser.FileChooser;

import java.io.File;


@Singleton
public class MockedFileChooserProvider implements Provider<FileChooser> {

    private File validSrtFile;

    public MockedFileChooserProvider() {
        this.validSrtFile = new File(SrtFiles.VALID);
    }

    @Override
    public FileChooser get() {
        return () -> validSrtFile;
    }
}
