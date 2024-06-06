#!/bin/bash
# ./test-manual-installation.sh [mac|mac-aarch64]


# builds image.zip for given arch and emulate manual installation

arch=$1
./ci/osx/build-image.sh $arch

rm -f /usr/local/bin/subtitle-buddy

cd build

mkdir temp

unzip *.zip -d temp

ln -s $(pwd)/temp/appLauncher-$arch/bin/appLauncher /usr/local/bin/subtitle-buddy

subtitle-buddy