#!/bin/bash
# ./ftp-upload-app.sh sshhost artifactName mac|mac-aarch64
# sshhost example: 'user@192.168.178.42'


# build mac app.zip and send via ftp to linux server for hosting
# need to specify target arch mac -> x64, mac-aarch64 -> aarch64
# need to specify name: for example subtitle-buddy-1.1.0-mac-x64-image.zip

ssh_host="$1"
artifact_name="$2"
arch="$3"

echo "artifact name: $artifact_name"
echo "building for arch: $arch"

# remove old app files possibly for diff arch
rm -rf ./build/jpackage/*.app
rm -f ./build/jpackage/*.zip

./ci/osx/build-app.sh "$arch"

# upload the .app.zip file via ftp to linux host
dir="build/jpackage"
file=$(ls "$dir"/*.zip 2> /dev/null | head -n 1)
if [ -z "$file" ]; then
    echo "No .zip file found in $directory"
    exit 1
else
    filename=$(basename "$file")
    echo "Found artifact: $filename"
    ./ci/osx/ftp-upload.sh "$ssh_host" "$dir" "$filename" "$artifact_name"
fi
