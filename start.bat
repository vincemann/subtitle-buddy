@echo off
setlocal

set VERSION6="1.6.0_21"
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    @echo Output: %%g
    set JAVAVER=%%g
)
set JAVAVER=%JAVAVER:"=%
@echo Output: %JAVAVER%

for /f "delims=. tokens=1-3" %%v in ("%JAVAVER%") do (
    @echo Major: %%v
    @echo Minor: %%w
    @echo Build: %%x
)

if %%v == 1 (
    if %%w >= 9 (
        java -jar --module-path javafx-sdk-11.0.2\lib --add-modules=javafx.controls,javafx.fxml Subtitle-Buddy-1.0.0-all.jar
    )else(
        java -jar Subtitle-Buddy-1.0.0-all.jar
    )
)else(
    java -jar --module-path javafx-sdk-11.0.2\lib --add-modules=javafx.controls,javafx.fxml Subtitle-Buddy-1.0.0-all.jar
)

endlocal
