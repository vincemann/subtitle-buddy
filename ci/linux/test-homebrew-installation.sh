#!/bin/bash
# always execute from project root
# reinstall subtitle buddy via homebrew

brew update
brew uninstall subtitle-buddy
brew cleanup

# remove possible deb installation
sudo rm -rf /opt/subtitle-buddy
rm -f /usr/local/bin/subtitle-buddy

rm -f ~/.cache/Homebrew/downloads/*subtitle-buddy*.zip

# remove all cached stuff
rm -rf /home/linuxbrew/.linuxbrew/Cellar/subtitle-buddy

# remove start symlink
rm /home/linuxbrew/.linuxbrew/bin/subtitle-buddy

brew tap vincemann/homebrew-repo

brew install subtitle-buddy --verbose

# start
subtitle-buddy