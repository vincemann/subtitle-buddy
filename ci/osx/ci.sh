#!/bin/bash
# execute all ci scripts for linux in a row
# user just needs to interactively test the application when it opens and type in pws
# also executes ci scripts on remote linux host (for example for updating homebrew formula file)
# also uploads all artifacts via ftp to linux host -> files will end up in ftp root dir, which should be ./server

# remember to start ftp server on linux via sudo systemctl start vsftpd.service
# remember to start file server in ./server in dev env: python3 -m http.server 8000
# remember to start ssh server on linux via sudo systemctl start ssh.service

# artifacts:
# *-image-x86.zip (manual installation & homebrew)
# *-x86.app
# *-image-aarch64.zip 
# *-aarch64.app

version="1.1.0"
name="subtitle-buddy-$version-mac"
ssh_host="vince@192.168.178.69"
ci_scripts_dir="~/projekte/important/Subtitle-Buddy/ci/osx"

./gradlew clean

echo "running tests"
./gradlew test

rm -rf ~/.subtitle-buddy
echo "gradle run"
./gradlew run

echo "manual installation x86"
rm -rf ~/.subtitle-buddy
./ci/osx/test-manual-installation.sh "mac"

echo "homebrew installation x86"
rm -rf ~/.subtitle-buddy
./ci/osx/ftp-upload-image.sh "${name}-x86-image.zip" "mac"
ssh $ssh_host "${ci_scripts_dir}/update-homebrew-formular.sh mac"
./ci/osx/test-homebrew-installation.sh


echo ".app installation x86"
rm -rf ~/.subtitle-buddy
./ci/osx/test-app-installation.sh "mac"
./ci/osx/ftp-upload-app.sh "${name}-x86-app.zip" "mac"

# continue with aarch64...

