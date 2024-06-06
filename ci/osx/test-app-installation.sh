#!/bin/bash

# create mac .app and zip it up 
# then emulate user behavior by unzipping and opening app

app="subtitle-buddy.app"
archive="subtitle-buddy-app.zip"

./gradlew jpackage
cd build/jpackage
zip -r "$archive" "$app"
mkdir temp
unzip "$archive" -d temp
cd temp
open -a "$app"