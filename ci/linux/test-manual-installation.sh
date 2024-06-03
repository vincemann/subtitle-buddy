#!/bin/bash
# always execute from project root

# remove possible installation
rm -f /usr/local/bin/subtitle-buddy

./gradlew clean jlinkZip
./gradlew jlinkZip

cd build

mkdir temp

unzip *.zip -d temp


ln -sf $(pwd)/temp/appLauncher-linux/bin/appLauncher /usr/local/bin/subtitle-buddy

subtitle-buddy