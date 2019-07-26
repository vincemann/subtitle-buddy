package com.youneedsoftware.subtitleBuddy.systemCommandExecutor.ynsSystemExecutor;

import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.ynsSystemExecutor.cdCommandHandler.CdCommandHandler;

import java.util.Arrays;
import java.util.List;

public class WindowsSystemCommandExecutor extends AbstractSystemCommandExecutor {

    public WindowsSystemCommandExecutor(CdCommandHandler cdCommandHandler) {
        super(cdCommandHandler);
    }

    @Override
    public List<String> getCommandHeader() {
        return Arrays.asList("cmd" ,"/c");
    }

    @Override
    protected String getCurrentWorkingDirCommand() {
        return "cd";
    }

    @Override
    public String getCommandPrefix() {
        //todo adminmode?
        return "";
    }
}
