#!/bin/bash
# always execute from project root

# remove possible installation
sudo rm -rf /opt/subtitle-buddy
rm -f /usr/local/bin/subtitle-buddy


./gradlew clean tarJlink

cd build/releases

mkdir temp

tar --extract --file *.tar.gz --directory temp

ln -s $(pwd)/temp/bin/appLauncher /usr/local/bin/subtitle-buddy

subtitle-buddy