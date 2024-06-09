#!/bin/bash

rm -f /usr/local/bin/subtitle-buddy
rm -rf ~/.subtitle-buddy
sudo dpkg --remove subtitle-buddy
brew remove subtitle-buddy