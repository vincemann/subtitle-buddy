#!/bin/bash
# always execute from project root

# remove possible installation
rm -f /usr/local/bin/subtitle-buddy

./gradlew jlinkZip

cd build

mkdir temp

unzip *.zip -d temp

ln -s $(pwd)/temp/appLauncher-mac/bin/appLauncher /usr/local/bin/subtitle-buddy

subtitle-buddy