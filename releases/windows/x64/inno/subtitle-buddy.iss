[Setup]
AppName=Subtitle Buddy
AppVersion=2.0.0
DefaultDirName={pf}\SubtitleBuddy
DefaultGroupName=com.github.vincemann
OutputDir=output
OutputBaseFilename=subtitle-buddy-installer
Compression=lzma
SolidCompression=yes
SetupIconFile=icon.ico

[Files]
Source: "image/*"; DestDir: "{app}\image"; Flags: recursesubdirs ignoreversion
Source: "start.bat"; DestDir: "{app}"; Flags: ignoreversion
Source: "icon.ico"; DestDir: "{app}"; Flags: ignoreversion

[Icons]
Name: "{group}\Subtitle Buddy"; Filename: "{app}\start.bat"; IconFilename: "{app}\icon.ico"
Name: "{userdesktop}\Subtitle Buddy"; Filename: "{app}\start.bat"; IconFilename: "{app}\icon.ico"; Tasks: desktopicon

[Tasks]
Name: "desktopicon"; Description: "Create a &desktop icon"; GroupDescription: "Additional icons:"

[Run]
Filename: "{app}\start.bat"; Description: "Launch Subtitle Buddy"; Flags: nowait postinstall skipifsilent