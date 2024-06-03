#!/bin/bash
./gradlew clean jpackage
./gradlew jpackage
cd build/jpackage
zip -r subtitle-buddy-app.zip subtite-buddy.app
mkdir temp
unzip subtitle-buddy-app.zip -d temp
cd temp
open -a subtitle-buddy.app