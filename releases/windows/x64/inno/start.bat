@echo off
setlocal

rem Set path to the Java executable
set PATH=.\jre\bin;%PATH%

rem Set the path to the JavaFX SDK
set JAVAFX_SDK=.\lib\javafx-sdk\lib

rem Run the application with the local JavaFX
java --module-path %JAVAFX_SDK% --add-modules javafx.controls,javafx.fxml -jar application.jar

endlocal