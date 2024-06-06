#!/bin/bash
#!/bin/bash
# usage: ./test-homebrew-installation.sh
# always execute from project root and on mac os
# reinstalls subtitle buddy via homebrew
# you need to update the formula file and publish the latest artifact before executing
# execute ./update-homebrew-formular.sh before

brew update --force

brew uninstall --force subtitle-buddy

# remove possible previous installation
sudo rm -rf /opt/subtitle-buddy
rm -f /usr/local/bin/subtitle-buddy

rm -f ~/Library/Caches/Homebrew/downloads/*subtitle-buddy*.tar.gz

# remove all cached stuff
rm -rf /usr/local/Cellar/subtitle-buddy

# remove start symlink
rm /usr/local/bin/subtitle-buddy

brew tap vincemann/homebrew-repo

brew install subtitle-buddy --verbose

# start
subtitle-buddy
