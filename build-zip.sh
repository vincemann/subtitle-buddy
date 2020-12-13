#!/bin/bash
# builds a zip containing fat jar, javafx11 runtime and start script that works for all java versions >= 1.8

# prerequisites:
# zip installed
# before runnning this script you need to have built the fat jar with jdk8 enabled via
# ./gradlew shadowJar
# the jar is expected to be at /build/libs/Subtitle-Buddy-1.0.0-all.jar
# javafx runtime is expected at /opt/javafx-sdk-11.0.2
# download from https://gluonhq.com/products/javafx/ unzip and move to /opt/


buildFolder="./build/libs"
zipFolder="$buildFolder/subtitle-buddy"
echo "removing old zip folder"
rm -rf "$zipFolder"
mkdir -p "$zipFolder"

echo "copying fat jar into zip folder"
cp "$buildFolder/Subtitle-Buddy-1.0.0-all.jar" "$zipFolder/Subtitle-Buddy-1.0.0-all.jar"

# copy javafx runtime into build folder
echo "copying javafx sdk into zip folder"
cp -R /opt/javafx-sdk-11.0.2/ "$zipFolder"

echo "copying start-scripts into zip folder"
cp start* "$zipFolder"

echo "creating zip file..."
cd "$buildFolder"
zip -r subtitle-buddy.zip subtitle-buddy

