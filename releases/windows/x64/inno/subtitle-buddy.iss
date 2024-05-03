[Setup]
AppName=Subtitle Buddy
AppVersion=1.1.0
DefaultDirName={pf}\SubtitleBuddy
DefaultGroupName=com.github.vincemann
OutputDir=output
OutputBaseFilename=subtitle-buddy-installer
Compression=lzma
SolidCompression=yes

[Files]
Source: "image/*"; DestDir: "{app}\image"; Flags: recursesubdirs ignoreversion
Source: "start.bat"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{group}\Subtitle Buddy"; Filename: "{app}\start.bat"

[Run]
Filename: "{app}\start.bat"; Description: "Launch Subtitle Buddy"; Flags: nowait postinstall skipifsilent