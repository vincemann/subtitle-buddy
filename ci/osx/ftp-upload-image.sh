#!/bin/bash
# build homebrew mac tar ball and send via ftp

./gradlew clean tarJlink
dir="build/releases"
./ci/osx/ftp-upload.sh $dir *.tar.gz
