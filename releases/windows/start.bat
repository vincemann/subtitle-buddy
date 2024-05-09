@echo off
setlocal

set DIR="%~dp0"
set JAVA_EXEC="%DIR:"=%image\\bin\\javaw"

pushd %DIR% & start "" %JAVA_EXEC% %CDS_JVM_OPTS% ${jvmArgs} -p "%~dp0/../app" -m ${moduleName}/${mainClassName}  %* & popd

endlocal
exit