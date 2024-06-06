#!/bin/bash
# ./build-image.sh [mac|mac-aarch64]
# builds mac image for given arch and zips it up
# files end up in ./build/image-$platform.zip


arch=$1
./gradlew jlinkZip -PtargetPlatform=$arch
