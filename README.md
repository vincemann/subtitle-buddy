# Subtitle Buddy  
This is an opensource, crossplatform Subtitle Player, written in pure Java, as an alternative to [Penguin Subtitleplayer](https://github.com/carsonip/Penguin-Subtitle-Player). 
It is used to add subtitles to movies, beeing streamed online, that offer no subtitles (or not the language you want).  
You can simply download the matching subtitle-file (.srt format) for the movie you are streaming, load them into this program and overlay your screen with the subtitles.  
Those subtitle-files (.srt) are present,in a big variety of languages, for almost every movie.  
Snychronizing the subtitles with the movie is as easy as shown here:  
  
![](demo.gif)
  
  
## features  
- Customizable subtitles (size, color, font)  
- add own fonts  
- easy start/stop synchronisation (next-click-counts, start/stop via spacekey)  
- time fine tuning (stepping small steps for- and backwards)  
- Skip to specified time  
  
  
## download  
You can download the latest release from [here](https://github.com/vincemann/Subtitle-Buddy/releases).  
  
## installation  
No Installation required. Just unpack the [zip](https://github.com/vincemann/Subtitle-Buddy/releases) wherever you want  
and run the start script.    
  
## synchronisation  
There are two convient ways to synchronize the loaded subtitles (.srt) with the movie.   
The gif shows the **"next-click-counts"**- feature in action.   
Press **alt+n**, then a finger icon will appear.   
This means, that the next click (left mouse click) will start/stop the loaded sutitles.  
Your next click should be a click on the play/ pause button of the browsers movie-player.   
You can also start/stop the loaded subtitles, by pressing the space-key.  
Most online video players start/stop the movie, if you focus the player and press space.  
This feature is disabled by deafault, to prevent accidental pause/resuming of the loaded subtitles.  
Both features can be enabled/disabled in the options window.  
  
## navigation  
You can get from settings mode (default) to movie mode, by pressing on the "movie-mode" button.  
A double-click on the subtitles, or pressing **alt+escape** will bring you back from movie mode to settings mode.  

  
## adding your own fonts  
The program will generate a folder named "fonts" in the directory, the jar is located in.  
You can add your own fonts to this folder.  
It is expected that there is always a font pair present, with the follwing syntax:  
\<fontname\>.\<filetype\> and \<fontname\>.italic.\<filetype\>.  
Supported file-types are .otf and .ttf.  
  
  
## requirements  
Should run on every gui based OS, that has min JRE 1.8 installed.  
Tested on Windows 7, Windows 10, Ubuntu 16.04, Ubunutu 18.04. Debian 9.  
This program makes use of this [library ](https://github.com/kwhat/jnativehook)  for capturing hotkeys and mouse clicks.  
See its "Software and Hardware Requirements" for additional details.  
  
  
## overlay Issues  
The always-on-top feature is not very reliable.  
It depends on your OS, whether the subtitles stay on top, when switching to fullscreen mode in your browsers video player.  
- There are no overlay issues detected on Windows yet.    
- On Ubuntu, after enableing fullscreen on your browsers video player, you have to press strg+tab, until the focus is back on the subtitle-buddy program.  

## encoding issues  
if you experience encoding isses ( some characters are not displayed properly),   
you should transform your srt file to utf-8 [here](https://subtitletools.com/convert-text-files-to-utf8-online).  
if you want a different encoding, you can set it in application.properties file -  
you could replace the encoding line to 'encoding = ISO-8859-1' for example.  

## configuration  
you can configure all kinds of things, by changing values directly in application.properties file, which resides  
in the same folder the jar is executed in (is created after first run of program).  
keep a backup of the file before modifying anything - only change if you know what you are doing.  
  
## building from sources  
Only Works with JDK8 installed and active.  
Simply download sources, navigate into the downlaoded folder and run the gradle wrapper from terminal with:  
./gradlew shadowJar  
or for windows users  
gradlew.bat shadowJar  
Then see the requirements in build-zip.sh, fulfill these and run build-zip.sh.  
The platform dependent zip files are in ./build/libs/  
   

  
