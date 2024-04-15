#!/bin/bash
# script for building release files for win,linux,mac - windows installer,appimage,dmg
# usage 'bash ./releases/build-all-releases.sh'
# only works on linux machines
# must be executed from project root dir
# docker must be installed (using wine image to build inno windows installer on linux host)
# genisoimage must be installed for building dmg file
# you may need to replace the appimage tool binary in ./releases/linux/appimagetool-x86_64.AppImage if not using x64

# correct jdk17 files must be copied into the following dirs:
# releases/linux/x64/SubtitleBuddy.AppDir/usr/lib/jre
# releases/macos/x64/SubtitleBuddy.app/Contents/Java/jre
# releases/windows/x64/inno/jre

# see platform specific build scripts in ./releases/


./gradlew clean
./releases/build-linux-release.sh
./releases/build-mac-release.sh
./releases/build-win-release.sh
