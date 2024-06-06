#!/bin/bash
# always execute from project root
# this is executed on linux hosting machine
# need to build image on macos before and send to this pc
# -> execute ./ftp-upload-image.sh on macos before this script to achieve this
# ftp uploaded file is expected to be in ~/subtitle-buddy-releases on linux hosting machine

cp ~/subtitle-buddy-releases/*mac*.zip build

./ci/update-formular-hash.sh "mac"
