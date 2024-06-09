#!/bin/bash
# ./ci/update-formula-hash.sh <linux|mac|mac-aarch64>


# updates sha256 hash of either linux or mac/mac-aarch64 in homebrew github repo formula file
# expects updated *$platform*image*.zip file in ./server dir
# push changes to repo


# input validations
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <linux|mac|mac-aarch64>"
    exit 1
fi
PLATFORM=$1
FORMULA_FILE="./brew/homebrew-repo/Formula/subtitle-buddy.rb"
if [ ! -f "$FORMULA_FILE" ]; then
    echo "Error: Formula file $FORMULA_FILE not found!"
    exit 1
fi


# evaluate new hash
NEW_SHA256=$(sha256sum server/*$PLATFORM*image.zip | awk '{ print $1 }')
echo "new hash: $NEW_SHA256"


# update hash in formula file
if [ "$PLATFORM" == "linux" ]; then
    perl -pi -e "s/^( *sha256 \").*(\".*linux.*)/\1$NEW_SHA256\2/" "$FORMULA_FILE"
elif [ "$PLATFORM" == "mac" ]; then
    perl -pi -e "s/^( *sha256 \").*(\".*mac-x64.*)/\1$NEW_SHA256\2/" "$FORMULA_FILE"
elif [ "$PLATFORM" == "mac-aarch64" ]; then
    perl -pi -e "s/^( *sha256 \").*(\".*mac-aarch64.*)/\1$NEW_SHA256\2/" "$FORMULA_FILE"
else
    echo "Error: First argument must be 'linux', 'mac', or 'mac-aarch64'"
    exit 1
fi
echo "Updated SHA256 checksum for $PLATFORM in $FORMULA_FILE"

# update repo
echo "pushing to github"
cd ./brew/homebrew-repo
git add -A
git commit -m "update hash for $PLATFORM"
git push
