#!/bin/bash
# always execute from project root
# must have executed update-formular.sh before

brew update --force

brew uninstall --force subtitle-buddy-dev-linux

# remove possible deb installation
sudo rm -rf /opt/subtitle-buddy
rm -f /usr/local/bin/subtitle-buddy

rm -f ~/.cache/Homebrew/downloads/*subtitle-buddy*.tar.gz

# remove all cached stuff
rm -rf /home/linuxbrew/.linuxbrew/Cellar/subtitle-buddy-dev-linux

# remove start symlink
rm /home/linuxbrew/.linuxbrew/bin/subtitle-buddy

brew tap vincemann/homebrew-repo

brew install subtitle-buddy-dev-linux --verbose

# start
subtitle-buddy