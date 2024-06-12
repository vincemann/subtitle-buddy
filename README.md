# Subtitle Buddy  
This is an opensource, crossplatform Subtitle Player, written in pure Java, as an alternative to [Penguin Subtitleplayer](https://github.com/carsonip/Penguin-Subtitle-Player).  
This program focuses on simple synchronization.  
It is used to add subtitles to movies, beeing streamed online, that offer no subtitles (or not the language you want).  
You can simply download the matching subtitle-file (.srt format) for the movie you are streaming, load them into this program and overlay your screen with the subtitles.  
Those subtitle-files (.srt) are present,in a big variety of languages, for almost every movie.  
Snychronizing the subtitles with the movie is as easy as shown here:  
  
![](demo.gif)
  
  
## features  
- easy start/stop synchronisation (next-click-counts, start/stop via spacekey)
- Skip to specified time
- time fine tuning (stepping small steps for- and backwards)  
- Customizable subtitles (size, color, font)  
- add own fonts  
  
## installation  
### download installer (win / linux / mac )  
Download your favorite installer from [here](https://github.com/vincemann/Subtitle-Buddy/releases) and double click it.  
Mac users can download the .app.zip, unzip it and double click.  

### homebrew (linux / mac)   
Install homebrew like shown [here](https://brew.sh/).  
Open terminal and type:
```
brew tap vincemann/homebrew-repo
brew install subtitle-buddy
```
start with ```subtitle-buddy```  

### manual (advanced)  
Download image.zip file for your os [here](https://github.com/vincemann/Subtitle-Buddy/releases).    
Unzip it and hook up the start script, for linux/mac you may want to create a symlink like:  
```ln -sf /path/to/start-script /usr/local/bin/subtitle-buddy```    
On Windows you may want to create a Desktop shortcut to the start script.  
After unzipping the start script is at ```./appLauncher-$platform/bin/appLauncher```.    

## synchronisation  
There are two convient ways to synchronize the loaded subtitles (.srt) with the movie.   
1. The gif shows the **"next-click-counts"**- feature in action:   
Press **alt+n**, then a finger icon will appear.   
This means, that the next click (left mouse click) will start/stop the loaded sutitles. 
Your next click should be a click on the play/pause button of the browsers movie-player.
  
2. You can also start/stop the loaded subtitles, by pressing the **space-key**.  
Most online video players start/stop the movie, if you focus the player and press space.  
This feature is disabled by deafault, to prevent accidental pause/resuming of the loaded subtitles.  
Both features can be enabled/disabled in the options window.  

After that you can finetune by pressing **<<** and **>>** until its perfect.
You can hide/display the deafault subtitle via **alt+d** when you are all set up.
  
## navigation  
You can get from settings mode (default) to movie mode, by pressing on the "movie-mode" button.  
A double-click on the subtitles, or pressing **escape** will bring you back from movie mode to settings mode.  

## configuration  
You can configure stuff by opening the options window.  
You can configure even more by modifying the application.properties file in the config directory:
```~/.subtitle-buddy``` on linux/mac and  
```%appdata%\subtitle-buddy``` on windows.  
If you break anything, just remove this folder, it will be regenerated.  
For example you can permanently hide the default subtitle by setting ```defaultSubtitle = ""```.  
You can change the font size by dragging on the edge of the blue subtitle box in movie mode.  
  
## adding your own fonts  
The program will generate a folder named "fonts" in the config directory,
you can add your own fonts to this folder.  
It is expected that there is always a font pair present, for example:  
```myAwesomeFont.otf``` and ```myAwesomeFont.italic.otf```    
Supported font types are at least ```.otf``` and ```.ttf```.  
  
## requirements  
Should run on most gui based OS.  
Tested on Windows 10 x86_64, Debian 10 x86_64 and MacOs X 11.5 x86_64.  
This program makes use of this [library ](https://github.com/kwhat/jnativehook)  for capturing hotkeys and mouse clicks.  
See its "Software and Hardware Requirements" for additional details.  
The installers are build for x86_64 if no architecture is in the filename.  
  
## encoding issues  
If you experience encoding isses ( some characters are not displayed properly),   
you should transform your srt file to utf-8 [here](https://subtitletools.com/convert-text-files-to-utf8-online).  
If you want a different encoding, you can set it in application.properties file -  
You could replace the encoding line to 'encoding = ISO-8859-1' for example.  
  
## building from sources  
Clone the repo and build the artifacts like shown here:  
(files will be built into ./build/releases and ./build/jpackage)  
**image (see manual installation)**  
linux/mac: ```./gradlew jlinkZip```   
windows: ```gradlew.bat jlinkZip```   
  
**linux**  
deb: ```./gradlew jpackage```  
AppImage: ```./gradlew buildLinuxAppImage```    
  
**mac**      
app: ```./gradlew jpackage```    
  
**windows**   
(can only be built on linux)    
installer: ```./gradlew clean buildWindowsInstaller -PtargetPlatform=win```   
 
    


