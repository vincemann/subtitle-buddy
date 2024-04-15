#!/bin/bash

./gradlew shadowJar -PtargetPlatform=mac
builddir=`pwd`

platform="mac"
# read version from generated jar file
jarfile=`readlink -f build/libs/*$platform*.jar | cut -d '/' -f 9`
echo "jar file: $jarfile"
version="${jarfile#Subtitle-Buddy-}"  # Remove the part before the version
version="${version%.jar}"             # Remove the .jar at the end
jar_path= "build/libs/$jarfile"
echo "version: $version"
# deploy jars
cp "$jar_path" releases/jar/
echo "deployed jar file"

echo "macos x64..."
rm releases/macos/x64/SubtitleBuddy.app/Contents/Java/application.jar
cp "$jar_path" releases/macos/x64/SubtitleBuddy.app/Contents/Java/application.jar
echo "jar copied"
mac_image_name="subtitle-buddy-$version-macos-x64.dmg"
zipname="subtitle-buddy-$version-macos-x64.zip"
echo "building dmg image"
cd releases/macos/x64
genisoimage -D -V "SubtitleBuddy" -no-pad -r -apple -o "$mac_image_name" SubtitleBuddy.app/
zip -r "$zipname" "$mac_image_name"
cd "$builddir"
echo "done with macox x64"