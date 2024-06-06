#!/bin/bash
# Send already build app.zip via ftp to linux server for hosting
# needs to be executed on mac os
# you need to execute ./ci/osx/test-app-installation.sh before -> it creates the artifacts (.app.zip)
# usage ./ftp-upload-image.sh

dir="build/jpackage"
file=$(ls "$dir"/*.zip 2> /dev/null | head -n 1)
if [ -z "$file" ]; then
    echo "No .zip file found in $directory"
    exit 1
else
    # Extract the file name from the full path
    filename=$(basename "$file")
    echo "Found artifact: $filename"
    ./ci/osx/ftp-upload.sh $dir $filename
fi
