#!/bin/bash
# build homebrew mac tar ball and send via ftp

./gradlew clean tarJlink
./ci/osx/ftp-upload.sh build/releases/*.tar.gz
