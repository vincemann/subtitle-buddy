#!/bin/bash

./gradlew shadowJar -PtargetPlatform=win
builddir=`pwd`

platform="win"
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

echo "windows x64..."
rm releases/windows/x64/inno/application.jar
cp "$jar_path" releases/windows/x64/inno/application.jar
echo "jar copied"
windows_image_name="subtitle-buddy-installer.exe"
zipname="subtitle-buddy-$version-windows-x64.zip"
echo "building installer"
cd releases/windows/x64/inno
docker run --rm -i -v "$PWD:/work" amake/innosetup subtitle-buddy.iss
zip -r "$zipname" "$windows_image_name"
cd "$builddir"
echo "done with windows x64"