#!/bin/bash
# usage: ./test-homebrew-installation.sh


# reinstalls via homebrew in a stateless way
# always execute from project root and on mac os
# you need to update the formula file and publish the latest artifact before executing
# execute ./update-homebrew-formular.sh before

# remove old installation and update brew
brew update --force
brew uninstall --force subtitle-buddy

# clean up all kind of maybe stateful files from prev installation
rm -f /usr/local/bin/subtitle-buddy
rm -f ~/Library/Caches/Homebrew/downloads/*subtitle-buddy*
rm -rf /usr/local/Cellar/subtitle-buddy

# install
brew tap vincemann/homebrew-repo
brew reinstall subtitle-buddy --verbose

# start
subtitle-buddy $sb_jvm_args

brew remove subtitle-buddy
