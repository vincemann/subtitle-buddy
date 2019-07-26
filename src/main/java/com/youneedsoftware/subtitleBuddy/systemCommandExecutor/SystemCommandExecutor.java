package com.youneedsoftware.subtitleBuddy.systemCommandExecutor;

import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.exception.FailedCommandException;

public interface SystemCommandExecutor {

    public ProcessResult executeCommand(String command) throws FailedCommandException;

    public void activateAdminMode(String pw);

    public void disableAdminMode();
}
