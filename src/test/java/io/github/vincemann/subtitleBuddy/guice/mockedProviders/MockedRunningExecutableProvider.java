package io.github.vincemann.subtitleBuddy.guice.mockedProviders;

import com.google.inject.Provider;
import io.github.vincemann.subtitleBuddy.TestFiles;
import io.github.vincemann.subtitleBuddy.runningExecutableFinder.RunningExecutableFinder;

import java.nio.file.Path;
import java.nio.file.Paths;


public class MockedRunningExecutableProvider implements Provider<RunningExecutableFinder>
{

    @Override
    public RunningExecutableFinder get() {
        return new RunningExecutableFinder() {
            @Override
            public Path findRunningExecutable(){
                return Paths.get(TestFiles.FAKE_RUNNING_EXECUTABLE);
            }
        };
    }
}
