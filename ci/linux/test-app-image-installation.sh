#!/bin/bash

./gradlew buildLinuxAppImage
./build/releases/*.AppImage $sb_jvm_args