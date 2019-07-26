package com.youneedsoftware.subtitleBuddy.systemCommandExecutor.ynsSystemExecutor;


import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.*;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.exception.*;
import com.youneedsoftware.subtitleBuddy.systemCommandExecutor.ynsSystemExecutor.cdCommandHandler.CdCommandHandler;
import com.youneedsoftware.subtitleBuddy.util.StringUtils;
import lombok.extern.log4j.Log4j;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * This class can be used to execute a system command from a Java application.
 * See the documentation for the public methods of this class for more
 * information.
 * <p>
 * Documentation for this class is available at this URL:
 * <p>
 * http://devdaily.com/java/java-processbuilder-process-system-exec
 * <p>
 * <p>
 * Copyright 2010 alvin j. alexander, devdaily.com.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 * <p>
 * You should have received a copy of the GNU Lesser Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Please ee the following page for the LGPL license:
 * http://www.gnu.org/licenses/lgpl.txt
 */
@Log4j
//no singleton!
public abstract class AbstractSystemCommandExecutor implements SystemCommandExecutor {
    private String adminPassword;
    private ThreadedStreamHandler inputStreamHandler;
    private ThreadedStreamHandler errorStreamHandler;
    private File currentDir;
    private CdCommandHandler cdCommandHandler;

    /**
     * Pass in the system command you want to run as a List of Strings, as shown here:
     * <p>
     * List<String> commands = new ArrayList<String>();
     * commands.add("/sbin/ping");
     * commands.add("-c");
     * commands.add("5");
     * commands.add("www.google.com");
     * AbstractSystemCommandExecutor commandExecutor = new AbstractSystemCommandExecutor(commands);
     * commandExecutor.executeCommand();
     * <p>
     * Note: I've removed the other constructor that was here to support executing
     * the sudo command. I'll add that back in when I get the sudo command
     * working to the point where it won't hang when the given password is
     * wrong.
     *
     */
    public AbstractSystemCommandExecutor(CdCommandHandler cdCommandHandler) {
        this.cdCommandHandler = cdCommandHandler;
        try {
            currentDir = getCurrentDir();
        } catch (FailedCommandException |InvalidSourceDirectoryException e) {
            log.error("could not initialize sourceDirectory -> cd commands wont work",e);
        }
    }

    private File getCurrentDir() throws InvalidTargetDirectoryException, FailedCommandException{
        ProcessResult processResult = executeCommand(getCurrentWorkingDirCommand());
        String newDir = StringUtils.removeAllNewLines(processResult.getStdout());
        if (!checkIfDirExists(newDir)) {
            throw new InvalidTargetDirectoryException("aktuelles starting wokringdir konnte nicht ermittelt werden");
        }
        return new File(newDir);
    }

    protected abstract String getCurrentWorkingDirCommand();

    private boolean checkIfDirExists(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.isDirectory()) {
            return false;
        }
        return true;
    }

    public abstract List<String> getCommandHeader();

    /**
     * @param commandInformation The command you want to run.
     */
    public ProcessResult executeCommand(final String commandInformation) throws FailedCommandException {

        ProcessResult processResult = new ProcessResult();


        List<String> command = initCommandInformation(commandInformation);
        if (currentDir != null) {
            if (cdCommandHandler.isCDCommand(commandInformation)) {
                File newDir = null;
                try {
                    newDir = cdCommandHandler.handleCDCommand(commandInformation, currentDir);
                } catch (NoCdCommandException | InvalidParentDirectoryException e) {
                    processResult.addReport("cd command konnte nicht erfolgreich behandelt werden!");
                    log.error("cd command konnte nicht erfolgreich behandelt werden!");
                }

                currentDir = newDir;

                log.debug("cd comand wurde erfolgreich behandelt");
                log.debug("currentWorkingDir: " + currentDir.getAbsolutePath());
                processResult.addReport("cd comand wurde erfolgreich behandelt!");
                processResult.addReport("currentWorkingDir: " + currentDir.getAbsolutePath());

                return processResult;
            }
            log.debug("currentWorkingDir: " + currentDir.getAbsolutePath());
        }

        int exitValue;

        try {
            log.debug("full command: " + Arrays.toString(command.toArray()));
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.directory(currentDir);
            Process process = pb.start();



            // i'm currently doing these on a separate line here in case i need to set them to null
            // to get the threads to stop.
            // see http://java.sun.com/j2se/1.5.0/docs/guide/misc/threadPrimitiveDeprecation.html
            InputStream inputStream = process.getInputStream();
            InputStream errorStream = process.getErrorStream();

            // these need to run as java threads to get the standard output and error from the command.
            // the inputstream handler gets a reference to our stdOutput in case we need to write
            // something to it, such as with the sudo command
            if(adminPassword!=null) {
                // you need this if you're going to write something to the command's input stream
                // (such as when invoking the 'sudo' command, and it prompts you for a password).
                OutputStream stdOutput = process.getOutputStream();
                inputStreamHandler = new ThreadedStreamHandler(inputStream, stdOutput, adminPassword);
            }else {
                inputStreamHandler = new ThreadedStreamHandler(inputStream);
            }
            errorStreamHandler = new ThreadedStreamHandler(errorStream);

            // TODO the inputStreamHandler has a nasty side-effect of hanging if the given password is wrong; fix it
            inputStreamHandler.start();
            errorStreamHandler.start();

            // TODO a better way to do this?
            //exitvaluet der shell interessiert nicht wirklich
            process.waitFor();

            // TODO a better way to do this?
            inputStreamHandler.interrupt();
            errorStreamHandler.interrupt();
            inputStreamHandler.join();
            errorStreamHandler.join();
            processResult.setStdout(inputStreamHandler.outputBuffer.toString());
            processResult.setStderr(errorStreamHandler.outputBuffer.toString());
            return processResult;
        }catch (IOException | InterruptedException e){
            throw new FailedCommandException("could not execute Command, caused by: ",e);
        }

    }

    @Override
    public void activateAdminMode(String pw) {
        this.adminPassword=pw;
    }

    @Override
    public void disableAdminMode() {
        this.adminPassword=null;
    }

    public ProcessResult executeCommand(final String commandInformation, String rootPW)
            throws FailedCommandException {
        this.adminPassword = rootPW;
        return executeCommand(commandInformation);
    }


    private List<String> initCommandInformation(String commandInformation) {
        checkNotNull(commandInformation);
        List<String> command = new ArrayList<>(getCommandHeader());
        command.add(getCommandPrefix()+commandInformation);
        return command;
    }

    public abstract String getCommandPrefix();

    /**
     * Get the standard output (stdout) from the command you just exec'd.
     */
    public StringBuilder getStandardOutputFromCommand() {
        return inputStreamHandler.getOutputBuffer();
    }

    /**
     * Get the standard error (stderr) from the command you just exec'd.
     */
    public StringBuilder getStandardErrorFromCommand() {
        return errorStreamHandler.getOutputBuffer();
    }


}