#!/bin/bash
# execute all ci scripts for linux in a row
# user just needs to interactively test the application when it opens and type in pws
# also uploads all artifacts via ftp to linux host and moves into server dir
# remember to start file server in ./server in dev env: python3 -m http.server 8000
# artifacts:
# image-x86.zip (manual installation & homebrew)
# x86.app
# image-aarch64.zip 
# aarch64.app
name="subtitle-buddy-1.1.0-mac"

server_dir="~/projekte/important/Subtitle-Buddy/server"
ftp_dir="~/subtitle-buddy-releases"
ssh_host="vince@192.168.178.69"
ci_scripts_dir="~/projekte/important/Subtitle-Buddy/ci/osx"

./gradlew clean

echo "running tests"
./gradlew test


rm -rf ~/.subtitle-buddy
echo "gradle run"
./gradlew run


rm -rf ~/.subtitle-buddy
echo "manual installation x86"
./ci/osx/test-manual-installation.sh


rm -rf ~/.subtitle-buddy
echo "homebrew installation x86"
./ci/osx/ftp-upload-image.sh
ssh $ssh_host ${ci_scripts_dir}/update-homebrew-formular.sh
./ci/osx/test-homebrew-installation.sh
ssh $ssh_host "mv ${ftp_dir}/*.zip ${server_dir}/${name}-x86-image.zip"


rm -rf ~/.subtitle-buddy
echo ".app installation x86"
./ci/osx/test-app-installation.sh
./ci/osx/ftp-upload-app.sh
ssh $ssh_host "mv ${ftp_dir}/*.zip ${server_dir}/${name}-x86-app.zip"

