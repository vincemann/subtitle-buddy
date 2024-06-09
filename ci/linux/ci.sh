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


# Start the file server in the background, that runs as long as this script
current_dir=$(pwd)
cleanup() {
  echo "Stopping the file server..."
  kill $file_server_pid
}

(
  cd ./server
  python3 -m http.server 8000 &
  file_server_pid=$!
)
trap cleanup EXIT
cd "$current_dir"


name="subtitle-buddy-1.1.0-linux"
./gradlew clean


echo "################################"
echo "running tests"
./gradlew test


echo "################################"
rm -rf ~/.subtitle-buddy
echo "gradle run"
./gradlew run


echo "################################"
rm -rf ~/.subtitle-buddy
echo "manual installation"
./ci/linux/test-manual-installation.sh
mv build/*.zip server/${name}-image.zip


echo "################################"
rm -rf ~/.subtitle-buddy
echo "jar installation"
./ci/linux/test-jar-installation.sh
mv build/libs/*linux*.jar server/${name}.jar


echo "################################"
rm -rf ~/.subtitle-buddy
echo "homebrew installation"
# ${platform}-image.zip is already present in server dir as expected by next scripts
./ci/linux/update-homebrew-formula.sh
./ci/linux/test-homebrew-installation.sh

echo "################################"
rm -rf ~/.subtitle-buddy
echo "app image installation"
./ci/linux/test-app-image-installation.sh
mv build/releases/*.AppImage server/${name}.AppImage


echo "################################"
rm -rf ~/.subtitle-buddy
echo "deb installation"
./ci/linux/test-deb-installation.sh
mv build/jpackage/*.deb server/${name}.deb


echo "################################"
echo "cleaning up"
./ci/cleanup.sh
kill $file_server_pid


echo "################################"
echo "finished"