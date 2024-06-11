#!/bin/bash
# ./build-app.sh [mac|mac-aarch64]


# builds .app for specified mac arch and zip it up
# file are in ./build/jpackage/subtitle-buddy.app
# and ./build/jpackage/subtitle-buddy-$arch.app.zip

arch=$1
app="subtitle-buddy.app"
archive="subtitle-buddy-$arch.app.zip"

./gradlew jpackage -PtargetPlatform=$arch
cd build/jpackage
# use ditto instead of zip in order to preserve symlink in zip archive
ditto -c -k --sequesterRsrc --keepParent "$app" "$archive"
cd ../..
