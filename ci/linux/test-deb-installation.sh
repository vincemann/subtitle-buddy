#!/bin/bash
# always execute from project root

# remove possible installation
sudo dpkg --remove subtitle-buddy
sudo rm -rf /opt/subtitle-buddy
rm -f /usr/local/bin/subtitle-buddy

./gradlew clean jpackage

sudo dpkg -i build/jpackage/*.deb

subtitle-buddy