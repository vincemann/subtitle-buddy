#!/bin/bash
# always execute from project root

# remove possible installation
rm -f /usr/local/bin/subtitle-buddy


./gradlew jlinkZip

# sometimes need to repeat for whatever reason
if ls ./build/*.zip 1> /dev/null 2>&1; then
    echo "Zip files found in the ./build directory."
else
    echo "No zip files found in the ./build directory. Running ./gradlew jlinkZip..."
    ./gradlew jlinkZip
fi


cd build

mkdir temp

unzip *.zip -d temp


ln -sf $(pwd)/temp/appLauncher-linux/bin/appLauncher /usr/local/bin/subtitle-buddy

subtitle-buddy $sb_jvm_args
