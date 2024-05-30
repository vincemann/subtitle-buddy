#!/bin/bash
# always execute from project root

# remove possible installation
sudo rm -rf /opt/subtitle-buddy
rm -f /usr/local/bin/subtitle-buddy


./gradlew clean jlinkZip

cd build

mkdir temp

unzip *.zip -d temp


ln -s $(pwd)/temp/bin/appLauncher /usr/local/bin/subtitle-buddy

subtitle-buddy