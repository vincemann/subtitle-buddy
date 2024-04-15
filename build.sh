#!/bin/bash

./gradlew clean
./build-linux.sh
./build-mac.sh

# WINDOWS
read  -n 1 -p "continue with windows?:" continue_windows

if [[ $continue_windows == 'y' ]]; then
	./build-win.sh
fi


# copy to stick for testing
lsblk
read  -n 1 -p "continue with copying to stick?:" continue_stick
if [[ $continue_stick == 'y' ]]; then
	echo "copying to stick"
	sudo mount /dev/sdc1 /mnt/usb
	sudo cp releases/macos/x64/$mac_image_name /mnt/usb/
	sudo cp releases/linux/x64/$linux_image_name /mnt/usb/
	sudo cp releases/windows/x64/inno/output/subtitle-buddy-installer.exe /mnt/usb/
fi
