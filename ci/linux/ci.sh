#!/bin/bash
# ./ci [skip]
# execute all ci scripts for linux in a row
# user just needs to interactively test the application when it opens and type in pws
# remember to start file server in ./server in dev env: python3 -m http.server 8000
# also moves all artifacts to ./server for deployment
# artifacts (x64 only):
# 1. image.zip (manual installation & homebrew)
# 2. .jar
# 3. .AppImage
# 4. .deb

name="subtitle-buddy-1.1.0-linux"
./gradlew clean


echo "running tests"
./gradlew test


rm -rf ~/.subtitle-buddy
echo "gradle run"
./gradlew run


rm -rf ~/.subtitle-buddy
echo "manual installation"
./ci/linux/test-manual-installation.sh
mv build/*.zip server/${name}-image.zip


rm -rf ~/.subtitle-buddy
echo "jar installation"
./ci/linux/test-jar-installation.sh
mv build/libs/*linux*.jar server/${name}-jar.zip


rm -rf ~/.subtitle-buddy
echo "homebrew installation"
./ci/linux/update-homebrew-formular.sh
./ci/linux/test-homebrew-installation.sh
rm server/image-linux.zip # remove this bc the manual installation already deployed the proper zip to server


rm -rf ~/.subtitle-buddy
echo "app image installation"
./ci/linux/test-app-image-installation.sh
mv build/releases/*.AppImage server/${name}-AppImage.zip


rm -rf ~/.subtitle-buddy
echo "deb installation"
./ci/linux/test-deb-installation.sh
mv build/jpackage/*.deb server/${name}.deb

echo "finished"