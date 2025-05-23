#!/bin/bash
# ./ci [setup-test | version]
# arg1 can be 'setup-test', 'version', or omitted.

# execute all ci scripts for linux in a row
# user just needs to interactively test the application when it opens and type in pws
# remember to start file server in ./server in dev env: python3 -m http.server 8000
# also moves all artifacts to ./server for deployment

# artifacts (x64 only):
# 1. name-image.zip (manual installation & homebrew)
# 2. name.jar
# 3. name.AppImage
# 4. name.deb

version="2.0.0"
name="subtitle-buddy-${version}-linux"



# what kind of test?
if [[ "$1" == "setup-test" ]];then
  test_file="`pwd`/src/test/resources/srt/valid.srt"
  export sb_jvm_args="--setup-test $test_file"
elif [[ "$1" == "version" ]];then
  export sb_jvm_args="--version"
fi
skip_tests=false
for arg in "$@"; do
    if [[ "$arg" == "skip-tests" ]]; then
        skip_tests=true
        break
    fi
done



# start file server in ./server
current_dir=$(pwd)
cleanup() {
  echo "Stopping the file server..."
  kill $file_server_pid
}
# Set a trap to call cleanup on script exit
trap cleanup EXIT
# Start the file server in the background and capture its PID
(
  cd ./server
  python3 -m http.server 8000 &
  file_server_pid=$!
  echo $file_server_pid > /tmp/file_server_pid.txt
)
file_server_pid=$(cat /tmp/file_server_pid.txt)
cd $current_dir



./gradlew clean


# tests
if $skip_tests; then
    echo "skipping tests"
else
    echo "################################"
    echo "running tests"
    ./gradlew test
fi


# gradle run
echo "################################"
rm -rf ~/.subtitle-buddy
echo "gradle run"
if [[ -z $sb_jvm_args ]]; then
  ./gradlew run
else
  ./gradlew run --args="$sb_jvm_args"
fi


echo "################################"
rm -rf ~/.subtitle-buddy
echo "manual installation"
./ci/linux/test-manual-installation.sh
mv build/*.zip server/${name}-image.zip


# echo "################################"
# rm -rf ~/.subtitle-buddy
# echo "jar installation"
# ./ci/linux/test-jar-installation.sh
# mv build/libs/*linux*.jar server/${name}.jar


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


echo "################################"
echo "finished"