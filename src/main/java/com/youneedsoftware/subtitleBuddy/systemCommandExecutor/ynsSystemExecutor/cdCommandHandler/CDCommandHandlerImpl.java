package com.youneedsoftware.subtitleBuddy.systemCommandExecutor.ynsSystemExecutor.cdCommandHandler;


import com.google.inject.Singleton;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.exception.InvalidParentDirectoryException;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.exception.InvalidSourceDirectoryException;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.exception.InvalidTargetDirectoryException;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.exception.NoCdCommandException;
import com.youneedsoftware.subtitleBuddy.util.StringUtils;
import lombok.NonNull;
import lombok.extern.log4j.Log4j;


import java.io.File;

@Log4j
@Singleton
public class CDCommandHandlerImpl implements CdCommandHandler {


    @Override
    public boolean isCDCommand(@NonNull String command) {
        if (command.length() < 4) {
            log.debug("Befehl zu klein um ein cd befehl zu sein");
            return false;
        }
        if (!command.substring(0, 2).equalsIgnoreCase("cd")) {
            log.debug("kein cd am anfang gefunden -> also kein cd befehl");
            return false;
        }
        return true;
    }

    /**
     * @param command
     * @param currentWorkingDir
     * @return newDir if succesFull
     */
    @Override
    public File handleCDCommand(@NonNull String command, @NonNull File currentWorkingDir) throws NoCdCommandException, InvalidParentDirectoryException, InvalidSourceDirectoryException {
        if (!isCDCommand(command)) {
            throw new NoCdCommandException();
        }
        if(!currentWorkingDir.isDirectory()){
            throw new InvalidSourceDirectoryException("SourceFile is no directory");
        }
        String modCdCommand = StringUtils.removeAllNewLines(command.substring(3));
        log.trace("modified cd TerminalCommand = " + modCdCommand);
        if (modCdCommand.equals("..")) {
            if (currentWorkingDir.getParent() == null) {
                throw new InvalidParentDirectoryException("es gibt keinen weiteren parent!");
            }
            String parent = StringUtils.removeAllNewLines(currentWorkingDir.getParent());
            File newDir = new File(parent);
            log.trace("parentdir: " + parent);
            if (newDir.isDirectory()) {
                return newDir;
            }else {
                throw new InvalidParentDirectoryException("Parent was no Directory");
            }
        }

        //wir haben einen cd befehl der nicht ".." ist
        String newDirPath = currentWorkingDir.getAbsolutePath() + modCdCommand;
        String modNewDirPath = StringUtils.removeAllNewLines(newDirPath);

        log.debug("new directory: " + modNewDirPath);

        File newDir = new File(modNewDirPath);
        if (!newDir.isDirectory() || !newDir.exists()) {
            throw new InvalidTargetDirectoryException("the directory: " + newDirPath + " does not exist");
        } else {
            return newDir;
        }
    }

}
