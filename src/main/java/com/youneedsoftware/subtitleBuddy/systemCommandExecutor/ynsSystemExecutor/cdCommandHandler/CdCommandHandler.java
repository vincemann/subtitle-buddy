package com.youneedsoftware.subtitleBuddy.systemCommandExecutor.ynsSystemExecutor.cdCommandHandler;

import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.exception.InvalidParentDirectoryException;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.exception.NoCdCommandException;

import java.io.File;

public interface CdCommandHandler {

    public boolean isCDCommand(String command);
    public File handleCDCommand(String command, File currentWorkingDir) throws NoCdCommandException, InvalidParentDirectoryException;
}
