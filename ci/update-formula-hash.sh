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
NEW_SHA256=$(sha256sum server/*$PLATFORM-image.zip | awk '{ print $1 }')
echo "new hash: $NEW_SHA256"


TEMP_FILE=$(mktemp) || { echo "Failed to create temporary file"; exit 1; }
# update hash in formula file
awk -v platform="$PLATFORM" -v newsha="$NEW_SHA256" '
  {
    print $0
    if (platform == "linux" && /url .*linux.*image.zip/) {
      if (getline > 0 && /sha256/) {
        sub(/"[^"]+"/, "\"" newsha "\"")
        print $0
      }
    } else if (platform == "mac" && /url .*mac-image.zip/) {
      if (getline > 0 && /sha256/) {
        sub(/"[^"]+"/, "\"" newsha "\"")
        print $0
      }
    } else if (platform == "mac-aarch64" && /url .*mac-aarch64-image.zip/) {
      if (getline > 0 && /sha256/) {
        sub(/"[^"]+"/, "\"" newsha "\"")
        print $0
      }
    }
  }
' "$FORMULA_FILE" > "$TEMP_FILE"

if [ $? -eq 0 ]; then
  mv "$TEMP_FILE" "$FORMULA_FILE"
  echo "Updated SHA256 checksum for $PLATFORM in $FORMULA_FILE"
else
  rm "$TEMP_FILE"
  echo "Failed to update SHA256 checksum for $PLATFORM"
  exit 1
fi

# update repo
echo "pushing to github"
cd ./brew/homebrew-repo
git add -A
git commit -m "update hash for $PLATFORM"
git push
