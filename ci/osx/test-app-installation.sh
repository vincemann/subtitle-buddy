#!/bin/bash
./gradlew clean jpackage
open -a build/jpackage/*.app