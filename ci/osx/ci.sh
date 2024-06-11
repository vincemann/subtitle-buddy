#!/bin/bash
# ./ci [setup-test | version] [skip-tests]
# arg1 can be 'setup-test', 'version', or omitted.

# execute all ci scripts for linux in a row
# also executes ci scripts on remote linux host (for example for updating homebrew formula file)
# also uploads all artifacts via ftp to linux host -> files will end up in ftp root dir, which should be ./server

# remember to start file server on linux in ./server in dev env: python3 -m http.server 8000
# remember to start ssh server on linux via sudo systemctl start ssh.service - start with sftp enabled and user: subtitle-buddy
# remember to add ssh key for subtitle-buddy to allow ssh/sftp access without pw prompt
# ssh start dir of user should be in subtitle-buddy project root
# sftp startDir/projects should be subtitle-buddy root dir (ssh_config is in ./ci dir)

# artifacts:
# name-image-x64.zip (manual installation & homebrew)
# name-x64.app.zip
# name-image-aarch64.zip 
# name-aarch64.app.zip

version="1.1.0"
name_prefix="subtitle-buddy-$version-mac"
SSH_HOST=subtitle-buddy@192.168.178.69


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


# set arch to x64
name="$name_prefix"
./gradlew clean


if $skip_tests; then
    echo "skipping tests"
else
    echo "################################"
    echo "running tests"
    ./gradlew test
fi





echo "################################"
rm -rf ~/.subtitle-buddy
echo "gradle run"
if [[ -z $sb_jvm_args ]]; then
  ./gradlew run
else
  ./gradlew run --args="$sb_jvm_args"
fi

echo "################################"
echo "manual installation x64"
rm -rf ~/.subtitle-buddy
./ci/osx/test-manual-installation.sh "mac"

echo "################################"
echo "homebrew installation x64"
rm -rf ~/.subtitle-buddy
./ci/osx/ftp-upload-image.sh "$SSH_HOST" "${name}-image.zip" "mac"
ssh "$SSH_HOST" "cd projects; sudo -u vince ./ci/osx/update-homebrew-formula.sh mac"
./ci/osx/test-homebrew-installation.sh


echo "################################"
echo ".app installation x64"
rm -rf ~/.subtitle-buddy
./ci/osx/test-app-installation.sh "mac"
./ci/osx/ftp-upload-app.sh "$SSH_HOST" "${name}.app.zip" "mac"

# set arch to aarch64
name="$name_prefix-aarch64"
echo "################################"
echo "continue with aarch64..."
./gradlew clean


echo "################################"
echo "build image and upload for aarch64..."
# build image.zip and upload to hosting linux machine
./ci/osx/build-image.sh "mac-aarch64"
# upload image.zip to linux host
./ci/osx/ftp-upload-image.sh "$SSH_HOST" "${name}-image.zip" "mac-aarch64"


echo "################################"
echo "build .app and upload for aarch64..."
# build .app.zip and upload to hosting linux machine
./ci/osx/build-app.sh "mac-aarch64"
./ci/osx/ftp-upload-app.sh "$SSH_HOST" "${name}.app.zip" "mac-aarch64"

echo "################################"
echo "setup aarch64 for homebrew"
ssh "$SSH_HOST" "cd projects; sudo -u vince ./ci/osx/update-homebrew-formula.sh mac-aarch64"

echo "################################"
echo "cleaning up"
./ci/cleanup.sh

echo "################################"
echo "finished"
