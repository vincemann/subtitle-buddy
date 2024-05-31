#!/bin/bash
# always execute from project root
# this script builds new mac release for homebrew and sets everything up
# need to build image on macos before and send to this pc (./ci/osx/ftp-upload-image.sh)
# file is expected to be in ~/subtitle-buddy-releases

cp ~/subtitle-buddy-releases/*mac*.zip build

./ci/update-formular-hash.sh "mac"

# start file server 
PORT=8000
cd server

# Check if the port is already in use
if ! lsof -i:$PORT &> /dev/null; then
    echo "Port $PORT is available. Starting the HTTP server..."
    python3 -m http.server $PORT
else
    echo "Port $PORT is already in use. Please choose a different port or stop the process using it."
fi