#!/bin/bash
# always execute from project root
# reinstall subtitle buddy via homebrew

brew update --force

brew uninstall --force subtitle-buddy-linux

# remove possible deb installation
sudo rm -rf /opt/subtitle-buddy
rm -f /usr/local/bin/subtitle-buddy

rm -f ~/.cache/Homebrew/downloads/*subtitle-buddy*.tar.gz

# remove all cached stuff
rm -rf /home/linuxbrew/.linuxbrew/Cellar/subtitle-buddy-linux

# remove start symlink
rm /home/linuxbrew/.linuxbrew/bin/subtitle-buddy

brew tap vincemann/homebrew-repo

brew install subtitle-buddy-linux --verbose

# start
subtitle-buddy