#!/bin/bash
# ./test-app-installation.sh [mac|mac-aarch64]

# creates mac .app for specified arch and zips it up 
# then emulate user behavior by unzipping and opening app

arch=$1
app="subtitle-buddy-$arch.app"
archive="subtitle-buddy-$arch.app.zip"

./ci/osx/build-app.sh $arch

# emulate user behavior -> unzip and start
mkdir temp
unzip "$archive" -d temp
cd temp
open -a "$app"