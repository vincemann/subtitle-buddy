@echo off
setlocal

:: Get the directory where the script is located
set "DIR=%~dp0"

:: Remove the trailing backslash for consistency in path usage
set "DIR=%DIR:~0,-1%"

:: Define the path to the Java executable
set "JAVA_EXEC=%DIR%\image\bin\javaw"

:: Change to the directory where the script resides, then into image root dir
cd /d "%DIR%\image"

start "" "%JAVA_EXEC%" %CDS_JVM_OPTS% -Djlink=true --add-reads org.apache.commons.configuration2=ALL-UNNAMED --add-reads io.github.vincemann.merged.module=org.apache.commons.logging --add-reads org.apache.commons.configuration2=io.github.vincemann.merged.module --add-exports java.desktop/com.apple.eawt=io.github.vincemann.subtitlebuddy -p "%~dp0/../app" -m io.github.vincemann.subtitlebuddy/io.github.vincemann.subtitlebuddy.Main  %* & popd

endlocal
exit