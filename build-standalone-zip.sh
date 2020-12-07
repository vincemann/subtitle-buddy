#!/bin/bash
# builds the minimum java runtime for the application

# prerequisites:
# jdk11 needs to be installed and enabled 
# zip installed
# before runnning this script you need to have built the fat jar with jdk8 enabled via
# ./gradlew shadowJar
# the jar is expected to be at /build/libs/Subtitle-Buddy-1.0.0-all.jar
# javafx runtime is expected at /opt/javafx-sdk-11.0.2
# download from https://gluonhq.com/products/javafx/ unzip and move to /opt/

# list deps, if those changed update the command below
buildFolder="./build/libs"
zipFolder="$buildFolder/subtitle-buddy"
echo "removing old zip folder"
rm -rf "$zipFolder"
mkdir -p "$zipFolder"

echo "copying fat jar into zip folder"
cp "$buildFolder/Subtitle-Buddy-1.0.0-all.jar" "$zipFolder/Subtitle-Buddy-1.0.0-all.jar"

echo "modules needed: "
jdeps --module-path /opt/javafx-sdk-11.0.2/lib --add-modules=javafx.controls,javafx.fxml --list-deps "$buildFolder/Subtitle-Buddy-1.0.0-all.jar"

# for some reason the outputs of jdeps with module/concrete/package wont work, so I deleted all except the module itself
# creates the minimum java runtime
echo "creating minimum java runtime..."

# cant get the javafx runtime modules into the java runtime by adding module path --module-path /usr/lib/jvm/java-11-openjdk-amd64/jmods:/opt/javafx-sdk-11.0.2/lib and including the modules i need
# so i have to add modules at runtime from shipped whole javafx runtime...
jlink --no-header-files --no-man-pages --compress=2 --strip-debug \
--add-modules java.base,java.compiler,java.datatransfer,java.logging,java.management,\
java.naming,java.scripting,java.sql,java.xml,jdk.jsobject,jdk.unsupported,jdk.unsupported.desktop,jdk.xml.dom \
--output "$zipFolder/java-runtime"

# copy javafx runtime into build folder
mkdir -p "$zipFolder/javafx-sdk-11.0.2/"
echo "copying javafx sdk into zip folder"
cp -R /opt/javafx-sdk-11.0.2/ "$zipFolder/javafx-sdk-11.0.2/"

echo "copying start-scripts into zip folder"
cp start-subtitle-buddy* "$zipFolder"

echo "creating zip file..."
cd "$buildFolder"
zip -r subtitle-buddy.zip subtitle-buddy

