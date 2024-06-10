#!/bin/bash
# always execute from project root
# reinstall subtitle buddy via homebrew

brew update
brew uninstall subtitle-buddy
brew cleanup

rm -f /usr/local/bin/subtitle-buddy
rm -f ~/.cache/Homebrew/downloads/*subtitle-buddy*
rm -rf /home/linuxbrew/.linuxbrew/Cellar/subtitle-buddy
rm /home/linuxbrew/.linuxbrew/bin/subtitle-buddy

brew tap vincemann/homebrew-repo

brew install subtitle-buddy --verbose

# start
subtitle-buddy $sb_jvm_args