#!/bin/bash
# ./test-app-installation.sh [mac|mac-aarch64]

# creates mac .app for specified arch and zips it up 
# then emulate user behavior by unzipping and opening app

arch="$1"
app="subtitle-buddy.app"
archive="subtitle-buddy-$arch.app.zip"

./ci/osx/build-app.sh "$arch"

# emulate user behavior -> unzip and start
cd build/jpackage
mkdir temp
unzip "$archive" -d temp
cd ../..

# need to use abs path here to not execute gloablly installed app
open -W -a $(pwd)/build/jpackage/temp/"$app" --args $sb_jvm_args