#!/bin/bash
#!/bin/bash
# always execute from project root
# reinstall subtitle buddy via homebrew

brew update --force

brew uninstall --force subtitle-buddy-mac

# remove possible previous installation
sudo rm -rf /opt/subtitle-buddy
rm -f /usr/local/bin/subtitle-buddy

rm -f ~/Library/Caches/Homebrew/downloads/*subtitle-buddy*.tar.gz

# remove all cached stuff
rm -rf /usr/local/Cellar/subtitle-buddy-mac

# remove start symlink
rm /usr/local/bin/subtitle-buddy

brew tap vincemann/homebrew-repo

brew install subtitle-buddy-mac --verbose

# start
subtitle-buddy
