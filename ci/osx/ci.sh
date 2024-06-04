#!/bin/bash
# execute all ci scripts for linux in a row
# user just needs to interactively test the application when it opens and type in pws
# also uploads all artifacts (.app and image.zip) via ftp to linux host
name="subtitle-buddy-1.1.0-mac"

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
ssh vince@192.168.178.69 ~/projekte/important/Subtitle-Buddy/ci/osx/update-homebrew-formular.sh
./ci/osx/test-homebrew-installation.sh
ssh vince@192.168.178.69 "mv ~/subtitle-buddy-releases/*.zip ~/projekte/important/Subtitle-Buddy/server/${name}-x86-image.zip"


rm -rf ~/.subtitle-buddy
echo ".app installation x86"
./ci/osx/test-app-installation.sh
./ci/osx/ftp-upload-app.sh
ssh vince@192.168.178.69 "mv ~/subtitle-buddy-releases/*.zip ~/projekte/important/Subtitle-Buddy/server/${name}-x86-app.zip"

