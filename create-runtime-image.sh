jarfile="build/libs/SubtitleBuddy-1.1.0-linux.jar"
modulepath="modulepath"
if [ ! -f "$jarfile" ]; then
    echo "Jar File does not exist. Run gradlew clean build && gradlew run && gradlew jar before"
    exit 1
fi
if [ ! -f "$modulepath" ]; then
    echo "Module path File does not exist. Run gradlew run and copy printed modulepath into file called 'modulepath' beforeß"
    exit 1
fi
echo "evaluating image"
deps=$(jdeps --module-path `cat modulepath` --add-modules io.github.vincemann.subtitlebuddy,javafx.controls,javafx.fxml --multi-release 9 --print-module-deps $jarfile)
echo "deps: $deps"
echo "building image"
jlink --module-path `cat modulepath` --no-header-files --no-man-pages --compress=2 --strip-debug --add-modules $deps --output java-runtime