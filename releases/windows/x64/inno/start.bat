@echo off
setlocal

set DIR="%~dp0"
set JAVA_EXEC="%DIR:"=%image\bin\javaw"

pushd %DIR% & start "" %JAVA_EXEC% %CDS_JVM_OPTS% -Djlink=true --add-reads org.apache.commons.configuration2=ALL-UNNAMED --add-reads io.github.vincemann.merged.module=org.apache.commons.logging --add-reads org.apache.commons.configuration2=io.github.vincemann.merged.module --add-exports org.apache.commons.logging/org.apache.commons.logging.impl=org.apache.commons.configuration2 -p "%~dp0/../app" -m io.github.vincemann.subtitlebuddy/io.github.vincemann.subtitlebuddy.Main  %* & popd

endlocal
exit