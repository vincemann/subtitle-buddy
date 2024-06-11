#!/bin/bash
# ./ci.sh [skipBuild]

# build windows installer and image
# sync all source files to shared folder
# copy win installer to shared folder and replace
# copy image to shared folder for manual installation

# on windows machine:
# just navigate to 192.168.178.69/sharedwin10 via file explorer (samba)
# copy folder on pc
# execute win installer and gradlew clean test, maybe try gradlew clean jpackage

# if java17 is not installed use './testing/win/gradlew.bat test' or
# './testing/win/gradlew.bat clean jpackage' and so on
# this is a wrapper script that sets the jdk to the shipped jdk17 and sets up javafx


name="subtitle-buddy"
target=/home/vince/shared/win10
builddir="$(pwd)"


if [[ -z $1 ]];then
	./gradlew clean buildWindowsInstaller -PtargetPlatform=win
fi

echo "replacing files"
sudo rm -rf ${target}/*.exe
sudo rm -rf ${target}/${name}
sudo rm -rf ${target}/image

sudo cp ${builddir}/build/releases/*.exe ${target}/
sudo cp -R ${builddir}/build/image ${target}/

# copy sources and test folder for testing
sudo mkdir ${target}/${name}
sudo rsync -av --no-perms --no-owner --no-group --exclude 'out' --exclude 'brew' --exclude 'server' --exclude 'build' --exclude '.git' --exclude '.gradle' --exclude '.idea' ${builddir}/ ${target}/${name}/