package guice.mockedProviders;

import com.google.inject.Provider;
import com.youneedsoftware.subtitleBuddy.runningExecutableFinder.RunningExecutableFinder;
import constants.TestConstants;

import java.nio.file.Path;
import java.nio.file.Paths;


public class MockedRunningExecutableProvider implements Provider<RunningExecutableFinder>
{

    @Override
    public RunningExecutableFinder get() {
        return new RunningExecutableFinder() {
            @Override
            public Path findRunningExecutable(){
                return Paths.get(TestConstants.FAKE_RUNNING_EXECUTABLE);
            }
        };
    }
}
