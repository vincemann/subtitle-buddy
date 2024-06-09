#!/bin/bash
# ./update-homebrew-formula.sh

# expects *linux-image.zip to be in ./server dir
./ci/update-formula-hash.sh "linux"
