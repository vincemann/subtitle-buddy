#!/bin/bash

# build fat jar with javafx files for linux
# build linux AppImage binary x64
# create zip of binary 
# correct jdk17 files must be copied into the following dirs:
# releases/linux/x64/SubtitleBuddy.AppDir/usr/lib/jre

./gradlew shadowJar -PtargetPlatform=linux
builddir=`pwd`


platform="linux"
# read version from generated jar file
jarfile=`readlink -f build/libs/*$platform*.jar | cut -d '/' -f 9`
echo "jar file: $jarfile"
version=$(echo $jarfile | sed -E 's/SubtitleBuddy-([0-9]+\.[0-9]+\.[0-9]+)-[a-z]+\.jar/\1/')
jar_path="build/libs/$jarfile"
echo "version: $version"
echo "jar path $jar_path"
# deploy jars
cp "$jar_path" releases/jar/
echo "deployed jar file"



echo "linux x64..."
rm releases/linux/x64/SubtitleBuddy.AppDir/usr/bin/application.jar
cp "$jar_path" releases/linux/x64/SubtitleBuddy.AppDir/usr/bin/application.jar
echo "jar copied"
echo "building app image"
linux_image_name="subtitle-buddy-$version-linux-x64.AppImage"
zipname="subtitle-buddy-$version-linux-x64.zip"
cd releases/linux/x64
export ARCH=x86_64; ../appimagetool-x86_64.AppImage SubtitleBuddy.AppDir "$linux_image_name"
rm "$zipname"
zip -r "$zipname" "$linux_image_name"
cd "$builddir"
echo "done with linux x64"
