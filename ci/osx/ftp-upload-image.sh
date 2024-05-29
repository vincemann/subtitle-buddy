#!/bin/bash
# build homebrew mac tar ball and send via ftp

./gradlew clean tarJlink
dir="build/releases"
file=$(ls "$dir"/*.tar.gz 2> /dev/null | head -n 1)
./ci/osx/ftp-upload.sh $dir $file
