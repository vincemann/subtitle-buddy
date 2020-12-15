#!/bin/bash
# builds a zip containing fat jar, javafx11 runtime and start script that works for all java versions >= 1.8

# prerequisites:
# zip installed
# before runnning this script you need to have built the fat jar with jdk8 enabled via
# ./gradlew shadowJar
# the jar is expected to be at /build/libs/Subtitle-Buddy-1.0.0-all.jar
# javafx runtimes are expected at:
# /opt/javafx-sdk-11.0.2;/opt/javafx-win-sdk-11.0.2;/opt/javafx-osx-sdk-11.0.2
# download from https://gluonhq.com/products/javafx/ unzip, rename and move to /opt/
# finshed zips will be at ./build/lib/subtitle-buddy-<os>.zip

buildFolder="./build/libs"
zipFolderWin="$buildFolder/subtitle-buddy-win"
zipFolderLinux="$buildFolder/subtitle-buddy-linux"
zipFolderOsx="$buildFolder/subtitle-buddy-osx"

echo "emptying old zip folders"
rm -rf "$zipFolderWin"
rm -rf "$zipFolderLinux"
rm -rf "$zipFolderOsx"
mkdir -p "$zipFolderWin"
mkdir -p "$zipFolderLinux"
mkdir -p "$zipFolderOsx"

echo "copying fat jar into zip folders"
cp "$buildFolder/Subtitle-Buddy-1.0.0-all.jar" "$zipFolderWin/Subtitle-Buddy-1.0.0-all.jar"
cp "$buildFolder/Subtitle-Buddy-1.0.0-all.jar" "$zipFolderLinux/Subtitle-Buddy-1.0.0-all.jar"
cp "$buildFolder/Subtitle-Buddy-1.0.0-all.jar" "$zipFolderOsx/Subtitle-Buddy-1.0.0-all.jar"


echo "copying javafx sdk into zip folders"
cp -R /opt/javafx-win-sdk-11.0.2/ "$zipFolderWin/"
cp -R /opt/javafx-sdk-11.0.2/ "$zipFolderLinux"
cp -R /opt/javafx-osx-sdk-11.0.2/ "$zipFolderOsx"
# rename skd folders to be uniform inside zip
mv "$zipFolderWin/javafx-win-sdk-11.0.2" "$zipFolderWin/javafx-sdk-11.0.2"
mv "$zipFolderOsx/javafx-osx-sdk-11.0.2" "$zipFolderOsx/javafx-sdk-11.0.2"

echo "copying start-scripts into zip folder"
cp start.sh "$zipFolderLinux"
cp start.sh "$zipFolderOsx"
cp start.bat "$zipFolderWin"

echo "creating zip files"
cd "$buildFolder"
zip -r subtitle-buddy-win.zip "subtitle-buddy-win"
zip -r subtitle-buddy-linux.zip "subtitle-buddy-linux"
zip -r subtitle-buddy-osx.zip "subtitle-buddy-osx"

