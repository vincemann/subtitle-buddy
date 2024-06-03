#!/bin/bash
./gradlew clean jpackage
cd build/jpackage
zip -r subtitle-buddy-app.zip subtite-buddy.app
mkdir temp
unzip subtitle-buddy-app.zip -d temp
open -a temp/subtitle-buddy.app