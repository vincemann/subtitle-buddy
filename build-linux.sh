#!/bin/bash

./gradlew shadowJar -PtargetPlatform=linux
builddir=`pwd`


platform="linux"
# read version from generated jar file
jarfile=`readlink -f build/libs/*$platform*.jar | cut -d '/' -f 9`
echo "jar file: $jarfile"
version="${jarfile#Subtitle-Buddy-}"  # Remove the part before the version
version="${version%.jar}"             # Remove the .jar at the end
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
zip -r "$zipname" "$linux_image_name"
cd "$builddir"
echo "done with linux x64"

