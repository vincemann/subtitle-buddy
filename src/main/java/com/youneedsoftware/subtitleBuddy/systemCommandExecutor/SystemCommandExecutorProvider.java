package com.youneedsoftware.subtitleBuddy.systemCommandExecutor;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.youneedsoftware.subtitleBuddy.os.OS;
import com.youneedsoftware.subtitleBuddy.os.UnsupportedOperatingSystemException;
import com.youneedsoftware.subtitleBuddy.os.osDetector.OperatingSystemDetector;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.ynsSystemExecutor.WindowsSystemCommandExecutor;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.ynsSystemExecutor.LinuxSystemCommandExecutor;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.ynsSystemExecutor.cdCommandHandler.CdCommandHandler;

@Singleton
public class SystemCommandExecutorProvider implements Provider<SystemCommandExecutor> {

    private CdCommandHandler cdCommandHandler;
    private OperatingSystemDetector operatingSystemDetector;

    @Inject
    public SystemCommandExecutorProvider(CdCommandHandler cdCommandHandler, OperatingSystemDetector operatingSystemDetector) {
        this.cdCommandHandler = cdCommandHandler;
        this.operatingSystemDetector=operatingSystemDetector;
    }


    @Override
    public SystemCommandExecutor get() {
        try {
            OS os = operatingSystemDetector.detectOperatingSystem();
            switch (os){
                case LINUX:
                    return new LinuxSystemCommandExecutor(cdCommandHandler);
                case OSX:
                    return new LinuxSystemCommandExecutor(cdCommandHandler);
                case WINDOWS:
                    return new WindowsSystemCommandExecutor(cdCommandHandler);
            }
            //todo change exeception handling approach
            throw new RuntimeException(new UnsupportedOperatingSystemException());
        }catch (UnsupportedOperatingSystemException e){
            throw new RuntimeException(e);
        }
    }
}
