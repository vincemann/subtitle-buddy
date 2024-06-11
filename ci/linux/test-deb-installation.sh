#!/bin/bash
# always execute from project root

# remove possible installation
sudo dpkg --remove subtitle-buddy
rm -f /usr/local/bin/subtitle-buddy

./gradlew jpackage

sudo dpkg -i build/jpackage/*.deb

subtitle-buddy $sb_jvm_args

sudo dpkg --remove subtitle-buddy