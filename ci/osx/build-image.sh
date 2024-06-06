#!/bin/bash
# ./build-image.sh [mac|mac-aarch64]


arch=$1
./gradlew jlinkZip -PtargetPlatform=$arch
