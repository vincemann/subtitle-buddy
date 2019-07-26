package com.youneedsoftware.subtitleBuddy.systemCommandExecutor.ynsSystemExecutor;


import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.ynsSystemExecutor.cdCommandHandler.CdCommandHandler;

import java.util.ArrayList;
import java.util.List;

public class LinuxSystemCommandExecutor extends AbstractSystemCommandExecutor {
    private volatile boolean adminMode = false;


    public LinuxSystemCommandExecutor(CdCommandHandler cdCommandHandler) {
        super(cdCommandHandler);
    }

    @Override
    public List<String> getCommandHeader() {
        List<String> commandHeader = new ArrayList<>(2);
        commandHeader.add("/bin/bash");
        commandHeader.add("-c");
        return commandHeader;
    }

    @Override
    public void activateAdminMode(String pw) {
        super.activateAdminMode(pw);
        this.adminMode=true;
    }

    @Override
    protected String getCurrentWorkingDirCommand() {
        return "pwd";
    }

    @Override
    public void disableAdminMode() {
        super.disableAdminMode();
        this.adminMode=false;
    }

    @Override
    public String getCommandPrefix() {
        if(adminMode){
            return "sudo -S ";
        }else {
            return "";
        }
    }
}
