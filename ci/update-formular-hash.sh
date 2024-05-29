#!/bin/bash
# run from project root
# update sha256 hash of either linux or mac in homebrew github repo formular
# push changes to repo
# ./ci/update-formular-hash.sh <linux|mac>

# Check if the correct number of arguments is provided
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <linux|mac>"
    exit 1
fi

PLATFORM=$1
FORMULA_FILE="./brew/homebrew-repo/Formula/subtitle-buddy.rb"

# Ensure the formula file exists
if [ ! -f "$FORMULA_FILE" ]; then
    echo "Error: Formula file $FORMULA_FILE not found!"
    exit 1
fi

NEW_SHA256=$(sha256sum build/releases/*$PLATFORM*.tar.gz | awk '{ print $1 }')
echo "new hash: $NEW_SHA256"

# Create a temporary file
TEMP_FILE=$(mktemp)

# Update the appropriate SHA256 checksum in the formula file and write to the temporary file
if [ "$PLATFORM" == "linux" ]; then
    sed "/^ *url \".*linux.*\"/{n;s/^\( *sha256 \)\"[^\"]*\"/\1\"$NEW_SHA256\"/;}" "$FORMULA_FILE" > "$TEMP_FILE"
elif [ "$PLATFORM" == "mac" ]; then
    sed "/^ *url \".*mac.*\"/{n;s/^\( *sha256 \)\"[^\"]*\"/\1\"$NEW_SHA256\"/;}" "$FORMULA_FILE" > "$TEMP_FILE"
else
    echo "Error: First argument must be 'linux' or 'mac'"
    rm "$TEMP_FILE"
    exit 1
fi

# Move the temporary file to the original formula file
mv "$TEMP_FILE" "$FORMULA_FILE"

echo "Updated SHA256 checksum for $PLATFORM in $FORMULA_FILE"

# echo "pushing to github"
# cd ./brew/homebrew-repo
# git add -A
# git commit -m "update hash"
# git push

# cd ../..

