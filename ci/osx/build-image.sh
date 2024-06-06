#!/bin/bash
# ./build-image.sh [mac|mac-aarch64]
# builds mac image for given arch and zips it up
# files end up as: ./build/image-mac.zip ./build/image/appLauncher-mac


arch=$1
./gradlew jlinkZip -PtargetPlatform=$arch
