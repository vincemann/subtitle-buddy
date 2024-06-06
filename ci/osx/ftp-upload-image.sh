#!/bin/bash
# ./ftp-upload-image.sh artifactName [mac|mac-aarch64] 


# build homebrew mac image.zip and send via ftp to linux server for hosting
# need to specify target arch mac -> x64, mac-aarch64 -> aarch64
# need to specify name: for example subtitle-buddy-1.1.0-mac-x64-image.zip

artifact_name="$1"
arch="$2"

echo "artifact name: $artifact_name"
echo "building for arch: $arch"

./ci/osx/build-image.sh $arch

# upload file to linux host via ftp
dir="build"
file=$(ls "$dir"/*.zip 2> /dev/null | head -n 1)
if [ -z "$file" ]; then
    echo "No .zip files found in $directory"
    exit 1
else
    filename=$(basename "$file")
    echo "Found file: $filename"
    ./ci/osx/ftp-upload.sh $dir $filename $artifact_name
fi

