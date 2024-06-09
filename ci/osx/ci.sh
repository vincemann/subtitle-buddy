#!/bin/bash
# execute all ci scripts for linux in a row
# user just needs to interactively test the application when it opens and type in pws
# also executes ci scripts on remote linux host (for example for updating homebrew formula file)
# also uploads all artifacts via ftp to linux host -> files will end up in ftp root dir, which should be ./server

# remember to start file server in ./server in dev env: python3 -m http.server 8000
# remember to start ssh server on linux via sudo systemctl start ssh.service - start with sftp enabled and user: subtitle-buddy

# artifacts:
# *-image-x64.zip (manual installation & homebrew)
# *-x64.app.zip
# *-image-aarch64.zip 
# *-aarch64.app.zip

version="1.1.0"
name_prefix="subtitle-buddy-$version-mac"
ssh_host="subtitle-buddy@192.168.178.69"

# set arch to x64

name="$name_prefix-x64"
./gradlew clean

echo "running tests"
./gradlew test

rm -rf ~/.subtitle-buddy
echo "gradle run"
./gradlew run

echo "################################"
echo "manual installation x64"
rm -rf ~/.subtitle-buddy
./ci/osx/test-manual-installation.sh "mac"

echo "################################"
echo "homebrew installation x64"
rm -rf ~/.subtitle-buddy
./ci/osx/ftp-upload-image.sh "${name}-image.zip" "mac"
ssh $ssh_host "cd projects; sudo -u vince ./ci/osx/update-homebrew-formula.sh mac"
./ci/osx/test-homebrew-installation.sh


echo "################################"
echo ".app installation x64"
rm -rf ~/.subtitle-buddy
./ci/osx/test-app-installation.sh "mac"
./ci/osx/ftp-upload-app.sh "${name}.app.zip" "mac"

# set arch to aarch64
name="$name_prefix-aarch64"
echo "################################"
echo "continue with aarch64..."
./gradlew clean


# build image.zip and upload to hosting linux machine
./ci/osx/build-image.sh "mac-aarch64"
# upload image.zip to linux host
./ci/osx/ftp-upload-image.sh "${name}-image.zip" "mac-aarch64"


# build .app.zip and upload to hosting linux machine
./ci/osx/build-app.sh "mac-aarch64"
./ci/osx/ftp-upload-app.sh "${name}.app.zip" "mac-aarch64"
