package io.github.vincemann.subtitlebuddy.guice.mockedProviders;

import com.google.inject.Provider;
import io.github.vincemann.subtitlebuddy.TestFiles;
import io.github.vincemann.subtitlebuddy.config.ExecutableLocator;

import java.nio.file.Path;
import java.nio.file.Paths;


public class MockedRunningExecutableProvider implements Provider<ExecutableLocator>
{

    @Override
    public ExecutableLocator get() {
        return new ExecutableLocator() {
            @Override
            public Path findPath(){
                return Paths.get(TestFiles.FAKE_RUNNING_EXECUTABLE);
            }
        };
    }
}
