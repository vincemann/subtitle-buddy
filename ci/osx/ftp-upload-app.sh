#!/bin/bash
# Send app.zip via ftp to linux server for hosting
# needs to be executed on mac os
# you need to execute ./ci/osx/test-app-installation.sh before -> it creates the zip and image

dir="build/jpackage"
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
