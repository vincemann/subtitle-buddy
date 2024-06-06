#!/bin/bash
# ./update-homebrew-formula.sh [mac|mac-aarch64]


# this should be executed on linux hosting machine
# need to build image.zip on macos before and send to this linux pc
# -> execute ./ftp-upload-image.sh on macos before this script to achieve this

arch=$1
./ci/update-formular-hash.sh $arch
