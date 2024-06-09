#!/bin/bash
# ./build-app.sh [mac|mac-aarch64]


# builds .app for specified mac arch and zip it up
# file are in ./build/jpackage/subtitle-buddy.app
# and ./build/jpackage/subtitle-buddy-$arch.app.zip

arch=$1
archive="subtitle-buddy-$arch.app.zip"

./gradlew jpackage -PtargetPlatform=$arch
cd build/jpackage
zip -r "$archive" "*.app"
cd ../..
