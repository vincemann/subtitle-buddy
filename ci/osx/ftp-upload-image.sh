#!/bin/bash
# build homebrew mac tar ball and send via ftp to linux server for hosting

./gradlew jlinkZip
dir="build"
file=$(ls "$dir"/*.zip 2> /dev/null | head -n 1)
# Check if a file was found
if [ -z "$file" ]; then
    echo "No .zip files found in $directory"
    exit 1
else
    # Extract the file name from the full path
    filename=$(basename "$file")
    echo "Found file: $filename"
    ./ci/osx/ftp-upload.sh $dir $filename
fi

